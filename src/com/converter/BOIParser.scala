package com.converter

import java.net.URL
import java.io.File
//import java.util.Map

import scala.xml.XML
import sys.process._

/**
 * Created by ido on 15/06/15.
 */
class BOIParser extends Runnable {
  val boiXml: String = "http://www.boi.org.il/currency.xml"
  var xmlFile: String = "data.xml"
  var map:Map[String, Currency] = Map()
  var date: String = "An error occured reading file"

  def buildMap (): Map[String,Currency] = {
    val src = XML.loadFile(xmlFile)
    date = (src\\"LAST_UPDATE").text
    println("Updated to:  " + date)
    for (curr<-src\\"CURRENCY"){
      val name = (curr\\"NAME").text
      val unit = (curr\\"UNIT").text.toInt
      val code = (curr\\"CURRENCYCODE").text
      val country = (curr\\"COUNTRY").text
      val rate = (curr\\"RATE").text.toDouble
      val change = (curr\\"CHANGE").text.toDouble
      val currentCourency = new Currency(name,unit,code,country,rate,change)
      map += (code -> currentCourency)
    }
    map
  }

  def getLastUpdated(): String = date

  def getCountries(): Array[String] = {
    val countries: Array[String] = new Array[String](map.size+1)
    countries(0) = "Israel"
    var i = 1
    map.values.foreach(curr => {
      countries(i) = curr.country
      i = i+1
    })
    countries
  }

  def getLabels(): Array[String] = {
    val labels: Array[String] = new Array[String](map.size+1)
    labels(0) = "Israel Shekel"
    var i = 1
    map.values.foreach(curr => {
      labels(i) = curr.country + " " + curr.name
      i = i+1
    })
    labels
  }

  def downloadFile(): Unit = {
    new URL(boiXml) #> new File(xmlFile) !!

  }
  var time: Int = 0
  override def run(): Unit = {
    while (true) {
      println(time)
      time = time+ 1
      Thread.sleep(1000)    }

  }
}
