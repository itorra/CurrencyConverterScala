package com.converter

/**
 * Created by ido on 15/06/15.
 */
class Currency(_name: String, _unit: Int, _code: String,_country: String, _rate: Double, _change: Double) {
  val name: String = _name
  val unit: Int = _unit
  val code: String = _code
  val country: String = _country
  val rate: Double = _rate
  val change: Double = _change

  def getRate = rate

  override def toString = s"Currency($name, $unit, $code, $rate, $change)"
}

