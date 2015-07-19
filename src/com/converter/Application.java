package com.converter;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The Application class runs the application
 *
 * @author Ido Algom
 * @author Dassi Rosen
 * @version 20 Jul 2015
 */
public class Application {

    static Logger mainLogger = Logger.getLogger(Application.class.getName());
    static final String LOG_PROPERTIES_FILE = "log4j.properties";

    /**
     * The main function
     * Connects between - Parser, Rate Calculator and the Form
     *
     */

    public static void main(String[] args) {
        logConfig();
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

    /**
     * log4j Initializer
     * Init Log4J properties
     */

    private static void logConfig(){
        Properties logProperties = new Properties();
        try {
            logProperties.load(new FileInputStream(LOG_PROPERTIES_FILE));
            PropertyConfigurator.configure(logProperties);
            mainLogger.info("Logging initialized...");
        }
        catch (IOException exception){
            throw new RuntimeException("Unable to load log properties " + LOG_PROPERTIES_FILE);
        }
    }
}