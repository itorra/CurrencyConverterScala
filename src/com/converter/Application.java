package com.converter;

/**
 * Created by ido on 18/07/15.
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
        IRateCalculatorModel calc = new RateCalculator(parser.getMap());
        Form form = new Form(calc,parser);
        form.start();
    }
}
