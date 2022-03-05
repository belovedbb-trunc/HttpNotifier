package com.http.notify.lib.unit;

import com.http.notify.lib.domain.Client;
import com.http.notify.lib.response.GenericResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

class ClientTest {

    private final Integer DEFAULT_MAX_POOL_SIZE = 16;
    private final Integer DEFAULT_CORE_POOL_SIZE = 8;
    private String url;
    BiConsumer<GenericResponse<String>, GenericResponse<Throwable>> responseCallback;

    @BeforeEach
    void setup() {
        url = "URL";
        responseCallback = (stringGenericResponse, throwableGenericResponse) -> {};
    }

    @Test
    void testClient() {
        Client client = new Client.ClientBuilder(url, responseCallback).build();
        assertThat(client).isNotNull();
        assertThat(client.getUrl()).isEqualTo(url);
        assertThat(client.getResponseCallback()).isEqualTo(responseCallback);
        assertThat(client.getClientExecutor()).isNotNull();
        assertThat(client.getClientExecutor().getMaxPoolSize()).isEqualTo(DEFAULT_MAX_POOL_SIZE);
        assertThat(client.getClientExecutor().getCorePoolSize()).isEqualTo(DEFAULT_CORE_POOL_SIZE);
    }


    @Test
    void testClientBuilder() {
        int CUSTOM_VALUE = 1;
        Client client = new Client.ClientBuilder(url, responseCallback)
            .maxPoolSize(CUSTOM_VALUE)
            .queueCapacity(CUSTOM_VALUE)
            .timeoutConnection(CUSTOM_VALUE)
            .poolSize(CUSTOM_VALUE)
            .build();
        assertThat(client).isNotNull();
        assertThat(client.getUrl()).isEqualTo(url);
        assertThat(client.getResponseCallback()).isEqualTo(responseCallback);
        assertThat(client.getClientExecutor()).isNotNull();
        assertThat(client.getTimeoutConnection()).isEqualTo(CUSTOM_VALUE);
        //defaults are inserted at execution
        assertThat(client.getClientExecutor().getMaxPoolSize()).isEqualTo(DEFAULT_MAX_POOL_SIZE);
        assertThat(client.getClientExecutor().getCorePoolSize()).isEqualTo(DEFAULT_CORE_POOL_SIZE);
    }
}
