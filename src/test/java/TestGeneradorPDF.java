import org.cris.AdaDos.TareaTres.GeneradorPDF;
import org.cris.AdaDos.models.Alumno;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGeneradorPDF {

    private final String rutaPDFDestino = "test_reporte_calificaciones.pdf";
    private final String rutaPDFConvertido = "test_convertido.pdf";

    @AfterEach
    public void limpiarArchivos() {
        File f1 = new File(rutaPDFDestino);
        if (f1.exists()) {
            f1.delete();
        }
        File f2 = new File(rutaPDFConvertido);
        if (f2.exists()) {
            f2.delete();
        }
        File f3 = new File("rutaPDFDestino.pdf");
        if (f3.exists()) {
            f3.delete();
        }
        File f4 = new File("rutaPDFake.csv");
        if (f4.exists()) {
            f4.delete();
        }
    }

    @Test
    public void TestGeneradorPDF() {
        GeneradorPDF exportadorPDF = new GeneradorPDF();

        List<Alumno> listaAlumnos = List.of(
                new Alumno("1", "Dior", "Kirk", "Cris"),
                new Alumno("2", "Dior", "Kirk", "Cris"),
                new Alumno("3", "Dior", "Kirk", "Cris"),
                new Alumno("4", "Dior", "Kirk", "Cris")
        );
        listaAlumnos.get(0).setCalificacion("95");
        listaAlumnos.get(1).setCalificacion("70");

        try {
            exportadorPDF.generarPdfDesdeLista(listaAlumnos, rutaPDFDestino);
            File file = new File(rutaPDFDestino);
            assertTrue(file.isFile());
            assertTrue(file.exists());
            assertTrue(file.length() > 0);
        } catch (Exception e) {
            fail("No debió lanzar excepción al generar PDF válido: " + e.getMessage());
        }
    }

    @Test
    public void TestGeneradorPDFake() {
        String rutaInvalida = "directorio_inexistente_98765/rutaPDFake.pdf";
        GeneradorPDF exportadorPDF = new GeneradorPDF();

        List<Alumno> listaAlumnos = List.of(
                new Alumno("1", "Dior", "Kirk", "Cris"),
                new Alumno("2", "Dior", "Kirk", "Cris")
        );

        assertThrows(Exception.class, () -> {
            exportadorPDF.generarPdfDesdeLista(listaAlumnos, rutaInvalida);
        });

        File file = new File(rutaInvalida);
        assertFalse(file.isFile());
        assertFalse(file.exists());
    }

    @Test
    public void testConvertirCsvAPdfConArchivoExistente() {
        GeneradorPDF exportadorPDF = new GeneradorPDF();
        File csvOrigen = new File("ArchivosCSV/tabla_usuarios.csv");
        if (csvOrigen.exists()) {
            exportadorPDF.convertirCsvAPdf("ArchivosCSV/tabla_usuarios.csv", rutaPDFConvertido);
            File file = new File(rutaPDFConvertido);
            assertTrue(file.exists());
            assertTrue(file.length() > 0);
        }
    }
}
