package com.marina.bankingapi.common.exception;


public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException() {
        super("Insufficient funds");
    }
}
