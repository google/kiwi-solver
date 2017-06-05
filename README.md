# Kiwi-Solver

Kiwi is a minimalist and extendable Constraint Programming (CP) solver specifically designed for education. 

The particularities of Kiwi stand in its generic trailing state restoration mechanism, its propagator-based propagation algorithm, and its modulable use of variables and heuristics. 

By developing Kiwi, the author does not aim to create an alternative to full-featured constraint solvers but rather to provide readers with a basic architecture that will (hopefully) help them to understand the core mechanisms hidden under the hood of constraint solvers, to develop their own extended constraint solver, or to test innovative ideas.

## Disclaimer

This repository contains a Java version of Kiwi — the [first version of Kiwi](https://arxiv.org/abs/1705.00047) was implemented in Scala by rhartert during his PhD. The original source code of Kiwi was the result of rethinking and simplifying the architecture of the open-source [OscaR](https://bitbucket.org/oscarlib/oscar/wiki/Home) solver to achieve what the author believes to be a good trade-off between performance, clarity, and conciseness.
