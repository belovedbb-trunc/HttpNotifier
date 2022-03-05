package com.http.notify.lib.response;


import java.time.Instant;

//Class that handles any kind of response, either success or failure
public class GenericResponse<T> {
    public static final String SUCCESS_KEY = "Success";
    public static final String FAILED_KEY = "Failed";

    //status of the response either success or failed
    private String status;
    //initial payload/message sent to the server
    private String message;
    //current time
    private final long currentTime;
    //data received, if error, then a throwable, otherwise, it's usually a string
    private T data;

    public GenericResponse() {
        currentTime = Instant.now().toEpochMilli();
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

}
