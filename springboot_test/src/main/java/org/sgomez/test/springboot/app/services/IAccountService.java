package org.sgomez.test.springboot.app.services;

import org.sgomez.test.springboot.app.models.Account;

import java.math.BigDecimal;

public interface IAccountService {
    Account findById(Long id);
    int checkTotalTransfers(Long bankId);
    BigDecimal checkBalance(Long accountId);
    void transfer(Long accountOrigin,Long accountTarget, BigDecimal amount, Long bankId);
}
