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

import kiwi.core.PropagQueue;
import kiwi.core.Propagator;
import kiwi.core.Trail;
import kiwi.trailed.TrailedInt;
import kiwi.util.Array;
import kiwi.util.Stack;

public class IntVarImpl implements IntVar {
	
	private final PropagQueue pQueue;
	private final Trail trail;
	
	private final int initMin;
	private final int initMax;
	
	private final TrailedInt minT;
	private final TrailedInt maxT;
	private final TrailedInt sizeT;
	
	private final int[] values;
	private final int[] positions;

	private final Stack<Propagator> changeWatchers = new Stack<Propagator>();
	private final Stack<Propagator> assignWatchers = new Stack<Propagator>();
	private final Stack<Propagator> boundsWatchers = new Stack<Propagator>();
	
	public IntVarImpl(PropagQueue pQueue, Trail trail, int initMin, int initMax) {
		this.pQueue = pQueue;
		this.trail = trail;
		this.initMin = initMin;
		this.initMax = initMax;
		this.minT = new TrailedInt(trail, initMin);
		this.maxT = new TrailedInt(trail, initMax);
		final int size = initMax - initMin + 1;
		this.sizeT = new TrailedInt(trail, size);
		this.values = Array.tabulate(size, i -> i);
		this.positions = values.clone();
	}
	
	public PropagQueue getPropagQueue() { return pQueue; };
	
	public Trail getTrail() { return trail; };

	public int getMin() { return minT.getValue(); }

	public int getMax() { return maxT.getValue(); }

	public int getSize() { return sizeT.getValue(); }
	
	public boolean isAssigned() { return sizeT.getValue() == 1; }
	
	public boolean contains(int value) {
		if (value < initMin) return false;
		if (value > initMax) return false;
		return positions[value - initMin] < sizeT.getValue();
	}
	
	private void swap(int pos1, int pos2) {
		int v1 = values[pos1];
		int v2 = values[pos2];
		int id1 = v1 - initMin;
		int id2 = v2 - initMin;
		values[pos1] = v2;
		values[pos2] = v1;
		positions[id1] = pos2;
		positions[id2] = pos1;
	}
	
	public boolean assign(int value) {
		if (value < minT.getValue()) return false;
		if (value > maxT.getValue()) return false;
		int size = sizeT.getValue();
		
		// We know that the variable is already assigned to value.
		if (size == 1) return true;
		
		// The value is not in the domain.
		int position = positions[value - initMin];
		if (position >= size) return false;
		
		// Remove the value and update the domain.
		swap(position, 0);
		minT.setValue(value);
		maxT.setValue(value);
		sizeT.setValue(1);
	    awakeAssign();
	    awakeBounds();
	    awakeChange();
		return true;
	}
	
	public boolean remove(int value) {		
		// The value is already removed if it is not contained
		// in the range [min, max].
		int min = minT.getValue();
		int max = maxT.getValue();
		if (value < min || value > max) return true;
		
		// We cannot remove the value if the variable is 
		// already assigned to it.
		int size = sizeT.getValue();
		if (size == 1) return false;
		
		// Check that the value is not already removed.
		int position = positions[value - initMin];
		if (position >= size) return true;
		
	    // At this point, we know that the value is in the domain and that
	    // the domain is not assigned. We thus remove the value.
	    size--;
	    swap(position, size);
	    sizeT.setValue(size);
	    
	    // We now have to notify the propagators and to update one of the 
	    // bound if necessary.
	    if (size == 1) {
	    	// Removing the value assigned the variable.
	    	if (value == min) minT.setValue(max);
	    	else maxT.setValue(min);
	    	awakeAssign();
	    	awakeBounds();	    	
	    } else if (min == value) {
	    	// We removed the minimum value and thus have to find the new one.
	    	int i = min - initMin + 1;
	    	while (positions[i] >= size) i++;
	    	minT.setValue(i + initMin);
	    	awakeBounds();	
	    } else if (max == value) {
	    	// We removed the maximum value and thus have to find the new one.
	    	int i = max - initMin - 1;
	    	while (positions[i] >= size) i--;
	    	maxT.setValue(i + initMin);
	    	awakeBounds();	
	    }
		awakeChange();
		return true;
	}
	
	public boolean updateMin(int value) {
		int max = maxT.getValue();
		if (max < value) return false;
		if (value == max) return true;
		int min = minT.getValue();
		if (value <= min) return true;
		// Remove values.
		int i = min - initMin;
		int size = sizeT.getValue();
		while (i < value - initMin) {
			int position = positions[i];
			if (position < size) swap(position, --size);
			i++;
		}
		// Search new min.
		while (size <= positions[i]) i++;
		
		// Update the domain.
		minT.setValue(i + initMin);
		sizeT.setValue(size);

	    // Awake propagators.
		if (size == 1) awakeAssign();
		awakeBounds();
		awakeChange();
		return true;
	}
	
	public boolean updateMax(int value) {
		int min = minT.getValue();
		if (min > value) return false;
		if (value == min) return true;
		int max = maxT.getValue();
		if (value >= max) return true;
		// Remove values.
		int i = max - initMin;
		int size = sizeT.getValue();
		while (i > value - initMin) {
			int position = positions[i];
			if (position < size) swap(position, --size);
			i--;
		}
		// Search new max.
		while (size <= positions[i]) i--;

		// Update the domain.
		maxT.setValue(i + initMin);
		sizeT.setValue(size);

	    // Awake propagators.
		if (size == 1) awakeAssign();
		awakeBounds();
		awakeChange();
		return true;
	}
	
	public int copyDomain(int[] array) {
		int size = sizeT.getValue();
		System.arraycopy(values, 0, array, 0, size);
		return size;
	}

	public void watchChange(Propagator propagator) { changeWatchers.push(propagator); }

	public void watchAssign(Propagator propagator) { boundsWatchers.push(propagator); }

	public void watchBounds(Propagator propagator) { changeWatchers.push(propagator); }

	private void awakeAssign() { assignWatchers.forEach(p -> pQueue.enqueue(p)); }
	
	private void awakeBounds() { boundsWatchers.forEach(p -> pQueue.enqueue(p)); }
	
	private void awakeChange() { changeWatchers.forEach(p -> pQueue.enqueue(p)); }
}
