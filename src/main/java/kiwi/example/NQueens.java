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

import kiwi.Solver;
import kiwi.search.BinaryVarVal;
import kiwi.variable.IntVar;

public class NQueens {

  public static void main(String[] args) {
    
    Solver solver = new Solver();

    int n = 10;
    IntVar[] queens = new IntVar[n];
    IntVar[] queensUp = new IntVar[n];
    IntVar[] queensDown = new IntVar[n];

    for (int i = 0; i < n; i++) {
      queens[i] = solver.intVar(0, n - 1);
      queensUp[i] = solver.offset(queens[i], i);
      queensDown[i] = solver.offset(queens[i], -i);
    }

    solver.allDifferent(queens);
    solver.allDifferent(queensUp);
    solver.allDifferent(queensDown);

    solver.setHeuristic(new BinaryVarVal(queens, i -> queens[i].size()));

    solver.onSolution(() -> {
      System.out.println("Solution: ");
      for (IntVar q: queens) {
        System.out.print(q);
      }
      System.out.println();
    });
    
    solver.solve();
  }
}
