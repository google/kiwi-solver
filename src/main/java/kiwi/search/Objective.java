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

import kiwi.variable.IntVar;

public class Objective {
  
  private final IntVar objVar; 
  private final boolean minimize;
  private int bestValue;
  
  public Objective(IntVar objVar, boolean minimize) {
    this.objVar = objVar;
    this.minimize = minimize;
    this.bestValue = minimize ? IntVar.MAX_VALUE : IntVar.MIN_VALUE;
  }
  
  public void tighten() {
    bestValue = minimize ? objVar.max() - 1 : objVar.min() + 1;
  }
  
  public boolean propagate() {
    return minimize ? objVar.updateMax(bestValue) : objVar.updateMin(bestValue);
  }
}
