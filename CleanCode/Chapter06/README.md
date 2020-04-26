# Chapter 6: Objects and Data Structures

## Data Abstraction

Hiding implementation is not just a matter of putting a layer of functions between the variables. Hiding implementation is about abstractions! A class does not simply push its variables out through getters and setters. Rather it exposes abstract interfaces that allow its users to manipulate the _essence_ of the data, without having to know its implementation.

This is not merely accomplished by using interfaces and/or getters and setters. Serious thought needs to be put into the best way to represent the data that an object contains. The worst option is to blithely add getters and setters.

## Data/Object Anti-Symmetry

Objects hide their data behind abstractions and expose functions that operate on that data. Data structure expose their data and have no meaningful functions.

Procedural code (code using data structures) makes it easy to add new functions without changing the existing data structures. OO code, on the other hand, makes it easy to add new classes without changing existing functions.

Procedural code makes it hard to add new data structures because all the functions must change. OO code makes it hard to add new functions because all the classes must change (not so true in some cases, such as Java 8, where default methods can be declared in interfaces and may be suitable in some cases).

## The Law of Demeter

A method `f` of a class `C` should only call the methods of these:

* `C`
* An object created by `f`
* An object passed as an argument to `f`
* An object held in an instance variable of `C`

The method should not invoke methods on objects that are returned by any of the allowed functions. In other words, talk to friends, not to strangers.

### Train Wrecks

This kind of code is often called a train wreck because it looks like a bunch of coupled train cars. Chains of calls like this are generally considered to be sloppy style and should be avoided.

```
final String outputDir = ctxt.getOptions().getScratchDir().getAbsolutePath();
```

### Hybrids

This confusion sometimes leads to unfortunate hybrid structures that are half object and half data structure. They have functions that do significant things, and they also have either public variables or public accessors and mutators that, for all intents and purposes, make the private variables public.

Such hybrids make it hard to add new functions but also make it hard to add new data structures. They are the worst of both worlds and should be avoided.

### Hiding Structure

In the previous code snippet, we should not be able to navigate through `ctxt`, `options`, and `scratchDir`.

If `ctxt` is an object, we should be telling it to _do something_; we should not be asking it about its internals. For exampe, if the intent of getting the absolute path of the scratch directory was to create a scratch file of a given name, we could tell the `ctxt` object to do this:

```
BufferedOutputStream bos = ctxt.createScratchFileStream(classFileName);
```
## Data Transfer Objects

The quintessential form of a data structure is a class with public variables and no functions. This is sometimes called a data transfer object, or DTO. DTOs are very useful structures, especially when communicating with databases or parsing messages from sockets, and so on.

Somewhat more common is the "bean". Beans have private variables manipulated by getters and setters.

### Active Record

Active Records are special forms of DTOs. They are data structures with public (or bean-accessed) variables; but they typically have navigational methods like `save` and `find`.

Unfortunately we often find that developers try to treat these data structures as though they were objects by putting business rule methods in them. This is awkward because it creates a hybrid between a data structure and an object.

The solution, of course, is to treat the Active Record as a data structure and to create separate objects that contain the business rules and that hide their internal data.
