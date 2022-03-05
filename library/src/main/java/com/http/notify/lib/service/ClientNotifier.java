package com.http.notify.lib.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.http.notify.lib.domain.Client;
import com.http.notify.lib.response.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;

//This class implement Notify event and tries to notify a client of a response from server using callbacks
public class ClientNotifier implements NotifyEvent<String, HttpResponse<String>> {

    private final Logger log = LoggerFactory.getLogger(ClientNotifier.class);

    private final Client client;

    public ClientNotifier(Client client) {
        this.client = client;
    }

    //message - gotten from cli (or other medium)
    //It creates a url and perform a POST request to the configured url using specified defaults/custom config
    @Override
    public NotifyEvent<String, HttpResponse<String>> notify(String message) {
        client.getClientExecutor().execute(() -> {
            HttpResponse<String> response = null;
            Throwable throwable = null;
            try{
                response = payloadConnector(URI.create(client.getUrl()), message);
            }catch (Exception ex) {
                log.error("An exception has occurred ", ex);
                throwable = ex;
            }
            eventHook(message, response, throwable);
        });
        return this;
    }

    public void notify(Set<String> messages) {
        messages.forEach(this::notify);
    }

    // process and triggers a response callback
    @Override
    public void eventHook(String payload, HttpResponse<String> response, Throwable error) {
        log.info("called event hook ");
        ResponseUtil.GenericResponseUtil<Throwable> errorResponse = new ResponseUtil.GenericResponseUtil<>();
        errorResponse.wrapResponse(error).setMessage(payload).setFailure();
        ResponseUtil.GenericResponseUtil<String> successResponse = new ResponseUtil.GenericResponseUtil<>();
        if(Objects.nonNull(response)) {
            if(isStatusError(response.statusCode())) successResponse.setMessage(payload).setFailure().wrapResponse(response.body());
            else successResponse.setSuccess().setMessage(payload).wrapResponse(response.body());
        }
        client.getResponseCallback().accept(successResponse.build(), errorResponse.build());
    }

    //form of destroying an executor
    public void destroyExecutor() {
        client.getClientExecutor().destroy();
    }

    protected HttpResponse<String> payloadConnector(URI url, Object payload) throws Exception {
        HttpClient httpClient = HttpClient
            .newBuilder()
            .sslContext(SSLContext.getDefault())
            .connectTimeout(Duration.ofSeconds(client.getTimeoutConnection()))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
        return payloadConnector(httpClient, url, payload);
    }

    protected HttpResponse<String> payloadConnector(HttpClient httpClient, URI url, Object payload) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(url)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private boolean isStatusError(int status) {
        return status >= 500;
    }

}
