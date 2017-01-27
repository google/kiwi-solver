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
package kiwi.variable;

import kiwi.core.Propagator;
import kiwi.core.PropagQueue;
import kiwi.core.Trail;

public interface IntVar {
	
	public PropagQueue getPropagQueue();
	
	public Trail getTrail();
	
	public int getMin();
	
	public int getMax();
	
	public int getSize();
	
	public boolean isAssigned();
	
	public boolean contains(int value);
	
	public boolean assign(int value);
	
	public boolean remove(int value);

	public boolean updateMin(int value);
	
	public boolean updateMax(int value);

	public void watchChange(Propagator propagator);

	public void watchAssign(Propagator propagator);

	public void watchBounds(Propagator propagator);
	
	public int copyDomain(int[] array);
}
