package org.sgomez.test.springboot.app.services;

import org.sgomez.test.springboot.app.models.Account;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {
    Account save(Account account);
    Account findById(Long id);
    List<Account> findAll();
    int checkTotalTransfers(Long bankId);
    BigDecimal checkBalance(Long accountId);
    void transfer(Long accountOrigin,Long accountTarget, BigDecimal amount, Long bankId);
}
