package com.cy.bi

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaReceiver {
  def main(args: Array[String]): Unit = {

    if (args.length != 4) {
      System.err.println("Usage: KafkaReceiver <zkrum> <group> <topics> <num>")
      System.exit(1)
    }

    val Array(zkrum, group, topics, num) = args
    val conf = new SparkConf().setAppName("KafkaReceiver").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(5))

    val topicsMap = topics.split(",").map((_,num.toInt)).toMap
    val message = KafkaUtils.createStream(ssc, zkrum, group, topicsMap)
    message.map(_._2).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    ssc.start()
    ssc.awaitTermination()
  }
}
