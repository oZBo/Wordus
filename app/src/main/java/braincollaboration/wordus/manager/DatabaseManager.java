package braincollaboration.wordus.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.List;

import braincollaboration.wordus.database.WordusDatabaseHelper;
import braincollaboration.wordus.WordusApp;
import braincollaboration.wordus.background.BackgroundManager;
import braincollaboration.wordus.background.DefaultBackgroundCallback;
import braincollaboration.wordus.background.IBackgroundTask;
import braincollaboration.wordus.model.Word;

/**
 * Created by evhenii on 13.07.17.
 */

public class DatabaseManager {

    private static final DatabaseManager ourInstance = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return ourInstance;
    }

    private DatabaseManager() {
    }

    public void getWordsList(@NonNull DefaultBackgroundCallback<List<Word>> callback) {
        BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<List<Word>>() {

            @Override
            public List<Word> execute() {
                return WordusDatabaseHelper.getDataSet(WordusApp.getCurrentActivity().getApplicationContext());
            }
        }, callback);

    }

    public void getNotFoundWordsList(final Context context, @NonNull DefaultBackgroundCallback<List<Word>> callback) {
        BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<List<Word>>() {

            @Override
            public List<Word> execute() {
                return WordusDatabaseHelper.getNotFoundWordDataSet(context);
            }
        }, callback);

    }

    public void addWordNameInDB(final Word word, DefaultBackgroundCallback<Boolean> callback) {
        BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<Boolean>() {

            @Override
            public Boolean execute() {
                SQLiteDatabase db = WordusDatabaseHelper.getReadableDB(WordusApp.getCurrentActivity().getApplicationContext());
                if (db != null && !WordusDatabaseHelper.isDBContainAWord(db, word.getWordName())) {
                    db = WordusDatabaseHelper.getWritableDB(WordusApp.getCurrentActivity().getApplicationContext());
                    if (db != null) {
                        WordusDatabaseHelper.addWordNameInDB(db, word.getWordName(), word.getHasLookedFor());
                        return true;
                    }
                }
                return false;
            }
        }, callback);
    }

    public void addWordDescriptionInDB(final Context context, final Word word, DefaultBackgroundCallback<Boolean> callback) {
        BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<Boolean>() {

            @Override
            public Boolean execute() {
                SQLiteDatabase db = WordusDatabaseHelper.getReadableDB(context);
                if (db != null && WordusDatabaseHelper.isDBContainAWord(db, word.getWordName())) {
                    db = WordusDatabaseHelper.getWritableDB(context);
                    if (db != null) {
                        WordusDatabaseHelper.addWordDescriptionInDB(db, word.getWordName(), word.getWordDescription(), word.getHasLookedFor());
                        return true;
                    }
                }
                return false;
            }
        }, callback);
    }

    public void deleteWord(final Word word, DefaultBackgroundCallback<Void> callback) {
        BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<Void>() {

            @Override
            public Void execute() {
                SQLiteDatabase db = WordusDatabaseHelper.getWritableDB(WordusApp.getCurrentActivity().getApplicationContext());
                if (db != null) {
                    WordusDatabaseHelper.deleteWord(db, word.getWordName());
                }
                return null;
            }
        }, callback);
    }

}