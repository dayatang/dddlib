package org.openkoala.businesslog.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: zjzhai
 * Date: 12/15/13
 * Time: 4:52 PM
 */
public class ThreadPool {
    private static ThreadPool ourInstance = new ThreadPool();

    private final static ExecutorService service = Executors.newFixedThreadPool(10);

    public static ThreadPool getInstance() {
        return ourInstance;
    }

    public static ExecutorService executorService() {
        return service;
    }


    private ThreadPool() {
    }
}
