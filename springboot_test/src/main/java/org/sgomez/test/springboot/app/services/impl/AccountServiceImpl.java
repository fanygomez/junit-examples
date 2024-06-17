package org.sgomez.test.springboot.app.services.impl;

import org.sgomez.test.springboot.app.models.Account;
import org.sgomez.test.springboot.app.models.Bank;
import org.sgomez.test.springboot.app.repositories.AccountRepository;
import org.sgomez.test.springboot.app.repositories.BankRepository;
import org.sgomez.test.springboot.app.services.IAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {
    private  AccountRepository accountRepository;
    private  BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    @Transactional // Para escritura
    public Account save(Account account) {
        System.out.println("AccountServiceImpl.save");
        return accountRepository.save(account);
    }
    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        System.out.println("AccountServiceImpl.findById");
        return accountRepository.findById(id).orElseThrow();
    }
    @Override
    @Transactional(readOnly = true)
    public List<Account> findAll() {
        System.out.println("AccountServiceImpl.findAll");
        return accountRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Bank bankFindById(Long id) {
        System.out.println("AccountServiceImpl.bankFindById");
        return bankRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int checkTotalTransfers(Long bankId) {
        System.out.println("AccountServiceImpl.checkTotalTransfers");
        Bank bank = bankFindById(bankId);
        return bank.getNTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal checkBalance(Long accountId) {
        System.out.println("AccountServiceImpl.checkBalance");
        Account account = findById(accountId);
        return account.getBalance();
    }
    @Override
    @Transactional
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
