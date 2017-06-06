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
package kiwi.variable;

import kiwi.Solver;
import kiwi.variable.IntVar;
import kiwi.variable.IntVarOffset;

public class IntVarOffsetTest extends IntVarTest {

  @Override
  public IntVar intVar(Solver solver, int min, int max) {
    IntVar x = solver.intVar(min, max);
    return new IntVarOffset(new IntVarOffset(new IntVarOffset(x, -10), 12), -2);
  }
  
  @Override
  public IntVar intVar(Solver solver, int[] values) {
    IntVar x = solver.intVar(values);
    return new IntVarOffset(new IntVarOffset(new IntVarOffset(x, -10), 12), -2);
  }
}
