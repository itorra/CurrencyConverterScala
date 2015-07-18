//package com.converter;
//
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//public class CurrencyConverterTest {
//    private String fromCurr;
//    private String toCurr;
//    private double valToConvert;
//    private RateCalculator calculator;
//    private BOIParser parser ;
//
//    @Before
//    public void init() {
//        calculator = new RateCalculator();
//        parser = new BOIParser();
//        calculator.start(parser.getMap());
//        parser.loadLocal();
//    }
//
//
//    @Test
//    public void convertCurrencyToItself(){
//        fromCurr = new String("USA");
//        toCurr = fromCurr;
//        valToConvert = 1.0;
//        double result = calculator.calcRate(fromCurr, toCurr, valToConvert);
//        double expectedResult = 1.0;
//        assertTrue("converted currency should be equal to itself", expectedResult == result);
//    }
//
//    @Test
//    public void convertShekelToUSD(){
//        fromCurr = new String("USA");
//        toCurr = new String("Israel");
//        valToConvert = 1.0;
//        double result =  calculator.calcRate(fromCurr, toCurr, valToConvert);
//        double expectedResult = 3.78;
//        assertTrue(expectedResult == result);
//    }
//
//    @Test
//    public void convertUSDtoYen(){
//        fromCurr = new String("USA");
//        toCurr = new String("Japan");
//        valToConvert = 1.0;
//        double result = calculator.calcRate(fromCurr, toCurr, valToConvert);
//        double expectedResult = 123.99;
//        assertTrue(expectedResult == result);
//    }
//
//
//}
