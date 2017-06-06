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

import kiwi.propagation.PropagationQueue;
import kiwi.propagation.Propagator;
import kiwi.trail.Trail;

/** */
public class IntVarOpposite extends IntVar {

  private final IntVar variable;

  public IntVarOpposite(IntVar variable) {
    this.variable = variable;
  }

  @Override public PropagationQueue propagQueue() {
    return variable.propagQueue();
  }

  @Override public Trail trail() {
    return variable.trail();
  }

  @Override public int min() {
    return -variable.max();
  }

  @Override public int max() {
    return -variable.min();
  }

  @Override public int size() {
    return variable.size();
  }

  @Override public boolean isAssigned() {
    return variable.isAssigned();
  }

  @Override public boolean contains(int value) {
    return variable.contains(-value);
  }

  @Override public boolean assign(int value) {
    return variable.assign(-value);
  }

  @Override public boolean remove(int value) {
    return variable.remove(-value);
  }

  @Override public boolean updateMin(int value) {
    return variable.updateMax(-value);
  }

  @Override public boolean updateMax(int value) {
    return variable.updateMin(-value);
  }

  @Override public int copyDomain(int[] array) {
    int size = variable.copyDomain(array);
    for (int i = 0; i < size; i++) {
      array[i] = -array[i];
    }
    return size;
  }

  @Override public void watchChange(Propagator propagator) {
    variable.watchChange(propagator);
  }

  @Override public void watchAssign(Propagator propagator) {
    variable.watchAssign(propagator);
  }

  @Override public void watchBounds(Propagator propagator) {
    variable.watchBounds(propagator);
  }
}
