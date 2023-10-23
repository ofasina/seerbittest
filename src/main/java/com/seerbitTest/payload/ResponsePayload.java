package com.seerbitTest.payload;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ResponsePayload {
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private long count;
    public String message;

    public ResponsePayload(String message) {
        this.message = message;
    }

    public ResponsePayload(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, long count){
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }
}
