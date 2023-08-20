## Intro to Kafka
Kafka
  - asynchrounous Publish Subscribe model
  - a replayable logs
    - can play the role of event store
      - Event Store: a middle groud between a messaging system and a database
    - replayable logs decouple services from one another
      - much like a messaging system does
      - but they also provide a central point of storage that is fault-tolerant and scalable
      - a shared source of truth that any application can fall back to
      - you can not query a replayable log
        - it is purely about storage
        - and pushing it to somewhere new (pure data movement)
    - Benefits of replayable logs:
      - 1st: it makes it easy to react to events that are happening now, with a toolset specifically designed for manipulating them
      - 2nd: it provides a central repository that can push whole datasets to wherever they may be needed
  
  - Microservices 
    - don’t share their databases with one another
      - IntegrationDatabase antipattern
      - databases have very rich APIs that are wonderfully useful on their own
      - but when widely shared they make it hard to work out if and how one application is going to affect others

  - Streaming systems:
    - still have db like attributes
      - tables - for lookups
      - transactions - for atomicity
    - but approach is more to functional or dataflow language
      - immutability
      - pure functions
      - data transformations

## The Origins of Streaming
  - the world of real-time analytics, has heavily influenced the way we build event-driven systems today


## Kafka is like REST but Asynchronous
Kafka
  - provides an asynchronous protocol for connecting programs together
  - different from TCP, HTTP, or RPC protocol
    - difference is the presence of a `broker`
  - Broker
    - broadcasts messages to any programs that are interested in them
    - storing them for as long as is needed
    - perfect for streaming or fire-and-forget messaging
  - Ex:
    - a Request-Response model
      - you have service for querying customer info
      - getCustomer() method, passing a CustomerId
    - implementation using Kafka
      - use 2 topics
        - 1st topic: transports the request
        - 2nd topic: transports the response
      - People build system like this
        - but in such cases the broker doesn’t contribute all that much
        - There is no requirement for broadcast
        - There is also no requirement for storage
      - would you be better off using a stateless protocol like HTTP?
      - Kafka is use for event-based communication
  - Events:
    - are business facts that have value to more than one service and are worth keeping around.