# Chapter 17: Smells and Heuristics

## Comments

### C1: Inappropriate Information

It is inappropriate for a comment to hold information better held in a different kind of system such as your source code control system, your issue tracking system, or any other record-keeping system. Comments should be reserved for technical notes about the code and design.

### C2: Obsolete Comment

A comment that has gotten old, irrelevant, and incorrect is obsolete. They become floating islands of irrelevance and misdirection in the code.

### C3: Redundant Comment

A comment is redundant if it describes something that adequately describes itself. Comments should say things that the code cannot say for itself.

### C4: Poorly Written Comment

Choose your words carefully. Use correct grammar and punctuation. Don’t ramble. Don’t state the obvious. Be brief.

### C5: Commented-Out Code

That code sits there and rots, getting less and less relevant with every passing day. It calls functions that no longer exist. It uses variables whose names have changed. It follows conventions that are long obsolete. It pollutes the modules that contain it and distracts the people who try to read it. It can be safely deleted! If it is needed again, the source code control system can be used to get it back.

## Environment

### E1: Build Requires More Than One Step

You should be able to check out the system with one simple command and then issue one other simple command to build it.

### E2: Tests Require More Than One Step

You should be able to run all the unit tests with just one command.

## Functions

### F1: Too Many Arguments

Functions should have a small number of arguments. No argument is best, followed by one, two, and three. More than three is very questionable and should be avoided with prejudice.

### F2: Output Arguments

Output arguments are counterintuitive. Readers expect arguments to be inputs, not outputs.

If your function must change the state of something, have it change the state of the object it is called on.

### F3: Flag Arguments

Boolean arguments loudly declare that the function does more than one thing. They are confusing and should be eliminated.

### F4: Dead Function

Methods that are never called should be discarded. Keeping dead code around is wasteful.

## General

### G1: Multiple Languages in One Source File

The ideal is for a source file to contain one, and only one, language. Realistically, we will probably have to use more than one. But we should take pains to minimize both the number and extent of extra languages in our source files.

### G2: Obvious Behavior Is Unimplemented

Following “The Principle of Least Surprise,” any function or class should implement the behaviors that another programmer could reasonably expect.

When an obvious behavior is not implemented, readers and users of the code can no longer depend on their intuition about function names. They lose their trust in the original author and must fall back on reading the details of the code.

### G3: Incorrect Behavior at the Boundaries

Every boundary condition, every corner case, every quirk and exception represents something that can confound an elegant and intuitive algorithm. *Don’t rely on your intuition*. Look for every boundary condition and write a test for it.

### G4: Overridden Safeties

It is risky to override safeties.

### G5: Duplication!

Find and eliminate duplication wherever you can.

Every time you see duplication in the code, it represents a missed opportunity for abstraction. That duplication could probably become a subroutine or perhaps another class outright. By folding the duplication into such an abstraction, you increase the vocabulary of the language of your design. Other programmers can use the abstract facilities you create. Coding becomes faster and less error prone because you have raised the abstraction level.
