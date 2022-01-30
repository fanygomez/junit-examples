package org.sgomez.junit5app.example.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sgomez.junit5app.example.exceptions.DineroInsuficienteException;

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
    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Cuenta cuenta = new Cuenta("Sebas", new BigDecimal("1000.12345"));

       Exception exception = assertThrows(DineroInsuficienteException.class,()->{
            cuenta.debito(new BigDecimal(1500));
        });
       String msjActual = exception.getMessage();
       String msjEsperado = "Dinero Insuficiente";
       assertEquals(msjEsperado,msjActual);
       System.out.println("Dinero Insufiente: Passed");
    }

    @Test
    void testTransFerirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("Sebas", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Jose", new BigDecimal("1500.8989"));

        Banco banco = new Banco("Banco Agri");
        banco.transferir(cuenta2,cuenta1, new BigDecimal(500));

        assertEquals("1000.8989",cuenta2.getSaldo().toPlainString());
        assertEquals("3000",cuenta1.getSaldo().toPlainString());

    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuenta1 = new Cuenta("Jose", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Sebas", new BigDecimal("1500.8989"));

        Banco banco = new Banco("Banco Agri");
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.transferir(cuenta2,cuenta1, new BigDecimal(500));
        //Agrupacion
        assertAll(
                () -> { assertEquals("1000.8989",cuenta2.getSaldo().toPlainString());},
                () -> { assertEquals("3000",cuenta1.getSaldo().toPlainString()); } ,
                () -> { assertEquals("3000",cuenta1.getSaldo().toPlainString());},
                () -> { assertEquals(2,banco.getCuentas().size());},
                () -> { assertEquals(2,banco.getCuentas().size());},
                () -> { assertEquals("Banco Agri",cuenta1.getBanco().getNombre()); },
                () -> { assertEquals("Sebas", banco.getCuentas().stream().
                        filter(cuenta -> cuenta.getPersona().equals("Sebas"))
                        .findFirst()
                        .get().getPersona());
                        },
                ()-> assertFalse(banco.getCuentas().stream().
                    anyMatch(cuenta -> cuenta.getPersona().equals("Jose G")))
                );


        //validar relacion
        //cuenta -> bancoss
        //assertEquals(2,banco.getCuentas().size());
        //Relacion banco -> cuenta
        //assertEquals("Banco Agricola",cuenta1.getBanco().getNombre());

        //Buscar en la lista de cuentas el nombre de Sebas
//        assertEquals("Sebas", banco.getCuentas().stream().
//                filter(cuenta -> cuenta.getPersona().equals("Sebas"))
//                    .findFirst()
//        .get().getPersona());

        //
    }
}