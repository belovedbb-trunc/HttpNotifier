package com.http.notify.lib.service;

//P payload
//R response
public interface NotifyEvent<P, R> {
    NotifyEvent<P, R> notify(String message);
    void eventHook(P payload, R response, Throwable error);
}

