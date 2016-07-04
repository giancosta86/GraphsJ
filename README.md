# GraphsJ

*ScalaFX didactic application for graph algorithms*


![Add/Delete GraphPlan](https://github.com/giancosta86/GraphsJ-scenarios/blob/master/screenshots/AdGraphPlan.png)


## Introduction

GraphsJ is a modern didactic application dedicated to interactively executing graph algorithms; it features both a ScalaFX visual user interface and a [Scenario Development Kit](https://github.com/giancosta86/GraphsJ-sdk) - to create *custom scenarios* based on the [EighthBridge](https://github.com/giancosta86/EighthBridge) toolkit for ScalaFX.

Starting from version 5.0, GraphsJ targets two paramount, complementary fields of study:

* **Operations Research**, whose concepts constitute the very core of EighthBridge and GraphsJ itself

* **Artificial Intelligence**, via the introduction of [LambdaPrism](https://github.com/giancosta86/LambdaPrism) into the application kernel


The application includes predefined scenarios dealing with problems from both fields - in particular:

* A customized version of **GraphPlan** for Artificial Intelligence

* Prim's **Shortest Spanning Tree (SST)** for Operations Research


GraphsJ is profoundly influenced by the elegance and effectiveness of *Functional Programming*, which it mixes with OOP techniques thanks to the [Scala](http://www.scala-lang.org/) programming language.


## Features

* *Modern user interface*, employing **ScalaFX**

* *Flexible and vastly enhanced canvas* to draw and edit graphs, now supporting **zoom** and **pan**, similar to map navigation

* ScalaFX-based **SDK**, enabling anyone to easily *create new scenarios*

* **Scenario-discovery engine**, *automatically adding and removing scenarios*

* **Automated installation of the predefined scenarios**, retrieved from [GraphsJ-scenarios](https://github.com/giancosta86/GraphsJ-scenarios)

* Graphs can be exported as PNG images

* *Simplified model*, inspired by **Functional Programming**

* *Per-scenario options dialog and help*, customizable by scenario developers

* **XML-based document files**, to foster *interoperability and compatibility with previous versions*

* **Open source** code available: developers can create a new scenario starting from a consolidated base


## Requirements

Java 8u91 or later compatible is recommended.



## Running GraphsJ

The suggested way to run GraphsJ is [MoonDeploy](https://github.com/giancosta86/moondeploy), as it will automatically download and launch the application - just go to the [latest release](https://github.com/giancosta86/GraphsJ/releases/latest) page and open the file **App.moondeploy**.

Otherwise, to start the application:
1. Download and decompress the zip archive
2. Run the file *bin/GraphsJ* (on UNIX) or *bin/GraphsJ.bat* (on Windows)

**Users who installed GraphsJ 3 via MoonDeploy can just run the program: MoonDeploy will automatically update it to GraphsJ 4**.



## Online help

GraphsJ features a very user-friendly interface, but ideas and suggestions can be found in the [wiki](https://github.com/giancosta86/GraphsJ/wiki)!



## Scenario development kit

For detailed information, please refer to the [SDK project page](https://github.com/giancosta86/GraphsJ-sdk).



## Screenshots

![Add/Delete GraphPlan](https://github.com/giancosta86/GraphsJ-scenarios/blob/master/screenshots/AdGraphPlan.png)

![Prim's Shortest Spanning Tree](https://github.com/giancosta86/GraphsJ-scenarios/blob/master/screenshots/PrimSST.png)



## Special thanks

Special thanks to:

* [Professor Silvano Martello](http://www.or.deis.unibo.it/staff_pages/martello/cvitae.html)
* [Dott. Claudia D'Ambrosio](http://www.lix.polytechnique.fr/~dambrosio/)

for their suggestions.

The icons in the menu and toolbar are part of the [Crystal Clear icon set](https://commons.wikimedia.org/wiki/Crystal_Clear), by [Everaldo Coelho](https://en.wikipedia.org/wiki/Everaldo_Coelho).



## Further references

* [Facebook page](https://www.facebook.com/graphsj)

* [GraphsJ Scenario Development Kit](https://github.com/giancosta86/GraphsJ-sdk)

* [GraphsJ - Scenarios](https://github.com/giancosta86/GraphsJ-scenarios)

* [EighthBridge](https://github.com/giancosta86/EighthBridge)

* [LambdaPrism](https://github.com/giancosta86/LambdaPrism)
