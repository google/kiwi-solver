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
package kiwi.trailed;

import kiwi.core.Change;
import kiwi.core.Trail;
import kiwi.util.StackInt;

public class TrailedInt implements Change {

  private final Trail trail;

  private final StackInt oldValues = new StackInt();

  private int currentValue;

  private long timestamp = -1L;

  public TrailedInt(Trail trail, int initValue) {
    this.trail = trail;
    currentValue = initValue;
  }

  public void undo() {
    currentValue = oldValues.pop();
  }

  public int getValue() {
    return currentValue;
  }

  public void setValue(int value) {
    if (timestamp != trail.getTimestamp()) {
      timestamp = trail.getTimestamp();
      oldValues.push(currentValue);
      trail.store(this);
    }
    currentValue = value;
  }
}
