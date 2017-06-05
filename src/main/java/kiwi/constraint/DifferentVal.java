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

public class DifferentVal extends Propagator {

  private final IntVar x;
  private final int k;

  public DifferentVal(IntVar x, int k) {
    this.x = x;
    this.k = k;
  }

  @Override 
  public boolean setup() {
    return x.remove(k);
  }
  
  @Override 
  public boolean propagate() {
    return true;
  }
}