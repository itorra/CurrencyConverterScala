package com.converter

import org.apache.log4j.Logger;
import scala.collection.Map

class RateCalculator() extends IRateCalculatorModel {
  var map: Map[String, Currency] = _
  var rateTable = Array.ofDim[Double](0, 0)
  var i: Int = 1
  val calcLogger = Logger.getLogger(this.getClass.getName)

  override def calcRate(from: String, to: String, amount: Double): Double = {
    val fromIndex = countryToIndex(from)
    val toIndex = countryToIndex(to)
    val retVal = rateTable(fromIndex)(toIndex) * amount
    calcLogger.info("conversion result: " + retVal)
    retVal
  }

  def countryToIndex(country: String): Int = {
    var retVal: Int = 0
    var num: Int = 1
    if (!country.equals("Israel")) {
      map.values.foreach(curr => {
        if (curr.country.equals(country)) {
          retVal = num
        } else {
          num = num + 1
        }
      })
    }
    retVal
  }

  def placeInitValue(curr: Currency) = {
    rateTable(0)(i) = 1 / (curr.rate / curr.unit)
    rateTable(i)(0) = (curr.rate / curr.unit)
    i = i + 1
  }

  override def getMatrix: Array[Array[Double]] = rateTable

  override def createDataBase(imap: Map[String, Currency]): Unit = {
    map = imap
    rateTable = Array.ofDim[Double](map.size + 1, map.size + 1)
    map.values.foreach(c => placeInitValue(c))
    rateTable(0)(0) = 1
    for (i <- 1 until map.size + 1) {
      for (j <- 1 until map.size + 1) {
        rateTable(i)(j) = rateTable(i)(0) * rateTable(0)(j)
      }
    }
  }
}
