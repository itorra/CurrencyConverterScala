package com.converter

import java.net.{UnknownHostException, URL}
import java.io.File
import javax.annotation.processing.FilerException

//import java.util.Map

import scala.xml.XML
import sys.process._

/**
 * Created by ido on 15/06/15.
 */
class TestParser extends Runnable {
  val boiXml: String = "http://www.boi.org.il/currency.xml"
  var xmlFile: String = "data.xml"
  var xmlTmpFile: String = "dataTmp.xml"
  var map:Map[String, Currency] = Map()
  var date: String = "An error occurred reading file"
  var src: xml.Elem = _
  var isValidDataExist: Boolean = false

  override def run(): Unit = { this.synchronized {
    while (true) {
      try {
        loadBOIXML()
        dataHandle()
      } catch {
        case ex: UnknownHostException => {
        println("Connection Error")
        loadLocal()
        }
        case ex: FilerException => {
          loadLocal()
        }
      }
      try {
        println("going to sleep for a minute")
        Thread.sleep(60000)
      } catch {
        case ex: InterruptedException => {
          println(ex)
        }
      }
    }
  } }

  def dataHandle(): Unit = {
    val tmpDate = (src\\"LAST_UPDATE").text
    if ( !isSameDate(tmpDate) ) {
      buildMap()
      date = tmpDate
      isValidDataExist = true
      storeXMLFile()
    }
  }

  def loadLocal(): Unit = {
    if (!isValidDataExist) {
      try {
        loadLocalXML()
        dataHandle()
      } catch {
        case ex: FilerException => throw new Exception("error2")
      }
    }
  }

  def getMap(): Map[String,Currency] = map

  def buildMap (): Unit = {
    for (curr<-src\\"CURRENCY"){
      val name = (curr\\"NAME").text
      val unit = (curr\\"UNIT").text.toInt
      val code = (curr\\"CURRENCYCODE").text
      val country = (curr\\"COUNTRY").text
      val rate = (curr\\"RATE").text.toDouble
      val change = (curr\\"CHANGE").text.toDouble
      val currentCurrency= new Currency(name,unit,code,country,rate,change)
      map += (code -> currentCurrency)
    }
  }

  def isSameDate(rhs: String): Boolean = {
    if (rhs.size == 0)
      throw new Exception("error1")
    date.equals(rhs)
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

  def loadBOIXML(): Unit = {
    src = XML.load(boiXml)
  }
  
  def loadLocalXML(): Unit = {
    src = XML.load(xmlFile)
  }
  
  def storeXMLFile(): Unit = {
    XML.save(xmlFile,src)
  }

}
