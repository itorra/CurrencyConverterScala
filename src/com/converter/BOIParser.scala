package com.converter

import java.net.URL
import java.io.File
//import java.util.Map

import scala.xml.XML
import sys.process._

/**
 * Created by ido on 15/06/15.
 */
class BOIParser {
  val boiXml: String = "http://www.boi.org.il/currency.xml"
  var xmlFile: String = "data.xml"



    def buildMap (): Map[String,Currency] = {
    val src = XML.loadFile(xmlFile)
    var map:Map[String, Currency] = Map()
    for (curr<-src\\"CURRENCY"){
      val name = (curr\\"NAME").text
      val unit = (curr\\"UNIT").text.toInt
      val code = (curr\\"CURRENCYCODE").text
      val country = (curr\\"COUNTRY").text
      val rate = (curr\\"RATE").text.toDouble
      val change = (curr\\"CHANGE").text.toDouble
      val c = new Currency(name,unit,code,country,rate,change)
      map += (code -> c)
    }
    map
  }

  def downloadFile(): Unit = {
    new URL(boiXml) #> new File(xmlFile) !!
    //TODO - To check if download was ok
  }

}
