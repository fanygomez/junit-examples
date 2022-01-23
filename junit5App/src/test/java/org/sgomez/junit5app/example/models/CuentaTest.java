package org.sgomez.junit5app.example.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fanygomez
 */
class CuentaTest {

    @Test
    void testNombreCuenta(){
        String strEsperado = "Fany";
        Cuenta cuenta = new Cuenta("Fany",new BigDecimal("100.123"));

        assertEquals(strEsperado,cuenta.getPersona());
        //assertTrue(cuenta.getPersona().equals("Fany"));
    }
    /*
        Validar que el saldo no sea negativo
     */
    @Test
    void testSaldoCuenta(){
        Cuenta cuenta = new Cuenta("Fany", new BigDecimal("1000.123"));
        //assertEquals(1000.123,cuenta.getSaldo().doubleValue());
        assertNotNull(cuenta.getSaldo());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("Sebas", new BigDecimal("50.98"));
        Cuenta cuenta2 = new Cuenta("Sebas", new BigDecimal("50.98"));

        //assertNotEquals(cuenta2,cuenta);
        assertEquals(cuenta2,cuenta);
    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Sebas", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));
        System.out.println(cuenta.getSaldo().intValue());
        assertNotNull(cuenta.getSaldo());
       // assertNotEquals(900,cuenta.getSaldo().intValue());
        assertEquals("900.12345",cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCredito() {
        Cuenta cuenta = new Cuenta("Sebas", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        System.out.println(cuenta.getSaldo().intValue());
        System.out.println(cuenta.getSaldo().toPlainString());
        //assertNotEquals(1100,cuenta.getSaldo().intValue());
        assertEquals("1100.12345",cuenta.getSaldo().toPlainString());
    }
}