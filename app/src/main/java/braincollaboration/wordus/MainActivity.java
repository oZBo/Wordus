package braincollaboration.wordus;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import braincollaboration.wordus.RecyclerView.RecyclerAdapter;
import braincollaboration.wordus.SQLite.WordusDatabaseHelper;
import braincollaboration.wordus.utils.IsDBContainAWord;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView wordsListView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        initRecyclerView();
        bottomScreenBehavior();
    }

    private void initRecyclerView() {
        // если мы уверены, что изменения в контенте не изменят размер layout-а RecyclerView
        // передаем параметр true - это увеличивает производительность
        wordsListView.setHasFixedSize(true);

        // используем linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        wordsListView.setLayoutManager(mLayoutManager);

        // создаем адаптер
        ArrayList<String> myDataSet = getDataSet();
        mAdapter = new RecyclerAdapter(myDataSet);
        wordsListView.setAdapter(mAdapter);
    }

    private ArrayList<String> getDataSet() {
        ArrayList<String> dataSet = new ArrayList<>();
        try {
            WordusDatabaseHelper wordusDatabaseHelper = new WordusDatabaseHelper(this);
            SQLiteDatabase db = wordusDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query (WordusDatabaseHelper.TABLE_NAME,
                    new String[] {WordusDatabaseHelper.COLUMN_NAME},
                    null, null, null, null, null);
            if (cursor.moveToFirst()) {
                dataSet.add(cursor.getString(0));
                while (cursor.moveToNext()) {
                    dataSet.add(cursor.getString(0));
                }
            }
            db.close();
            cursor.close();
        } catch (SQLException sqle) {
            Toast.makeText(this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
        }

        return dataSet;
    }

    private void bottomScreenBehavior() {
        // get view of the bottom screen
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        // set of the bottom screen behavior
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });

        // set of the bottom screen state
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void initWidgets(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        wordsListView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void findAWord(Editable text) {
        if (!text.toString().equals("")) {
            addInDB(text.toString());
        } else {
            Toast.makeText(MainActivity.this, R.string.empty_word_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void addInDB(String s) {
        try {
            WordusDatabaseHelper wordusDatabaseHelper = new WordusDatabaseHelper(this);
            SQLiteDatabase db = wordusDatabaseHelper.getWritableDatabase();

            // checks is database contains current Word
            if(!IsDBContainAWord.isDBContainAWord(db, new String[] {s})) {
                ContentValues wordNameValue = WordusDatabaseHelper.makeWordValue(null, WordusDatabaseHelper.COLUMN_NAME, s);
                WordusDatabaseHelper.insertWord(db, wordNameValue);
                Toast.makeText(this, R.string.word_added, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.word_already_contain, Toast.LENGTH_SHORT).show();
            }
            db.close();
        } catch (SQLException sqle) {
            Toast.makeText(this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        showSearchDialog();
    }

    private void showSearchDialog() {
        Context context = this;

        // get our search_dialog layout
        LayoutInflater li = LayoutInflater.from(context);
        View searchView = li.inflate(R.layout.search_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        // set the dialog view with our layout.xml
        dialogBuilder.setView(searchView);

        final EditText userInput = (EditText) searchView.findViewById(R.id.input_text);

        dialogBuilder
                .setCancelable(true)
                .setPositiveButton("Поиск",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                findAWord(userInput.getText());
                            }
                        });

        AlertDialog dialog = dialogBuilder.create();

        // to show soft-key by opening
        try {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        } catch (NullPointerException e){}

        dialog.show();
    }
}
