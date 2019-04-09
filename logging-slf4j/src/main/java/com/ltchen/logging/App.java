package com.ltchen.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Hello world!
 *
 */
public class App {
    private static final Log log = LogFactory.getLog(App.class);
    public static void main( String[] args ) {
        log.error("Hello World!");
        System.out.println( "Hello World!" );
    }
}
