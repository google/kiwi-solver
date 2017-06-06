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

import kiwi.util.Stack;

/**
 * Superclass to be instantiated by any search heuristic.
 */
public interface Heuristic {

  /**
   * Pushes the next decisions to be taken on top of the decisions stack. 
   * First check that the current state of the problem is a leaf. If not, it
   * adds the next decisions to applyon top of decisions stack. The last pushed
   * decision will be the first to be applied.
   * 
   * Note that the behavior of the solver is not determined if the content of
   * the decisions stack is changed.
   * 
   * @param decisions
   *          The stack of decisions to be taken. The new decisions must be
   *          pushed on top of the stack. The method should not remove any 
   *          decision contained in the stack.
   * 
   * @return true if the decision stack is unchanged; false otherwise.
   */
  public boolean pushNextDecisions(Stack<Decision> decisions);
}
