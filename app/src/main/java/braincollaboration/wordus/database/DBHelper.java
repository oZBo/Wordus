package braincollaboration.wordus.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import braincollaboration.wordus.model.Word;

/**
 * Created by braincollaboration on 19/03/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Words.db";
    private static final String TABLE_WORDS = "Words";
    private static final int DATABASE_VERSION = 1;

    private static final String KEY_WORD_ID = "id";
    private static final String KEY_WORD_NAME = "name";
    private static final String KEY_WORD_DESCRIPTION = "description";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_WORDS + "(" + KEY_WORD_ID + "INTEGER PRIMARY KEY," + KEY_WORD_NAME + " TEXT," + KEY_WORD_DESCRIPTION + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_WORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
            onCreate(sqLiteDatabase);
        }
    }

    public void addWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WORD_NAME, word.getName());
        values.put(KEY_WORD_DESCRIPTION, word.getDescription());

        db.insert(TABLE_WORDS, null, values);
        db.close();
    }

    public Word getWord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORDS, new String[]{KEY_WORD_ID, KEY_WORD_NAME, KEY_WORD_DESCRIPTION}, KEY_WORD_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Word word = new Word();
        word.setId(Integer.parseInt(cursor.getString(0)));
        word.setName(cursor.getString(1));
        word.setDescription(cursor.getString(2));

        return word;
    }

    public List<Word> getAllWords() {
        List<Word> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WORDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setId(Integer.parseInt(cursor.getString(0)));
                word.setName(cursor.getString(1));
                word.setDescription(cursor.getString(2));
                // Adding contact to list
                contactList.add(word);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;
    }

    public int getWordsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    public int updateWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WORD_NAME, word.getName());
        values.put(KEY_WORD_DESCRIPTION, word.getDescription());

        // updating row
        return db.update(TABLE_WORDS, values, KEY_WORD_ID + " = ?",
                new String[]{String.valueOf(word.getId())});
    }

    public void deleteWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORDS, KEY_WORD_ID + " = ?",
                new String[] { String.valueOf(word.getId()) });
        db.close();
    }

}
