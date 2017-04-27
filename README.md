Kiwi-Solver
-----------

Kiwi is minimalist and extendable Constraint Programming (CP) solver specifically designed for education. 

The particularities of Kiwi stand in its generic trailing state restoration mechanism, its propagator-based propagation algorithm, and its modulable use of variables. 

This repository contains a Java version of Kiwi. The first version of Kiwi was implemented in Scala by rhartert during its PhD. The original source code of the core components of Kiwi was under 200 lines of Scala code and was the result of rethinking and simplifying the architecture of the open-source [OscaR](https://bitbucket.org/oscarlib/oscar/wiki/Home) solver to propose what the author believe to be a good trade-off between performance, clarity, and conciseness.

By developing Kiwi, the author does not aim to provide an alternative to full featured constraint solvers but rather to provide readers with a basic architecture that will (hopefuly) help them to understand the core mechanisms hidden under the hood of constraint solvers, to develop their own extended constraint solver, or to test innovative ideas.
