package com.example

import org.apache.spark.{SparkContext, SparkConf}

/**
  * This job was created for testing purposes.
  *
  * It does a very simple batch processing of a text (word count, what else?).
  */
object TestBatchJob extends App {

  val line1 = "Alice was beginning to get very tired of sitting by her sister on the " +
    "bank, and of having nothing to do: once or twice she had peeped into the " +
    "book her sister was reading, but it had no pictures or conversations in " +
    "it, 'and what is the use of a book,' thought Alice 'without pictures or " +
    "conversations?'"

  val conf = new SparkConf().
    setAppName("Test Batch Job")

  val sc = new SparkContext(conf)
  val textFile = sc.parallelize(List(line1))

  val tokenCounts = textFile.flatMap(line => line.split(" "))
    .map(word => (word, 1))
    .reduceByKey(_ + _)

  tokenCounts.take(10).foreach(println)

  sc.stop()
}
