package org.sgomez.test.springboot.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sgomez.test.springboot.app.models.Account;
import org.sgomez.test.springboot.app.models.Bank;
import org.sgomez.test.springboot.app.repositories.AccountRepository;
import org.sgomez.test.springboot.app.repositories.BankRepository;
import org.sgomez.test.springboot.app.services.Data;
import org.sgomez.test.springboot.app.services.IAccountService;
import org.sgomez.test.springboot.app.services.impl.AccountServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class SpringbootTestApplicationTests {
	AccountRepository accountRepository;
	BankRepository bankRepository;

	IAccountService accountService;

	@BeforeEach
	void setUp() {
		accountRepository = mock(AccountRepository.class);
		bankRepository = mock(BankRepository.class);
		accountService = new AccountServiceImpl(accountRepository,bankRepository);
	}

	@Test
	void contextLoads() {
		when(accountRepository.findById(1L)).thenReturn(Data.Account_OO1);
		when(accountRepository.findById(2L)).thenReturn(Data.Account_OO2);
		when(bankRepository.findById(1L)).thenReturn(Data.BANK);

		BigDecimal originalBalance = accountService.checkBalance(1L);
		BigDecimal targetBalance = accountService.checkBalance(2L);
		assertEquals("850",originalBalance.toPlainString());
		assertEquals("2000",targetBalance.toPlainString());

		accountService.transfer(1L,2L,new BigDecimal("100"),1L);

		originalBalance = accountService.checkBalance(1L);
		targetBalance = accountService.checkBalance(2L);

		assertEquals("750",originalBalance.toPlainString());
		assertEquals("2100",targetBalance.toPlainString());

		int total = accountService.checkTotalTransfers(1L);
		assertEquals(1,total);

		verify(accountRepository,times(3)).findById(1L);
		verify(accountRepository,times(3)).findById(2L);
		verify(accountRepository,atMost(2)).update(any(Account.class));

		verify(bankRepository,times(2)).findById(1L);
		verify(bankRepository).update(any(Bank.class));
	}

}
