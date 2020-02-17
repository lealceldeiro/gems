# Chapter 9: GUI Applications

## 9.1 Why are GUIs single-threaded?

There have been many attempts to write multithreaded GUI frameworks, but because of persistent problems with race conditions and deadlock, they all eventually arrived at the single-threaded event queue model in which a dedicated thread fetches events off a queue and dispatches them to application-defined event handlers.

Single-threaded GUI frameworks achieve thread safety via thread confinement; all GUI objects, including visual components and data models, are accessed exclusively from the event thread.

### 9.1.1 Sequential event processing
