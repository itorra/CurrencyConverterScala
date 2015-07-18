package com.converter;

/**
 * The RateCalculatble interface defines the logic methods of the program
 * RateCalculatble is implemented in scala
 * @version 20 Jul 2015
 * @author Ido Algom
 * @author Dassi Rosen
 */

public interface IRateCalculatorModel {

    /**
     *Converts one currency to another currency
     * @param from string represents the source currency name
     * @param to string represents the wanted result currency
     * @param amount double represents the value to be converted
     * @return the rate of the new currency times the amount
     */
    double calcRate(String from,String to,double amount);

}
