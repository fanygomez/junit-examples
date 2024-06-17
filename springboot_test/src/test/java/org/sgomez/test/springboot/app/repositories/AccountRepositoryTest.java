package org.sgomez.test.springboot.app.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sgomez.test.springboot.app.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    AccountRepository accountRepository;
    @Test
    void testFindById() {
        var  account = accountRepository.findById(1L);
        assertTrue(account.isPresent());
        assertEquals("Ale", account.orElseThrow().getPerson());
    }
    @Test
    void testFindByPerson() {
       var account = accountRepository.findByPerson("Ale");
        assertTrue(account.isPresent());
        assertEquals("Ale", account.orElseThrow().getPerson());
        assertEquals("2000.00", account.orElseThrow().getBalance().toPlainString());
    }
    @Test
    void testFindByPersonThrowException() {
        var account = accountRepository.findByPerson("Sebas");
        assertThrows(NoSuchElementException.class, account::orElseThrow);
        assertFalse(account.isPresent());
    }
    @Test
    void testFindAll() {
        var accounts = accountRepository.findAll();
        assertFalse(accounts.isEmpty());
        assertEquals(2, accounts.size());
    }
    @Test
    void testSave(){
        // GIVEN
        var newAccount = new Account(null, "Valentina", new BigDecimal("1500"));

        // WHEN
        var accountCreated = accountRepository.save(newAccount);

        // THEN
        assertEquals("Valentina", accountCreated.getPerson());
        assertEquals("1500", accountCreated.getBalance().toPlainString());
//        assertEquals(3, accountFound.getId()); // no recomendable validar el id.
    }
    @Test
    void testUpdate(){
        // GIVEN
        var newAccount = new Account(null, "Valentina", new BigDecimal("1250"));

        // WHEN
        Account accountSaved = accountRepository.save(newAccount);
        // THEN

        assertEquals("Valentina", accountSaved.getPerson());
        assertEquals("1250", accountSaved.getBalance().toPlainString());

        // WHEN
        accountSaved.setBalance(new BigDecimal("3800"));
        Account accountUpdated = accountRepository.save(accountSaved);
        // THEN
        assertEquals("Valentina", accountUpdated.getPerson());
        assertEquals("3800", accountUpdated.getBalance().toPlainString());

    }
    @Test
    void testDelete(){
        var accountFound = accountRepository.findById(2L).orElseThrow();

        assertEquals("Fanny", accountFound.getPerson());
        assertEquals("850.00", accountFound.getBalance().toPlainString());

        accountRepository.delete(accountFound);

        assertThrows(NoSuchElementException.class, () -> {
            accountRepository.findById(accountFound.getId()).orElseThrow();
        });

        assertEquals(1, accountRepository.findAll().size());
    }
}