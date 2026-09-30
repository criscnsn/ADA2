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
    private static class Alumno {
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
    }

    // /***************MIRANDA MODIFICA LA RUTA DONDE LO VAYAS A ALMACENAR*******************************************************/
    private String rutaEntrada = "/ArchivosCSV/tabla_alumnos.csv";
    private String rutaSalida = "/ArchivosCSV/calificaciones_alumnos.csv";
    private List<Alumno> alumnos = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public void generacionCalificaciones() {
        //Seleccionar archivo
        System.out.println("Ruta actual: " + rutaEntrada);
        System.out.print("Presione Enter para usarla o ingrese una nueva ruta:");
        String nuevaRuta = scanner.nextLine();
        if (!nuevaRuta.isEmpty()){
            rutaEntrada = nuevaRuta;
        }
        //*************+VALIDADOR ARCHIVO***********
        if (!rutaEntrada.endsWith(".csv")) {
            System.err.println("Error: El archivo debe ser .csv");
            return;
        }
        /******NO BORRAR!!!!!!!!*******/
        if (!cargarDatos()){
            return;
        }
        //***********Captura de calificaciones******
        System.out.println("CAPTURA DE CALIFICACIONES (Disenio de Sofatware)");
        for (Alumno alumno : alumnos) {
            boolean calificacionValida = false;
            while (!calificacionValida) {
                System.out.print("Ingresa la calificación de " + alumno.getNombreAlumno() + " (" + alumno.getMatricula() + "): ");

                String entrada = scanner.nextLine();

                if (entrada.isEmpty()) {
                    alumno.calificacion = null;
                    calificacionValida = true;
                } else {
                    try {
                        int calificacionTemp = Integer.parseInt(entrada);
                        if (calificacionTemp >= 1 && calificacionTemp <= 100) {
                            alumno.calificacion = calificacionTemp;
                            calificacionValida = true;
                        } else {
                            System.out.println("La calificación debe ser entre 1 y 100.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Debes ingresar un número entero o presionar enter.");
                    }
                }
            }
        }
        //******Confirmacion de salida****** PDF CSV O NADOTA
        System.out.print("\nIngrese 1 si quiere generar el archivo CSV o 2 si desea generar el PDF: ");
        try {
            int opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    generarArchivoSalida(rutaSalida, false);
                    break;
                case 2:
                    String rutaTemporal = rutaSalida.replace(".csv", "_temp.csv");
                    String rutaFinalPDF = rutaSalida.replace(".csv", ".pdf");
                    generarArchivoSalida(rutaTemporal, true);

                    GeneradorPDF generador = new GeneradorPDF();
                    generador.convertirCsvAPdf(rutaTemporal, rutaFinalPDF);

                    new File(rutaTemporal).delete();
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Debe ingresar un número entero.");
            scanner.nextLine();
        }
    }

    private boolean cargarDatos() {
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
                    String notaTexto;

                    if (alumno.calificacion == null) {
                        notaTexto = "S/C";
                    } else {
                        notaTexto = String.valueOf(alumno.calificacion);
                    }
                    pw.println(alumno.getMatricula() + ",Disenio de Software," + notaTexto);
                }
                System.out.println("Archivo generado en: " + rutaDestino);

            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}