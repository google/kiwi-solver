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
package kiwi.variables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import kiwi.Solver;
import kiwi.trail.Trail;
import kiwi.variable.IntVar;

import org.junit.Test;

public abstract class IntVarTest {

  public abstract IntVar intVar(Solver solver, int min, int max);

  public abstract IntVar intVar(Solver solver, int[] values);

  // Returns true if the variable contains all the values.
  private boolean containsAll(IntVar x, int... values) {
    for (int i = 0; i < values.length; i++) {
      if (!x.contains(values[i])) {
        return false;
      }
    }
    return true;
  }

  // All values should be contained in the initial domain
  @Test
  public void test1() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 10);
    assertEquals(x.size(), 6);
    assertTrue(containsAll(x, 5, 6, 7, 8, 9, 10));
  }

  // All values should be contained in the initial domain (sparse)
  @Test
  public void test1a() {
    Solver solver = new Solver();
    int[] values = new int[]{-10, 0, 10, 100};
    IntVar x = intVar(solver, values);
    assertEquals(x.size(), 4);
    assertTrue(containsAll(x, values));
  }

  // Contains should return true if value is in the domain
  @Test
  public void test2() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertTrue(x.contains(5));
    assertTrue(x.contains(10));
    assertTrue(x.contains(15));
  }

  // Contains should return false if value is not in the domain
  @Test
  public void test3() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertFalse(x.contains(-1000));
    assertFalse(x.contains(-100));
    assertFalse(x.contains(4));
    assertFalse(x.contains(16));
    assertFalse(x.contains(100));
    assertFalse(x.contains(1000));
  }

  // UpdateMin should remove all values lesser than min
  @Test
  public void test4() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertEquals(x.size(), 11);
    assertTrue(x.updateMin(10));
    assertEquals(x.size(), 6);
    assertEquals(x.min(), 10);
    assertTrue(containsAll(x, 10, 11, 12, 13, 14, 15));
  }

  // UpdateMin with a lesser or equal value than min should not impact the
  // domain
  @Test
  public void test5() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertTrue(x.updateMin(4));
    assertEquals(x.size(), 11);
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 15);
    assertTrue(x.updateMin(5));
    assertEquals(x.size(), 11);
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 15);
  }

  // UpdateMin to max should assign max
  @Test
  public void test6() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertTrue(x.updateMin(15));
    assertEquals(x.size(), 1);
    assertTrue(x.isAssigned());
    assertTrue(x.contains(15));
  }

  // UpdateMin greater than max should fail
  @Test
  public void test7() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertFalse(x.updateMin(20));
  }

  // UpdateMax should adjust the maximum value and the size
  @Test
  public void test8() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertEquals(x.size(), 11);
    assertTrue(x.updateMax(10));
    assertEquals(x.size(), 6);
    assertEquals(x.max(), 10);
    assertTrue(containsAll(x, 5, 6, 7, 8, 9, 10));
  }

  // UpdateMax with a greater or equal value than max should not impact the
  // domain
  @Test
  public void test9() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertTrue(x.updateMax(20));
    assertEquals(x.size(), 11);
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 15);
    assertTrue(x.updateMax(15));
    assertEquals(x.size(), 11);
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 15);
  }

  // UpdateMax to min should assign min
  @Test
  public void test10() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertTrue(x.updateMax(5));
    assertEquals(x.size(), 1);
    assertTrue(x.isAssigned());
    assertTrue(x.contains(5));
  }

  // UpdateMax lesser than min should fail
  @Test
  public void test11() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertFalse(x.updateMax(0));
  }

  // Bounds should be restored when a backtrack occurs
  @Test
  public void test13() {
    Solver solver = new Solver();
    Trail trail = solver.trail();
    IntVar x = intVar(solver, 5, 15);
    trail.newLevel();
    assertTrue(x.updateMax(10));
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 10);
    assertEquals(x.size(), 6);
    assertTrue(containsAll(x, 5, 6, 7, 8, 9, 10));
    trail.newLevel();
    assertTrue(x.updateMax(9));
    assertTrue(x.updateMin(6));
    assertEquals(x.min(), 6);
    assertEquals(x.max(), 9);
    assertEquals(x.size(), 4);
    assertTrue(containsAll(x, 6, 7, 8, 9));
    trail.newLevel();
    trail.undoLevel();
    assertEquals(x.min(), 6);
    assertEquals(x.max(), 9);
    assertEquals(x.size(), 4);
    assertTrue(containsAll(x, 6, 7, 8, 9));
    trail.undoLevel();
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 10);
    assertEquals(x.size(), 6);
    assertTrue(containsAll(x, 5, 6, 7, 8, 9, 10));
    trail.undoLevel();
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 15);
    assertEquals(x.size(), 11);
    assertTrue(containsAll(x, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
  }

  // Assign should make min equal to max
  @Test
  public void test14() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertTrue(x.assign(10));
    assertTrue(x.contains(10));
    assertEquals(x.min(), 10);
    assertEquals(x.max(), 10);
  }

  // Assign should reduce the size to 1
  @Test
  public void test15() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertTrue(x.assign(10));
    assertEquals(x.size(), 1);
  }

  // Assign an out of bounds value should fail
  @Test
  public void test16() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 15);
    assertFalse(x.assign(20));
  }

  // Removed values should not be contained in the domain anymore
  @Test
  public void test17() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 10);
    assertTrue(x.remove(5));
    assertFalse(x.contains(5));
    assertTrue(x.remove(7));
    assertFalse(x.contains(7));
    assertTrue(x.remove(8));
    assertFalse(x.contains(8));
  }

  // Remove a value should reduce the size
  @Test
  public void test18() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 10);
    int size = x.size();
    assertTrue(x.remove(5));
    assertEquals(x.size(), size - 1);
    assertTrue(x.remove(5));
    assertEquals(x.size(), size - 1);
    assertTrue(x.remove(6));
    assertEquals(x.size(), size - 2);
  }

  // Remove a removed value should not impact the domain
  @Test
  public void test19() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 10);
    int size = x.size();
    assertTrue(x.remove(4));
    assertEquals(x.size(), size);
    assertTrue(x.remove(11));
    assertEquals(x.size(), size);
  }

  // Remove the minimal value should change the minimum value
  @Test
  public void test20() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 10);
    assertTrue(x.remove(5));
    assertEquals(x.min(), 6);
    assertTrue(x.remove(6));
    assertTrue(x.remove(7));
    assertEquals(x.min(), 8);
    assertTrue(x.remove(10));
    assertEquals(x.min(), 8);
  }

  // Remove all but one value should assign that value
  @Test
  public void test21() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 5, 10);
    assertTrue(x.remove(5));
    assertTrue(x.contains(7));
    assertTrue(x.remove(6));
    assertTrue(x.contains(7));
    assertTrue(x.remove(9));
    assertTrue(x.contains(7));
    assertTrue(x.remove(10));
    assertTrue(x.contains(7));
    assertTrue(x.remove(8));
    assertTrue(x.contains(7));
    assertTrue(x.isAssigned());
  }

  // Removed values should be restored when a backtrack occurs
  @Test
  public void test22() {
    Solver solver = new Solver();
    Trail trail = solver.trail();
    IntVar x = intVar(solver, 5, 10);
    trail.newLevel();
    assertTrue(x.remove(5));
    assertTrue(x.remove(6));
    trail.newLevel();
    assertTrue(x.remove(9));
    trail.newLevel();
    assertTrue(x.remove(8));
    assertFalse(x.contains(5));
    assertFalse(x.contains(6));
    assertTrue(x.contains(7));
    assertFalse(x.contains(8));
    assertFalse(x.contains(9));
    assertTrue(x.contains(10));
    trail.undoLevel();
    assertFalse(x.contains(5));
    assertFalse(x.contains(6));
    assertTrue(x.contains(7));
    assertTrue(x.contains(8));
    assertFalse(x.contains(9));
    assertTrue(x.contains(10));
    trail.undoLevel();
    assertFalse(x.contains(5));
    assertFalse(x.contains(6));
    assertTrue(x.contains(7));
    assertTrue(x.contains(8));
    assertTrue(x.contains(9));
    assertTrue(x.contains(10));
    trail.undoLevel();
    assertTrue(x.contains(5));
    assertTrue(x.contains(6));
    assertTrue(x.contains(7));
    assertTrue(x.contains(8));
    assertTrue(x.contains(9));
    assertTrue(x.contains(10));
  }

  // Remove the assigned value should fail
  @Test
  public void test23() {
    Solver solver = new Solver();
    IntVar x = intVar(solver, 10, 10);
    assertFalse(x.remove(10));
  }

  // UpdateMin should adjust the minimum value and the size (sparse)
  @Test
  public void test24() {
    Solver solver = new Solver();
    int[] values = new int[]{10, 11, 15, 16, 17, 20, 21, 25};
    IntVar x = intVar(solver, values);
    assertTrue(x.updateMin(12));
    assertEquals(x.size(), 6);
    assertEquals(x.min(), 15);
  }

  // UpdateMin should remove all values lesser than min (sparse)
  @Test
  public void test25() {
    Solver solver = new Solver();
    int[] values = new int[]{10, 11, 15, 16, 17, 20, 21, 25};
    IntVar x = intVar(solver, values);
    assertTrue(x.updateMin(16));
    assertFalse(x.contains(10));
    assertFalse(x.contains(11));
    assertFalse(x.contains(15));
  }

  // UpdateMax should adjust the maximum value and the size (sparse)
  @Test
  public void test26() {
    Solver solver = new Solver();
    int[] values = new int[]{10, 11, 15, 16, 17, 20, 21, 25};
    IntVar x = intVar(solver, values);
    assertTrue(x.updateMax(19));
    assertEquals(x.size(), 5);
    assertEquals(x.max(), 17);
  }

  // UpdateMax should remove all values greater than max (sparse)
  @Test
  public void test27() {
    Solver solver = new Solver();
    int[] values = new int[]{10, 11, 15, 16, 17, 20, 21, 25};
    IntVar x = intVar(solver, values);
    assertTrue(x.updateMax(17));
    assertFalse(x.contains(20));
    assertFalse(x.contains(21));
    assertFalse(x.contains(25));
  }

  // Copy domain and to Array
  @Test
  public void test28() {
    Solver solver = new Solver();
    int[] values1 = new int[]{10, 11, 15, 16, 17, 20, 21, 25};
    int[] domain1 = new int[values1.length];
    IntVar x = intVar(solver, values1);
    assertTrue(containsAll(x, values1));
    assertEquals(x.size(), values1.length);
    assertEquals(x.size(), x.copyDomain(domain1));
    assertTrue(containsAll(x, domain1));
    assertTrue(x.remove(11));
    assertTrue(x.remove(17));
    assertTrue(x.remove(25));
    assertEquals(x.size(), 5);
    int[] values2 = new int[]{10, 15, 16, 20, 21};
    int[] domain2 = new int[values2.length];
    assertTrue(containsAll(x, values2));
    assertEquals(x.size(), values2.length);
    assertEquals(x.size(), x.copyDomain(domain2));
    Arrays.sort(domain2);
    for (int i = 0; i < x.size(); i++) {
      assertEquals(values2[i], domain2[i]);
    }
  }
}
