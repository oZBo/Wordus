package braincollaboration.wordus;

import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import braincollaboration.wordus.adapter.WordAdapter;
import braincollaboration.wordus.adapter.WordAdapterCallback;
import braincollaboration.wordus.asyncTask.AddWordInDBCallback;
import braincollaboration.wordus.asyncTask.AddWordInDBTask;
import braincollaboration.wordus.asyncTask.CheckWordDuplicateCallback;
import braincollaboration.wordus.asyncTask.CheckWordDuplicateTask;
import braincollaboration.wordus.asyncTask.GetDataSetCallback;
import braincollaboration.wordus.asyncTask.GetDataSetTask;
import braincollaboration.wordus.dialog.SearchDialog;
import braincollaboration.wordus.dialog.SearchDialogCallback;
import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.view.BottomScreenBehavior;
import braincollaboration.wordus.view.HidingScrollRecyclerViewListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView wordsRecycleView;
    private FloatingActionButton fab;
    private List<Word> mDataSet;
    private WordAdapter adapter;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private TextView topText;
    private TextView bottomText;
    private WordAdapterCallback wordAdapterCallback;

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

        topText = (TextView) findViewById(R.id.bottom_sheet_top_text);
        bottomText = (TextView) findViewById(R.id.bottom_sheet_bottom_text);

        initAdapterCallback();
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

    private void initRecyclerView(List<Word> dataSet) {
        mDataSet = dataSet;
        adapter = new WordAdapter(R.layout.header_separator, this, wordAdapterCallback);
        adapter.setItemList(dataSet);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        //set white divide line between rv items
        wordsRecycleView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        wordsRecycleView.setOnScrollListener(new HidingScrollRecyclerViewListener() {
            @Override
            public void onHide() {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            @Override
            public void onShow() {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        wordsRecycleView.setLayoutManager(mLayoutManager);
        wordsRecycleView.setAdapter(adapter);
        wordsRecycleView.setItemAnimator(itemAnimator);
    }

    private void bottomScreenBehavior() {
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomScreenBehavior(fab));
    }

    @Override
    public void onClick(View v) {
        SearchDialog searchDialog = new SearchDialog(new SearchDialogCallback() {
            @Override
            public void findAWord(Editable text) {
                if (!text.toString().equals("")) {
                    checkDuplicateAndThenAddInDB(text.toString());
                } else {
                    Toast.makeText(MainActivity.this, R.string.empty_word_error, Toast.LENGTH_SHORT).show();
                }
            }
        }, this);
        searchDialog.showDialog();
    }

    private void checkDuplicateAndThenAddInDB(final String s) {
        final String text = checkIsThisALetters(s);

        CheckWordDuplicateTask checkWordDuplicateTask = new CheckWordDuplicateTask(MainActivity.this, new CheckWordDuplicateCallback() {
            @Override
            public void dbNotContainDuplicate() {
                addInDBFinal(text);
            }

            @Override
            public void dbContainDuplicate() {
                Toast.makeText(MainActivity.this, R.string.word_already_contains_in_db, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void dbIsUnavailable() {
                Toast.makeText(MainActivity.this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
            }
        });

        if (text.compareTo("") != 0) {
            checkWordDuplicateTask.execute(text);
        } else {
            Toast.makeText(MainActivity.this, R.string.empty_word_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void addInDBFinal(final String text) {
        AddWordInDBTask addWordInDBTask = new AddWordInDBTask(MainActivity.this, new AddWordInDBCallback() {
            @Override
            public void dbIsUnavailable() {
                Toast.makeText(MainActivity.this, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void wordWasAdded() {
                Toast.makeText(MainActivity.this, getString(R.string.word_) + " " + text + " " + getString(R.string._successfully_added_in_db), Toast.LENGTH_SHORT).show();
            }
        });
        addWordInDBTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, text);

        //adding word in RecyclerView
        Word word = new Word();
        word.setWordName(text);
        mDataSet.add(word);
        adapter.refreshAWordList(mDataSet);
    }

    private String checkIsThisALetters(String text) {
        char[] word = text.toCharArray();
        String upperWord = "";
        int notLetters = 0;
        // checks is a target word consist of letters
        for (char aWord : word) {
            if (!Character.isLetter(aWord)) {
                notLetters++;
            }
        }

        // makes first letter in to upper case
        if (notLetters == 0) {
            word[0] = Character.toUpperCase(word[0]);

            for (char aWord : word) {
                upperWord += Character.toString(aWord);
            }
        }
        return upperWord;
    }

    private void initAdapterCallback() {

        wordAdapterCallback = new WordAdapterCallback() {
            @Override
            public List<Word> deleteWordItem(Word word) {
                mDataSet.remove(word);
                return mDataSet;
            }

            @Override
            public void setTopText(String s) {
                topText.setText(s);
            }

            @Override
            public void setBottomText(String s) {
                bottomText.setText(s);
            }

            @Override
            public void changeBottomSheetBehaviorForExpanded() {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public List<Word> getDataSet() {
                return mDataSet;
            }
        };
    }
}
