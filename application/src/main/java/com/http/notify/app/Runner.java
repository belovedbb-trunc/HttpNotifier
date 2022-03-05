package com.http.notify.app;

import com.http.notify.lib.domain.Client;
import com.http.notify.lib.response.GenericResponse;
import com.http.notify.lib.service.ClientNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class Runner {

    private final Logger log = LoggerFactory.getLogger(Runner.class);

    private final String url;
    private final String message;
    private final int interval;

    public Runner(String url, String message, int interval) {
        this.url = url;
        this.message = message;
        this.interval = interval;
    }

    //if interval is not inputted or it's negative, then only the specified message is sent once, else
    //messages are sent at the interval specified
    //a listener for shutdown hook is specified to destroy executor
    public void run() {
        ClientNotifier clientNotifier = buildClient(url);
        if(interval <= 0) {
            clientNotifier.notify(message);
            clientNotifier.destroyExecutor();
        } else {
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(() -> clientNotifier.notify(message), 0, interval, TimeUnit.SECONDS);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown hook has been called!");
            clientNotifier.destroyExecutor();
        }));
    }

    //build and handle callback response for client
    private ClientNotifier buildClient(String url) {
        BiConsumer<GenericResponse<String>, GenericResponse<Throwable>> responseCallback = (success, error) -> {
            if(success != null && GenericResponse.SUCCESS_KEY.equals(success.getStatus())) {
                log.info("Message - {}, Data - {}", success.getMessage(), success.getData());
            } else if(error != null && GenericResponse.FAILED_KEY.equals(error.getStatus())) {
                log.error("Message - {}, Data - {}", error.getMessage(), error.getData());
            }
        };
        Client client = new Client.ClientBuilder(url, responseCallback).build();
        return new ClientNotifier(client);
    }

}
