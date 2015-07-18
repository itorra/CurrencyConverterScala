package com.converter;

/**
 * The MessageConsumer interface allows the client GUI
 * to get messages from thr logical parts
 * in order to print them to the screen
 *
 * @author Ido Algom
 * @author Dassi Rosen
 * @version 20 Jul 2015
 */

public interface MessageConsumer {
    /**
     * @see View#consume
     */
    void consume(String msg);
}
