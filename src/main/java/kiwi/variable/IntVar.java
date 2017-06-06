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

  /** 
   * A constant holding the minimum value an {@code IntVar} can be assigned to.
   */
  public static final int MIN_VALUE = -1000000000;
  
  /** 
   * A constant holding the maximum value an {@code IntVar} can be assigned to.
   */
  public static final int MAX_VALUE = 1000000000;

  /**
   * Returns the {@code PropagationQueue} associated to this variable.
   * 
   * @return the {@code PropagationQueue} of this {@code IntVar}.
   */
  public abstract PropagationQueue propagQueue();

  /**
   * Returns the {@code Trail} associated to this {@code IntVar}.
   * 
   * @return the {@code Trail} of this {@code IntVar}.
   */
  public abstract Trail trail();

  /** 
   * Returns the minimum value contained in the domain of this variable.
   * 
   * @return the minimum {@code int} contained in this {@code IntVar}.
   */
  public abstract int min();

  /** 
   * Returns the maximum value contained in the domain of this variable.
   * 
   * @return the maximum {@code int} contained in this {@code IntVar}.
   */
  public abstract int max();

  /** 
   * Returns the number of values contained in the domain of this variable.
   * 
   * @return the number of values contained in this {@code IntVar}.
   */
  public abstract int size();

  /** 
   * Returns {@code true} if this {@code IntVar} is assigned. An {@code IntVar} 
   * is assigned if its domain contains a single value.
   * 
   * @return {@code true} if the variable is assigned; {@code false} otherwise.
   */
  public abstract boolean isAssigned();

  /** 
   * Returns {@code true} if and only if this {@code IntVar} contains the 
   * specified value.
   * 
   * @param  value the value to be check for containment in this 
   *         {@code IntVar}.
   * @return {@code true} if the variable contains the value; {@code false} 
   *         otherwise.
   */
  public abstract boolean contains(int value);

  /** 
   * Tries to assign this variable to the specified value.
   * 
   * <p>
   * If this {@code IntVar} is not assigned and contains {@code value} then all
   * the propagators registered on this {@code IntVar} are scheduled for 
   * propagation as specified in {@link #watchAssign watchAssign}, 
   * {@link #watchBounds watchBounds}, and {@link #watchChange watchChange}.
   * This method does nothing if this {@code IntVar} is already assigned to
   * {@code value}. 
   * </p>
   * 
   * @param  value  the value to which this {@code IntVar} must be assigned
   * @return        {@code true} if and only if the {@code value} is contained
   *                in the domain of this {@code IntVar}
   * @see           PropagationQueue#enqueue enqueue
   * @see           #watchAssign watchAssign
   * @see           #watchBounds watchBounds
   * @see           #watchChange watchChange
   */
  public abstract boolean assign(int value);

  /** 
   * Tries to remove the specified value from the domain of this variable.
   * 
   * <p>
   * If this {@code IntVar} is not assigned and contains {@code value} then all
   * the propagators registered on this {@code IntVar} are scheduled for 
   * propagation as specified in {@link #watchAssign watchAssign}, 
   * {@link #watchBounds watchBounds}, and {@link #watchChange watchChange}.
   * This method does nothing if {@code value} is not contained in the domain
   * of this {@code IntVar}. 
   * </p>
   * 
   * @param  value  the value to remove from the domain of this {@code IntVar}
   * @return        {@code true} if and only if this {@code IntVar} is not
   *                assigned to {@code value}
   * @see           PropagationQueue#enqueue enqueue
   * @see           #watchAssign watchAssign
   * @see           #watchBounds watchBounds
   * @see           #watchChange watchChange
   */
  public abstract boolean remove(int value);

  /** 
   * Tries to remove all the values that are smaller than the specified value.
   * 
   * <p>
   * If {@code value} is greater than {@link #min} and smaller or equal to 
   * {@link #max} then all the propagators registered on this {@code IntVar} 
   * are scheduled for propagation as specified in 
   * {@link #watchAssign watchAssign}, {@link #watchBounds watchBounds}, and 
   * {@link #watchChange watchChange}. This method does nothing if 
   * {@code value} is smaller or equal to {@link #min}. 
   * </p>
   * 
   * @param  value  the value such that all the smaller values contained in the
   *                domain of this {@code IntVar} must be removed
   * @return        {@code true} if and only if {@code value} is lower or equal
   *                to {@link #max}
   * @see           PropagationQueue#enqueue enqueue
   * @see           #watchAssign watchAssign
   * @see           #watchBounds watchBounds
   * @see           #watchChange watchChange
   */
  public abstract boolean updateMin(int value);

  /** 
   * Tries to remove all the values that are greater than the specified value.
   * 
   * <p>
   * If {@code value} is lower than {@link #max} and greater or equal to 
   * {@link #min} then all the propagators registered on this {@code IntVar} 
   * are scheduled for propagation as specified in 
   * {@link #watchAssign watchAssign}, {@link #watchBounds watchBounds}, and 
   * {@link #watchChange watchChange}. This method does nothing if 
   * {@code value} is greater or equal to {@link #max}. 
   * </p>
   * 
   * @param  value  the value such that all the greater values contained in the
   *                domain of this {@code IntVar} must be removed
   * @return        {@code true} if and only if {@code value} is greater or 
   *                equal to {@link #min}
   * @see           PropagationQueue#enqueue enqueue
   * @see           #watchAssign watchAssign
   * @see           #watchBounds watchBounds
   * @see           #watchChange watchChange
   */
  public abstract boolean updateMax(int value);

  /** 
   * Registers the propagator on domain assignations.
   * 
   * <p>
   * The propagator will be scheduled for propagation using the 
   * {@link PropagationQueue#enqueue enqueue} method each time this 
   * {@code IntVar} is assigned to a value.
   * </p>
   * 
   * @param propagator  propagator to be registered on this {@code IntVar}
   * @see               #assign assign
   * @see               #remove remove
   * @see               #updateMin updateMin
   * @see               #updateMax updateMax
   */
  public abstract void watchAssign(Propagator propagator);

  /** 
   * Registers the propagator on bound changes. 
   * 
   * <p>
   * The propagator will be scheduled for propagation using the 
   * {@link PropagationQueue#enqueue enqueue} method each time the value of
   * {@link #min} or {@link #max} is removed from the domain of this 
   * {@code IntVar}.
   * </p>
   * 
   * @param propagator  propagator to be registered on this {@code IntVar}
   * @see               #assign assign
   * @see               #remove remove
   * @see               #updateMin updateMin
   * @see               #updateMax updateMax
   */
  public abstract void watchBounds(Propagator propagator);
  
  /** 
   * Registers the propagator on the domain changes. 
   * 
   * <p>
   * The propagator will be scheduled for propagation using the 
   * {@link PropagationQueue#enqueue enqueue} method each time a value is 
   * removed from the domain of this {@code IntVar}.
   * </p>
   * 
   * @param propagator  propagator to be registered on this {@code IntVar}
   * @see               #assign assign
   * @see               #remove remove
   * @see               #updateMin updateMin
   * @see               #updateMax updateMax
   */
  public abstract void watchChange(Propagator propagator);
  

  /** 
   * Copies the values contained in the domain of this {@code IntVar} in the 
   * specified array.
   * 
   * <p>
   * This function gives no guarantee on the order of the copied values.
   * </p>
   * 
   * @return the size of the domain
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
