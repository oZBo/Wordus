package braincollaboration.wordus.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import braincollaboration.wordus.R;
import braincollaboration.wordus.WordusApp;
import braincollaboration.wordus.adapter.IWordAdapterCallback;
import braincollaboration.wordus.adapter.WordAdapter;
import braincollaboration.wordus.api.JsonResponseNodeTypeDecryption;
import braincollaboration.wordus.background.DefaultBackgroundCallback;
import braincollaboration.wordus.manager.DatabaseManager;
import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.utils.CheckForLetters;
import braincollaboration.wordus.utils.Constants;
import braincollaboration.wordus.view.RecyclerViewWithFAB;
import braincollaboration.wordus.view.bottomsheet.BottomScreenBehavior;
import braincollaboration.wordus.view.dialog.ConfirmationDialog;
import braincollaboration.wordus.view.dialog.base.DefaultDialogCallback;
import braincollaboration.wordus.view.dialog.TextInputDialog;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, IWordAdapterCallback {

    private RecyclerViewWithFAB recyclerView;
    private FloatingActionButton fab;
    private List<Word> mDataSet;
    private WordAdapter adapter;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDataFromDB();
    }

    private void initWidgets() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = (RecyclerViewWithFAB) findViewById(R.id.recycler_view);
    }

    private void loadDataFromDB() {
        DatabaseManager.getInstance().getWordsList(new DefaultBackgroundCallback<List<Word>>() {
            @Override
            public void doOnSuccess(List<Word> result) {
                initWidgets();
                configureBottomSheet();
                initRecyclerView(result);
                initRetrofit();
            }
        });
    }

    private void initRecyclerView(List<Word> dataSet) {
        mDataSet = dataSet;
        adapter = new WordAdapter(this, R.layout.header_separator, dataSet, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private void configureBottomSheet() {
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomScreenBehavior(fab));
    }

    private void initRetrofit() {
        new JsonResponseNodeTypeDecryption().parse(Constants.RESPONSE);
    }

    @Override
    public void onClick(View v) {
        TextInputDialog inputDialog = new TextInputDialog(MainActivity.this, new DefaultDialogCallback<String>() {
            @Override
            public void onPositiveButtonClickedWithResult(String s) {
                if (!s.equals("")) {
                    addWord(CheckForLetters.checkIsThisALetters(s));
                } else {
                    Toast.makeText(MainActivity.this, R.string.empty_word_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        inputDialog.show();
//        SearchDialog searchDialog = new SearchDialog(new SearchDialogCallback() {
//            @Override
//            public void findAWord(Editable text) {
//                if (!text.toString().equals("")) {
//                    addWord(CheckForLetters.checkIsThisALetters(text.toString()));
//                } else {
//                    Toast.makeText(MainActivity.this, R.string.empty_word_error, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }, this);
//        searchDialog.showDialog();
    }

    private void addWord(final String word) {
        DatabaseManager.getInstance().addWordInDB(word, new DefaultBackgroundCallback<Boolean>() {
            @Override
            public void doOnSuccess(Boolean result) {
                if (result) {
                    addWordToListView(word);
                    Toast.makeText(WordusApp.getCurrentActivity(), getString(R.string.word_) + " " + word + " " + getString(R.string._successfully_added_in_db), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WordusApp.getCurrentActivity(), R.string.word_already_contains_in_db, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addWordToListView(String word) {
        Word wordClass = new Word();
        wordClass.setWordName(word);
        mDataSet.add(wordClass);
        adapter.refreshWordList(mDataSet);
    }

    @Override
    public void onItemClicked(Word word) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onItemDeleteButtonClicked(final Word word) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(MainActivity.this, new DefaultDialogCallback() {
            @Override
            public void onPositiveButtonClicked() {
                DatabaseManager.getInstance().deleteWord(word, new DefaultBackgroundCallback<Void>() {
                    @Override
                    public void doOnSuccess(Void result) {
                    }
                });
            }
        });
        confirmationDialog.show();

    }
}
