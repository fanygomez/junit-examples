package org.sgomez.test.springboot.app.services.impl;

import org.sgomez.test.springboot.app.models.Account;
import org.sgomez.test.springboot.app.models.Bank;
import org.sgomez.test.springboot.app.repositories.AccountRepository;
import org.sgomez.test.springboot.app.repositories.BankRepository;
import org.sgomez.test.springboot.app.services.IAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class AccountServiceImpl implements IAccountService {
    private  AccountRepository accountRepository;
    private  BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Account findById(Long id) {
        System.out.println("AccountServiceImpl.findById");
        return accountRepository.findById(id).orElseThrow();
    }
    public Bank bankFindById(Long id) {
        System.out.println("AccountServiceImpl.bankFindById");
        return bankRepository.findById(id).orElseThrow();
    }

    @Override
    public int checkTotalTransfers(Long bankId) {
        System.out.println("AccountServiceImpl.checkTotalTransfers");
        Bank bank = bankFindById(bankId);
        return bank.getNTransferencias();
    }

    @Override
    public BigDecimal checkBalance(Long accountId) {
        System.out.println("AccountServiceImpl.checkBalance");
        Account account = findById(accountId);
        return account.getBalance();
    }

    @Override
    public void transfer(Long accountOrigin, Long accountTarget, BigDecimal amount, Long bankId) {
        
        System.out.println("AccountServiceImpl.transfer");

        Account accountOrigen = findById(accountOrigin);
        accountOrigen.debit(amount);

        Account accountDest = findById(accountTarget);
        accountDest.credit(amount);

        Bank bank = bankFindById(bankId);
        int totalTransfers = bank.getNTransferencias();
        bank.setNTransferencias(++totalTransfers);
        bankRepository.save(bank);

    }
}
