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

public class StackInt {

	private int[] array = new int[16];
	private int index = 0;
	
	public int getSize() { return index; }
	
	public boolean isEmpty() { return index == 0; }
	
	public int top() { return array[index - 1]; }
	
	public void push(int elem) {
		if (index == array.length) growStack();
		array[index] = elem;
		index++;
	}
	
	public int pop() { return array[--index]; }
	
	private void growStack() {
		int[] newArray = new int[index * 2];
		System.arraycopy(array, 0, newArray, 0, index);
		array = newArray;
	}
}
