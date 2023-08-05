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


## **`Consumer Groups`**
Consumer Groups:
  - All the consumers in an application read data as a consumer groups
  - Each consumer within a group reads from exclusive partitions
  - ![](screenshots/2023-08-05-20-05-21.png)
  - What if too many Consumer?
    - Some consumers will be inactive - bottle neck
    - no. of consumer > no. of topic
    - ![](screenshots/2023-08-05-20-06-53.png)
  - It is acceptable to have multple consumer groups on the same topic
  - ![](screenshots/2023-08-05-20-08-01.png)
  - Why multiple consumer groups?
    - 1 consumer group per service
      - Ex: 1 consumer group for location service, and 1 for notification service reading the same message
  - To create distinct consumer groups, use the consumer property `group.id`
  
## **`Consumer Offsets`**
  - Kafka stores the offsets at which a consumer group has been reading
  - The offsets committed are in Kafka topic named `__consumer_offsets`
  - When a consumer in a group has processed daata received from Kafka, it should **periodically committing the offsets** 
    - Kafka broker will write to `__consumer_offsets`, not the group itself
    - can also indicate how far the consumer has succesfully reading into the Kafka Topic
    - when failure, it can go back to the latest committed state

## **`Delivery semantics for consumers`**
  - By default, Java COnsumers will automatically connit offsets (at least one)
  - There are 3 delivery semantics if you choose to commit manually
    - `At lease once (usualy preferred)`
      - Offsets are committed after the message is processed
      - If the processing goes wrong , the message will be read again
      - This can result in duplicate processing of messages. Make sure your processing is `idempotent` 
        - means, proccesing again the messages wont impact your systems
    - `At most once`
      - offsets are committed as soon as messages are received
    - `Exactly once`
      - For Kafka => Kafka workflows: use the Transactional API (easy with Kafka Streams API)
      - For Kafka => External System workflows: use an idempotent consumer

## **`Kafka Brokers`**
  - A kafka cluster is composed of multipler brokers (servers)
  - Each broker is identified with its id
  - Each broker containers certain topic partitions
  - After connecting to any broker(called a boostrap broker), you will be connected to the entire cluster
  - A good number to get started is 3 brokers, some big clusters have over 100 brokers

## **`Brokers and Topics`**
  - Example of Topic-A with 3 partitions and Topic-B with 2 partitions
  - Note: data is distributed, and broker 103 doesnt have any topic B data
  - ![](screenshots/2023-08-05-20-32-11.png)

## **`Kafka Broker Discovery`**
  - Each Kafka broker is also called a `bootstrap server`
    - means that `you only need to connect to one broker`, and the Kafka clients will know how to be connected to the entire cluster (smart clients)
    - ![](screenshots/2023-08-05-20-34-32.png)
  - Each broker knows about all brokers, topics and partitions (metadata)

## **`Topic Replication Factor`**
  - Topics should have a replication `factor > 1 (usually between 2 and 3)`
  - This way if a broker is down, another broker can have the data and can serve the data
  - Example: Topic-A with 2 partitions and replication factor of 2
  - ![](screenshots/2023-08-05-20-48-44.png)
  - If we lose Broker 102
    - Broker 101 and 103 can still serve the data, they have replica of Topic A

## **`Concept of Leader for a Partition`**
  - At any time only ONE broker can be a leader for a given partition
  - Producers can only send data to the broker that is leader of a partition
    - The other brokers will replicate the data
    - Therefore, each partition has one leader and multiple ISR(in-sync replica)
    - ![](screenshots/2023-08-05-20-54-48.png)
  - Kafka Consumers by default will read from the leader broker for a partition

## **`Kafka Consumers Replica Fetching (Kafka v2.4+)`**
  - It is possible to configure consumers to read from the closest replica
  - This may help to improve latency, and also decrease network costs if using the cloud
  - ![](screenshots/2023-08-05-20-59-29.png)
    - In this example, the consumer is reading from the replica, not the leader

## **`Producer Level Message Compression`**
  - Producer usually send data that is text-based, for example with JSON data
  - It its important to apply compression to the producer
    - less memory
  - Compression can be enabled at the Producer level and does not require any configuration change in the Brokers or in the Consumers
  - compression.type can be:
    - none (default)
    - `gzip`
    - `lz4`
    - `snappy`
    - `zstd` (Kafka 2.1)
  - Compression is more effective the bigger the batch of message being sent to Kafka!
    - ![](screenshots/2023-08-05-22-35-13.png)
  - Advantages:
    - Much smaller producer request size (compression ratio up to 4x!)
    - Faster to transfer data over the network => less latency
    - Better throughput
    - Better disk utilisation in Kafka (stored messages on disk are smaller)
  - Disadvantages (very minor)
    - Producer must commit some CPU cycles to compressions
    - Consumers must commit some CPU cycles to decompressions
  - Overall:
    - Consider testing `snappy` or `lz4` for optimal speed / compression ration
    - Consider tweaking `linger.ms` and `batch.size` to have bigger batches, and therefor more compression and higher throughput
    - Use compression in production


## **`Broker/Topic Level Message Conversion`**
  - There is also a setting you can set at the broker level (all topics) or topic-level
  - `compression.type=producer` (default)
    - the broker takes the compressed batch from the producer client and writes it directly to the topic's log file without recompressing the data
    - pushes the necessity of compression at the producer
  - `compression.type=none`
    - all batches are decompressed by the broker
  - `compression.type=lz4`
    - if its matching the producer setting, data is stored on disk as is
    - if its a different compression setting, batches are decompressed by the broker and then redecompressed using the specified compression algorithm
  - `WARNING` : If you enable broker side compression, it will consume extra CPU cycles. Optimal is using Producer Level Message Conversion


## **`linger.ms & batch.size`**
  - by default, Kafka producers try to send records as soon as possible
    - It will have up to `max.in.flight.requests.per.connection=5`, meaning up to 5 message batches being in flight (being sent between the producer in the broker) at most
    - After this, if more messages must be sent while others are in flight, Kafka is smart and will start batching them before the next batch send
  - Smart batching helps increase throughput while maintaining very low latency
    - Added benefit: batches have higher compression ratio so better efficiency
  - Two settings to influence the batching mechanism
    - `linger.ms` (default 0)
      - how long to wait until we send a batch
      - Adding a small number, for example, 5 ms helps add more messages in the batch at the expense of latency
    - `batch.size` (default 16KB)
      - max number of bytes that will be included in a batch
      - Increasing a batch size to something like 32KB or 64 KB can help increasing the:
        - compression
        - throughput
        - efficiency of requests
      - Disadvantage of increasing the batch size
        - may increase memorey usage on the producer side
        - increase latency, as the producer will wait for the batch to fill  up
      - Any message that is bigger than the batch size will not be batched
      - A batch is allocated per partition, so make sure that you dont set it to a number thats too high, otherwise youll run waste memory!
      - if a batch is filled before `linger.ms`, increase the batch size
    - ![](screenshots/2023-08-05-23-48-28.png)

## **`High Throughput Producer`**
  - increase the `linger.ms` and the producer will wait a few milliseconds for the batches to fill up before sending them
  - if you are sending full batches and have memory to spare, you can use `batch.size` and ssend larger batches
  - Introducer some producer-level compression for more efficiency in sends
  - ```java
    // high troughput producer (at the expense of a bit of latency and CPU usage)
    properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
    properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
    properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024))
    ```
  - What is Producer Throughput:
    - This refers to how quickly the Kafka producer can send messages to the Kafka broker. 
    - Higher producer throughput means the producer can send more messages to Kafka in a given time frame. 
    - To achieve higher producer throughput, various factors come into play, such as message size, batch size, compression settings, and network latency.
  - What is Latency
    - refers to the time it takes for a single message to be sent from the producer to the Kafka broker
    - Low latency means that messages are sent quickly with minimal delay.
  - Latency vs Throughput
    - ⬆️ Larger Batches, ⬇️ Lower Latency
      - When you increase the producer's throughput by sending more messages in each batch or increasing the number of messages sent per unit of time, the latency tends to decrease.
        - This is because the producer accumulates more messages before sending them in a batch, reducing the overhead of establishing a new connection and sending individual messages.
      - By batching multiple messages together, the producer can send them in one network request, reducing the per-message overhead. 
      - This batching approach typically leads to lower latency as the number of network requests is reduced.

## **`Producer Acknowledgements (acks)`**
  - Producers can choose to receive acknowledgements of data writes
    - confirmation that write successfully happend
    - `acks=0`
      - Producer wont wait for acknowledgement (possible data loss) 
    - `acks=1`
      - Producer will wait for leader acknowledgement (limited data loss)
    - `acks=all`
      - Leader + Replicas acknowldgement (no data loss)
  - ![](screenshots/2023-08-06-00-12-03.png)

## **`Kafka Topic Durability`**
  - For a topic replication factor of 3, topic data durability can withstand 2 brokers loss
  - As a rule, for a replication factor of N, you can permanently lose up to N-1 brokers and still recover your data

## **`Zookeeper`**
  - manages brokers (keeps a list of them)
  - helps in performing leader election for partitions
  - sends notifications to Kafka in case of changes:
    - eg: new topic, broker dies, broker comes up, delete topics
  - Kafka 2.x cant work without Zookeeper
  - Kafka 3.x can work without Zookeeper(KIP 500) - using Kafka Raft instead
  - Kafka 4.x will not have Zookeeper
  - Zookeeper by design to operates with an odd number of servers(1, 3, 5, 7)
  - Zookeeper has a leader (writes) the rest of the servers are followers (reads)
  - (Zookeeper does NOT store consumer offsets with Kafka > v0.10)
  - Zookeeper Cluster (`ensemble`)
  - ![](screenshots/2023-08-06-00-20-34.png)
  - Should you use Zookeeper?
    - With Kafka Brokers?
      - Yes, until Kafka 4.0 is out
      - while waiting forr Kafka without Zookeeper to be production ready
    - With Kafka Clients?
      - Over time, the Kafka clients and CLI have been migrated to leverage the brokers as a connection endpoint instead of Zookeeper
      - Since Kafka 0.10, consumers store offset in Kafka and Zookeeper, and must not connect to Zookeeper as its deprecated
      - Since Kafka 2.2, the kafka-topics.sh CLI command references Kafka brokers and not Zookeeper for topic management (creation, deletion, etc)
      - as a modern day Kafka developer. never use Zookeeper as a configuration in your Kafka clients, and other programs that connect to Kafka as it is unsecure

## **`About Kafka KRaft`**
  - KIP 500 - Kafka project started to work to remove the Zookeeper dependency
  - Zookeeper shows scaling issues when Kafka clusters have > 100_000 partitions
  - By removing Zookeeper, Kafka can:
    - Scale to millions of partitions, and becomes easier to maintain and set-up
    - improves stability, makes it easier to monitor, support and admin
    - Single security model for the whole system
    - Single process to start with Kafka
    - Faster controller shutdown and recovery time
  - KRaft Architecture