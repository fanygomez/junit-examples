package org.sgomez.test.springboot.app.services;

import org.sgomez.test.springboot.app.models.Account;
import org.sgomez.test.springboot.app.models.Bank;

import java.math.BigDecimal;

public class Data {
    public static  final Account Account_OO1 = new Account(1L,"Fanny",new BigDecimal("850"));
    public static  final Account Account_OO2 = new Account(2L,"Ale",new BigDecimal("2000"));
    public static final  Bank BANK = new Bank(1L,"Bank X",0);
    // usar mock que retorne una nueva instancia, en lunar de una constante que se comparta la misma data por cada test que lo invoque
    public static Account mockCreateAccount001(){
        return new Account(1L,"Fanny",new BigDecimal("850"));
    }
    public static Account mockCreateAccount002(){
        return new Account(2L,"Ale",new BigDecimal("2000"));
    }
    public static Bank mockCreateBank(){
        return new Bank(1L,"Bank X",0);
    }
}

