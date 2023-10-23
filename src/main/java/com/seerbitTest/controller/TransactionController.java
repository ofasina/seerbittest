package com.seerbitTest.controller;

import com.seerbitTest.exception.InvalidRequestTransactionException;
import com.seerbitTest.exception.NoRecordFoundException;
import com.seerbitTest.payload.ResponsePayload;
import com.seerbitTest.payload.RequestPayload;
import com.seerbitTest.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping(value="/create")
    public ResponseEntity<?> createTransaction(@RequestBody RequestPayload transactionRequest) {
        try {
            transactionService.createTransaction(transactionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InvalidRequestTransactionException e) {
            if (e.isFutureTransaction()) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
            } 
            else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body");
        }
    }

    @GetMapping(value="/statistics")
    public ResponseEntity<ResponsePayload> getTransactionStatistics() {
        try {
            ResponsePayload response = transactionService.getTransactionStatistics();
            return ResponseEntity.ok(response);
        } catch (NoRecordFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponsePayload(e.getMessage()));
        }
    }

    @DeleteMapping(value="/transactions")
    public ResponseEntity<?> deleteTransactions() {
        transactionService.deleteTransaction();
        return ResponseEntity.noContent().build();
    }
}
