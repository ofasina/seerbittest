package com.seerbitTest.payload;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPayload {
    private BigDecimal amount;
    private String timestamp;
}
