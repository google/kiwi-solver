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
package kiwi.example;

import kiwi.core.DFSearch;
import kiwi.core.Heuristic;
import kiwi.core.PropagQueue;
import kiwi.core.Trail;
import kiwi.heuristic.BinaryVarVal;
import kiwi.propagator.AllDifferent;
import kiwi.variable.IntVar;
import kiwi.variable.IntVarImpl;
import kiwi.variable.IntVarOffset;

public class NQueens {

  public static void main(String[] args) {

    PropagQueue pQueue = new PropagQueue();
    Trail trail = new Trail();
    DFSearch search = new DFSearch(pQueue, trail);

    int n = 10;

    IntVar[] queens = new IntVar[n];
    IntVar[] queensUp = new IntVar[n];
    IntVar[] queensDown = new IntVar[n];

    for (int i = 0; i < n; i++) {
      queens[i] = new IntVarImpl(pQueue, trail, 0, n - 1);
      queensUp[i] = new IntVarOffset(queens[i], i);
      queensDown[i] = new IntVarOffset(queens[i], -i);
    }

    AllDifferent allDiff1 = new AllDifferent(queens);
    AllDifferent allDiff2 = new AllDifferent(queensUp);
    AllDifferent allDiff3 = new AllDifferent(queensDown);

    allDiff1.setup();
    allDiff2.setup();
    allDiff3.setup();

    pQueue.enqueue(allDiff1);
    pQueue.enqueue(allDiff2);
    pQueue.enqueue(allDiff3);

    Heuristic heuristic = new BinaryVarVal(queens, i -> queens[i].getSize());

    long t = System.currentTimeMillis();
    search.search(heuristic);
    long time = System.currentTimeMillis() - t;

    System.out.println("time : " + time);
    System.out.println("nodes: " + search.getNodes());
  }
}
