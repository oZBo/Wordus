package braincollaboration.wordus;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import braincollaboration.wordus.adapter.WordAdapter;
import braincollaboration.wordus.adapter.WordAdapterCallback;
import braincollaboration.wordus.api.ABBYYLingvoAPI;
import braincollaboration.wordus.api.Controller;
import braincollaboration.wordus.api.JsonResponseNodeTypeDecryption;
import braincollaboration.wordus.api.modelSearch.MeaningOfTheWord;
import braincollaboration.wordus.background.DefaultBackgroundCallback;
import braincollaboration.wordus.dialog.SearchDialog;
import braincollaboration.wordus.dialog.SearchDialogCallback;
import braincollaboration.wordus.manager.DatabaseManager;
import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.utils.Constants;
import braincollaboration.wordus.view.BottomScreenBehavior;
import braincollaboration.wordus.view.HidingScrollRecyclerViewListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView wordsRecycleView;
    private FloatingActionButton fab;
    private List<Word> mDataSet;
    private WordAdapter adapter;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private TextView topText; //TODO please be more informative with view names
    private TextView bottomText; //TODO please be more informative with view names
    private WordAdapterCallback wordAdapterCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        getDataSet();
        bottomScreenBehavior();
        initRetrofit();
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
        DatabaseManager.getInstance().getWordsList(new DefaultBackgroundCallback<List<Word>>() {
            @Override
            public void doOnSuccess(List<Word> result) {
                initRecyclerView(result);
            }
        });
    }

    private void initRecyclerView(List<Word> dataSet) {
        mDataSet = dataSet;
        adapter = new WordAdapter(R.layout.header_separator, this, wordAdapterCallback);
        adapter.setItemList(dataSet);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        //set white divide line between rv items
        wordsRecycleView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //TODO check why it is deprecated and use new one.
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

    private void initRetrofit() {
        ABBYYLingvoAPI abbyyLingvoAPI = Controller.getInstance();

        Call<MeaningOfTheWord> myCall = abbyyLingvoAPI.getWordMeaning();

        myCall.enqueue(new Callback<MeaningOfTheWord>() {
            @Override
            public void onResponse(Call<MeaningOfTheWord> call, Response<MeaningOfTheWord> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Log.e(Constants.LOG_TAG, "search response is success");

                    new JsonResponseNodeTypeDecryption<Response>(response);
                    Log.e(Constants.LOG_TAG, JsonResponseNodeTypeDecryption.wordMeaning.toString());
                } else {
                    Log.e(Constants.LOG_TAG, "search response isn't successful");
                }
            }

            @Override
            public void onFailure(Call<MeaningOfTheWord> call, Throwable t) {
                Log.e(Constants.LOG_TAG, "search response failure error " + t.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        SearchDialog searchDialog = new SearchDialog(new SearchDialogCallback() {
            @Override
            public void findAWord(Editable text) {
                if (!text.toString().equals("")) {
                    addWord(checkIsThisALetters(text.toString()));
                } else {
                    Toast.makeText(MainActivity.this, R.string.empty_word_error, Toast.LENGTH_SHORT).show();
                }
            }
        }, this);
        searchDialog.showDialog();
    }

    private void addWord(final String word) {
        if (word.compareTo("") != 0) {
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
        } else {
            Toast.makeText(WordusApp.getCurrentActivity(), R.string.empty_word_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void addWordToListView(String word) {
        Word wordClass = new Word();
        wordClass.setWordName(word);
        mDataSet.add(wordClass);
        adapter.refreshAWordList(mDataSet);
    }

    //TODO remove to util class or in adapter
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

            @Override //TODO rename callbacks name to more understandable
            public void setTopText(String s) {
                topText.setText(s);
            }

            @Override //TODO rename callbacks name to more understandable
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
