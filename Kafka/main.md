## By Stephane Maarek

### Conduktor
How Company Start

Source System -> Target System
ETL

Company Evolve
Many Source
Many Target
Ex: 4 source systems -> 6 target systems:
You need to write 24 integrations
Each integration comes with difficulties around:
  - Protocol
    - how the data is transported(TCP, HTTP, REST, FTP, JDBC)
  - Data Format
    - how the data is parsed(Binary, CSV, JSON, AVRO, ProtoBuff)
  - Data Schema and Evolution
    - how the data is shaped and may changed

Each source system will have an increased load from the connections

### Why Apache Kafka:

Decoupling of data streams & systems
Source System 
  - Producer 
    - creates data stream / data created real time
    - Ex: 
      - Website Events
      - Pricing Data
      - Financial Transactions
      - User Interactions
Target System 
  - Consumer
    - Ex:
      - Database
      - Analytics
      - Email System
      - Audit
![](screenshots/2023-08-05-19-10-43.png)

Why Apache Kafka
  - Distributed, resilient architecture, fault tolerance
  - Horizontal Scalability
    - Can scale to 100s of brokers
    - Can scale to millions of messages per second
  - High Performance (latency of less than 10ms) - real time

## Use Cases:
  - Messaging System
  - Activity Tracking
  - Gather metrics from many different locations
  - Application Logs gathering
  - Stream processing (with the Kafka Streams API for example)
  - De-coupling of system dependencies
  - Integration with Spark, Flink, Storm, Hadoop and other Big Data technologies
  - Micro-services pub/sub

## How company uses Kafka
  - Netflix:
    - use Kafka to apply recommendations in real time while youre watching TV shows
  - Uber: 
    - uses Kafka to gather user, taxi and trip data in real-time to compute and forecast demand, and compute surge pricing in real time
  - LinkedIn
    - uses Kafka to prevent spam, collect user interactions to make better connection recommendations in real-time
  - Remember: Kafka is only used as a transportation mechanism