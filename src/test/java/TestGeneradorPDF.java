import org.cris.AdaDos.TareaTres.GeneradorPDF;
import org.cris.AdaDos.models.Alumno;
import org.cris.AdaDos.utils.ExportadorCSV;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGeneradorPDF {

    @Test
    public void TestGeneradorPDF() {
        String rutaPDFDestino = "rutaPDFDestino.pdf";
        GeneradorPDF  exportadorPDF = new GeneradorPDF();

        List<Alumno> listaAlumnos = List.of(
                new Alumno("1","Dior","Kirk","Cris"),
                new Alumno("2","Dior","Kirk","Cris"),
                new Alumno("3","Dior","Kirk","Cris"),
                new Alumno("4","Dior","Kirk","Cris")
        );
        try{
            exportadorPDF.generarPdfDesdeLista(listaAlumnos, rutaPDFDestino);
            File file = new File(rutaPDFDestino);
            assertTrue(file.isFile());
            assertTrue(file.exists());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void TestGeneradorPDFake() {
        String rutaPDFDestino = "rutaPDFake.csv";
        GeneradorPDF  exportadorPDF = new GeneradorPDF();

        List<Alumno> listaAlumnos = List.of(
                new Alumno("1","Dior","Kirk","Cris"),
                new Alumno("2","Dior","Kirk","Cris"),
                new Alumno("3","Dior","Kirk","Cris"),
                new Alumno("4","Dior","Kirk","Cris")
        );
        try{
            exportadorPDF.generarPdfDesdeLista(listaAlumnos, rutaPDFDestino);
            File file = new File(rutaPDFDestino);
            assertTrue(file.isFile());
            assertTrue(file.exists());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
