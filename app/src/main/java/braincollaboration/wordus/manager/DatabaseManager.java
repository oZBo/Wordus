package braincollaboration.wordus.manager;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.List;

import braincollaboration.wordus.SQLite.WordusDatabaseHelper;
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
        IBackgroundTask<List<Word>> loadWordsTask = new IBackgroundTask<List<Word>>() {
            @Override
            public List<Word> execute() {
                return WordusDatabaseHelper.getDataSet(WordusApp.getCurrentActivity().getApplicationContext());
            }
        };
        BackgroundManager.getInstance().doUiBlockingBackgroundTask(loadWordsTask, callback);
    }

    public void addWordInDB(final String word, DefaultBackgroundCallback<Boolean> callback) {
        IBackgroundTask<Boolean> backgroundTask = new IBackgroundTask<Boolean>() {
            @Override
            public Boolean execute() {
                SQLiteDatabase db = WordusDatabaseHelper.getReadableDB(WordusApp.getCurrentActivity().getApplicationContext());
                if (db != null && !WordusDatabaseHelper.isDBContainAWord(db, word)) {
                    db = WordusDatabaseHelper.getWritableDB(WordusApp.getCurrentActivity().getApplicationContext());
                    if (db != null) {
                        WordusDatabaseHelper.addInDB(db, word);
                        return true;
                    }
                }
                return false;
            }
        };
        BackgroundManager.getInstance().doUiBlockingBackgroundTask(backgroundTask, callback);
    }

    public void deleteWord(final Word word, DefaultBackgroundCallback<Void> callback) {
        IBackgroundTask<Void> backgroundTask = new IBackgroundTask<Void>() {
            @Override
            public Void execute() {
                SQLiteDatabase db = WordusDatabaseHelper.getWritableDB(WordusApp.getCurrentActivity().getApplicationContext());
                if (db != null) {
                    WordusDatabaseHelper.deleteWord(db, word.getWordName());
                }
                return null;
            }
        };
        BackgroundManager.getInstance().doUiBlockingBackgroundTask(backgroundTask, callback);
    }

}