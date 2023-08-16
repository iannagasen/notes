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
    - Publisher
      - ```java
        package org.reactivestreams

        public interface Publisher<T> {
          void subscribe(Subscriber<? super T> s)
        }
        ```
    - Subscriber
      - ```java
        public interface Subscriber<T> {
          void onSubscribe(Subscription s);
          void onNext(T t);
          void onError(Throwable t);
          void onComplete();
        }
        ```
    - Subscription
      - ```java
        public interface Subscription {
          void request(long n);
          void cancel();
        }
        ```
    - Processor
      - ```java
        public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {
          
        }
        ```