package com.cy.bi

import org.apache.spark.SparkConf
import org.apache.spark.streaming.flume.FlumeUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object FlumePush {

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      System.err.println("Usage: FlumePush <hostname> <port>")
      System.exit(1)
    }

    val Array( hostname, port ) = args

    val conf = new SparkConf() //.setAppName("FlumePush").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(5))

    //val lines = ssc.socketTextStream("localhost",6789)
    val flumeStream = FlumeUtils.createStream(ssc, hostname,port.toInt)

    //val res = lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
    val res = flumeStream.map(x => new String(x.event.getBody().array()).trim)
        .flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
    res.print()


    ssc.start()
    ssc.awaitTermination()
  }
}
