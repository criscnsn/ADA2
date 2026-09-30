package org.cris.AdaDos.tareaUno;
import org.cris.AdaDos.TareaTres.GeneradorPDF;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
/*
1. Entrada: un archivo csv con lista de alumnos y 4 columnas
2. El programa permitirá capturar las calificaciones de "Diseño de software"
de todos los estudiantes de la lista (del 1 al 100 puros enteros)
3. Capturadas todas las calificaciones el usuario tendrá una opción para
generar un archivo CSV con 3 columnas: matricula, nombre asignatura y calificación.
NOTA: Como no puede generarse el archivo si no se ingresa algun dato
directamente hice que se tengan que ingresar todos los datos en la V1
*/
public class tareaUno {

    //ALUMNOS (Define caracteristicas)******************
    public static class Alumno {
        String matricula, primerApellido, segundoApellido, nombres;
        Integer calificacion;
        public Alumno(String matricula, String primerApellido, String segundoApellido, String nombres) {
            this.matricula = matricula;
            this.primerApellido = primerApellido;
            this.segundoApellido = segundoApellido;
            this.nombres = nombres;
            this.calificacion = null;
        }
        public String getNombreAlumno() {
            return nombres + " " + primerApellido + " " + segundoApellido;
        }
        public String getMatricula() {
            return matricula;
        }
        public String getCalificacionFormateada() {
            if (calificacion == null) {
                return "S/C";
            }
            return String.valueOf(calificacion);
        }
    }

    // /***************MIRANDA MODIFICA LA RUTA DONDE LO VAYAS A ALMACENAR*******************************************************/
    private String rutaEntrada = "ArchivosCSV/tabla_alumnos.csv";
    private String rutaSalida = "ArchivosCSV/calificaciones_alumnos.csv";
    private List<Alumno> alumnos = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public void generacionCalificaciones() {
        new CapturadorDeCalificaciones(this).ejecutar();
    }

    public String getRutaEntrada() {
        return rutaEntrada;
    }

    public void setRutaEntrada(String rutaEntrada) {
        this.rutaEntrada = rutaEntrada;
    }

    public String getRutaSalida() {
        return rutaSalida;
    }

    public void setRutaSalida(String rutaSalida) {
        this.rutaSalida = rutaSalida;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean cargarDatos() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaEntrada), StandardCharsets.UTF_8))) {
            br.readLine();
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] dato = linea.split(",");
                if (dato.length != 4) {
                    System.err.println("El archivo NO tiene las 4 columnas requeridas.");
                    return false;
                }
                alumnos.add(new Alumno(dato[0], dato[1], dato[2], dato[3]));
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error al leer el archivo:" + e.getMessage());
            return false;
        }
    }

    public void generarArchivoSalida(String rutaDestino, boolean esPDF) {

        if (!esPDF) {
            for (Alumno alumno : alumnos) {
                if (alumno.calificacion == null) {
                    System.err.println("No se puede generar el CSV.");
                    System.err.println("El alumno " + alumno.getNombreAlumno() + " no tiene calificación.");
                    return;
                }
            }
        }else {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(rutaDestino), StandardCharsets.UTF_8))) {
                pw.println("Matricula,Nombre Asignatura,Calificacion");

                for (Alumno alumno : alumnos) {
                    pw.println(alumno.getMatricula() + ",Disenio de Software," + alumno.getCalificacionFormateada());
                }
                System.out.println("Archivo generado en: " + rutaDestino);

            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        tareaUno tarea = new tareaUno();
        tarea.cargarDatos();
        tarea.generacionCalificaciones();
        tarea.generarArchivoSalida("rutaSalida.txt", true);
    }
}
