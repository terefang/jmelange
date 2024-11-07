package com.github.terefang.jmelange.commons.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class LogUtil
{
    public static void trace(String msg) {
        log.trace(msg);
    }
    
    public static void debug(String msg) {
        log.debug(msg);
    }
    
    public static void info(String msg) {
        log.info(msg);
    }
    
    public static void warn(String msg) {
        log.warn(msg);
    }
    
    public static void error(String msg) {
        log.error(msg);
    }
    
    
    public static void trace(Object msg) {
        log.trace(Objects.toString(msg));
    }
    
    public static void debug(Object msg) {
        log.debug(Objects.toString(msg));
    }
    
    public static void info(Object msg) {
        log.info(Objects.toString(msg));
    }
    
    public static void warn(Object msg) {
        log.warn(Objects.toString(msg));
    }
    
    public static void error(Object msg) {
        log.error(Objects.toString(msg));
    }
}
