package com.example.foodrecipes;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutor {

  private static AppExecutor instance;

  public static AppExecutor getInstance() {
    if (instance == null) {

      instance = new AppExecutor();
    }
    return instance;
  }
  // allows extra execulators
  private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);

  public ScheduledExecutorService networkIO() {

    return networkIO();
  }
}
