# Chapter 12: Reflection in a modular world

Drawbacks of using `exports` directives for code that’s primarily supposed to be used reflectively:

  - Only allows access to public members, which often requires making implementation details public.
  - Allows other modules to compile code against those exposed classes and members.
  - Qualified exports may couple you to an implementation instead of a specification.
  - Marks the package as being part of a module’s public API.

`opens` was designed specifically for the use case of reflection and behaves very differently from `exports`:

  - It allows access to all members, thus not impacting any decisions regarding visibility
  - It prevents compilation against code in opened packages and only allows access at run time.
  - It marks the package as being designed for use by a reflection-based framework.
