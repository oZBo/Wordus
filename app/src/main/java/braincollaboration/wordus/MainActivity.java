package braincollaboration.wordus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import braincollaboration.wordus.adapter.WordAdapter;
import braincollaboration.wordus.asyncTask.AddWordInDBCallback;
import braincollaboration.wordus.asyncTask.AddWordInDBTask;
import braincollaboration.wordus.asyncTask.GetDataSetCallback;
import braincollaboration.wordus.asyncTask.GetDataSetTask;
import braincollaboration.wordus.dialog.SearchDialog;
import braincollaboration.wordus.dialog.SearchDialogCallback;
import braincollaboration.wordus.model.Word;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView wordsRecycleView;
    private FloatingActionButton fab;

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
        WordAdapter adapter = new WordAdapter(R.layout.header_separator);
        adapter.setItemList(dataSet);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        wordsRecycleView.setLayoutManager(mLayoutManager);
        wordsRecycleView.setAdapter(adapter);
        wordsRecycleView.setItemAnimator(itemAnimator);
    }

    private void getDataSet() {

        GetDataSetTask getDataSetTask = new GetDataSetTask(MainActivity.this, new GetDataSetCallback() {
            @Override
            public void returnDataSet(List<Word> dataSet) {
                initRecyclerView(dataSet);
            }
        });
        getDataSetTask.execute();

    }

    private void bottomScreenBehavior() {
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

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
