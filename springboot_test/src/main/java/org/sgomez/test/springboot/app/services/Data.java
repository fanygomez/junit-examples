package org.sgomez.test.springboot.app.services;

import org.sgomez.test.springboot.app.models.Account;
import org.sgomez.test.springboot.app.models.Bank;

import java.math.BigDecimal;

public class Data {
    public static  final Account Account_OO1 = new Account(1L,"Fanny",new BigDecimal("850"));
    public static  final Account Account_OO2 = new Account(2L,"Ale",new BigDecimal("2000"));
    public static final  Bank BANK = new Bank(1L,"Bank X",0);
}
