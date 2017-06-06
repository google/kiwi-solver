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
package kiwi;

import java.util.function.Predicate;

import kiwi.propagation.PropagationQueue;
import kiwi.propagation.Propagator;
import kiwi.search.DFSearch;
import kiwi.search.Heuristic;
import kiwi.search.Objective;
import kiwi.search.SearchStats;
import kiwi.trail.Trail;
import kiwi.util.Action;
import kiwi.variable.IntVar;
import kiwi.variable.IntVarImpl;
import kiwi.variable.IntVarSingleton;

public class Solver {

  private final Trail trail;
  private final PropagationQueue pQueue;
  private final DFSearch search;

  private boolean feasible = true;

  public Solver() {
    this.trail = new Trail();
    this.pQueue = new PropagationQueue();
    this.search = new DFSearch(pQueue, trail);
    this.feasible = true;
  }

  public boolean isFeasible() {
    return feasible;
  }
  
  public Trail trail() {
    return trail;
  }

  public void setObjective(Objective obj) {
    this.search.setObjective(obj);
  }

  public void onSolution(Action action) {
    search.addSolutionAction(action);
  }

  public SearchStats solve(Heuristic heuristic, Predicate<SearchStats> stopCondition) {
    return search.search(heuristic, stopCondition);
  }

  public SearchStats solve(Heuristic heuristic) {
    return solve(heuristic, s -> false);
  }

  public IntVar intVar(int min, int max) {
    return new IntVarImpl(pQueue, trail, min, max);
  }

  public IntVar intVar(int value) {
    return new IntVarSingleton(pQueue, trail, value);
  }

  public IntVar intVar(int[] values) {
    return new IntVarImpl(pQueue, trail, values);
  }

  public boolean add(Propagator propagator) {
    feasible = feasible && propagator.setup() && pQueue.propagate();
    return feasible;
  }
}
