package com.http.notify.lib.unit;

import com.http.notify.lib.response.GenericResponse;
import com.http.notify.lib.response.ResponseUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseUtilTest {

    @Test
    void testGenericResponseUtilSetMessage() {
        ResponseUtil.GenericResponseUtil<Object> genericResponseUtil = new ResponseUtil.GenericResponseUtil<>();
        assertSame(genericResponseUtil, genericResponseUtil.setSuccess().setFailure().setMessage("Not all who wander are lost"));
    }


    @Test
    void testGenericResponseUtilWrapResponse() {
        ResponseUtil.GenericResponseUtil<Object> genericResponseUtil = new ResponseUtil.GenericResponseUtil<>();
        assertSame(genericResponseUtil, genericResponseUtil.wrapResponse("Data"));
    }

    @Test
    void testGenericResponseUtilBuildResponse() {
        ResponseUtil.GenericResponseUtil<Object> genericResponseUtil = new ResponseUtil.GenericResponseUtil<>();
        assertEquals(genericResponseUtil.wrapResponse("Data").build().getClass(), GenericResponse.class);
    }
}

