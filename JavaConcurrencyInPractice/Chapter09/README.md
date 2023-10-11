# Chapter 9: GUI Applications

## 9.1 Why are GUIs single-threaded?

There have been many attempts to write multithreaded GUI frameworks, but because of persistent problems with race
conditions and deadlock, they all eventually arrived at the single-threaded event queue model in which a dedicated
thread fetches events off a queue and dispatches them to application-defined event handlers.

Single-threaded GUI frameworks achieve thread safety via thread confinement; all GUI objects, including visual
components and data models, are accessed exclusively from the event thread.

### 9.1.1 Sequential event processing

GUI applications are oriented around processing fine-grained events such as mouse clicks, key presses, or timer
expirations. Events are a kind of task; the event handling machinery provided by AWT and Swing is structurally similar
to an `Executor`.

Tasks that execute in the event thread must return control to the event thread quickly. To initiate a long-running task
such as spell-checking a large document, searching the file system, or fetching a resource over a network, you must run
that task in another thread so control can return quickly to the event thread.

### 9.1.2 Thread confinement in Swing

The Swing single-thread rule: Swing components and models should be created, modified, and queried only from the
event-dispatching thread.

### 9.2 Short-running GUI tasks

For simple, short-running tasks, the entire action can stay in the event thread; for longer-running tasks, some of the
processing should be offloaded to another thread.

So long as tasks are short-lived and access only GUI objects (or other thread-confined or **thread-safe application
objects**), you can almost totally ignore threading concerns and do everything from the event thread.

### 9.3 Long-running GUI tasks

Long-running tasks (such as spell checking, background compilation, or fetching remote resources) must run in another
thread so that the GUI remains responsive while they run.

### 9.3.1 Cancellation

Cancellation could implemented directly using thread interruption, but it is much easier to use `Future`, which was
designed to manage cancellable tasks. When cancel is called on a `Future` with `mayInterruptIfRunning` set to `true`,
the `Future` implementation interrupts the thread that is executing the task if it is currently running.

### 9.3.2 Progress and completion indication

`FutureTask` has a `done` hook that similarly facilitates completion notification. i.e.:

```java
/**
 * Background task class supporting cancellation, completion notification, and progress notification.
 */
abstract class BackgroundTask<V> implements Runnable, Future<V> {
  private final FutureTask<V> computation = new Computation();
  private class Computation extends FutureTask<V> {
    public Computation() {
      super(new Callable<V>() {
        public V call() throws Exception {
          return BackgroundTask.this.compute();
        }
      });
    }
    protected final void done() {
      GuiExecutor.instance().execute(new Runnable() {
        public void run() {
          V value = null;
          Throwable thrown = null;
          boolean cancelled = false;
          try {
            value = get();
          } catch (ExecutionException e) {
            thrown = e.getCause();
          } catch (CancellationException e) {
            cancelled = true;
          } catch (InterruptedException consumed) {
          } finally {
            onCompletion(value, thrown, cancelled);
          }
        };
      });
    }
  }
  protected void setProgress(final int current, final int max) {
    GuiExecutor.instance().execute(new Runnable() {
      public void run() { onProgress(current, max); }
    });
  }
  // Called in the background thread
  protected abstract V compute() throws Exception;
  // Called in the event thread
  protected void onCompletion(V result, Throwable exception,
    boolean cancelled) { }
  protected void onProgress(int current, int max) { }
  // Other Future methods forwarded to computation
}

// ... Initiating a long-running, cancellable task with BackgroundTask

startButton.addActionListener(new ActionListener() {
  public void actionPerformed(ActionEvent e) {
    class CancelListener implements ActionListener {
      BackgroundTask<?> task;
      public void actionPerformed(ActionEvent event) {
        if (task != null)
          task.cancel(true);
      }
    }
    final CancelListener listener = new CancelListener();
    listener.task = new BackgroundTask<Void>() {
      public Void compute() {
        while (moreWork() && !isCancelled())
          doSomeWork();
        return null;
      }
      public void onCompletion(boolean cancelled, String s,
        Throwable exception) {
        cancelButton.removeActionListener(listener);
        label.setText("done");
      }
    };
    cancelButton.addActionListener(listener);
    backgroundExec.execute(listener.task);
  }
});
```

## 9.4 Shared data models

Swing presentation objects, including data model objects such as `TableModel` or `TreeModel` , are confined to the event
thread.

When some processing of data is performed in another thread, different from the event thread, for example, to update a
tree model, this could be done by using a thread-safe tree model, by “pushing” the data from the background task to the
event thread by posting a task with invokeLater, or by having the event thread poll to see if the data is available.

### 9.4.1 Thread-safe data models

As long as responsiveness is not unduly affected by blocking, the problem of multiple threads operating on the data can
be addressed with a thread-safe data model. If the data model supports fine-grained concurrency, the event thread and
background threads should be able to share it without responsiveness problems.

It may sometimes be possible to get thread safety, consistency and good responsiveness with a versioned data model such
as `CopyOnWriteArrayList`.

### 9.4.2 Split data models

A program that has both a presentation-domain and an application-domain data model is said to have a split-model design.

Consider a split-model design when a data model must be shared by more than one thread and implementing a thread-safe
data model would be inadvisable because of blocking, consistency, or complexity reasons.

## Summary

GUI frameworks are nearly always implemented as single-threaded subsystems in which all presentation-related code runs
as tasks in an event thread. Because there is only a single event thread, long-running tasks can compromise
responsiveness and so should be executed in background threads. Helper classes which provide support for cancellation,
progress indication, and completion indication, can simplify the development of long-running tasks that have both GUI
and non-GUI components.
