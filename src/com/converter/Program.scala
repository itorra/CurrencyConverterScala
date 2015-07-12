package com.converter

/**
 * Created by ido on 26/06/15.
 */
object Program {

  def main(args: Array[String]) {
    println("hello")
    val parser:BOIParser = new BOIParser
    parser.downloadFile()
    val rc: RateCalculator = new RateCalculator(parser.buildMap())
//    rc.showMotherFucker()

    val num = rc.calcRate("Lebanon","USA",20)
    print(num)

  }
}
