package com.example.broker.chain.business;

public interface BusinessHandler {
    void setNext(BusinessHandler next);
    void handle(String topic, String message);
}
