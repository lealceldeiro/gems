# Chapter 1: Introduction

## 1.1 What Is a Design Pattern?

In general, a pattern has four essential elements:

* The *pattern name* is a handle we can use to describe a design problem, its solutions, and consequences in a word or two.
* The *problem* describes when to apply the pattern.
* The *solution* describes the elements that make up the design, their relationships, responsibilities, and collaborations.
* The *consequences* are the results and trade-offs of applying the pattern.

## 1.2 Design Patterns in MVC

Model/View/Controller (MVC) consists of three kinds of objects. The Model is the application object, the View is its screen presentation, and the Controller defines the way the user interface reacts to user input.

MVC uses other design patterns, such as Factory Method to specify the default controller class for a view and Decorator to add scrolling to a view. But the main relationships in MVC are given by the Observer, Composite, and Strategy design patterns.

## 1.5 The Catalog

|           |            |                  |      Purpose     |                         |
|-----------|------------|------------------|------------------|-------------------------|
|           |            |  **Creational**  |  **Structural**  |      **Behavioral**     |
| **Scope** |  **Class** | Factory Method   | Adapter (class)  | Interpreter             |
|           |            |                  |                  | Template Method         |
|           | **Object** | Abstract Factory | Adapter (object) | Chain of Responsibility |
|           |            | Builder          | Bridge           | Command                 |
|           |            | Prototype        | Composite        | Iterator                |
|           |            | Singleton        | Decorator        | Mediator                |
|           |            |                  | Facade           | Memento                 |
|           |            |                  | Flyweight        | Observer                |
|           |            |                  | Proxy            | State                   |
|           |            |                  |                  | Strategy                |
|           |            |                  |                  | Visitor                 |

