package kiwi;

import java.util.function.IntUnaryOperator;

import kiwi.constraint.AllDifferent;
import kiwi.constraint.Different;
import kiwi.constraint.LowerEqual;
import kiwi.constraint.Sum;
import kiwi.propagation.PropagationQueue;
import kiwi.propagation.Propagator;
import kiwi.search.BinaryVarVal;
import kiwi.search.DFSearch;
import kiwi.search.Heuristic;
import kiwi.search.Objective;
import kiwi.trail.Trail;
import kiwi.util.Action;
import kiwi.variable.IntVar;
import kiwi.variable.IntVarImpl;
import kiwi.variable.IntVarOffset;
import kiwi.variable.IntVarOpposite;
import kiwi.variable.IntVarSingleton;

public class Solver {

  private final Trail trail;
  private final PropagationQueue pQueue;
  private final DFSearch search;

  private boolean feasible = true;

  private Heuristic heuristic = null;

  public Solver() {
    this.trail = new Trail();
    this.pQueue = new PropagationQueue();
    this.search = new DFSearch(pQueue, trail);
    this.feasible = true;
  }

  public Trail trail() {
    return trail;
  }

  public void setHeuristic(Heuristic heuristic) {
    this.heuristic = heuristic;
  }

  public void setObjective(Objective obj) {
    this.search.setObjective(obj);
  }

  public void onSolution(Action action) {
    search.addSolutionAction(action);
  }

  public void solve() {
    if (heuristic == null) {
      System.err.println("Solve with no specified heuristic.");
    }
    search.search(heuristic);
  }

  public boolean isFeasible() {
    return feasible;
  }

  public IntVar intVar(int min, int max) {
    return new IntVarImpl(pQueue, trail, min, max);
  }

  public IntVar intVar(int value) {
    return new IntVarSingleton(pQueue, trail, value);
  }

  public IntVar intVar(int[] values) {
    return new IntVarImpl(pQueue, trail, values);
  }

  public IntVar opposite(IntVar x) {
    return new IntVarOpposite(x);
  }

  public boolean add(Propagator p) {
    if (!feasible) {
      return false;
    }
    feasible &= p.setup();
    feasible &= pQueue.propagate();
    if (!feasible) {
      System.err.println("UNFEASIBLE");
    }
    return feasible;
  }

  public boolean lowerEqual(IntVar x, IntVar y) {
    Propagator p = new LowerEqual(x, y, false);
    feasible &= p.setup();
    return feasible;
  }

  public boolean lowerEqual(IntVar x, int k) {
    feasible &= x.updateMax(k);
    return feasible;
  }

  public boolean lower(IntVar x, IntVar y) {
    Propagator p = new LowerEqual(x, y, true);
    feasible &= p.setup();
    return feasible;
  }

  public boolean lower(IntVar x, int k) {
    feasible &= x.updateMax(k - 1);
    return feasible;
  }

  public boolean greaterEqual(IntVar x, IntVar y) {
    return add(new LowerEqual(y, x, false));
  }

  public boolean greaterEqual(IntVar x, int k) {
    feasible &= x.updateMin(k);
    return feasible;
  }

  public boolean greater(IntVar x, IntVar y) {
    return add(new LowerEqual(y, x, true));
  }

  public boolean greater(IntVar x, int k) {
    feasible &= x.updateMin(k + 1);
    return feasible;
  }

  public boolean equal(IntVar x, int k) {
    feasible &= x.assign(k);
    return feasible;
  }

  public boolean different(IntVar x, int k) {
    feasible &= x.remove(k);
    return feasible;
  }
  
  public boolean different(IntVar x, IntVar y) {
    return add(new Different(x, y));
  }

  public boolean allDifferent(IntVar[] variables) {
    return add(new AllDifferent(variables));
  }

  public IntVar offset(IntVar x, int k) {
    return new IntVarOffset(x, k);
  }

  public IntVar sum(IntVar[] variables, int k) {
    int min = k;
    int max = k;
    for (int i = 0; i < variables.length; i++) {
      // TODO check overflow
      min += variables[i].min();
      max += variables[i].max();
    }
    final IntVar result = intVar(min, max);
    sum(variables, result, k); // should always return true
    return result;
  }

  public boolean sum(IntVar[] variables, IntVar sum, int k) {
    return add(new Sum(variables, sum, k));
  }

  public void useBinaryFirstFail(IntVar[] vars) {
    setHeuristic(new BinaryVarVal(vars, i -> vars[i].size(), i -> vars[i].min()));
  }

  public void useBinary(IntVar[] vars, IntUnaryOperator varCost, IntUnaryOperator valSelector) {
    setHeuristic(new BinaryVarVal(vars, varCost, valSelector));
  }
}
