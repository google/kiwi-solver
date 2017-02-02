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
import kiwi.util.StackInt;

/** 
 * {@code Trail} contains the chronological sequences of changes to undo. 
 */
public class Trail {

  private long timestamp = 0L;

  private final Stack<Change> changes = new Stack<Change>();
  private final StackInt levels = new StackInt();

  public long getTimestamp() {
    return timestamp;
  }

  public void store(Change change) {
    changes.push(change);
  }

  public void newLevel() {
    levels.push(changes.getSize());
    timestamp++;
  }

  public void undoLevel() {
    if (levels.getSize() > 0)
      undoUntil(levels.pop());
    timestamp++;
  }

  public void undoAll() {
    while (levels.getSize() > 0) {
      undoUntil(levels.pop());
    }
    timestamp++;
  }

  private void undoUntil(int size) {
    while (changes.getSize() > size) {
      changes.pop().undo();
    }
  }
}
