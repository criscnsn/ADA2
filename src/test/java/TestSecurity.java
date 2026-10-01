import org.cris.AdaDos.TareaDos.Security;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class TestSecurity {

    @Test
    public void testValidarContrasenaCorrecta() {
        String contraseniaSimple = "pepe";
        String contraseniaAlmacenada = "vUsj0lMZkOSPk6XHD5BLEg==$VwBD4ZdjxnMICLLlO5ocoUbikjThhyGrCLYVMJQuSJE=";

        assertTrue(Security.validarContrasenia(contraseniaSimple, contraseniaAlmacenada),
                "La contraseña correcta debe ser validada exitosamente");
    }

    @Test
    public void testValidarContrasenaUsuarioGael() {
        String contraseniaGael = "12345";
        String contraseniaAlmacenada = "Mlg6UsvGOvQd/Q0dUKw0xQ==$De/s0sMojaVcKE35i1KbgGT33l7bNrF0PmSyHAhowu4=";

        assertTrue(Security.validarContrasenia(contraseniaGael, contraseniaAlmacenada),
                "La contraseña del usuario Gael debe ser validada exitosamente");
    }

    @Test
    public void testValidarContrasenaIncorrecta() {
        String contraseniaIncorrecta = "contraseniaErronea";
        String contraseniaAlmacenada = "vUsj0lMZkOSPk6XHD5BLEg==$VwBD4ZdjxnMICLLlO5ocoUbikjThhyGrCLYVMJQuSJE=";

        assertFalse(Security.validarContrasenia(contraseniaIncorrecta, contraseniaAlmacenada),
                "Una contraseña incorrecta debe retornar false");
    }

    @Test
    public void testValidarContrasenaVacia() {
        String contraseniaVacia = "";
        String contraseniaAlmacenada = "vUsj0lMZkOSPk6XHD5BLEg==$VwBD4ZdjxnMICLLlO5ocoUbikjThhyGrCLYVMJQuSJE=";

        assertFalse(Security.validarContrasenia(contraseniaVacia, contraseniaAlmacenada),
                "Una contraseña vacía no debe coincidir");
    }

    @Test
    public void testValidarContrasenaFormatoInvalidoSinSeparador() {
        String password = "pepe";
        String passwordSinSeparador = "vUsj0lMZkOSPk6XHD5BLEgVwBD4ZdjxnMICLLlO5ocoUbikjThhyGrCLYVMJQuSJE";

        assertFalse(Security.validarContrasenia(password, passwordSinSeparador),
                "Formato sin separador $ debe ser rechazado");
    }

    @Test
    public void testValidarContrasenaFormatoInvalidoMultiplesSeparadores() {
        String password = "pepe";
        String passwordConMultiplesSeparadores = "parte1$parte2$parte3";

        assertFalse(Security.validarContrasenia(password, passwordConMultiplesSeparadores),
                "Formato con más de 2 partes debe ser rechazado");
    }
}
