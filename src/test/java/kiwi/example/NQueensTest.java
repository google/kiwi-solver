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
import kiwi.variable.IntVar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NQueensTest {
  
  @Test
  public void test3Queens() {
    assertEquals(0, solveNQueens(3));
  }
  
  @Test
  public void test4Queens() {
    assertEquals(2, solveNQueens(4) );
  }
  
  @Test
  public void test8Queens() {
    assertEquals(92, solveNQueens(8));
  }
  
  @Test
  public void test10Queens() {
    assertEquals(724, solveNQueens(10));
  }

  private int solveNQueens(int n) {   
    Solver solver = new Solver();
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
    solver.useBinaryFirstFail(queens);
    int[] nSols = new int[1];
    solver.onSolution(() -> nSols[0]++);
    solver.solve();
    return nSols[0];
  }
}
