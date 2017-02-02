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
package kiwi.propagator;

import kiwi.core.Propagator;
import kiwi.trailed.TrailedInt;
import kiwi.variable.IntVar;

public class AllDifferent extends Propagator {

  private final IntVar[] unassigned;
  private final TrailedInt nUnassignedT;

  public AllDifferent(IntVar[] variables) {
    unassigned = variables.clone();
    nUnassignedT = new TrailedInt(unassigned[0].getTrail(), variables.length);
  }

  public boolean setup() {
    for (int i = 0; i < unassigned.length; i++) {
      unassigned[i].watchAssign(this);
    }
    return propagate();
  }

  public boolean propagate() {
    int nUnassigned = nUnassignedT.getValue();
    if (nUnassigned == 1)
      return true;
    boolean reduce = true;
    while (reduce) {
      reduce = false;
      for (int i = nUnassigned - 1; i >= 0; i--) {
        IntVar variable = unassigned[i];
        if (variable.isAssigned()) {
          //
          nUnassigned--;
          unassigned[i] = unassigned[nUnassigned];
          unassigned[nUnassigned] = variable;
          //
          int value = variable.getMin();
          for (int j = 0; j < nUnassigned; j++) {
            IntVar var = unassigned[j];
            if (var.contains(value)) {
              if (!var.remove(value))
                return false;
              reduce |= var.isAssigned();
            }
          }
        }
      }
    }
    nUnassignedT.setValue(nUnassigned);
    return true;
  }
}
