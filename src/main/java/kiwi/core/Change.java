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

/**
 * Superclass to be instantiated by any trailed change.
 * 
 * <p>
 * A {@code Change} represents any kind of undoable operation that affects the
 * state of the solver, its variables, or its propagators. A {@code Change} is
 * typically trailed and undone when a backtrack occurs.
 * <p>
 */
public interface Change {
  /** Undoes the change */
  public void undo();
}
