import org.cris.AdaDos.tareaUno.CapturadorDeCalificaciones;
import org.cris.AdaDos.tareaUno.tareaUno;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class TestCapturadorDeCalificaciones {

    private final String rutaPdfTest = "test_capturador_salida.pdf";
    private final String rutaCsvTest = "test_capturador_salida.csv";

    @AfterEach
    public void limpiarArchivos() {
        File f1 = new File(rutaPdfTest);
        if (f1.exists()) {
            f1.delete();
        }
        File f2 = new File(rutaCsvTest);
        if (f2.exists()) {
            f2.delete();
        }
        File fTemp = new File("test_capturador_salida_temp.csv");
        if (fTemp.exists()) {
            fTemp.delete();
        }
    }

    @Test
    public void testConstructores() {
        CapturadorDeCalificaciones c1 = new CapturadorDeCalificaciones();
        assertNotNull(c1.getTarea());

        tareaUno customTarea = new tareaUno();
        CapturadorDeCalificaciones c2 = new CapturadorDeCalificaciones(customTarea);
        assertSame(customTarea, c2.getTarea());
    }

    @Test
    public void testGettersYSetters() {
        CapturadorDeCalificaciones capturador = new CapturadorDeCalificaciones();

        tareaUno t = new tareaUno();
        capturador.setTarea(t);
        assertSame(t, capturador.getTarea());

        capturador.setNuevaRuta("ruta/test.csv");
        assertEquals("ruta/test.csv", capturador.getNuevaRuta());

        capturador.setCalificacionTemp(95);
        assertEquals(95, capturador.getCalificacionTemp());

        capturador.setOpcion(2);
        assertEquals(2, capturador.getOpcion());

        capturador.setRutaTemporal("ruta/temp.csv");
        assertEquals("ruta/temp.csv", capturador.getRutaTemporal());

        capturador.setRutaFinalPDF("ruta/final.pdf");
        assertEquals("ruta/final.pdf", capturador.getRutaFinalPDF());
    }

    @Test
    public void testSeleccionarRutaRechazaArchivoNoCsv() {
        tareaUno tarea = new tareaUno();
        // Simulamos que el usuario ingresa un archivo que no termina en .csv
        String inputSimulado = "archivo_invalido.txt\n";
        tarea.setScanner(new Scanner(new ByteArrayInputStream(inputSimulado.getBytes(StandardCharsets.UTF_8))));

        CapturadorDeCalificaciones capturador = new CapturadorDeCalificaciones(tarea);
        boolean resultado = capturador.seleccionarRuta();

        assertFalse(resultado, "Debe rechazar archivos que no tengan extensión .csv");
    }

    @Test
    public void testSeleccionarRutaConEnterUsaRutaPorDefecto() {
        tareaUno tarea = new tareaUno();
        // Presionar Enter (cadena vacía) mantiene la ruta por defecto
        String inputSimulado = "\n";
        tarea.setScanner(new Scanner(new ByteArrayInputStream(inputSimulado.getBytes(StandardCharsets.UTF_8))));

        CapturadorDeCalificaciones capturador = new CapturadorDeCalificaciones(tarea);
        File archivo = new File(tarea.getRutaEntrada());
        if (archivo.exists()) {
            boolean resultado = capturador.seleccionarRuta();
            assertTrue(resultado, "Presionar enter debe usar la ruta por defecto y cargar si existe");
        }
    }

    @Test
    public void testGenerarPDF() {
        tareaUno tarea = new tareaUno();
        tarea.setRutaSalida(rutaCsvTest);

        List<tareaUno.Alumno> alumnos = new ArrayList<>();
        tareaUno.Alumno a = new tareaUno.Alumno("111", "Lopez", "Diaz", "Juan");
        a.setCalificacion(88);
        alumnos.add(a);
        tarea.setAlumnos(alumnos);

        CapturadorDeCalificaciones capturador = new CapturadorDeCalificaciones(tarea);
        capturador.generarPDF();

        // Debe generar el PDF final
        File pdfFinal = new File(rutaPdfTest);
        assertTrue(pdfFinal.exists(), "El archivo PDF generado debe existir");
        assertTrue(pdfFinal.length() > 0, "El archivo PDF no debe estar vacío");

        // Y debe haber eliminado el archivo temporal _temp.csv
        File tempCsv = new File("test_capturador_salida_temp.csv");
        assertFalse(tempCsv.exists(), "El archivo temporal debe haber sido eliminado");
    }
}
