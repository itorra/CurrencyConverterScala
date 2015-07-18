package com.converter;

import scala.collection.Map;

/**
 * The RateCalculatorModel interface defines the logic methods of the program
 * RateCalculatorModel is implemented in scala
 *
 * @author Ido Algom
 * @author Dassi Rosen
 * @version 20 Jul 2015
 */

public interface IRateCalculatorModel {
    /**
     * Converts one currency to another currency
     *
     * @param from   string represents the source currency name
     * @param to     string represents the wanted result currency
     * @param amount double represents the value to be converted
     * @return the rate of the new currency times the amount
     */
    double calcRate(String from, String to, double amount);

    /**
     * Return a 2D array of all currency rates
     *
     * @return 2D array of doubles representing rates
     */
    double[][] getMatrix();

    /**
     * Creates the main data structure for application
     */
    void createDataBase(Map<String, Currency> map);
}
