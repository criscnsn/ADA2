module org.cris.AdaDos {
    // Módulos necesarios de JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Módulo para iText (PDFs)
    // Nota: itextpdf 5.x es un módulo automático
    requires itextpdf;

    // Paquetes base de Java que usas en Security y otros
    requires java.base;
    requires java.sql; // A veces necesario si usas JDBC futuramente

    // Permitir que JavaFX acceda a tu Main y a tus controladores (reflexión)
    opens org.cris.AdaDos to javafx.fxml;
    opens org.cris.AdaDos.controllers to javafx.fxml;

    opens org.cris.AdaDos.models to javafx.base;

    // Exportar el paquete principal para que sea visible
    exports org.cris.AdaDos;
    exports org.cris.AdaDos.TareaDos;
    exports org.cris.AdaDos.models;
    exports org.cris.AdaDos.utils;
    exports org.cris.AdaDos.TareaTres;
    exports org.cris.AdaDos.tareaUno;
}