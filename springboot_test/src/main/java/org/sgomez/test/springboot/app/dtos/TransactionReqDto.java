package org.sgomez.test.springboot.app.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class TransactionReqDto {
    private Long accountOriginId;
    private Long accountTargetId;
    private BigDecimal amount;
    private Long bankId;
}
