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

import kiwi.propagation.PropagQueue;
import kiwi.trail.Trail;
import kiwi.util.Action;
import kiwi.util.Stack;

public class DFSearch {

  private final PropagQueue pQueue;
  private final Trail trail;

  private final Stack<Decision> decisions = new Stack<>();
  private final Stack<Action> solutionActions = new Stack<>();

  private int nFails;
  private int nNodes;
  
  private Objective objective = null;

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
  
  public void addSolutionAction(Action action) {
    solutionActions.push(action);
  }
  
  public void foundSolution() {
    if (objective != null) {
      objective.tighten();
    }
    solutionActions.forEach(action -> action.execute());
  }
  
  public void setObjective(Objective obj) {
    this.objective = obj;
  }
  
  private boolean propagate() {
    // Propagate the objective only if it is not null.
    boolean feasible = objective == null || objective.propagate();
    // Propagate the propagators only if the problem is still feasible.
    return feasible && pQueue.propagate();
  }

  /** Starts the search */
  public void search(Heuristic heuristic) {
    nNodes = 0;
    nFails = 0;

    // Perform root propagation.
    if (!propagate()) {
      return;
    }

    // Check if the root is a solution. 
    // If it is not, push the next decisions to try on the decisions stack.
    if (heuristic.pushDecisions(decisions)) {
      foundSolution();
      return;
    }

    // Save the root state.
    trail.newLevel();

    // Start the search.
    while (!decisions.isEmpty()) {
      nNodes++;

      // Apply the next decision and propagate.
      if (!decisions.pop().apply() || !pQueue.propagate()) {
        trail.undoLevel();
        nFails++;
        continue;
      }

      // Check if the node is a solution. If not, it pushes
      // the next decisions to try on the decisions stack.
      if (heuristic.pushDecisions(decisions)) {
        foundSolution();
        trail.undoLevel();
        continue;
      }

      // Save the node state.
      trail.newLevel();
    }

    decisions.clear();
    trail.undoAll();
  }
}
