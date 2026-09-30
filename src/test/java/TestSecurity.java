import org.cris.AdaDos.TareaDos.Security;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSecurity {

    @Test
    public void testValidarContrasena() {
        String contraseniaSimple = "pepe";
        String contrasenia1 = "vUsj0lMZkOSPk6XHD5BLEg==$VwBD4ZdjxnMICLLlO5ocoUbikjThhyGrCLYVMJQuSJE=";

        assertTrue(Security.validarContrasenia(contraseniaSimple, contrasenia1));
    }

}
