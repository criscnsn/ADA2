package org.cris.AdaDos.TareaDos;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class Security {
    private static final String PIMIENTA = "wizyfn30hz80ping";

    public static String agregarSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private static String encriptar(String contrasenia, String SALT) {
        try {
            String mezcla = SALT + contrasenia + PIMIENTA;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(mezcla.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validarContrasenia(String passwordIngresada, String passwordAlmacenada) {
        String[] partes = passwordAlmacenada.split("\\$");
        if (partes.length != 2) {
            return false;
        }
        String saltOriginal = partes[0];
        String hashOriginal = partes[1];

        String nuevoHash = encriptar(passwordIngresada, saltOriginal);
        return nuevoHash.equals(hashOriginal);
    }

    /* EJECUTAR SOLO PARA GENERAR EL PRIMER CSV o AGREGAR NUEVOS USUARIOS ***************************/
    public static void main(String[] args) {
        int usrRgstrd = 0;
        String archivoEntrada = "ArchivosCSV/tabla_usuarios.csv";
        String archivoSalida = "ArchivosCSV/tabla_usuarios_encriptados.csv";

        System.out.println("ENCRIPTACIÓN DE USUARIOS");
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivoEntrada), StandardCharsets.UTF_8));
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivoSalida), StandardCharsets.UTF_8))
        ) {
            String linea;

            if ((linea = br.readLine()) != null) {
                pw.println("Usuario,Contrasenia");
            }
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 2) {
                    String usuario = datos[0].trim();
                    String passPlana = datos[1].trim();

                    String salt = agregarSalt();
                    String hash = encriptar(passPlana, salt);
                    String passwordFinal = salt + "$" + hash;

                    pw.println(usuario + "," + passwordFinal);
                    usrRgstrd++;
                }
            }
            System.out.println("Archivo guardado en: " + archivoSalida);
            System.out.println("Se registraron " + usrRgstrd + " usuarios");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}