package com.converter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CurrencyConverterTest {
    private String fromCurr;
    private String toCurr;
    private double valToConvert;
    private RateCalculator calculator;
    private BOIParser parser;

    @Before
    public void init() {
        calculator = new RateCalculator();
        parser = new BOIParser();
        parser.loadLocal();
        calculator.createDataBase(parser.getMap());
    }

    @Test(expected = Exception.class)
    public void verifyExceptionThrownOnInvalidInput(){
        fromCurr = new String("USA");
        toCurr = new String("Israel");
        String invalidInput = "I am an invalid input";
        valToConvert = Double.parseDouble(invalidInput);
        double result = calculator.calcRate(fromCurr, toCurr, valToConvert);

    }

    @Test
    public void isFormVisible(){
        View form = new View(calculator, parser);
        form.start();
        assertTrue("the application is showing on screen", form.getMainFrame().isDisplayable());
    }

    @Test
    public void convertCurrencyToItself(){
        fromCurr = new String("USA");
        toCurr = fromCurr;
        valToConvert = 1.0;
        double result = calculator.calcRate(fromCurr, toCurr, valToConvert);
        assertTrue("converted currency should be equal to itself", 0.99 <= result && result <= 1.01 );
    }

    @Test
    public void convertShekelToUSD(){
        fromCurr = new String("USA");
        toCurr = new String("Israel");
        valToConvert = 1.0;
        double result =  calculator.calcRate(fromCurr, toCurr, valToConvert);
        double expectedResult = 3.824;
        assertTrue("converted currency should be in range", expectedResult-0.2 <= result && result<= expectedResult+0.2);
    }

    @Test
    public void convertYenToUSD(){
        fromCurr = new String("Japan");
        toCurr = new String("USA");
        valToConvert = 100.0;
        double result =  calculator.calcRate(fromCurr, toCurr, valToConvert);
        double expectedResult = 0.80;
        assertTrue("converted currency should be in range", expectedResult-0.2 <= result && result<= expectedResult+0.2);
    }
}