package braincollaboration.wordus.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import braincollaboration.wordus.SQLite.WordusDatabaseHelper;

public class IsDBContainAWord {

    public static Boolean isDBContainAWord (SQLiteDatabase db, String[] word) {
        Cursor cursor = db.query(WordusDatabaseHelper.TABLE_NAME,
                new String[]{WordusDatabaseHelper.COLUMN_NAME},
                WordusDatabaseHelper.COLUMN_NAME + " = ?",
                word,
                null, null, null);

        Boolean contain = cursor.moveToFirst();
        cursor.close();
        return contain;
    }

}
