package com.cy.bi.project.dao

import com.cy.bi.project.domain.{CourseClickCount, CourseSearchClickCount}
import com.cy.bi.project.utils.HBaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

/***
  * 课程点击数 数据库访问层
  */

object CourseSearchClickCountDAO {
  val tableName = "course_search_click"
  val cf = "info"
  val qulifer = "click_count"

  def save(list: ListBuffer[CourseSearchClickCount]): Unit = {

    val table = HBaseUtils.getInstance().getTable(tableName)

    for(ele <- list) {
      table.incrementColumnValue(ele.day_search_course.getBytes,
        cf.getBytes,
        qulifer.getBytes,
        ele.click_count)
    }

  }

  def query(day_course:String): Long = {
    val table = HBaseUtils.getInstance().getTable(tableName)

    val get = new Get(day_course.getBytes)
    val value = table.get(get).getValue(cf.getBytes, qulifer.getBytes)

    if(value == null){
      0L
    }else {
      Bytes.toLong(value)
    }
  }

  def main(args: Array[String]): Unit = {

    val list = new ListBuffer[CourseSearchClickCount]
    list.append(CourseSearchClickCount("20181111_www.baidu.com_1",10))
    list.append(CourseSearchClickCount("20181111_www.baidu.com_2",100))

    save(list)
    println(query("20181111_www.baidu.com_1").toString + ':' + query("20181111_www.baidu.com_2").toString)
  }

}
