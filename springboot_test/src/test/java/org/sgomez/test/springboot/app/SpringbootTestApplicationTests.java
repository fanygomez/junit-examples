package org.sgomez.test.springboot.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.sgomez.test.springboot.app.services.Data.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.sgomez.test.springboot.app.exceptions.MoneyIsNotEnoughException;
import org.sgomez.test.springboot.app.models.Account;
import org.sgomez.test.springboot.app.models.Bank;
import org.sgomez.test.springboot.app.repositories.AccountRepository;
import org.sgomez.test.springboot.app.repositories.BankRepository;
import org.sgomez.test.springboot.app.services.Data;
import org.sgomez.test.springboot.app.services.IAccountService;
import org.sgomez.test.springboot.app.services.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

@SpringBootTest
class SpringbootTestApplicationTests {
//	@Mock //mockito
	@MockBean //Spring boot
	AccountRepository accountRepository;
//	@Mock
	@MockBean //Spring boot
	BankRepository bankRepository;
//	@InjectMocks //mockito
	@Autowired //Spring boot
	AccountServiceImpl accountService;

	@BeforeEach
	void setUp() {
// 		Lo siguiente se reemplaza por el uso del derador @mock
//		accountRepository = mock(AccountRepository.class);
//		bankRepository = mock(BankRepository.class);
//		accountService = new AccountServiceImpl(accountRepository,bankRepository);
	}

	@Test
	void contextLoads() {
		when(accountRepository.findById(1L)).thenReturn(mockCreateAccount001());
		when(accountRepository.findById(2L)).thenReturn(mockCreateAccount002());
		when(bankRepository.findById(1L)).thenReturn(mockCreateBank());

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
		verify(accountRepository,atMost(2)).save(any(Account.class));

		verify(bankRepository,times(2)).findById(1L);
		verify(bankRepository).save(any(Bank.class));

		verify(accountRepository,times(6)).findById(anyLong());
		verify(accountRepository,never()).findAll();
	}
	@Test
	void transfer_failed_MoneyIsNotEnoughException() {
		when(accountRepository.findById(1L)).thenReturn(mockCreateAccount001());
		when(accountRepository.findById(2L)).thenReturn(mockCreateAccount002());
		when(bankRepository.findById(1L)).thenReturn(mockCreateBank());

		BigDecimal originalBalance = accountService.checkBalance(1L);
		BigDecimal targetBalance = accountService.checkBalance(2L);
		assertEquals("850",originalBalance.toPlainString());
		assertEquals("2000", targetBalance.toPlainString());

		assertThrows(MoneyIsNotEnoughException.class,() -> accountService.transfer(1L,2L,new BigDecimal("1200"),1L));

		originalBalance = accountService.checkBalance(1L);
		targetBalance = accountService.checkBalance(2L);

		assertEquals("850",originalBalance.toPlainString());
		assertEquals("2000",targetBalance.toPlainString());

		int total = accountService.checkTotalTransfers(1L);
		assertEquals(0,total);

		verify(accountRepository,times(3)).findById(1L);
		verify(accountRepository,times(2)).findById(2L);
		verify(accountRepository,never()).save(any(Account.class));

		verify(bankRepository,times(1)).findById(1L);
		verify(bankRepository,never()).save(any(Bank.class));

		verify(accountRepository,times(5)).findById(anyLong());
		verify(accountRepository,never()).findAll();
	}
	@Test
	void validAccount(){
		when(accountRepository.findById(1L)).thenReturn(mockCreateAccount001());

		var account01 = accountService.findById(1L);
		var account02 = accountService.findById(1L);

		assertSame(account01,account02);
		assertEquals("Fanny",account01.getPerson());

		verify(accountRepository, times(2)).findById(anyLong());
	}
}
