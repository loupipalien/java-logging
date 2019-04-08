package com.ltchen.logging.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : ltchen
 */
public class HelloWorld {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    public static void main(String[] args) {
        logger.info("hello world!");
    }
}
