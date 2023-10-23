package com.seerbitTest.service;

import com.seerbitTest.dto.TransactionDTO;
import com.seerbitTest.exception.InvalidRequestTransactionException;
import com.seerbitTest.exception.NoRecordFoundException;
import com.seerbitTest.payload.ResponsePayload;
import com.seerbitTest.payload.RequestPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private static final ConcurrentMap<Long, TransactionDTO> transactions = new ConcurrentHashMap<>();

    @Override
    public void createTransaction(RequestPayload transactionRequest) throws InvalidRequestTransactionException {
        // converting iso 8601 to epoch of long value
        long timestamp = Instant.parse(transactionRequest.getTimestamp()).toEpochMilli();

        // check if time stamp is of the future
        if (isTransactionFuture(timestamp)) {
            throw new InvalidRequestTransactionException(false, "");
        }
// check if time stamp is 30 secs ago
        long currentTimestamp = Instant.now().toEpochMilli();
        if (!isTransactionOld(timestamp, currentTimestamp)) {
            throw new InvalidRequestTransactionException(false, "");
        }

        // transaction in right format add to transaction dto
        TransactionDTO transaction = new TransactionDTO(transactionRequest.getAmount(), timestamp);
        transactions.put(timestamp, transaction);
        log.info("This is the transaction {}", transactions);
    }

    private boolean isTransactionFuture(long transactionTimestamp) {
        System.out.println(Instant.now().toEpochMilli());
        return transactionTimestamp > Instant.now().toEpochMilli();
    }

    private boolean isTransactionOld(long transactionTimestamp, long currentTimestamp) {
        return (currentTimestamp - transactionTimestamp) <= 30 * 1000L;
    }

    @Override
    public ResponsePayload getTransactionStatistics() {
        // get current timestamp in milliseconds using epoch
        long currentTimestamp = Instant.now().toEpochMilli();

        // use summary statistics to calculate statistical information sum, count, max, min and avg.
        // using doublesummary statics for double data type
        // filter out transaction that is more than 30 secs
        DoubleSummaryStatistics stats = transactions.values().stream()
                .filter(transaction -> isTransactionOld(transaction.getTimestamp(), currentTimestamp))
                .map(TransactionDTO::getAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .summaryStatistics();

        if (stats.getCount() == 0) {
            throw new NoRecordFoundException("No transactions available");
        }

        // round off value to 2 decimal place using half round up.
        BigDecimal sum = BigDecimal.valueOf(stats.getSum()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal average = BigDecimal.valueOf(stats.getAverage()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal max = BigDecimal.valueOf(stats.getMax()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal min = BigDecimal.valueOf(stats.getMin()).setScale(2, RoundingMode.HALF_UP);
        long count = stats.getCount();

        return new ResponsePayload(sum, average, max, min, count);
    }

    @Override
    public void deleteTransaction() {
        transactions.clear();
    }

}
