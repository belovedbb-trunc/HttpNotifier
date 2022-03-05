package com.http.notify.lib.threading;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

//Handle ThreadPoolExecutor for the client
//handles initialization of threadPool executor,
// setting the defaults for handling spike and spawning threads based on policy
public class ClientExecutor extends ThreadPoolTaskExecutor {
    private int poolSize;
    private int maxPoolSize;
    private int queueCapacity;

    public ClientExecutor() {
        super.setThreadFactory(Executors.defaultThreadFactory());
        super.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        super.initialize();
        super.setWaitForTasksToCompleteOnShutdown(true);
        super.setKeepAliveSeconds(10);
        super.setCorePoolSize(8);
        super.setMaxPoolSize(16);
        super.setQueueCapacity(200);
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    @Override
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    @Override
    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    @Override
    public void execute(Runnable command) {
        handleSizes();
        super.execute(command);
    }

    void handleSizes() {
        if(poolSize> 0) super.setCorePoolSize(poolSize);
        if(maxPoolSize> 0) super.setMaxPoolSize(maxPoolSize);
        if(queueCapacity> 0) super.setQueueCapacity(queueCapacity);
    }

}
