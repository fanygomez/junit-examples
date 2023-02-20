package org.sgomez.test.springboot.app.repositories;

import org.sgomez.test.springboot.app.models.Bank;

import java.util.List;

public interface BankRepository {
    List<Bank> findAll();
    Bank findById(Long id);
    void update(Bank bank);
}
