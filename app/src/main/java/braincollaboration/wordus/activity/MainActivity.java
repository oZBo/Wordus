package braincollaboration.wordus.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import braincollaboration.wordus.R;
import braincollaboration.wordus.WordusApp;
import braincollaboration.wordus.adapter.IWordAdapterCallback;
import braincollaboration.wordus.adapter.WordAdapter;
import braincollaboration.wordus.api.JsonResponseNodeTypeDecryption;
import braincollaboration.wordus.background.DefaultBackgroundCallback;
import braincollaboration.wordus.view.dialog.SearchDialog;
import braincollaboration.wordus.view.dialog.SearchDialogCallback;
import braincollaboration.wordus.manager.DatabaseManager;
import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.utils.CheckForLetters;
import braincollaboration.wordus.utils.Constants;
import braincollaboration.wordus.utils.DebugWordListUtil;
import braincollaboration.wordus.view.bottomsheet.BottomScreenBehavior;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, IWordAdapterCallback {

    private RecyclerView wordsRecycleView;
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
        wordsRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
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
        adapter = new WordAdapter(this, R.layout.header_separator, DebugWordListUtil.getStubWordList(), this);
        wordsRecycleView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        wordsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        wordsRecycleView.setItemAnimator(new DefaultItemAnimator());
        wordsRecycleView.setAdapter(adapter);
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
        new JsonResponseNodeTypeDecryption().parse(Constants.RESPONSE_PICTURE);
    }

    @Override
    public void onClick(View v) {
        SearchDialog searchDialog = new SearchDialog(new SearchDialogCallback() {
            @Override
            public void findAWord(Editable text) {
                if (!text.toString().equals("")) {
                    addWord(CheckForLetters.checkIsThisALetters(text.toString()));
                } else {
                    Toast.makeText(MainActivity.this, R.string.empty_word_error, Toast.LENGTH_SHORT).show();
                }
            }
        }, this);
        searchDialog.showDialog();
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
        adapter.refreshAWordList(mDataSet);
    }

    @Override
    public void onItemClicked(Word word) {
        Toast.makeText(this, word.getWordName() + " clicked", Toast.LENGTH_SHORT).show();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onItemDeleteButtonClicked(Word word) {
        Toast.makeText(this, word.getWordName() + " deleted", Toast.LENGTH_SHORT).show();
        DatabaseManager.getInstance().deleteWord(word);
    }
}
