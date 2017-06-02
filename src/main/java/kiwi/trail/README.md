# Trail and state restoration

This package contains the system responsible for storing and restoring the state of the solver. 

Trailing is the prominent approach used to implement such restoration mechanism. It basically consists to maintain the sequence of changes -- the trail -- that occurred on each object. The previous state of an object can thus be restored by undoing the changes in anti-chronological order. 

## Changes

Each time a state changing operation is performed, the necessary information to undo this operation is stored on the trail. 
In Kiwi, we chose to directly store the functions responsible of undoing state changes as first class objects, i.e., as closures.
Each stateful object thus relies on its own restoration mechanism which is handled by the closure itself.

## The Trail

Our trailing system is implemented with two stacks:

- The first is a stack of Change objects that represents the trail. It is sorted chronologically such that the most recent change is on top of the stack (the root node being the empty stack).
- The second stack maps each node of the current branch to its corresponding prefix in the trail. Thanks to the incrementality of the trail, only the position of the last change that lead to a node needs to be stored to characterize the whole sequence of changes that lead to this node. 
