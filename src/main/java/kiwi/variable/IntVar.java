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

import java.util.Arrays;

import kiwi.propagation.PropagationQueue;
import kiwi.propagation.Propagator;
import kiwi.trail.Trail;

/**
 * Superclass to be instantiated by any integer variable.
 * 
 * <p>
 * An {@code IntVar} basically represents the set of integer values that can be
 * assigned to the variable. A variable is assigned when its domain becomes a 
 * singleton, i.e, it contains a single value. The variable cannot be empty. 
 * Operation that directly impact the domain of the variable must return false 
 * if the operation would have lead to an empty domain. 
 * </p>
 * 
 * <p>
 * An {@code IntVar} must be able to restore its state when a backtrack occurs. 
 * </p>
 */
public abstract class IntVar {

  public static final int MIN_VALUE = -1000000000;
  
  public static final int MAX_VALUE = 1000000000;

  /** 
   * Returns the propagation queue associated to this variable.
   * 
   * @return
   */
  public abstract PropagationQueue getPropagQueue();

  /** 
   * Returns the trail associated to this variable. 
   * 
   * @return
   */
  public abstract Trail getTrail();

  /** 
   * Returns the minimum value contained in the variable's domain. 
   * 
   * <p>
   * This operation is always performed in constant time.
   * </p>
   * 
   * @return
   */
  public abstract int min();

  /** 
   * Returns the maximum value contained in the variable's domain. 
   * 
   * <p>
   * This operation is always performed in constant time.
   * </p>
   * 
   * @return
   */
  public abstract int max();

  /** 
   * Returns the numver of values contained in the variable's domain. 
   * 
   * @return
   */
  public abstract int size();

  /** 
   * Returns true if the variable is assigned. 
   * 
   * <p>
   * This operation is always performed in constant time.
   * </p>
   * 
   * @return
   */
  public abstract boolean isAssigned();

  /** 
   * Returns true if value is contained in the variable's domain. 
   * 
   * <p>
   * This operation is always performed in constant time.
   * </p>
   * 
   * @return
   */
  public abstract boolean contains(int value);

  /** 
   * Assigns the variable to value. 
   * 
   * <p>
   * This operation enqueues all the propagator registered on this variable.
   * This operation is linear in the number of propogator registered on the variable. 
   * </p>
   * 
   * @return
   */
  public abstract boolean assign(int value);

  /** 
   * Removes value from the variable's domain. 
   * 
   * @return
   */
  public abstract boolean remove(int value);

  /** 
   * Removes all value lower than value. 
   * 
   * @return
   */
  public abstract boolean updateMin(int value);

  /** 
   * Removes all value higher than value. 

   * Constant time if fail.
   * 
   * Linear in the number of value contained between the current minimum and the new one.
   * 
   * @return
   */
  public abstract boolean updateMax(int value);

  /** 
   * Registers the propagator on the domain changes. 
   * 
   * Constant time if fail.
   * 
   * Linear in the number of value contained between the current maximum and the new one.
   * 
   * @return
   */
  public abstract void watchChange(Propagator propagator);
  
  /** 
   * Registers the propagator on domain assignations.
   * 
   * Constant time.
   * 
   * @return
   */
  public abstract void watchAssign(Propagator propagator);

  /** 
   * Registers the propagator on bound changes. 
   * 
   * Constant time.
   * 
   * @return
   */
  public abstract void watchBounds(Propagator propagator);

  /** 
   * Copies the values contained in the domain in array.
   * 
   * Constant time.
   * 
   * @return
   */
  public abstract int copyDomain(int[] array);
  
  @Override 
  public String toString() {
    int[] domain = new int[size()];
    copyDomain(domain);
    Arrays.sort(domain);
    StringBuffer bf = new StringBuffer("{");
    for (int i = 0; i < domain.length - 1; i++) {
      bf.append(domain[i] + ", ");
    }
    bf.append(domain[domain.length - 1] + "}");
    return bf.toString();
  }
}
