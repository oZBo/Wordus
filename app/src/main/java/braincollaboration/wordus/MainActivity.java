package braincollaboration.wordus;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import java.util.List;

import braincollaboration.wordus.adapter.RecyclerAdapter;
import braincollaboration.wordus.SQLite.WordusDatabaseHelper;
import braincollaboration.wordus.asyncTask.AddWordInDBCallback;
import braincollaboration.wordus.asyncTask.AddWordInDBTask;
import braincollaboration.wordus.asyncTask.GetDataSetCallback;
import braincollaboration.wordus.asyncTask.GetDataSetTask;
import braincollaboration.wordus.dialog.SearchDialog;
import braincollaboration.wordus.dialog.SearchDialogCallback;
import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.utils.Constants;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView wordsRecycleView;
    private FloatingActionButton fab;
    private List<Word> mDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        getDataSet();
        bottomScreenBehavior();
    }

    private void initWidgets() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        wordsRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void initRecyclerView(List<Word> dataSet) {

        // creates items for recyclerView as Word model objects
        RecyclerView.Adapter mAdapter = new RecyclerAdapter(dataSet, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        wordsRecycleView.setLayoutManager(mLayoutManager);
        wordsRecycleView.setAdapter(mAdapter);
        wordsRecycleView.setItemAnimator(itemAnimator);
    }

    private void getDataSet() {

        GetDataSetTask getDataSetTask = new GetDataSetTask(MainActivity.this, new GetDataSetCallback() {
            @Override
            public void returnDataSet(List<Word> dataSet) {
                mDataSet = dataSet;
                initRecyclerView(dataSet);
            }
        });
        getDataSetTask.execute();

    }

    private void bottomScreenBehavior() {
        // get view of the bottom screen
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        // set of the bottom screen behavior
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    fab.setEnabled(false);
                } else {
                    fab.setEnabled(true);
                }
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

    private void addInDB(final String text) {
        AddWordInDBTask addWordInDBTask = new AddWordInDBTask(MainActivity.this, new AddWordInDBCallback() {
            @Override
            public void dbIsUnavailable() {
                Toast.makeText(MainActivity.this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void dbContainDuplicate() {
                Toast.makeText(MainActivity.this, R.string.word_already_contains_in_db, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void wordWasAdded() {
                Toast.makeText(MainActivity.this, text + " " + getString(R.string.word_successfully_added_in_db), Toast.LENGTH_SHORT).show();
            }
        });
        addWordInDBTask.execute(text);
    }

    @Override
    public void onClick(View v) {
        SearchDialog searchDialog = new SearchDialog(new SearchDialogCallback() {
            @Override
            public void findAWord(Editable text) {
                if (!text.toString().equals("")) {
                    addInDB(text.toString());
                } else {
                    Toast.makeText(MainActivity.this, R.string.empty_word_error, Toast.LENGTH_SHORT).show();
                }
            }
        }, this);
        searchDialog.showDialog();
    }
}
