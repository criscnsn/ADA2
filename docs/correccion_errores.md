# Corrección de errores

## Ejecución inicial

```text
Ruta actual: /home/cristopher/Cuarto_Semestre/DisenoDeSoftware/ADA2/ArchivosCSV/tabla_alumnos.csv
Presione Enter para usarla o ingrese una nueva ruta:ArchivosCSV/tabla_alumnos.csv
CAPTURA DE CALIFICACIONES (Disenio de Sofatware)
Ingresa la calificación de Pablo Juan Canul Portillo (21204567): 1
Ingresa la calificación de Mario Montalvo Lopez (21236769): 2
Ingresa la calificación de Mariano Miguel Uicab Moo (21217676): 4
Ingresa la calificación de Jorge Gabriel Ramírez Shiu (21209876): 5
Ingresa la calificación de Juan Gabriel Lopez Dzib (20204523): 6
Ingresa la calificación de Diego Luis Hernandez Arroyo (22206754): 7
Ingresa la calificación de Esteban Quito Membrano Ayuso (22204532): 8
Ingresa la calificación de José Luis Chuil Lares (21208976): 9

Ingrese 1 si quiere generar el archivo CSV o 2 si desea generar el PDF: 1

Process finished with exit code 0
```

---

## Extract Method

Funciona hasta cierto punto, ya que la clase `tareaUno` es una clase que se encarga de todo, por lo que se debe dividir en métodos más pequeños y claros.

Lo que se hizo fue dividir el método `generacionCalificaciones()` en tres partes, ya que se encargaba de seleccionar la ruta, mostrar en consola la interfaz donde se capturaban las calificaciones y la parte de selección del formato:

- `seleccionarRuta()`
- `capturarCalificacionesEnConsola()`
- `seleccionarYGenerarFormato()`

## Introduce Explaining Variable

Se simplificó una expresión compleja en una variable temporal con un nombre descriptivo para hacer el código más fácil de leer.

```java
boolean isCalificacionValid = calificacionTemp >= 1 && calificacionTemp <= 100;
if (isCalificacionValid) {
    alumno.calificacion = calificacionTemp;
    calificacionValida = true;
} else {
    System.out.println("La calificación debe ser entre 1 y 100.");
}
```

## Replace Method with Method Object

Se creó una clase `CapturadorDeCalificaciones` para encapsular la ejecución del método `generacionCalificaciones()`, donde viven todas las variables temporales que utilizan los submétodos, evitando así que se pasen parámetros.

## Inline Temp

En el método `cargarArchivo()` dentro de la clase `CapturaController` tenemos las líneas:

```java
Stage stage = (Stage) btnCargarArchivo.getScene().getWindow();
File file = fileChooser.showOpenDialog(stage);
```

Que podrían reducirse a una sola línea, evitando la creación de una variable temporal innecesaria:

```java
File file = fileChooser.showOpenDialog(btnCargarArchivo.getScene().getWindow());
```

## Replace Temp with Query

En los archivos `ExportadorCSV.java`, `GeneradorPDF.java` y `tareaUno.java` se tenía una lógica duplicada para decidir si se imprime el valor de la calificación o `"S/C"`. Por lo que se creó un método llamado `getCalificacionFormateada()` dentro de la clase `Alumno`:

```java
public String getCalificacionFormateada() {
    if (calificacion.get() == null || calificacion.get().trim().isEmpty()) {
        return "S/C";
    }
    return calificacion.get();
}
```

---

### Ejecución después de la refactorización

```text
Ruta actual: /home/cristopher/Cuarto_Semestre/DisenoDeSoftware/ADA2/ArchivosCSV/tabla_alumnos.csv
Presione Enter para usarla o ingrese una nueva ruta:ArchivosCSV/tabla_alumnos.csv
CAPTURA DE CALIFICACIONES (Disenio de Sofatware)
Ingresa la calificación de Pablo Juan Canul Portillo (21204567): 123
La calificación debe ser entre 1 y 100.
Ingresa la calificación de Pablo Juan Canul Portillo (21204567): 12
Ingresa la calificación de Mario Montalvo Lopez (21236769): 45
Ingresa la calificación de Mariano Miguel Uicab Moo (21217676): 66
Ingresa la calificación de Jorge Gabriel Ramírez Shiu (21209876): 12
Ingresa la calificación de Juan Gabriel Lopez Dzib (20204523): 54
Ingresa la calificación de Diego Luis Hernandez Arroyo (22206754): 12
Ingresa la calificación de Esteban Quito Membrano Ayuso (22204532): 56
Ingresa la calificación de José Luis Chuil Lares (21208976): 12

Ingrese 1 si quiere generar el archivo CSV o 2 si desea generar el PDF: 1

Process finished with exit code 0
```
