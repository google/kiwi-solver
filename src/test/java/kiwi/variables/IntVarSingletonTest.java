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

import kiwi.Solver;
import kiwi.variable.IntVar;

import org.junit.Test;

public class IntVarSingletonTest {

  // All values should be contained in the initial domain
  @Test
  public void test1() {
    Solver solver = new Solver();
    IntVar x = solver.intVar(5);
    assertEquals(x.size(), 1);
    assertTrue(x.isAssigned());
    assertTrue(x.contains(5));
  }

  // Contains should return false if value is not in the domain
  @Test
  public void test3() {
    Solver solver = new Solver();
    IntVar x = solver.intVar(5);
    assertFalse(x.contains(-5));
    assertFalse(x.contains(0));
    assertFalse(x.contains(4));
    assertFalse(x.contains(6));
  }

  // UpdateMin with a lesser or equal value than min should not impact the
  // domain
  @Test
  public void test5() {
    Solver solver = new Solver();
    IntVar x = solver.intVar(5);
    assertTrue(x.updateMin(4));
    assertEquals(x.size(), 1);
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 5);
    assertTrue(x.updateMin(5));
    assertEquals(x.size(), 1);
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 5);
  }

  // UpdateMin greater than max should fail
  @Test
  public void test7() {
    Solver solver = new Solver();
    IntVar x = solver.intVar(5);
    assertFalse(x.updateMin(6));
  }

  // UpdateMax with a greater or equal value than max should not impact the
  // domain
  @Test
  public void test9() {
    Solver solver = new Solver();
    IntVar x = solver.intVar(5);
    assertTrue(x.updateMax(6));
    assertEquals(x.size(), 1);
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 5);
    assertTrue(x.updateMax(5));
    assertEquals(x.size(), 1);
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 5);
  }

  // UpdateMax lesser than min should fail
  @Test
  public void test11() {
    Solver solver = new Solver();
    IntVar x = solver.intVar(5);
    assertFalse(x.updateMax(0));
  }

  // Assign the assigned value should do nothing.
  @Test
  public void test14() {
    Solver solver = new Solver();
    IntVar x = solver.intVar(5);
    assertTrue(x.assign(5));
    assertTrue(x.contains(5));
    assertTrue(x.isAssigned());
    assertEquals(x.min(), 5);
    assertEquals(x.max(), 5);
  }

  // Assign an out of bounds value should fail
  @Test
  public void test16() {
    Solver solver = new Solver();
    IntVar x = solver.intVar(5);
    assertFalse(x.assign(20));
  }

  // Remove a removed value should not impact the domain
  @Test
  public void test19() {
    Solver solver = new Solver();
    IntVar x = solver.intVar(5);
    assertTrue(x.remove(4));
    assertTrue(x.isAssigned());
    assertTrue(x.remove(6));
    assertTrue(x.isAssigned());
  }

  // Remove the assigned value should fail
  @Test
  public void test23() {
    Solver solver = new Solver();
    IntVar x = solver.intVar(5);
    assertFalse(x.remove(5));
  }

  // Copy domain and to Array
  @Test
  public void test28() {
    Solver solver = new Solver();
    int[] domain = new int[1];
    IntVar x = solver.intVar(5);
    assertEquals(x.size(), x.copyDomain(domain));
    assertEquals(domain[0], 5);
  }
}
