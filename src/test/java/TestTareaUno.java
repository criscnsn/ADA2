import org.cris.AdaDos.tareaUno.tareaUno;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestTareaUno {

    private final String rutaTempSalida = "test_salida_tareaUno.csv";
    private final String rutaTempCSVInvalido = "test_invalido_columnas.csv";

    @AfterEach
    public void limpiarArchivos() {
        File f1 = new File(rutaTempSalida);
        if (f1.exists()) {
            f1.delete();
        }
        File f2 = new File(rutaTempCSVInvalido);
        if (f2.exists()) {
            f2.delete();
        }
    }

    @Test
    public void testAlumnoModeloInterno() {
        tareaUno.Alumno alumno = new tareaUno.Alumno("21204567", "Canul", "Portillo", "Pablo Juan");

        assertEquals("21204567", alumno.getMatricula());
        assertEquals("Pablo Juan Canul Portillo", alumno.getNombreAlumno());
        assertEquals("S/C", alumno.getCalificacionFormateada(), "Inicialmente la calificación debe ser S/C");

        alumno.setCalificacion(90);
        assertEquals("90", alumno.getCalificacionFormateada());
        assertEquals(90, alumno.getCalificacion());
    }

    @Test
    public void testCargarDatosExitoso() {
        tareaUno tarea = new tareaUno();
        // Usa la ruta por defecto ArchivosCSV/tabla_alumnos.csv
        File archivo = new File(tarea.getRutaEntrada());
        if (archivo.exists()) {
            boolean resultado = tarea.cargarDatos();
            assertTrue(resultado, "Debe cargar exitosamente los alumnos del CSV");
            assertFalse(tarea.getAlumnos().isEmpty(), "La lista de alumnos no debe estar vacía");
            assertEquals("21204567", tarea.getAlumnos().get(0).getMatricula());
        }
    }

    @Test
    public void testCargarDatosArchivoInexistente() {
        tareaUno tarea = new tareaUno();
        tarea.setRutaEntrada("archivo_inexistente_12345.csv");

        boolean resultado = tarea.cargarDatos();
        assertFalse(resultado, "Debe fallar al intentar cargar un archivo que no existe");
        assertTrue(tarea.getAlumnos().isEmpty(), "La lista de alumnos debe permanecer vacía");
    }

    @Test
    public void testCargarDatosColumnasInvalidas() throws Exception {
        // Crear un CSV con columnas insuficientes (2 en vez de 4)
        try (PrintWriter pw = new PrintWriter(rutaTempCSVInvalido, StandardCharsets.UTF_8)) {
            pw.println("Matricula,Nombre");
            pw.println("12345,Juan");
        }

        tareaUno tarea = new tareaUno();
        tarea.setRutaEntrada(rutaTempCSVInvalido);

        boolean resultado = tarea.cargarDatos();
        assertFalse(resultado, "Debe fallar si el archivo no tiene 4 columnas");
    }

    @Test
    public void testGenerarArchivoSalidaModoPDF() throws Exception {
        tareaUno tarea = new tareaUno();
        List<tareaUno.Alumno> lista = new ArrayList<>();
        tareaUno.Alumno a1 = new tareaUno.Alumno("1001", "Perez", "Gomez", "Mario");
        a1.setCalificacion(85);
        tareaUno.Alumno a2 = new tareaUno.Alumno("1002", "Lopez", "Diaz", "Rosa");
        // a2 con calificacion null
        lista.add(a1);
        lista.add(a2);
        tarea.setAlumnos(lista);

        // En modo esPDF = true, genera el archivo formateado incluso con alumnos S/C
        tarea.generarArchivoSalida(rutaTempSalida, true);

        File generado = new File(rutaTempSalida);
        assertTrue(generado.exists(), "El archivo generado debe existir");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(generado), StandardCharsets.UTF_8))) {
            assertEquals("Matricula,Nombre Asignatura,Calificacion", br.readLine());
            assertEquals("1001,Disenio de Software,85", br.readLine());
            assertEquals("1002,Disenio de Software,S/C", br.readLine());
        }
    }

    @Test
    public void testGenerarArchivoSalidaModoCSVConCalificacionesIncompletas() {
        tareaUno tarea = new tareaUno();
        List<tareaUno.Alumno> lista = new ArrayList<>();
        tareaUno.Alumno a1 = new tareaUno.Alumno("1001", "Perez", "Gomez", "Mario");
        // a1 sin calificacion (null)
        lista.add(a1);
        tarea.setAlumnos(lista);

        // En modo esPDF = false (modo CSV estricto), no debe generar el archivo si faltan notas
        tarea.generarArchivoSalida(rutaTempSalida, false);

        File generado = new File(rutaTempSalida);
        assertFalse(generado.exists(), "No debe generarse el archivo CSV si faltan calificaciones");
    }


    @Test
    public void testGettersYSetters() {
        tareaUno tarea = new tareaUno();
        tarea.setRutaEntrada("nueva/entrada.csv");
        assertEquals("nueva/entrada.csv", tarea.getRutaEntrada());

        tarea.setRutaSalida("nueva/salida.csv");
        assertEquals("nueva/salida.csv", tarea.getRutaSalida());

        assertNotNull(tarea.getScanner());
    }
}
