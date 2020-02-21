# Chapter 12: Testing Concurrent Programs

Most tests of concurrent classes fall into one or both of the classic categories of _safety_(nothing bad ever happens) and _liveness_(“something good eventually happens).

Unfortunately, test code can introduce timing or synchronization artifacts that can mask
bugs that might otherwise manifest themselves (Bugs that disappear when you add debugging or test code are playfully called Heisenbugs).

## 12.1 Testing for correctness
