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

/**
 * Superclass to be instantiated by any search decision.
 * 
 * <p>
 * A {@code Decision} is taken by the search heuristic to drive the tree search
 * on specific directions. A {@code Decision} typically impacts directly the 
 * domain of an {@code IntVar} by removing values from its domain.
 * </p>
 */
public interface Decision {
  /** 
   * Applies the decision. 
   * 
   * @return {@code true} if and only if the decision did not failed directly.
   */
  public boolean apply();
}