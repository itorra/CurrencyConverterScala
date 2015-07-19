package com.converter

import java.net.{UnknownHostException}
import javax.annotation.processing.FilerException
import org.apache.log4j.Logger;

import scala.xml.XML

class BOIParser extends Runnable {
  val boiXml: String = "http://www.boi.org.il/currency.xml"
  var xmlFile: String = "data.xml"
  var xmlTmpFile: String = "dataTmp.xml"
  var map: Map[String, Currency] = Map()
  var date: String = "An error occurred reading file"
  var src: xml.Elem = _
  var isValidDataExist: Boolean = false
  var msgConsumer: MessageConsumer = null
  val parserLogger = Logger.getLogger(this.getClass.getName)

  def setConsumer(messageConsumer: MessageConsumer): Unit = {
    msgConsumer = messageConsumer
  }

  override def run(): Unit = {
    this.synchronized {
      while (true) {
        try {
          loadBOIXML()
          dataHandle()
        } catch {
          case exception: UnknownHostException => {
            parserLogger.error("Connection Error. Loading local file", exception)
            msgConsumer.consume("Connection Error - loading local file last updated at " + date)
            loadLocal()
          }
          case exception: Exception => {
            parserLogger.error("Download Error. Loading local file", exception)
            msgConsumer.consume("Error Downloading file - loading local file last updated at " + date)
            loadLocal()
          }
        }
        try {
          Thread.sleep(300000)
        } catch {
          case ex: InterruptedException => {
            parserLogger.error(ex)
          }
        }
      }
    }
  }

  def dataHandle(): Unit = {
    val tmpDate = (src \\ "LAST_UPDATE").text
    if (!isSameDate(tmpDate)) {
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
        case ex: FilerException => {
          throw new Exception("error2")
        }
      }
    }
  }

  def getMap(): Map[String, Currency] = map

  def buildMap(): Unit = {
    for (curr <- src \\ "CURRENCY") {
      val name = (curr \\ "NAME").text
      val unit = (curr \\ "UNIT").text.toInt
      val code = (curr \\ "CURRENCYCODE").text
      val country = (curr \\ "COUNTRY").text
      val rate = (curr \\ "RATE").text.toDouble
      val change = (curr \\ "CHANGE").text.toDouble
      val currentCurrency = new Currency(name, unit, code, country, rate, change)
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
    val countries: Array[String] = new Array[String](map.size + 1)
    countries(0) = "Israel"
    var i = 1
    map.values.foreach(curr => {
      countries(i) = curr.country
      i = i + 1
    })
    countries
  }

  def getLabels(): Array[String] = {
    val labels: Array[String] = new Array[String](map.size + 1)
    labels(0) = "Israel Shekel"
    var i = 1
    map.values.foreach(curr => {
      labels(i) = curr.country + " " + curr.name
      i = i + 1
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
    XML.save(xmlFile, src)
  }
}
