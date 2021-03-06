package braincollaboration.wordus.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.utils.Constants;

public class WordusDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "wordus"; // Datebase name
    private static final int DB_VERSION = 1; // Databse ver.

    private static final String TABLE_NAME = "WORDS";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_HAS_LOOKED_FOR = "HAS_LOOKED_FOR";
    private static final String COLUMN_EVER_SHOWN_WORD = "EVER_SHOWN_WORD";

    private static volatile WordusDatabaseHelper instance;

    private WordusDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static WordusDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (WordusDatabaseHelper.class) {
                if (instance == null) {
                    instance = new WordusDatabaseHelper(context);
                }
            }
        }
        return instance;
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
                    + COLUMN_DESCRIPTION + " TEXT, "
                    + COLUMN_HAS_LOOKED_FOR + " INTEGER, "
                    + COLUMN_EVER_SHOWN_WORD + " INTEGER);");
        }
    }

    private static void insertWord(SQLiteDatabase db, ContentValues wordValues) {

        if (db != null && wordValues != null) {
            db.insert(TABLE_NAME, null, wordValues);
        }
    }

    private static void updateWord(SQLiteDatabase db,
                                   ContentValues wordValues,
                                   String whereClause,
                                   String whereArgs) {
        String[] whereArgs1 = {whereArgs};

        if ((whereClause != null || whereArgs != null) && db != null) {
            db.update(TABLE_NAME, wordValues, whereClause + " = ?", whereArgs1);
        }
    }

    public static void deleteWord(SQLiteDatabase db, String whereArgs) {
        String[] whereArgs1 = {whereArgs};

        if (whereArgs != null && db != null) {
            db.delete(TABLE_NAME, COLUMN_NAME + " = ?", whereArgs1);
            Log.d(Constants.LOG_TAG, whereArgs + " deleted from db");
        }
    }

    // makes Value to use it for datebase changes
    private static ContentValues makeWordValue(ContentValues contentValues, String where, String what) {
        ContentValues wordValues;

        if (contentValues == null) {
            wordValues = new ContentValues();
        } else {
            wordValues = contentValues;
        }

        wordValues.put(where, what);

        return wordValues;
    }

    private static ContentValues makeWordValue(ContentValues contentValues, String where, Integer what) {
        ContentValues wordValues;

        if (contentValues == null) {
            wordValues = new ContentValues();
        } else {
            wordValues = contentValues;
        }

        wordValues.put(where, what);

        return wordValues;
    }

    public static Boolean isDBContainAWord(SQLiteDatabase db, String word) {
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_NAME},
                COLUMN_NAME + " = ?",
                new String[]{word},
                null, null, null);

        Boolean contain = cursor.moveToFirst();
        cursor.close();
        return contain;
    }

    public static void addWordNameInDB(SQLiteDatabase db, String s, Boolean isDefine) {
        ContentValues wordNameValue = makeWordValue(null, COLUMN_NAME, s);
        wordNameValue = makeWordValue(wordNameValue, COLUMN_HAS_LOOKED_FOR, !isDefine ? 0 : 1);
        insertWord(db, wordNameValue);
        Log.d(Constants.LOG_TAG, s + " name added in db");

        db.close();
    }

    public static void addWordDescriptionInDB(SQLiteDatabase db, String wordName, String wordDescription, Boolean isDefine, int newFoundWord) {
        ContentValues wordNameValue = makeWordValue(null, COLUMN_DESCRIPTION, wordDescription);
        wordNameValue = makeWordValue(wordNameValue, COLUMN_HAS_LOOKED_FOR, !isDefine ? 0 : 1);
        wordNameValue = makeWordValue(wordNameValue, COLUMN_EVER_SHOWN_WORD, newFoundWord);
        updateWord(db, wordNameValue, COLUMN_NAME, wordName);
        if (wordDescription != null) {
            Log.d(Constants.LOG_TAG, wordName + " description added in db");
        } else {
            Log.d(Constants.LOG_TAG, wordName + " EMPTY description added in db");
        }

        db.close();
    }

    public static void updateEverShownWordStateInDB(SQLiteDatabase db, String wordName, int everShownWord) {
        ContentValues wordNameValue = makeWordValue(null, COLUMN_EVER_SHOWN_WORD, everShownWord);
        updateWord(db, wordNameValue, COLUMN_NAME, wordName);

        db.close();
    }

    public static List<Word> getDataSet(Context context) {
        List<Word> dataSet = new ArrayList<>();
        try {
            SQLiteDatabase db = getInstance(context).getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME,
                    new String[]{COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_HAS_LOOKED_FOR, COLUMN_EVER_SHOWN_WORD},
                    null, null, null, null, null);
            if (cursor.moveToFirst()) {
                Word word = new Word();
                word.setWordName(cursor.getString(0));
                word.setWordDescription(cursor.getString(1));
                word.setHasLookedFor(cursor.getInt(2) != 0);
                word.setEverShown(cursor.getInt(3));
                dataSet.add(word);
                while (cursor.moveToNext()) {
                    word = new Word();
                    word.setWordName(cursor.getString(0));
                    word.setWordDescription(cursor.getString(1));
                    word.setHasLookedFor(cursor.getInt(2) != 0);
                    word.setEverShown(cursor.getInt(3));
                    dataSet.add(word);
                }
            }
            db.close();
            cursor.close();
        } catch (SQLException sqle) {
            Log.e(Constants.LOG_TAG, "database unavailable");
        }

        return dataSet;
    }

    public static List<Word> getNotFoundWordDataSet(Context context) {
        List<Word> dataSet = new ArrayList<>();
        try {
            SQLiteDatabase db = getInstance(context).getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME,
                    new String[]{COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_HAS_LOOKED_FOR, COLUMN_EVER_SHOWN_WORD},
                    COLUMN_HAS_LOOKED_FOR + " = ?",
                    new String[]{Integer.toString(0)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                Word word = new Word();
                word.setWordName(cursor.getString(0));
                word.setWordDescription(cursor.getString(1));
                word.setHasLookedFor(cursor.getInt(2) != 0);
                word.setEverShown(cursor.getInt(3));
                dataSet.add(word);
                while (cursor.moveToNext()) {
                    word = new Word();
                    word.setWordName(cursor.getString(0));
                    word.setWordDescription(cursor.getString(1));
                    word.setHasLookedFor(cursor.getInt(2) != 0);
                    word.setEverShown(cursor.getInt(3));
                    dataSet.add(word);
                }
            }
            db.close();
            cursor.close();
        } catch (SQLException sqle) {
            Log.e(Constants.LOG_TAG, "database unavailable");
        }

        return dataSet;
    }

    public static SQLiteDatabase getWritableDB(Context context) {
        SQLiteDatabase db = null;
        try {
            db = getInstance(context).getWritableDatabase();
        } catch (SQLException sqlEx) {
            Log.e(Constants.LOG_TAG, "writable database unavailable");
        }
        return db;
    }

    public static SQLiteDatabase getReadableDB(Context context) {
        SQLiteDatabase db = null;
        try {
            db = getInstance(context).getReadableDatabase();
        } catch (SQLException sqlEx) {
            Log.e(Constants.LOG_TAG, "readable database unavailable");
        }
        return db;
    }
}
