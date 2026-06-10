package com.example.broker.chain;

import com.example.broker.model.RetryJob;

public interface RetryHandler {
    void setNext(RetryHandler next);
    void handle(RetryJob job);
}
