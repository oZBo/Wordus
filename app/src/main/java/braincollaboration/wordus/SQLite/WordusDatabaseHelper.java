package braincollaboration.wordus.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordusDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "wordus"; // Datebase name
    private static final int DB_VERSION = 1; // Datebse ver.

    public static final String TABLE_NAME = "WORDS";
    public static final String COLUMN_ID= "_id";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";

    public WordusDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_DESCRIPTION + " TEXT);");
        }
    }

    public static void insertWord(SQLiteDatabase db, ContentValues wordValues) {

        if (db != null && wordValues != null) {
            db.insert(TABLE_NAME, null, wordValues);
        }
    }

    public static void updateWord(SQLiteDatabase db,
                                   ContentValues wordValues,
                                   String whereClause,
                                   String whereArgs) {
        String[] whereArgs1 = {whereArgs};

        if ((whereClause != null || whereArgs != null) && db != null) {
            db.update(TABLE_NAME, wordValues, whereClause + " = ?", whereArgs1);
        }
    }

    public static void deleteWord (SQLiteDatabase db, String whereClause,
                                   String whereArgs) {
        String[] whereArgs1 = {whereArgs};

        if ((whereClause != null || whereArgs != null) && db != null) {
            db.delete(TABLE_NAME, whereClause + " = ?", whereArgs1);
        }
    }

    // makes Value to use it for datebase changes
    public static ContentValues makeWordValue (ContentValues contentValues, String where, String what) {
        ContentValues wordValues;

        if (contentValues == null) {
            wordValues = new ContentValues();
        } else {
            wordValues = contentValues;
        }

        wordValues.put(where, what);

        return wordValues;
    }

    public static ContentValues makeWordValue (ContentValues contentValues, String where, Integer what) {
        ContentValues wordValues;

        if (contentValues == null) {
            wordValues = new ContentValues();
        } else {
            wordValues = contentValues;
        }

        wordValues.put(where, what);

        return wordValues;
    }
}
