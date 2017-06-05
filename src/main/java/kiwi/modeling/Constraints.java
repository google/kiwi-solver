package kiwi.modeling;

import kiwi.Solver;
import kiwi.constraint.AllDifferent;
import kiwi.constraint.DifferentVal;
import kiwi.constraint.DifferentVar;
import kiwi.constraint.LowerEqualVal;
import kiwi.constraint.LowerEqualVar;
import kiwi.constraint.Sum;
import kiwi.propagation.Propagator;
import kiwi.variable.IntVar;

public class Constraints {
  
  public static Propagator lowerEqual(IntVar x, IntVar y) {
    return new LowerEqualVar(x, y, false);
  }

  public static Propagator lowerEqual(IntVar x, int k) {
    return new LowerEqualVal(x, k, false);
  }

  public static Propagator lower(IntVar x, IntVar y) {
    return new LowerEqualVar(x, y, true);
  }

  public static Propagator lower(IntVar x, int k) {
    return new LowerEqualVal(x, k, true);
  }
  
  public static Propagator greaterEqual(IntVar x, IntVar y) {
    return new LowerEqualVar(x, y, false);
  }

  public static Propagator greaterEqual(IntVar x, int k) {
    return new LowerEqualVal(Views.opposite(x), -k, false);
  }

  public static Propagator greater(IntVar x, IntVar y) {
    return new LowerEqualVar(x, y, true);
  }

  public static Propagator greater(IntVar x, int k) {
    return new LowerEqualVal(Views.opposite(x), -k, true);
  }
  
  public static Propagator different(IntVar x, IntVar y) {
    return new DifferentVar(x, y);
  }

  public static Propagator different(IntVar x, int k) {
    return new DifferentVal(x, k);
  }

  public static Propagator allDifferent(IntVar[] variables) {
    return new AllDifferent(variables);
  }

  public IntVar sum(Solver solver, IntVar[] variables, int k) {
    int min = k;
    int max = k;
    for (int i = 0; i < variables.length; i++) {
      // TODO check overflow
      min += variables[i].min();
      max += variables[i].max();
    }
    final IntVar result = solver.intVar(min, max);
    solver.add(sum(variables, result, k)); // should not fail.
    return result;
  }

  public static Propagator sum(IntVar[] variables, IntVar sum, int k) {
    return new Sum(variables, sum, k);
  }
}
