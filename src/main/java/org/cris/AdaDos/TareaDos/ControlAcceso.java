package org.cris.AdaDos.TareaDos;
/**
 *******RIP*******
 ControlAcceso.java
 ****2026-2026****
@deprecated se dejó de utilizar con la implementación de la interfaz grafica
*/
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ControlAcceso {
    private String usuariosEncriptados = "ArchivosCSV/tabla_usuarios_encriptados.csv";

    public boolean login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("**********LOGIN***************");
        System.out.print("Usuario: ");
        String usuarioIngresado = scanner.nextLine();
        System.out.print("Contraseña: ");
        String passIngresado = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(usuariosEncriptados), StandardCharsets.UTF_8))) {
            String linea;
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                String usuarioCSV = datos[0];
                String contraseniaAlmacenada = datos[1];

                if (usuarioCSV.equals(usuarioIngresado)) {
                    // Solo pasas la cadena completa, Security se encarga de partirla
                    if (Security.validarContrasenia(passIngresado, contraseniaAlmacenada)) {
                        return true;
                    }
                }
            }
            System.out.println("Usuario no encontrado.");
            return false;

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
}