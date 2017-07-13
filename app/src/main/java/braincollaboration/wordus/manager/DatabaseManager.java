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
                SQLiteDatabase readableDB = WordusDatabaseHelper.getReadableDB(WordusApp.getCurrentActivity().getApplicationContext());
                if (readableDB != null && WordusDatabaseHelper.isDBContainAWord(readableDB, word)) {
                    SQLiteDatabase writableDB = WordusDatabaseHelper.getWritableDB(WordusApp.getCurrentActivity().getApplicationContext());
                    WordusDatabaseHelper.addInDB(writableDB, word);
                    return true;
                }
                return false;
            }
        };
        BackgroundManager.getInstance().doUiBlockingBackgroundTask(backgroundTask, callback);
    }

}
