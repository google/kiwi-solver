package kiwi.modeling;

import java.util.function.IntUnaryOperator;

import kiwi.search.BinaryVarVal;
import kiwi.search.Heuristic;
import kiwi.variable.IntVar;

public class Heuristics {

  public static Heuristic binaryFirstFail(IntVar[] vars) {
    return new BinaryVarVal(vars, i -> vars[i].size(), i -> vars[i].min());
  }

  public static Heuristic binary(IntVar[] vars, IntUnaryOperator varCost,
      IntUnaryOperator valSelector) {
    return new BinaryVarVal(vars, varCost, valSelector);
  }
}
