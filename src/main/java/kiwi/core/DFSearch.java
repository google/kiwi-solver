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

import kiwi.util.Stack;

public class DFSearch {

  private final PropagQueue pQueue;
  private final Trail trail;

  private final Stack<Decision> decisions = new Stack<>();

  private int nFails;
  private int nNodes;

  public DFSearch(PropagQueue pQueue, Trail trail) {
    this.pQueue = pQueue;
    this.trail = trail;
  }

  /** Returns the number of explored nodes */
  public int getNodes() {
    return nNodes;
  }

  /** Returns the number of explored failed nodes */
  public int getFails() {
    return nFails;
  }

  /** Starts the search */
  public void search(Heuristic heuristic) {
    nNodes = 0;
    nFails = 0;

    // Perform root propagation.
    if (!pQueue.propagate())
      return;

    // Check if the root is a solution. If it is not, push the next decisions to try on the 
    // decisions stack.
    if (heuristic.pushDecisions(decisions))
      return;

    // Save the root state.
    trail.newLevel();

    // Start the search.
    while (!decisions.isEmpty()) {
      nNodes++;

      // Apply the next decision.
      decisions.pop().apply();

      // Propagate
      if (!pQueue.propagate()) {
        trail.undoLevel();
        nFails++;
        continue;
      }

      // Check if the node is a solution. If not, it pushes
      // the next decisions to try on the decisions stack.
      if (heuristic.pushDecisions(decisions)) {
        trail.undoLevel();
        break;
      }

      // Save the node state.
      trail.newLevel();
    }

    decisions.clear();
    trail.undoAll();
  }
}
