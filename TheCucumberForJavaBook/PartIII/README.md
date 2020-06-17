# Part III: More Techniques

## Chapter 14: Controlling Cucumber

Cucumber allows to choose which features and/or scenarios to run, based on tags, names, and line numbers. Using the special *rerun* plugin, we can choose to only run scenarios that failed the last time the features were executed.

There are other options that control Cucumber, such as the `glue` option (which gives us the freedom to structure our solution in the way that we want) and the `snippets` option (which gives us control of the formatting of our snippet names).

Cucumber integrates very well with JUnit, TestNG and can be ran using Ant, Mave and Gradle, including running it from a CI environment.

## Chapter 15: Working with a REST Web Service

When developing a *REST* web service with Cucumber, we need to keep in mind that the service is running in a different process than Cucumber. This means that we need to remember to start the service before we can run our features.

## Chapter 16: Working with Legacy Applications

Creating a Cucumber characterization test:

* Write a scenario that exercises some interesting—but mysterious—behavior of your system.
* Write a `Then` step that you know will fail.
* Wire up the step definitions and run the scenario. Let the failure in the `Then` step tell you what the actual behavior is.
* Change the failing `Then` step so that it describes the actual behavior of the system.
* Repeat.

Possible approach for adding new behavior to a legacy system:

* Examine the new feature. If needed, write a few characterization scenarios to examine and clarify the current behavior of the system in that area.
* Now, with the new feature in mind, modify those scenarios or write new ones to specify the desired new behavior.
* Run through the scenarios with your team’s stakeholder representative to check that you’re about to build the right thing. Correct the scenarios with them if necessary.
* Run the scenarios. For each failing scenario, examine the code you’ll need to change to make it pass, but work on only one failing scenario at a time.
* Write any extra characterization tests you need to give you the confidence to change that code.
* Change the code to make the scenario pass.
* Repeat from step 4 until all the scenarios pass.

Code coverage tools allow us to discover which specific lines of code in the system were executed during a test.

Mutation testing tools, like [pitest](http://pitest.org) can help in determining whether the tests we have are sufficient. Mutation testing can be a cost-effective way of confirming the quality of your automated testing.

