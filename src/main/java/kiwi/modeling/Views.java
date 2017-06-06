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
package kiwi.modeling;

import java.util.HashMap;

import kiwi.util.Tuple;
import kiwi.variable.IntVar;
import kiwi.variable.IntVarOffset;
import kiwi.variable.IntVarOpposite;

public class Views {

  // Contain all the opposite views.
  private static HashMap<IntVar, IntVar> oppositeViews = new HashMap<>();
  
  // Contain all the offset views.
  private static HashMap<Tuple<IntVar, Integer>, IntVar> offsetViews = new HashMap<>();
  
  public static IntVar opposite(IntVar x) {
    IntVar view = oppositeViews.get(x);
    if (view == null) {
      view = new IntVarOpposite(x);
      oppositeViews.put(x, view);
      oppositeViews.put(view, x);
    }
    return view;
  }

  public static IntVar offset(IntVar x, int k) {
    Tuple<IntVar, Integer> t = new Tuple<>(x, k);
    IntVar view = offsetViews.get(x);
    if (view == null) {
      view = new IntVarOffset(x, k);
      offsetViews.put(t, view);
      t = new Tuple<>(view, -k);
      offsetViews.put(t, x);
    }
    return view;
  }
}
