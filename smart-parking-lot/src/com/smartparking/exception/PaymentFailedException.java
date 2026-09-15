package com.smartparking.exception;

public class PaymentFailedException extends Exception {
    public PaymentFailedException(String message) {
        super(message);
    }
}
