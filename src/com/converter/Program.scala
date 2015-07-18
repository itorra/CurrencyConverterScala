package com.converter

import java.io.FileWriter
import java.net.URL

import scala.io.Source

/**
 * Created by ido on 26/06/15.
 */
object Program {

  def main(args: Array[String]) {

    val boiXml: String = "http://www.boi.org.il/currency.xml"
    var xmlFile: String = "data.xml"
    var xmlTmpFile: String = "dataTmp.xml"
    try{
      val src = scala.io.Source.fromURL(boiXml)
    }
    catch {
      case e:java.io.IOException => "could not connect"

    }

//    try {
//      val src = scala.io.Source.fromURL(boiXml)
//      val out = new java.io.FileWriter(xmlTmpFile)
//      out.write(src.mkString)
//      out.close
//    } catch {
//      case e: java.io.IOException => "error occured"
//    }

    try {

    }





  }

}
