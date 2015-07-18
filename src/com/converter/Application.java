package com.converter;

//import java.util.Map;

/**
 * The Application class is used to run the application
 * @version 20 Jul 2015
 * @author Ido Algom
 * @author Dassi Rosen
 */
public class Application {
    public static void main(String[] args) {
        BOIParser parser = new BOIParser();
        Thread t1 = new Thread(parser);
        t1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IRateCalculatorModel calc = new RateCalculator();
        calc.start(parser.getMap());
        FormTest form = new FormTest(calc,parser);
        parser.setConsumer(form);
        form.start();
    }
}
