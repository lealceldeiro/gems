# Part I: Cucumber Fundamentals

## Chapter 1: Why Cucumber?

Software teams work best when the developers and business stakeholders are communicating clearly with one another. A great way to do that is to collaboratively specify the work that’s about to be done using automated acceptance tests.

When the acceptance tests are written as examples, they stimulate people’s imaginations and help them see other scenarios they hadn’t previously considered.

When the team members write their acceptance tests collaboratively, they can develop their own ubiquitous language for talking about their problem domain. This helps them avoid misunderstandings.

Cucumber was designed specifically to help business stakeholders get involved in writing acceptance tests.

Each test case in Cucumber is called a scenario, and scenarios are grouped into features. Each scenario contains several steps.

The business-facing parts of a Cucumber test suite, stored in feature files, must be written according to syntax rules—known as Gherkin—so that Cucumber can read them.

Under the hood, step definitions translate from the business-facing language of steps into code.

## Chapter 2: First Taste

### Step by step

Working outside-in with Cucumber helps us to stay focused. We can let Cucumber guide us through the work to be done, leaving us free to concentrate on creating an elegant solution. By running Cucumber every time we make a change, any mistakes we make are found and resolved quickly, and we get plenty of feedback and encouragement about our progress.

### Directory Structure

Cucumber needs you to specify where your features and step definition are kept.

### Gherkin

Cucumber tests are expressed using a syntax called Gherkin. Gherkin files are plain text and have a `.feature` extension.

### Step Definitions

Step definitions are the glue that binds your Cucumber tests to the application you’re testing.

## Chapter 3: Gherkin Basics

The core *Gherkin* keywords `Feature`, `Scenario`, `Given`, `When`, and `Then` can be used to describe the behavior your stakeholders want as concrete examples.

There is a fundamental pattern to each Gherkin scenario, with a context (`Given`), an event (`When`), and an outcome (`Then`).

Each scenario must be capable of being run on its own and should not depend on data set up by other scenarios. This means every scenario has to include enough `Given` steps to set up all the data it needs.

You can add descriptions and comments to your `.feature` files to turn them into useful documentation of your system.

Using the `#language:` header, you can write your features in different spoken languages.

## Chapter 4: Step Definitions: From the Outside

How Cucumber executes a scenario:

![Image of How Cucumber executes a scenario](./image/scenario_execution.png "How Cucumber executes a scenario")

Step definitions provide a mapping from the Gherkin scenarios’ plainlanguage descriptions of user actions into Java code, which simulates those actions.

Step definitions are registered with Cucumber by using `@Given`, `@When`, `@Then`, or one of the aliases for your spoken language.

Step definitions use regular expressions to declare the steps that they can handle. Because regular expressions can contain wildcards, one step definition can handle several different steps.

A step definition communicates its result to Cucumber by raising, or not raising, an exception.

## Chapter 5: Expressive Scenarios

Readability should be your number-one goal when writing Gherkin features. Always try to sit together with a stakeholder when you write your scenarios, or at the very least pass them over for feedback once you’ve written them. Keep fine-tuning the language in your scenarios to make them more readable.

Use a Background to factor out repeated steps from a feature and to help tell a story.

Repetitive scenarios can be collapsed into a Scenario Outline.

Steps can be extended with multiline strings or data tables.

You can organize features into subfolders, like chapters in a book.

Tags allow you to mark up scenarios and features so you select particular sets to run or report on.

## Chapter 6: Keeping Your Cucumbers Sweet

### Main types of problems that might start to arise

| Symptom                                  | Problem              |
|------------------------------------------|----------------------|
| Some of our tests fail randomly          | Flickering scenarios |
| We keep breaking tests unintentionally   | Flickering scenarios |
| Our features take too long to run        | Slow features        |
| Our stakeholders don’t read our features | Bored stakeholders   |

Cucumber features are a valuable asset to any company. Some teams have ripped out and rewritten big parts of their systems, safe in the knowledge that they had a set of accurate, executable specifications to ensure the new solution worked just as well as the original. To those teams, the features were more valuable than the production code itself. If you’re going to invest in writing Cucumber features, you need to protect that investment by caring for them so that they’re as useful as possible to the whole team.

Don’t settle for features that are slow, that fail intermittently, or that are read by only half the team: iron out problems as they happen, and use each problem as a reason to make the tests even better than they were before.

Cucumber might just seem like a testing tool, but at its heart it’s really a collaboration tool. If you make a genuine effort to write features that work as documentation for the nontechnical stakeholders on your team, you’ll find you are forced to talk with them about details that you might never have otherwise made the time to talk about. Those conversations reveal insights about their understanding of the problem, insights that will help you build a much better solution than you would have otherwise.

This is Cucumber’s big secret: the tests and documentation are just a happy side effect; the real value lies in the knowledge you discover during those conversations.
