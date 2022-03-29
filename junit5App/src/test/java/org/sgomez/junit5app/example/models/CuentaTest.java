package org.sgomez.junit5app.example.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.sgomez.junit5app.example.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
/**
 * @author fanygomez
 */
//No es recomendable tener una sola instancia.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {
    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest(){
        this.cuenta = new Cuenta("Fany",new BigDecimal("100.123"));
        System.out.printf("Iniciando....\n");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el metodo de prueba. \n");
    }

    @BeforeAll
    //static void beforeAll() { // valido solo si, @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    static void beforeAll() {
        //Este metodo le partenece a la clase
        System.out.printf("Inicializando el test...");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }

    @Test
    @DisplayName("Probando Nombre la cuenta")
    void testNombreCuenta(){
        String strEsperado = "Fany";
        String real = cuenta.getPersona();

        assertNotNull(real,"La cuenta  no puede ser nula");
        //Mensajes personalizados usando expresiones
        assertEquals(strEsperado,real,() -> "El nombre de la cuenta no es el que se esperaba");
        assertTrue(real.equals("Fany"),() -> "Nombre de cuenta esperado debe ser igual al real.");
    }
    /*
        Validar que el saldo no sea negativo
        Solo ejecutar en ambiente dev
     */
    @Test
    @DisplayName("Validar que el saldo no sea negativo")
    void testSaldoCuenta(){
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(esDev);

        //assertEquals(1000.123,cuenta.getSaldo().doubleValue());
        assertNotNull(cuenta.getSaldo());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("testSaldoCuenta2: Validar que el saldo no sea negativo")
    void testSaldoCuenta2(){
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        //Bloque por ambiente
        assumingThat(esDev,() -> {
            //assertEquals(1000.123,cuenta.getSaldo().doubleValue());
            assertNotNull(cuenta.getSaldo());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);

        });
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);

    }

    @Test
    @DisplayName("Test referencias que sean iguales con el metodo equals()")
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("Sebas", new BigDecimal("50.98"));
        Cuenta cuenta2 = new Cuenta("Sebas", new BigDecimal("50.98"));

        //assertNotEquals(cuenta2,cuenta);
        assertEquals(cuenta2,cuenta);
    }

    @Test
    @DisplayName("test Debito Cuenta")
    void testDebitoCuenta() {
        cuenta = new Cuenta("Sebas", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));
        System.out.println(cuenta.getSaldo().intValue());
        assertNotNull(cuenta.getSaldo());
       // assertNotEquals(900,cuenta.getSaldo().intValue());
        assertEquals("900.12345",cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCredito() {
        cuenta = new Cuenta("Sebas", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        System.out.println(cuenta.getSaldo().intValue());
        System.out.println(cuenta.getSaldo().toPlainString());
        //assertNotEquals(1100,cuenta.getSaldo().intValue());
        assertEquals("1100.12345",cuenta.getSaldo().toPlainString());
    }
    @Test
    void testDineroInsuficienteExceptionCuenta() {
        cuenta = new Cuenta("Sebas", new BigDecimal("1000.12345"));

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
    //@Disabled
    @DisplayName("Test relaciones entre cuentas y el banco con AssertAll()")
    void testRelacionBancoCuentas() {
      //  fail();
        Cuenta cuenta1 = new Cuenta("Jose", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Sebas", new BigDecimal("1500.8989"));

        Banco banco = new Banco("Banco Agri");
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.transferir(cuenta2,cuenta1, new BigDecimal(500));
        //Agrupacion
        assertAll(
                () -> { assertEquals("1000.8989",cuenta2.getSaldo().toPlainString(),
                                    () -> "El valor de la cuenta2 no es el esperado");},
                () -> { assertEquals("3000",cuenta1.getSaldo().toPlainString(),
                                    () -> "El valor de la cuenta1 no es la esperada"); } ,
                () ->   assertEquals("3000",cuenta1.getSaldo().toPlainString(),
                                    () -> "El banco no tiene las cuentas esperadas"),
                () -> { assertEquals(2,banco.getCuentas().size(),
                                    () -> "");},
                () -> { assertEquals(2,banco.getCuentas().size(),
                                    () -> "");},
                () -> { assertEquals("Banco Agri",cuenta1.getBanco().getNombre(),
                                    () -> ""); },
                () -> { assertEquals("Sebas", banco.getCuentas().stream().
                        filter(cuenta -> cuenta.getPersona().equals("Sebas"))
                        .findFirst()
                        .get().getPersona(),
                        () -> "");
                        },
                ()-> assertFalse(banco.getCuentas().stream().
                    anyMatch(cuenta -> cuenta.getPersona().equals("Jose G")),
                                    () -> "")
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
    @EnabledOnOs(OS.WINDOWS)
    void testSoloWindows() {
    }
    @EnabledOnOs({OS.LINUX,OS.MAC})
    void testSoloLinuxMac() {
        System.out.printf("testSoloLinuxMac");
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void soloJdk8() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_11)
    void soloJdk11() {
    }

    @Test
    void imprimirSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k,v)-> System.out.println(k +" : "+v));

    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = "15")
    void testJavaVersion() {
    }

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
    void testArch64() {
    }

    @Test
    @EnabledIfSystemProperty(named = "user.name", matches = "stefh")
    void testUsername() {
    }
    /* Prueba solo para X ambiente */
    @Test
    @EnabledIfSystemProperty(named = "ENV",matches = "dev")
    void testDev() {
    }

    @Test
    void testImprimirVariablesAmbiente() {
        Map<String, String>  getEnv = System.getenv();
        getEnv.forEach((k,v)-> System.out.println(k +" = "+v));

    }

    @Test
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME",matches = ".*jdk-11.0.13")
    void testJavaHome() {
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "NUMBER_PROCESSORS",matches = "12")
    void testProcesadores() {
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ENVIRONMENT",matches = "dev")
    void testDev2() {
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ENVIRONMENT",matches = "prod")
    void testProd() {
    }

    /**
     * Test: timeout
     */
    @Nested
    @Tag("Timeout")
    class ejemploTimeoutTest{
        @Test
        @Timeout(5)
        void testTimeOut() throws InterruptedException {
            TimeUnit.SECONDS.sleep(4);
        }

        @Test
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
        void testTimeOut2() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

        @Test
        void testTimeoutAssertions() throws InterruptedException {
            assertTimeout(Duration.ofSeconds(5000), ()->{
                TimeUnit.SECONDS.sleep(5);
            });
        }
    }

}