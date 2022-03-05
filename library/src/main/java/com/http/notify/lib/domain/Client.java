package com.http.notify.lib.domain;

import com.http.notify.lib.response.GenericResponse;
import com.http.notify.lib.threading.ClientExecutor;

import java.util.function.BiConsumer;

//Client acts like the holder of information useful for the ClientNotifier event
public class Client {

    //configured url
    private final String url;
    //client executor that runs the task based on threadPool options
    private final ClientExecutor clientExecutor;

    //server response timeout
    private final int timeoutConnection;

    //response hook callback
    private final BiConsumer<GenericResponse<String>, GenericResponse<Throwable>> responseCallback;

    private Client(ClientBuilder builder){
        url = builder.url;
        timeoutConnection = builder.timeoutConnection;
        responseCallback = builder.responseCallback;
        clientExecutor = builder.clientExecutor;
    }

    public String getUrl() {
        return url;
    }

    public ClientExecutor getClientExecutor() {
        return clientExecutor;
    }

    public int getTimeoutConnection() {
        return timeoutConnection;
    }

    public BiConsumer<GenericResponse<String>, GenericResponse<Throwable>> getResponseCallback() {
        return responseCallback;
    }

    //This class is responsible for building a client instance and creating useful properties for the client
    public static class ClientBuilder {
        //configured url
        private final String url;
        //response hook callback
        private final BiConsumer<GenericResponse<String>, GenericResponse<Throwable>> responseCallback;
        //server response timeout
        private int timeoutConnection = 60;
        //handle core threads per pool
        private int poolSize;
        //handle maximum number of pool
        private int maxPoolSize;
        //handle queue size
        private int queueCapacity;
        //client executor that runs the task based on threadPool options, this is created on build
        private ClientExecutor clientExecutor;

        public ClientBuilder(String url, BiConsumer<GenericResponse<String>, GenericResponse<Throwable>> responseCallback) {
            this.url = url;
            this.responseCallback = responseCallback;
        }

        public ClientBuilder poolSize(int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        public ClientBuilder maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public ClientBuilder queueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
            return this;
        }

        public ClientBuilder timeoutConnection(int timeoutConnection) {
            this.timeoutConnection = timeoutConnection;
            return this;
        }

        //create a clientExecutor and assign it to this instance
        public Client build() {
            ClientExecutor executor = new ClientExecutor();
            executor.setQueueCapacity(queueCapacity);
            executor.setPoolSize(poolSize);
            executor.setMaxPoolSize(maxPoolSize);
            this.clientExecutor = executor;
            return new Client(this);
        }
    }

}
