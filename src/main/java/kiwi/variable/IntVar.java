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

import kiwi.propagation.PropagQueue;
import kiwi.propagation.Propagator;
import kiwi.trail.Trail;

/** IntVar */
public interface IntVar {

  /** 
   * Returns the propagation associated to this variable.
   * 
   * @return
   */
  public PropagQueue getPropagQueue();

  /** 
   * Returns the trail associated to this variable. 
   * 
   * @return
   */
  public Trail getTrail();

  /** 
   * Returns the minimum value contained in the variable's domain. 
   * 
   * @return
   */
  public int getMin();

  /** 
   * Returns the maximum value contained in the variable's domain. 
   * 
   * @return
   */
  public int getMax();

  /** 
   * Returns the numver of values contained in the variable's domain. 
   * 
   * @return
   */
  public int getSize();

  /** 
   * Returns true if the variable is assigned. 
   * 
   * @return
   */
  public boolean isAssigned();

  /** 
   * Returns true if value is contained in the variable's domain. 
   * 
   * @return
   */
  public boolean contains(int value);

  /** 
   * Assigns the variable to value. 
   * 
   * @return
   */
  public boolean assign(int value);

  /** 
   * Removes value from the variable's domain. 
   * 
   * @return
   */
  public boolean remove(int value);

  /** 
   * Removes all value lower than value. 
   * 
   * @return
   */
  public boolean updateMin(int value);

  /** 
   * Removes all value higher than value. 
   * 
   * @return
   */
  public boolean updateMax(int value);

  /** 
   * Registers the propagator on the domain changes. 
   * 
   * @return
   */
  public void watchChange(Propagator propagator);
  
  /** 
   * Registers the propagator on domain assignations.
   * 
   * @return
   */
  public void watchAssign(Propagator propagator);

  /** 
   * Registers the propagator on bound changes. 
   * 
   * @return
   */
  public void watchBounds(Propagator propagator);

  /** 
   * Copies the values contained in the domain in array.
   * 
   * @return
   */
  public int copyDomain(int[] array);
}
