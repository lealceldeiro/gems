# Chapter 4: Comments

The proper use of comments is to compensate for our failure to express ourself in code.

The older a comment is, and the farther away it is from the code it describes, the more likely it is to be just plain wrong. The reason is simple. Programmers can’t realistically maintain them.

Truth can only be found in one place: the code. Only the code can truly tell you what it does. It is the only source of truly accurate information.

## Comments Do Not Make Up for Bad Code

Clear and expressive code with few comments is far superior to cluttered and complex code with lots of comments. Rather than spend your time writing the comments that explain the mess you’ve made, spend it cleaning that mess.

## Explain Yourself in Code

There are certainly times when code makes a poor vehicle for explanation. Unfortunately, many programmers have taken this to mean that code is seldom, if ever, a good means for explanation. This is patently false. Example:

The following comment can be completed replaced

```
// Check to see if the employee is eligible for full benefits
if ((employee.flags & HOURLY_FLAG) &&
    (employee.age > 65))
```
by a well named function like this:

```
if (employee.isEligibleForFullBenefits())
```

## Good Comments

Some comments are necessary or beneficial. BUT, the only truly good comment is the comment you found a way not to write.

### Legal Comments

Copyright and authorship statements are necessary and reasonable things to put into a comment at the start of each source file.

### Informative Comments

It is sometimes useful to provide basic information with a comment. For example

```
// format matched kk:mm:ss EEE, MMM dd, yyyy
Pattern timeMatcher = Pattern.compile("\\d*:\\d*:\\d* \\w*, \\w* \\d*, \\d*");
```

### Explanation of Intent

Sometimes a comment goes beyond just useful information about the implementation and provides the intent behind a decision. i.e.:

```
public int compareTo(Object o) {
  if(o instanceof WikiPagePath) {
    // ...
  }

  return 1; // we are greater because we are the right type.
}
```

### Clarification

Sometimes it is just helpful to translate the meaning of some obscure argument or return value into something that’s readable. In general it is better to find a way to make that argument or return value clear in its own right; but when its part of the standard library, or in code that you cannot alter, then a helpful clarifying comment can be useful.

### Warning of Consequences

Sometimes it is useful to warn other programmers about certain consequences. For example, here is a comment that explains why a particular test case is turned off:

```
// Don't run unless you have some time to kill.
public void _testWithReallyBigFile() {
  writeLinesToFile(10000000);
  // ...
}
```

### TODO Comments

It is sometimes reasonable to leave “To do” notes in the form of `// TODO` comments.

`TODO`s are jobs that the programmer thinks should be done, but for some reason can’t do at the moment. It might be a reminder to delete a deprecated feature or a plea for someone else to look at a problem. It might be a request for someone else to think of a better name or a reminder to make a change that is dependent on a planned event. Whatever else a `TODO` might be, it is _not_ an excuse to leave bad code in the system.

### Amplification

A comment may be used to amplify the importance of something that may otherwise seem inconsequential.

### Javadocs in Public APIs

There is nothing quite so helpful and satisfying as a well-described public API. The javadocs for the standard Java library are a case in point. It would be difficult, at best, to write Java programs without them.

If you are writing a public API, then you should certainly write good javadocs for it. But keep in mind the rest of the advice in this chapter. Javadocs can be just as misleading, nonlocal, and dishonest as any other kind of comment.

## Bad Comments

Most comments fall into this category. Usually they are crutches or excuses for poor code or justifications for insufficient decisions, amounting to little more than the programmer talking to himself.

### Mumbling

If you decide to write a comment, then spend the time necessary to make sure it is the best comment you can write.

Any comment that forces you to look in another module for the meaning of that comment has failed to communicate to you and is not worth the bits it consumes.

### Redundant Comments

This kind of comments is certainly not more informative than the code. It does not justify the code, or provide intent or rationale. It is not easier to read than the code. Indeed, it is less precise than the code and entices the reader to accept that lack of precision in lieu of true understanding.

### Misleading Comments

Sometimes, with all the best intentions, a programmer makes a statement in his comments that isn’t precise enough to be accurate.

This misinformation, couched in a comment that is harder to read than the body of the code, could cause another programmer to find himself in a debugging session trying to figure out why his code executed different than expected.

### Mandated Comments

It is just plain silly to have a rule that says that every function must have a javadoc, or every variable must have a comment. Comments like this just clutter up the code, propagate lies, and lend to general confusion and disorganization.

### Journal Comments

Sometimes people add a comment to the start of a module every time they edit it. These comments accumulate as a kind of journal, or log, of every change that has ever been made.

Long ago there was a good reason to create and maintain these log entries at the start of every module. We didn’t have source code control systems that did it for us. Nowadays, however, these long journals are just more clutter to obfuscate the module. They should be completely removed.

### Noise Comments

Sometimes you see comments that are nothing but noise. They restate the obvious and provide no new information. i.e.:

```
/**
* Default constructor.
*/
protected Animal() {
}
```

Replace the temptation to create noise with the determination to clean your code. You’ll find it makes you a better and happier programmer.

### Scary Noise

Javadocs can also be noisy. If authors aren’t paying attention when comments are written (or pasted), why should readers be expected to profit from them?

### Don’t Use a Comment When You Can Use a Function or a Variable

Consider the following stretch of code:

```
// does the module from the global list <mod> depend on the
// subsystem we are part of?
if (smodule.getDependSubsystems().contains(subSysMod.getSubSystem()))
```

This could be rephrased without the comment as:

```
List<String> moduleDependees = smodule.getDependSubsystems();
String ourSubSystem = subSysMod.getSubSystem();
if (moduleDependees.contains(ourSubSystem))
```

### Position Markers

Sometimes programmers like to mark a particular position in a source file. i.e., banners like this:

```
// Actions //////////////////////////////////
```

There are rare times when it makes sense to gather certain functions together beneath a banner like this. But in general they are clutter that should be eliminated—especially the noisy train of slashes at the end.

If you overuse banners, they’ll fall into the background noise and be ignored.

### Closing Brace Comments

This kind of comments might make sense for long functions with deeply nested structures, however it serves only to clutter the kind of small and encapsulated functions that we prefer. So if you find yourself wanting to mark your closing braces, try to shorten your functions instead.

### Attributions and Bylines

Comments like this `/* Added by Rick */`.

Source code control systems are very good at remembering who added what, when. There is no need to pollute the code with little bylines. You might think that such comments would be useful in order to help others know who to talk to about the code. But the reality is that they tend to stay around for years and years, getting less and less accurate and relevant.

### Commented-Out Code

Others who see that commented-out code won’t have the courage to delete it. They’ll think it is there for a reason and is too important to delete.

There was a time, back in the sixties, when commenting-out code might have been useful. But we’ve had good source code control systems for a very long time now. Those systems will remember the code for us. We don’t have to comment it out any more.

### HTML Comments

HTML in source code comments makes the comments hard to read in the one place where they should be easy to read—the editor/IDE.

### Nonlocal Information

If you must write a comment, then make sure it describes the code it appears near. Don’t offer systemwide information in the context of a local comment.

### Too Much Information
