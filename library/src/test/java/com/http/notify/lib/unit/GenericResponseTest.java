package com.http.notify.lib.unit;

import com.http.notify.lib.domain.Client;
import com.http.notify.lib.response.GenericResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenericResponseTest {

    @Test
    void testGenericResponse() {
        GenericResponse<Object> response = new GenericResponse<>();
        response.setStatus(GenericResponse.SUCCESS_KEY);
        String DEFAULT_MESSAGE = "message";
        response.setMessage(DEFAULT_MESSAGE);
        String DEFAULT_DATA = "data";
        response.setData(DEFAULT_DATA);

        assertThat(response).isNotNull();
        assertThat(response.getCurrentTime()).isNotZero();
        assertThat(response.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(response.getStatus()).isEqualTo(GenericResponse.SUCCESS_KEY);
        assertThat(response.getData()).isEqualTo(DEFAULT_DATA);
    }

}
