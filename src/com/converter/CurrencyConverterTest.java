package com.converter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CurrencyConverterTest {
    private String fromCurr;
    private String toCurr;
    private double valToConvert;
    private RateCalculator calculator;
    private BOIParser parser ;

    @Before
    public void init() {
        calculator = new RateCalculator();
        parser = new BOIParser();
        calculator.start(parser.getMap());
        parser.loadLocal();
    }


    @Test
    public void convertCurrencyToItself(){
        fromCurr = new String("USA");
        toCurr = fromCurr;
        valToConvert = 1.0;
        double result = calculator.calcRate(fromCurr, toCurr, valToConvert);
        double expectedResult = 1.0;
        assertEquals("convert currency to itself", expectedResult, result, 0.0);
    }

    @Test
    public void convertShekelToUSD(){
        fromCurr = new String("Israel Shekel");
        toCurr = new String("USA Dollar");
        valToConvert = 1.0;
        double result =  calculator.calcRate(fromCurr, toCurr, valToConvert);
        double expectedResult = 0.26;
        assertEquals("convert shekel to USD", expectedResult, result, 0.5);
    }

    @Test
    public void convertUSDtoYen(){
        fromCurr = new String("USA Dollar");
        toCurr = new String("Japan Yen");
        valToConvert = 1.0;
        double result = calculator.calcRate(fromCurr, toCurr, valToConvert);
        double expectedResult = 123.99;
        assertEquals("convert USD to Yen", expectedResult, result , 0.5);
    }
}
