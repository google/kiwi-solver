/*
 * Copyright 2016, Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kiwi.search;

import java.util.function.IntUnaryOperator;

import kiwi.trail.TrailedInt;
import kiwi.util.Array;
import kiwi.util.Stack;
import kiwi.variable.IntVar;

public class BinaryVarVal implements Heuristic {

  private final IntVar[] variables;

  private final int[] unassigned;
  private final TrailedInt nUnassignedT;
  private final IntUnaryOperator varCost;
  private final IntUnaryOperator valSelector;

  public BinaryVarVal(IntVar[] variables, IntUnaryOperator varCost, IntUnaryOperator valSelector) {
    this.variables = variables;
    this.unassigned = Array.makeInt(variables.length, i -> i);
    this.nUnassignedT = new TrailedInt(variables[0].getTrail(), variables.length);
    this.varCost = varCost;
    this.valSelector = valSelector;
  }

  public boolean pushDecisions(Stack<Decision> decisions) {
    int varId = selectVar();
    if (varId == -1) {
      return true;
    }
    IntVar variable = variables[varId];
    int value = valSelector.applyAsInt(varId);
    decisions.push(() -> variable.remove(value));
    decisions.push(() -> variable.assign(value));
    return false;
  }

  private int selectVar() {
    int minId = -1;
    int minCost = Integer.MAX_VALUE;
    int nUnassigned = nUnassignedT.getValue();
    if (nUnassigned == 1)
      return unassigned[0];
    for (int i = nUnassigned - 1; i >= 0; i--) {
      int varId = unassigned[i];
      if (variables[varId].isAssigned()) {
        nUnassigned--;
        unassigned[i] = unassigned[nUnassigned];
        unassigned[nUnassigned] = varId;
      } else {
        int cost = varCost.applyAsInt(varId);
        if (cost < minCost) {
          minId = varId;
          minCost = cost;
        } else if (cost == minCost && varId < minId) {
          minId = varId;
        }
      }
    }
    nUnassignedT.setValue(nUnassigned);
    return minId;
  }
}
