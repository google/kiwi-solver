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
package kiwi.constraint;

import kiwi.propagation.Propagator;
import kiwi.variable.IntVar;

public class Different extends Propagator {

  private final IntVar x;
  private final IntVar y;

  public Different(IntVar x, IntVar y) {
    this.idempotent = true;
    this.x = x;
    this.y = y;
  }
  
  public boolean setup() {
    x.watchAssign(this);
    y.watchAssign(this);
    return propagate();
  }
  
  public boolean propagate() {
    if (x.isAssigned()) {
      return y.remove(x.min());
    } 
    if (y.isAssigned()) {
      return x.remove(y.min());
    }
    return false;
  }
}
