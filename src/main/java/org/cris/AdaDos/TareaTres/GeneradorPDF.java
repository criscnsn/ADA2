package org.cris.AdaDos.TareaTres;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.cris.AdaDos.models.Alumno; // Importamos tu modelo

import java.io.*;
import java.util.List;

public class GeneradorPDF {

    public void generarPdfDesdeLista(List<Alumno> listaAlumnos, String rutaPDFDestino) throws Exception {
        Document documento = new Document();
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaPDFDestino));
            documento.open();

            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
            Paragraph titulo = new Paragraph("Reporte de Calificaciones", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            documento.add(titulo);

            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);

            agregarCelda(tabla, "Matrícula", true);
            agregarCelda(tabla, "Nombre Completo", true);
            agregarCelda(tabla, "Calificación", true);

            for (Alumno al : listaAlumnos) {
                agregarCelda(tabla, al.getMatricula(), false);

                // Concatenamos el nombre aquí para que se vea bien
                String nombreCompleto = al.getNombres() + " " + al.getPrimerApellido() + " " + al.getSegundoApellido();
                agregarCelda(tabla, nombreCompleto, false);

                agregarCelda(tabla, al.getCalificacionFormateada(), false);
            }

            documento.add(tabla);
            System.out.println("PDF Generado en: " + rutaPDFDestino);

        } finally {
            documento.close();
        }
    }
    private void agregarCelda(PdfPTable tabla, String texto, boolean esHeader) {
        Font fuente;
        if (esHeader) {
            fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        } else if (texto.equals("S/C")) {
            fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.RED);
        } else {
            fuente = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        }

        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(5);

        if (esHeader) celda.setBackgroundColor(BaseColor.GRAY);

        tabla.addCell(celda);
    }
/**
 * @deprecated se utiliza de manera interna para tareaUno
 * pero realmente quedo obsoleto pues los datos se manejaran distinto
 * por eso se implemento la clase alumno
 */
    public void convertirCsvAPdf(String rutaCSVOrigen, String rutaPDFDestino) {
        Document documento = new Document();
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaPDFDestino));
            documento.open();

            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
            Paragraph titulo = new Paragraph("Reporte de Calificaciones", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            documento.add(titulo);

            // 3. Tabla de 3 columnas
            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);

            // Encabezados
            agregarCelda(tabla, "Matrícula", true);
            agregarCelda(tabla, "Asignatura", true);
            agregarCelda(tabla, "Calificación", true);

            // 4. Leer CSV y llenar tabla
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaCSVOrigen), "UTF-8"))) {
                String linea;
                br.readLine(); // Saltamos cabecera del CSV si existe

                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",");
                    if (datos.length >= 3) {
                        agregarCelda(tabla, datos[0], false); // Matrícula
                        agregarCelda(tabla, datos[1], false); // Asignatura (Diseño...)

                        String calif = datos[2];
                        agregarCelda(tabla, calif, false);
                    }
                }
            }

            documento.add(tabla);
            System.out.println("PDF Generado en: " + rutaPDFDestino);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            documento.close();
        }
    }
}
