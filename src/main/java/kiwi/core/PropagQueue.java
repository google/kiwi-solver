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
package kiwi.core;

import java.util.ArrayDeque;

/** PropagQueue */
public class PropagQueue {

  private final ArrayDeque<Propagator> queue = new ArrayDeque<Propagator>();

  /** Enqueues the propagator for propagation if needed. 
   * 
   *  Side effect: this method sets the inQueue boolean of propagator to true. 
   */
  public void enqueue(Propagator propagator) {
    if (!propagator.enqueued) {
      queue.addLast(propagator);
      propagator.enqueued = true;
    }
  }

  // Propagate all the propagator in the queue. Note that propagation is
  // likely to enqueue additional propagator while it is running. The method
  // return true if the propagation succeeded and false otherwise. The enqueued
  // boolean of all the propagator in the queue is set to false no matter if
  // the propagation succeeded or not.
  public boolean propagate() {
    boolean feasible = true;
    while (!queue.isEmpty()) {
      Propagator propagator = queue.removeFirst();
      // Propagate only if unfailed.
      feasible = feasible && propagator.propagate();
      // Dequeue the propagator after propagation to prevent
      // it from enqueuing itself.
      propagator.enqueued = false;
    }
    return feasible;
  }
}
