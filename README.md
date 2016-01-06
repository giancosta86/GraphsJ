# GraphsJ

*Elegant and modern didactic application for running graph algorithms*


## Introduction

GraphsJ is a modern didactic application dedicated to interactively execute graph algorithms; it features both a visual user interface and an SDK to develop *custom scenarios*.

Several students of Operations Research need to easily execute graph algorithms, in order to study them with a "hands-on" approach showing every step: GraphsJ meets this requirement by running, even step-by-step, some well-known algorithms - such as *Dijkstra's shortest path problem* - which often prove to be cross-subject.

On the other hand, research workers want to test their algorithms on a PC, as it's faster and less error-prone, in particular if one wants to perform several tests. However, writing a whole computer program only to test an algorithm is not always at hand - just the creation a GUI is indeed a rather complex, time-consuming task.

GraphsJ satisfies both user groups, because it provides an easy-to-use interface as well as a simple Java framework: this means that anyone knowing just a bit of Java can create their own algorithms, without dealing with details such as the creation of a user interface or disk I/O - these features are already provided by the application, so the developer only has to create a few classes, compile them and provide the program with their location, therefore focusing on the business logic making up the algorithms.

This new version, GraphsJ 3, is mainly oriented to a global refactoring of the software architecture, which is now much simplified and far more elegant; it is based on two open-source libraries developed by me - [Helios](https://github.com/giancosta86/Helios-core) and [Arcontes](https://github.com/giancosta86/Arcontes-core), as well as on a vast set of modern technologies, in particular JavaFX, in order to deliver an enhanced user experience.


## Features

* A modern user interface, based on JavaFX

* Flexible and greatly enhanced canvas to draw and edit your graphs

* Includes 4 predefined, widely-used algorithms

* Customizable fonts and colors for graph, vertexes and edges. Graphical settings are stored in each scenario file, along with the graph they describe

* Java-based SDK, enabling anyone to easily create new scenarios to plug into the program

* Full source code available, especially for the standard algorithms: developers can create a new algorithm starting from a consolidated base

* Per-scenario options dialog and help page, customizable by scenario developers

* XML-based document files, to foster interoperability

* New, redesigned and much more elegant architecture, based on [Helios](https://github.com/giancosta86/Helios-core) and [Arcontes](https://github.com/giancosta86/Arcontes-core).

* Short but complete online help

* Open source and, hopefully, elegant


## Requirements

Java 8 or later is recommended in order to run GraphsJ or to use its SDK.



## Running GraphsJ

The suggested way to run GraphsJ is [MoonDeploy](https://github.com/giancosta86/moondeploy), as it will automatically download and launch the application - just go to the [latest release](https://github.com/giancosta86/GraphsJ/releases/latest) page and open the file **App.moondeploy**.

Otherwise, to start the application:
1. Download and decompress the zip archive
2. Run the file *bin/GraphsJ* (on UNIX) or *bin/GraphsJ.bat* (on Windows)



## SDK

GraphsJ SDK is a library providing classes and interfaces to easily create scenarios that you can import and run within GraphsJ.

GraphsJ SDK consists of the following modules:


* **graphsj-sdk**: provides classes and interfaces required to create your custom scenarios

* **graphsj-algorithms**: contains the built-in algorithm classes, whose logic and results can be used within your own algorithms


All the modules are available on [Hephaestus](https://bintray.com/giancosta86/Hephaestus) and can be declared as Gradle or Maven dependencies.

**IMPORTANT**: all the modules provided by the SDK should be referenced as **provided** dependencies, because they are needed only during the compilation of your project, as they are provided by GraphsJ itself at runtime.


## Online help

GraphsJ features a fairly easy-to-use interface, but you can find out more about its commands by looking at the online help, accessible by clicking the menu item *?->Help* within the program.

You can also consult the [pseudocode](https://github.com/giancosta86/GraphsJ/blob/master/pseudocode.pdf) of the standard algorithms provided by the application.


## Examples

A few example files are available, ready to run within GraphsJ:

[Download the example files](https://github.com/giancosta86/GraphsJ/releases/download/v3.7/GraphsJ_3_Examples.zip)


## Special thanks

Most of the icons in the toolbar are a customization of the [Primo icon set](https://www.iconfinder.com/iconsets/Primo_Icons), by [Double-J Design](http://www.doublejdesign.co.uk/).

Thanks to Paolo Tagliapietra for our interesting chats about architectural and domain-related aspects, as well as for beta testing.


Special thanks to:

* [Professor Silvano Martello](http://www.or.deis.unibo.it/staff_pages/martello/cvitae.html)
* [Claudia D'Ambrosio](http://www.or.deis.unibo.it/staff_pages/dambrosio/cv_claudia_english.htm)

for their support and advice.


## Further references

* [Facebook page](https://www.facebook.com/graphsj)
* [Helios](https://github.com/giancosta86/Helios-core)
* [Arcontes](https://github.com/giancosta86/Helios-core)
