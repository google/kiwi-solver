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
package kiwi.util;

import java.util.function.Consumer;

public class Stack<T> {

  private Object[] array = new Object[16];

  private int index = 0;

  public int getSize() {
    return index;
  }

  public boolean isEmpty() {
    return index == 0;
  }

  @SuppressWarnings("unchecked")
  public T top() {
    return (T) array[index - 1];
  }

  public void push(T elem) {
    if (index == array.length) {
      growStack();
    }
    array[index] = (Object) elem;
    index++;
  }

  public void clear() {
    while (index > 0) {
      index--;
      array[index] = null;
    }
  }

  @SuppressWarnings("unchecked")
  public T pop() {
    return (T) array[--index];
  }

  @SuppressWarnings("unchecked")
  public void forEach(Consumer<T> c) {
    for (int i = 0; i < index; i++) {
      c.accept((T) array[i]);
    }
  }

  private void growStack() {
    Object[] newArray = new Object[index * 2];
    System.arraycopy(array, 0, newArray, 0, index);
    array = newArray;
  }
}
