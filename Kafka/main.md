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

## **`Kafka Topics`**
Topics 
  - parictula stream of data in a Kafka Cluster
  - similar to a table in a DB (w/o all the constraints)
  - identify a topic bi its `name`
  - support any kind of message format (JSON, text file)
  - The sequence of messages is called a `data stream`
  - You can not query topics, instead,
    - use Kafka Producers to send data
    - use Kafka Consumers to read data
  ![](screenshots/2023-08-05-19-29-06.png)

## **`Partitions and offsets`**
- Topics are split in `partitions`(Ex: 100 partitions)
  - Messages that are sent within Kafka topic
  - Messages within each partition are ordered
  - Each message within a partition gets and incremental id, called `offset`
  - ![](screenshots/2023-08-05-19-32-32.png)
  - Kafka topics are immutable: once data is written to a partition, it cannot be changed
    - data cannot be deleted or update
    - keep on writing through partition

Data in Kafka is kept only for a limited time (default is one week - configurable)
Offsets only have meaning for a specific partition
  - Ex: offset 3 in partition 0 doesnt represent the same data as offset 3 in partition 1
  - Offsets are not re-used even if previous messages have beed deleted
Order is guaranteed only within a partition (not across partitions)
You can have as many partitions per topic as you want


## **`Producers`**
Producers
  - write data to topics(which are mady by partitions)
  - know to which partition to write to (and whichKafka broker has it)
  - In case of Kafka broker failuers, PRoducers will automatically recover

Producer Message keys:
  - Producers can choose to send a key with the message (string, number, binary)
  - if key = null, data is sent round robin (partition 0, then 1, then 2...)
  - if key != null, the all messages for that key will always go to the same partition
  - A key are typically send if you need message orrdering for a specific field (ex: truck_id)
    - ![](screenshots/2023-08-05-19-44-40.png)

## **`Kafka Message Anatomy`**
![](screenshots/2023-08-05-19-46-34.png)


## **`Kafka Message Serializer`**
Kafka Message Serializer
  - Kafka only accepts bytes as an input from producers and sends bytes out as an output to consumers
  - `Message Serialization` means transforming objects / data into bytes
  - They are used on the value and the key
    - ![](screenshots/2023-08-05-19-49-29.png)


## **`Kafka Consumers`**
Consumers
  - read data from a topic(identified by name) - pull model
  - may read from 1 or more Partition from a topic
  - automatically know which broker to read from
  - In case of broker failures, consumers know how to recover
  - Data is read in order from low to high offset `within each partitions`


## **`Consumer Deserialization`**
Deserializer
  - indicates how to transform
  - consumer must know in advance the format of the message
  - ![](screenshots/2023-08-05-20-01-40.png)