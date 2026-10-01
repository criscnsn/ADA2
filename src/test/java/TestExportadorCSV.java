import org.cris.AdaDos.models.Alumno;
import org.cris.AdaDos.utils.ExportadorCSV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestExportadorCSV {

    private final String rutaPruebaCSV = "test_export_alumnos.csv";
    private final String rutaPruebaVaciaCSV = "test_export_vacio.csv";

    @AfterEach
    public void limpiarArchivos() {
        File f1 = new File(rutaPruebaCSV);
        if (f1.exists()) {
            f1.delete();
        }
        File f2 = new File(rutaPruebaVaciaCSV);
        if (f2.exists()) {
            f2.delete();
        }
    }

    @Test
    public void testGenerarReporteConAlumnos() throws Exception {
        List<Alumno> alumnos = new ArrayList<>();
        Alumno a1 = new Alumno("21204567", "Canul", "Portillo", "Pablo Juan");
        a1.setCalificacion("95");
        Alumno a2 = new Alumno("21236769", "Montalvo", "Lopez", "Mario");
        // a2 sin calificacion asignada (debe salir S/C)
        alumnos.add(a1);
        alumnos.add(a2);

        boolean resultado = ExportadorCSV.generarReporte(alumnos, rutaPruebaCSV);
        assertTrue(resultado, "El reporte debe generarse exitosamente");

        File archivoGenerado = new File(rutaPruebaCSV);
        assertTrue(archivoGenerado.exists(), "El archivo CSV debe existir");
        assertTrue(archivoGenerado.length() > 0, "El archivo CSV no debe estar vacío");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivoGenerado), StandardCharsets.UTF_8))) {
            String cabecera = br.readLine();
            assertEquals("Matricula,Nombre Asignatura,Calificacion", cabecera);

            String fila1 = br.readLine();
            assertEquals("21204567,Disenio de Software,95", fila1);

            String fila2 = br.readLine();
            assertEquals("21236769,Disenio de Software,S/C", fila2);

            assertNull(br.readLine(), "No deben haber más filas");
        }
    }

    @Test
    public void testGenerarReporteListaVacia() throws Exception {
        List<Alumno> alumnosVacios = new ArrayList<>();
        boolean resultado = ExportadorCSV.generarReporte(alumnosVacios, rutaPruebaVaciaCSV);
        assertTrue(resultado, "Debe generar reporte aunque la lista esté vacía");

        File archivoGenerado = new File(rutaPruebaVaciaCSV);
        assertTrue(archivoGenerado.exists());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivoGenerado), StandardCharsets.UTF_8))) {
            String cabecera = br.readLine();
            assertEquals("Matricula,Nombre Asignatura,Calificacion", cabecera);
            assertNull(br.readLine(), "La lista vacía no debe contener filas de datos");
        }
    }

    @Test
    public void testGenerarReporteRutaInvalida() throws Exception {
        String rutaInvalida = "directorio_que_no_existe_98765/resultado.csv";
        List<Alumno> alumnos = List.of(new Alumno("123", "Perez", "Gomez", "Juan"));

        boolean resultado = ExportadorCSV.generarReporte(alumnos, rutaInvalida);
        assertFalse(resultado, "Debe retornar false si la ruta es inaccesible o inválida");

        File archivoInvalido = new File(rutaInvalida);
        assertFalse(archivoInvalido.exists());
    }

    @Test
    public void testInstanciaExportadorCSV() throws Exception {
        ExportadorCSV exportador = new ExportadorCSV();
        assertNotNull(exportador);

        List<Alumno> alumnos = List.of(new Alumno("999", "Lopez", "Diaz", "Ana"));
        boolean resultado = exportador.generarReporte(alumnos, rutaPruebaCSV);
        assertTrue(resultado);
    }
}
