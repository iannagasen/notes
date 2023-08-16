# JOSH LONG - REACTIVE SPRING

## Reading a file Synchronously

```java
class Synchronous implements Reader {
  
  @Override
  public void read (
    File file, Consumer<Bytes> consumer, Runnable f
  ) throws IOException {
    try (FileInputStream in = new FileInputStream(file)) {
      byte[] data = new byte[FileCopyUtils.BUFFER_SIZE];
      int res;
      while((res = in.read(data, 0, data.length)) != -1) {
        consumer.accept(Bytes.from(data, res));
      }
      f.run();
    }
  }
}
```
- What is wrong with this code?
  - It is working IF:
    - there is the file needed
    - and the hard drive is working
  - What if used a different implementation of `InputStream` and read data from a network socket
  - What happens if the network is slow or down


### The Reactive Streams Initiative
  - What we want:
    - maps something to asynchronous I/O (By JDK)
    - supports `push-back` mechanism
    - or `flow-control` in distributed systems
  - Projects that support Reactive Programming
    - Vert.x
    - Akka Streams
    - RxJava 2
    - Reactor for Project Stream
  - De Facto standard -> The `Reactive Streams` initiative
  - It defines 4 types
    - **`Publisher<T>`**
      - a producer of values that may eventually arrive
      - a `Publisher<T>` produces values of type `T` to a `Subscriber<T>`
      - ```java
        package org.reactivestreams

        public interface Publisher<T> {
          void subscribe(Subscriber<? super T> s)
        }
        ```
    - **`Subscriber<T>`**
      - subscribes to a Publisher<T>
      - receives notifications on any new values of type `T` through its `onNext()`
      - if errors are called, `onError(Throwable)` is called
      - ```java
        public interface Subscriber<T> {
          void onSubscribe(Subscription s);
          void onNext(T t);
          void onError(Throwable t);
          void onComplete();
        }
        ```
    - **`Subscription`**
      - When a `Subscriber` first connects to a `Publisher`
        - it is given a `Subscription` in the `Subscriber::onSubscribe`
      - `Subscription` enables `backpressure`
        - Subscriber uses:
          - `Subscription::request` method to request more data, `long n` more times, or Long.MAX_VALUE which is effective unlimited
          - `Subscription::cancel` to halt the processing
      - ```java
        public interface Subscription {
          void request(long n);
          void cancel();
        }
        ```
    - **`Processor`**
      - ```java
        public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {

        }
        ```
  - UML Diagram

    - ```mermaid
      classDiagram
          class Publisher {
              +subscribe(s)
          }
          class Subscriber {
              +onSubscribe(s)
              +onNext(t)
              +onError(t)
              +onComplete()
          }
          class Subscription {
              +request(n)
              +cancel()
          }
          class Processor {
              +onSubscribe(s)
              +onNext(t)
              +onError(t)
              +onComplete()
          }

          Publisher --|> Subscriber : implements
          Processor --|> Subscriber : implements
          Processor --|> Publisher : implements
          Subscriber --|> Subscription : has
      ```