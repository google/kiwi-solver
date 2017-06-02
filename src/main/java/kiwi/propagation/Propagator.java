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
package kiwi.propagation;

/**
 * Superclass to be instantiated by any propagator.
 */
public abstract class Propagator {

  /** 
   * Indicates if the propagator is contained in the propagation queue. 
   */
  protected boolean enqueued;

  /** 
   * Indicates if the propagator might need to be called again just after 
   * propagation. 
   */  
  protected boolean idempotent;

  /**
   * Initializes the propagator and performs its initial propagation
   * 
   * @return false if the propagation failed.
   */
  public abstract boolean setup();

  /**
   * Propagates the last domain changes
   * 
   * @return false if the propagation failed.
   */
  public abstract boolean propagate();
}
