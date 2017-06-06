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

import java.util.function.Predicate;

import kiwi.propagation.PropagationQueue;
import kiwi.trail.Trail;
import kiwi.util.Action;
import kiwi.util.Stack;

public class DFSearch {

  private final PropagationQueue pQueue;
  private final Trail trail;

  private final Stack<Decision> decisions = new Stack<>();
  private final Stack<Action> solutionActions = new Stack<>();
  
  private Objective objective = null;

  public DFSearch(PropagationQueue pQueue, Trail trail) {
    this.pQueue = pQueue;
    this.trail = trail;
  }
  
  public void addSolutionAction(Action action) {
    solutionActions.push(action);
  }
  
  public void foundSolution(SearchStats stats) {
    stats.nSolutions++;
    solutionActions.forEach(action -> action.execute());
    if (objective != null) {
      objective.tighten();
    }
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
  public SearchStats search(Heuristic heuristic, Predicate<SearchStats> stopCondition) {
    SearchStats stats = new SearchStats();
    
    stats.startTime = System.currentTimeMillis();

    // Perform root propagation.
    if (!propagate()) {
      return stats;
    }

    // Check if the root is a solution. 
    // If it is not, push the next decisions to try on the decisions stack.
    if (heuristic.pushDecisions(decisions)) {
      foundSolution(stats);
      return stats;
    }

    // Save the root state.
    trail.newLevel();

    // Start the search.
    while (!decisions.isEmpty() && !stopCondition.test(stats)) {
      stats.nNodes++;

      // Apply the next decision and propagate.
      if (!decisions.pop().apply() || !pQueue.propagate()) {
        trail.undoLevel();
        stats.nFails++;
        continue;
      }

      // Check if the node is a solution. If not, it pushes
      // the next decisions to try on the decisions stack.
      if (heuristic.pushDecisions(decisions)) {
        foundSolution(stats);
        trail.undoLevel();
        continue;
      }

      // Save the node state.
      trail.newLevel();
    }

    // The search is complete if there's no remaining decisions to be applied.
    stats.completed = decisions.isEmpty();
    
    // Clear the remaining decisions (if the search is incomplete) and restore
    // the state of the root node.
    trail.undoAll();
    decisions.clear();
    
    // We consider that restoring the root state is part of the search hence 
    // we only compute the total time once the search has been reset.
    stats.searchTime = System.currentTimeMillis() - stats.startTime;
    
    return stats;
  }
}
