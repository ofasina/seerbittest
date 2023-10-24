package com.seerbitTest;

import com.seerbitTest.exception.InvalidRequestTransactionException;
import com.seerbitTest.payload.ResponsePayload;
import com.seerbitTest.payload.RequestPayload;
import com.seerbitTest.service.TransactionService;
import com.seerbitTest.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UnitTest {

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl();
    }

    @Test
    @DisplayName("Create a transaction with a future timestamp throws InvalidTransactionException")
    void createFutureTransaction() {

        RequestPayload transactionRequest = new RequestPayload();
        transactionRequest.setAmount(new BigDecimal("120.00"));
        transactionRequest.setTimestamp(Instant.now().plusSeconds(30).toString());

        assertThrows(InvalidRequestTransactionException.class, () -> {
            transactionService.createTransaction(transactionRequest);
        });
    }

    @Test
    @DisplayName("Create a valid transaction")
    void createValidTransaction() {

        RequestPayload transactionRequest = new RequestPayload();
        transactionRequest.setAmount(new BigDecimal("120.00"));
        transactionRequest.setTimestamp(Instant.now().toString());

        assertDoesNotThrow(() -> {
            transactionService.createTransaction(transactionRequest);
        });
    }


    @Test
    @DisplayName("Transaction before 30 secs from current timestamp ")
    void createOldTransaction() {

        RequestPayload transactionRequest = new RequestPayload();
        transactionRequest.setAmount(new BigDecimal("80.00"));
        transactionRequest.setTimestamp(Instant.now().minus(Duration.ofDays(10)).toString());

        InvalidRequestTransactionException exception = assertThrows(InvalidRequestTransactionException.class, () -> {
            transactionService.createTransaction(transactionRequest);
        });
        assertFalse(exception.isFutureTransaction());
        assertEquals("Transaction is too old", exception.getMessage());
    }

    @Test
    @DisplayName("Get transaction statistics for recent transactions")
    void getTransactionStatistics() throws InvalidRequestTransactionException {

        RequestPayload recentTransactionRequest1 = new RequestPayload(new BigDecimal("10.25"), Instant.now().minusSeconds(20).toString());
        RequestPayload recentTransactionRequest2 = new RequestPayload(new BigDecimal("20.50"), Instant.now().minusSeconds(15).toString());
        RequestPayload recentTransactionRequest3 = new RequestPayload(new BigDecimal("15.75"), Instant.now().minusSeconds(5).toString());

        transactionService.createTransaction(recentTransactionRequest1);
        transactionService.createTransaction(recentTransactionRequest2);
        transactionService.createTransaction(recentTransactionRequest3);

        ResponsePayload statistics = transactionService.getTransactionStatistics();

        assertEquals(new BigDecimal("46.50").setScale(2), statistics.getSum());
        assertEquals(new BigDecimal("15.50").setScale(2), statistics.getAvg());
        assertEquals(new BigDecimal("20.50").setScale(2), statistics.getMax());
        assertEquals(new BigDecimal("10.25").setScale(2), statistics.getMin());
        assertEquals(3, statistics.getCount());
    }

    @Test
    @DisplayName("delete transactions")
    void deleteTransaction() {
        transactionService.deleteTransaction();
    }
}
