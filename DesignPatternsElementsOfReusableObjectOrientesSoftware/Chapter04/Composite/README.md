# Composite

## Intent

Compose objects into tree structures to represent part-whole hierarchies. Composite lets clients treat individual objects and compositions of objects uniformly.

## Applicability

* you want to represent part-whole hierarchies of objects
* you want clients to be able to ignore the difference between compositions of objects and individual objects. Clients will treat all objects in the composite structure uniformly

## Structure

![Image of the structure for the Composite Pattern](./image/composite.png "Structure for the Composite Pattern")

## Participants

* **`Component`**:
  - declares the interface for objects in the composition
  - implements default behavior for the interface common to all classes, as appropriate
  - declares an interface for accessing and managing its child components
  - (optional) defines an interface for accessing a component's parent in the recursive structure, and implements it if that's appropriate
* **`Leaf`**:
  - represents leaf objects in the composition. A leaf has no children
  - defines behavior for primitive objects in the composition
* **`Composite`**:
  - defines behavior for components having children
  - stores child components
  - implements child-related operations in the `Component` interface
* **`Client`**: manipulates objects in the composition through the Component interface

## Collaborations

Clients use the Component class interface to interact with objects in the composite structure. If the recipient is a Leaf, then the request is handled directly. If the recipient is a Composite, then it usually forwards requests to its child components, possibly performing additional operations before and/or after forwarding.

## Consequences

The pattern:

* defines class hierarchies consisting of primitive objects and composite objects
* makes the client simple
* makes it easier to add new kinds of components
* can make your design overly general. The **disadvantage** of making it easy to add new components is that it makes it harder to restrict the components of a composite

## Related Patterns

Often the component-parent link is used for a Chain of Responsibility.

Decorator is often used with Composite. When decorators and composites are used together, they will usually have a common parent class. So decorators will have to support the Component interface with operations like `Add`, `Remove`, and `GetChild`.

Flyweight lets you share components, but they can no longer refer to their parents.

Iterator can be used to traverse composites.

Visitor localizes operations and behavior that would otherwise be distributed across Composite and Leaf classes.
