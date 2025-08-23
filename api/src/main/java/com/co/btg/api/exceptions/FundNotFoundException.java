package com.co.btg.api.exceptions;

public class FundNotFoundException extends RuntimeException {
    public FundNotFoundException(String message) {
        super(message);
    }
}
