package com.seerbitTest.service;

import com.seerbitTest.exception.InvalidRequestTransactionException;
import com.seerbitTest.payload.ResponsePayload;
import com.seerbitTest.payload.RequestPayload;

public interface TransactionService {

    void createTransaction(RequestPayload transactionRequest) throws InvalidRequestTransactionException;
    ResponsePayload getTransactionStatistics();
    void deleteTransaction();
}
