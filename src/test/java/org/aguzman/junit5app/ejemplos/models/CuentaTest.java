package org.aguzman.junit5app.ejemplos.models;

import org.aguzman.junit5app.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {
    //siempre dejar default y no public

    Cuenta cuenta;
    @BeforeEach
    void initMetodoTest(){
        this.cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        System.out.println("iniciando el metodo.");

    }

    @AfterEach
    void tearDown(){
        System.out.println("Finalizando el metodo prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("inicializando el test");
    }

    static @AfterAll
    void afterAll() {
        System.out.println("finalizando el test");
    }

    @Test
    @DisplayName("probando el nombre de la cuenta corriente!.")
    void testNombreCuenta() {
        //cuenta.setPersona("Andres");
        String esperado = "Andres";
        String real = cuenta.getPersona();
        assertNotNull(real,()-> "la cuenta no puede ser nula");
        assertEquals(esperado, real, ()-> "el nombre de la cuenta no es el que se esperaba: se esperaba "+esperado
        + " sin embargo fue "+real);
        assertTrue(real.equals("Andres"),()-> "nombre cuenta esperada debe ser igual a la real");
    }

    @Test
    @DisplayName("probando el saldo de la cuenta corriente, que no sea null, mayor que cero, valor esperado.")
    void testSaldoCuenta() {
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("testeando referencias que sean iguales con el metodo equals.")
    void testReferenciaCuenta() {
        cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        //assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);
    }

    @Test
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());

    }

    @Test
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());

    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void testTrasferirDineroCuenta() {
        Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));
        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Test
    //@Disabled
    @DisplayName("probando relaciones entre las cuentas y el banco con assertAll.")
    void testRelacionBancoCuentas() {
        //fail();
        Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));
        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertAll(() ->assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                                            ()-> "el valor del saldo de la cuenta2 no es el esperado"),
                () -> assertEquals("3000", cuenta1.getSaldo().toPlainString(),
                        ()-> "el valor del saldo de la cuenta1 no es el esperado"),
                () -> assertEquals(2, banco.getCuentas().size(),()->"el banco no tienes la cuentas esperadas"),
                () -> assertEquals("Banco del Estado", cuenta1.getBanco().getNombre()),
                () -> assertEquals("Andres", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Andres"))
                            .findFirst()
                            .get().getPersona()),
                () ->assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getPersona().equals("Jhon Doe"))));


    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testSoloWindows(){

    }
    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC} )
    void testSoloLinuxMac(){

    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testNoWindows() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void soloJdk8() {

    }

    @Test
    @EnabledOnJre(JRE.JAVA_18)
    void soloJDK18() {

    }

    @Test
    @DisabledOnJre(JRE.JAVA_18)
    void testNoJDK18() {

    }

    @Test
    void imprimirSystemProperties(){
        Properties properties=System.getProperties();
        properties.forEach((k,v)-> System.out.println(k+":"+v));
    }

    @Test
    @EnabledIfSystemProperty(named="java.version",matches=".*18.*")
    void testJavaVersion() {
    }

    @Test
    @DisabledIfSystemProperty(named = "os.arch", matches = ".*32:*")
    void testSolo64(){
    }

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*32:*")
    void testNo64(){
    }

    @Test
    @EnabledIfSystemProperty(named ="user.name", matches = "w10")
    void testUsername(){

    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void testDev(){

    }

    @Test
    void imprimirVariablesAmbiente(){
        Map<String, String> getenv=System.getenv();
        getenv.forEach((k,v)-> System.out.println(k+" = "+v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-8.0.*")
    void testJavaHome(){

    }

    @Test
    @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
    void testProcesadores(){

    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
    void testEnv(){

    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
    void testEnvProdDisabled(){

    }

}