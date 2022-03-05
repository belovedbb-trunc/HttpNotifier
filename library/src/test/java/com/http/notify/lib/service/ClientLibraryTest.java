package com.http.notify.lib.service;

import com.http.notify.lib.domain.Client;
import com.http.notify.lib.response.GenericResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ClientLibraryTest {

    private final Integer DEFAULT_STATUS_CODE = 200;
    private final String DEFAULT_BODY = "body";

    private String url;
    BiConsumer<GenericResponse<String>, GenericResponse<Throwable>> responseCallback;

    @Mock
    ClientNotifier notifier;

    @Mock
    HttpClient httpClient;

    @BeforeEach
    void setup() {
        url = "https://localhost:8080";
        responseCallback = (stringGenericResponse, throwableGenericResponse) -> {
            System.out.println("dhdslfsdfljdf");
        };
    }

    @Test
    void testSuccessfulCallWithEmptyCallback() {
        responseCallback = (stringGenericResponse, throwableGenericResponse) -> {
            assertThat(stringGenericResponse).isNotNull();
            assertThat(throwableGenericResponse).isNotNull();
        };
        Client client = new Client.ClientBuilder(url, responseCallback).build();
        var m = new ClientNotifier(client);
            m.notify("Hello");
        m.destroyExecutor();
    }

    @Test
    void testSuccessfulCallWithMockedServerResponse() throws Exception {
        responseCallback = (stringGenericResponse, throwableGenericResponse) -> {
            assertThat(stringGenericResponse).isNotNull();
            assertThat(stringGenericResponse.getData()).isEqualTo(DEFAULT_BODY);
            assertThat(throwableGenericResponse).isNotNull();
            assertThat(throwableGenericResponse.getData()).isNull();
        };
        Client client = new Client.ClientBuilder(url, responseCallback).build();
        ClientNotifier notifierObject = new ClientNotifier(client);
        Mockito.when(notifier.payloadConnector(any(), any())).thenReturn(getMockedResponse());
        notifierObject.notify("Hello");
        notifierObject.destroyExecutor();
        Mockito.reset(notifier);
    }

    @Test
    void testErrorCallbackWhenServerIsDown() {
        responseCallback = (stringGenericResponse, throwableGenericResponse) -> {
            assertThat(stringGenericResponse).isNotNull();
            assertThat(stringGenericResponse.getData()).isNull();
            assertThat(throwableGenericResponse).isNotNull();
            assertThat(throwableGenericResponse.getData()).isNotNull();
        };
        Mockito.reset(notifier);
        Client client = new Client.ClientBuilder(url, responseCallback).build();
        ClientNotifier notifierObject = new ClientNotifier(client);
        notifierObject.notify("Hello");
    }

    @Test
    void testErrorCallbackWhenServerResponseIsDelayed() throws Exception {
        responseCallback = (stringGenericResponse, throwableGenericResponse) -> {
            assertThat(stringGenericResponse).isNotNull();
            assertThat(stringGenericResponse.getData()).isNull();
            assertThat(throwableGenericResponse).isNotNull();
            assertThat(throwableGenericResponse.getData()).isNotNull();
        };
        Mockito.reset(httpClient);
        Client client = new Client.ClientBuilder(url, responseCallback).timeoutConnection(1).build();
        ClientNotifier notifierObject = new ClientNotifier(client);
        Mockito.when(httpClient.send(any(), any())).thenAnswer((Answer<HttpResponse<String>>) invocation -> {
            Thread.sleep(5000);
            return getMockedResponse();
        });
        notifierObject.payloadConnector(httpClient, URI.create(url), "hello");
    }

    @Test
    void testErrorCallbackWhenRequestOverloadThreadPoolQueue() {
        responseCallback = (stringGenericResponse, throwableGenericResponse) -> {
            assertThat(stringGenericResponse).isNotNull();
            assertThat(stringGenericResponse.getData()).isNull();
            assertThat(throwableGenericResponse).isNotNull();
            assertThat(throwableGenericResponse.getData()).isNotNull();
        };
        Client client = new Client
            .ClientBuilder(url, responseCallback)
            .poolSize(1)
            .queueCapacity(1)
            .maxPoolSize(1)
            .timeoutConnection(1).build();
        ClientNotifier notifierObject = new ClientNotifier(client);
        for(int i = 0; i < 100; i++) {
            notifierObject.notify("shann");
        }
    }

    private HttpResponse<String> getMockedResponse() {
        return new HttpResponse<>() {
            @Override
            public int statusCode() {
                return DEFAULT_STATUS_CODE;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return DEFAULT_BODY;
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return URI.create(url);
            }

            @Override
            public HttpClient.Version version() {
                return HttpClient.Version.HTTP_1_1;
            }
        };
    }
}
