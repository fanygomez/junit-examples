package org.sgomez.test.springboot.app.services;

import org.sgomez.test.springboot.app.dtos.TransactionReqDto;
import org.sgomez.test.springboot.app.models.Account;
import org.sgomez.test.springboot.app.models.Bank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Data {
//    public static  final Account Account_OO1 = new Account(1L,"Fanny",new BigDecimal("850"));
//    public static  final Account Account_OO2 = new Account(2L,"Ale",new BigDecimal("2000"));
//    public static final  Bank BANK = new Bank(1L,"Bank X",0);
    // usar mock que retorne una nueva instancia, en lunar de una constante que se comparta la misma data por cada test que lo invoque
    public static Optional<Account> mockCreateAccount001(){
        return Optional.of(new Account(1L, "Fanny", new BigDecimal("850")));
    }
    public static Optional<Account> mockCreateAccount002(){
        return Optional.of(new Account(2L, "Ale", new BigDecimal("2000")));
    }
    public static Optional<Account> mockCreateAccount003(){
        return Optional.of(new Account(null, "Hector", new BigDecimal("1500")));
    }
    public static List<Account> mockCreateAccountsList(){
        return Arrays.asList(mockCreateAccount001().orElseThrow(), mockCreateAccount002().orElseThrow());
    }
    public static Optional<Bank> mockCreateBank(){
        return Optional.of(new Bank(1L, "Bank X", 0));
    }
    public static TransactionReqDto mockTransactionReqDto(){
        var reqDto = new TransactionReqDto();
        reqDto.setAccountOriginId(1L);
        reqDto.setAccountTargetId(2L);
        reqDto.setAmount(BigDecimal.valueOf(100));
        reqDto.setBankId(1L);
        return  reqDto;
    }
    public static Map<String, Object> mockTransferResponseDto(){
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Success");
        response.put("transaction", mockTransactionReqDto());
        return response;
    }
}

