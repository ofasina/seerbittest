package com.seerbitTest.exception;

public class InvalidRequestTransactionException extends Exception {
//    private  final boolean futureTransaction;
//
//    public InvalidTransactionException(boolean futureTransaction) {
//        this.futureTransaction = futureTransaction;
//    }
//
//    public boolean isFutureTransaction() {
//        return futureTransaction;
//    }

    private final boolean futureTransaction;

    public InvalidRequestTransactionException(boolean futureTransaction) {
        this.futureTransaction = futureTransaction;
    }

    public InvalidRequestTransactionException(boolean futureTransaction, String message) {
        super(message);
        this.futureTransaction = futureTransaction;
    }

    public boolean isFutureTransaction() {
        return futureTransaction;
    }
}
