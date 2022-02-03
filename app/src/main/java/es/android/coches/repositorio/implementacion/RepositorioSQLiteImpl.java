package es.android.coches.repositorio.implementacion;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import es.android.coches.entidad.Pregunta;
import es.android.coches.repositorio.interfaz.Repositorio;

public class RepositorioSQLiteImpl extends SQLiteOpenHelper implements Repositorio<Pregunta> {

    // Atributos constantes
    public static final int DATABASE_VERSION = 1; // Versión de la base de datos
    public static final String DATABASE_NAME = "Preguntas.db"; // Nombre de la base de datos

    public RepositorioSQLiteImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override //  - onCreate: Si no existe la BBDD

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + ContratoPregunta.EntradaPregunta.NOMBRE_TABLA +
                " (" + ContratoPregunta.EntradaPregunta._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ContratoPregunta.EntradaPregunta.NOMBRE + " TEXT NOT NULL,"
                + ContratoPregunta.EntradaPregunta.FOTO + " TEXT NOT NULL,"
                + "UNIQUE (" + ContratoPregunta.EntradaPregunta.NOMBRE + "))");
                // La restricción UNIQUE en SQL se utiliza para garantizar que no se
                // inserten valores duplicados en una columna específica y que no forme parte de la CLAVE PRIMARIA.

        // insertamos los registros en la BBDD
        Pregunta pregunta1 = new Pregunta("Seat", "seat.jpeg");
        this.save(pregunta1, db);
        Pregunta pregunta2 = new Pregunta("Dacia", "dacia.jpeg");
        this.save(pregunta2, db);
        Pregunta pregunta3 = new Pregunta("Mercedes", "mercedes.jpeg");
        this.save(pregunta3, db);
        Pregunta pregunta4 = new Pregunta("BMW", "bmw.jpeg");
        this.save(pregunta4, db);
        Pregunta pregunta5 = new Pregunta("Audi", "audi.jpeg");
        this.save(pregunta5, db);

    }

    @Override //  - onUpdate: Cuando se especifique una nueva versión de BBDD
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public Optional<Pregunta> get(long id) {
        return Optional.empty();
    }

    @Override //  Para obtener la información de las preguntas
    public List<Pregunta> getAll() {

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                ContratoPregunta.EntradaPregunta.NOMBRE_TABLA, // Nombre de la tabla
                null, // Lista de Columnas a consultar
                null, // Columnas para la cláusula WHERE
                null, // Valores a comparar con las columnas del WHERE
                null, // Agrupar con GROUP BY
                null, // Condición HAVING para GROUP BY
                null // Cláusula ORDER BY
        );

        List<Pregunta> preguntas = new LinkedList<>();
        while(c.moveToNext()){ // Mientras el cursor tenga filas de datos
            @SuppressLint("Range")
            String nombre = c.getString( // en variable nombre me coges el string que tiene la columna NOMBRE
                    c.getColumnIndex(ContratoPregunta.EntradaPregunta.NOMBRE));
            @SuppressLint("Range")
            String foto = c.getString( // en variable foto me coges el string que tiene la columna FOTO
                    c.getColumnIndex(ContratoPregunta.EntradaPregunta.FOTO));
            preguntas.add(new Pregunta(nombre, foto)); // Y lo añades al List

            /* NOMBRE     FOTO
                seat    seat.jpeg
                audi    audi.jpeg
                ....    ........*/
        }
        return preguntas;
    }

    @Override
    public void save(Pregunta pregunta) {
        this.save(pregunta, null);
    }

    // creamos nuevo método save() que se usa internamente en el onCreate
    private void save(Pregunta pregunta, SQLiteDatabase db) {

        if(db == null)
            // Obtenemos la BBDD para escritura
            db = getWritableDatabase();

        // Creamos un contenedor de valores
        ContentValues values = new ContentValues();
        // Agregamos al contenedor los pares clave-valor (En columna NOMBRE, me insertas el nombre)
                                                               //(En columna FOTO, me insertas la foto)
        values.put(ContratoPregunta.EntradaPregunta.NOMBRE, pregunta.getNombre());
        values.put(ContratoPregunta.EntradaPregunta.FOTO, pregunta.getFoto());

        // Insertar... Me insertas en esta tabla, estos valores
        db.insert(ContratoPregunta.EntradaPregunta.NOMBRE_TABLA, null, values);
    }


        /*// Obtenemos la BBDD para escritura
        SQLiteDatabase db = getWritableDatabase();

        // Creamos un contenedor de valores
        ContentValues values = new ContentValues();

        // Agregamos al contenedor los pares clave-valor (En columna NOMBRE, me insertas el nombre)
                                                        //(En columna FOTO, me insertas la foto)
        values.put(ContratoPregunta.EntradaPregunta.NOMBRE, pregunta.getNombre());
        values.put(ContratoPregunta.EntradaPregunta.FOTO, pregunta.getFoto());

        // Insertar... Me insertas en esta tabla, estos valores
        db.insert(ContratoPregunta.EntradaPregunta.NOMBRE_TABLA, null, values);

    }*/

    @Override
    public void update(Pregunta pregunta) {

        // Obtenemos la BBDD para escritura
        SQLiteDatabase db = getWritableDatabase();

        // Contenedor de valores
        ContentValues values = new ContentValues();

        // Pares clave-valor
        values.put(ContratoPregunta.EntradaPregunta.NOMBRE, pregunta.getNombre());
        values.put(ContratoPregunta.EntradaPregunta.FOTO, pregunta.getFoto());

        // Actualizar...
        db.update(ContratoPregunta.EntradaPregunta.NOMBRE_TABLA, values, "nombre=?", // filtro para seleccionar registro a actualizar
                new String[] {pregunta.getNombre()}); // array para sustituir ? por valores
    }

    @Override
    public void delete(Pregunta pregunta) {

        // Traeme la BBDD para escribir en ella
        SQLiteDatabase db = getWritableDatabase();

        // Borrar en esta tabla, la fila donde nombre sea igual al nombre que traes de la clase Pregunta
        db.delete(ContratoPregunta.EntradaPregunta.NOMBRE_TABLA, "nombre=?", // filtro para seleccionar registro a borrar
                new String[] {pregunta.getNombre()}); // array para sustituir ? por valores

    }
}

class ContratoPregunta {

    // Contructor privado para que no se pueda instanciar la clase accidentalmente
    private ContratoPregunta() {}

    // BaseColumns: Interfaz que proporciona los nombres de columna _ID y _COUNT.
    //_ID: El ID único de una fila. _COUNT El recuento de filas en un directorio.
    public static class EntradaPregunta implements BaseColumns {

        // Nombre que tiene la tabla
        public static final String NOMBRE_TABLA = "Pregunta";
        //Nombre de las columnas
        public static final String NOMBRE = "nombre";
        public static final String FOTO = "foto";
    }
}
