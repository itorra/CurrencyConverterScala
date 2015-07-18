package com.converter;

/**
 * The Application class runs the application
 *
 * @author Ido Algom
 * @author Dassi Rosen
 * @version 20 Jul 2015
 */
public class Application {
    public static void main(String[] args) {
        BOIParser parser = new BOIParser();
        Thread thread = new Thread(parser);//continuously checks for an updated data file
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IRateCalculatorModel calc = new RateCalculator();
        calc.createDataBase(parser.getMap());
        View form = new View(calc, parser);
        parser.setConsumer(form);
        form.start();
    }
}
