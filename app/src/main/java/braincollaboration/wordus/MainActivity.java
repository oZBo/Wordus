package braincollaboration.wordus;

import android.database.sqlite.SQLiteDatabase;
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

import braincollaboration.wordus.SQLite.WordusDatabaseHelper;
import braincollaboration.wordus.adapter.WordAdapter;
import braincollaboration.wordus.adapter.WordAdapterCallback;
import braincollaboration.wordus.api.ABBYYLingvoAPI;
import braincollaboration.wordus.api.Controller;
import braincollaboration.wordus.api.modelSearch.Body;
import braincollaboration.wordus.api.modelSearch.Item;
import braincollaboration.wordus.api.modelSearch.Item_;
import braincollaboration.wordus.api.modelSearch.Markup;
import braincollaboration.wordus.api.modelSearch.Markup_;
import braincollaboration.wordus.api.modelSearch.Markup__;
import braincollaboration.wordus.api.modelSearch.MeaningOfTheWord;
import braincollaboration.wordus.background.BackgroundManager;
import braincollaboration.wordus.background.IBackgroundCallback;
import braincollaboration.wordus.background.IBackgroundTask;
import braincollaboration.wordus.dialog.SearchDialog;
import braincollaboration.wordus.dialog.SearchDialogCallback;
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
        // make dataSet
        BackgroundManager.getInstance().doBackgroundTask(
                new IBackgroundTask<List<Word>>() {
                    @Override
                    public List<Word> execute() {
                        return WordusDatabaseHelper.getDataSet(WordusApp.getCurrentActivity().getApplicationContext());
                    }
                },
                new IBackgroundCallback<List<Word>>() {
                    @Override
                    public void doOnSuccess(List<Word> dataSet) {
                        initRecyclerView(dataSet);
                    }

                    @Override
                    public void doOnError(Exception e) {
                        Toast.makeText(WordusApp.getCurrentActivity(), R.string.database_unavailable, Toast.LENGTH_SHORT).show();
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
                // not usable, just test code
                if (response.isSuccessful() && response.code() == 200) {
                    Log.e(Constants.LOG_TAG, "search response is success");

                    MeaningOfTheWord mainResponseClass = response.body();

                    List<Item> items = mainResponseClass.getItems();
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getDictionary().equals("Dahl (Ru-Ru)")) {
                            Log.e(Constants.LOG_TAG, items.get(i).getDictionary());

                            List<Body> bodies = items.get(i).getBody();
                            List<Markup> markup = bodies.get(0).getMarkup();
                            for (int b = 0; b < markup.size(); b++) {
                                Log.e(Constants.LOG_TAG, markup.get(b).getText());
                            }
                        } else if (items.get(i).getDictionary().equals("Explanatory (Ru-Ru)")) {
                            Log.e(Constants.LOG_TAG, items.get(i).getDictionary());

                            List<Item_> items_ = items.get(i).getBody().get(0).getItems();
                            for (int b = 0; b < items_.size(); b++) {
                                List<Markup_> markup_ = items_.get(b).getMarkup();
                                for (int c = 0; c < markup_.size(); c++) {
                                    if (markup_.get(c).getItems() == null) {
                                        List<Markup__> markup__ = markup_.get(c).getMarkup();
                                    }
                                }
                            }
                        }
                    }
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
                    checkDuplicateAndThenAddInDB(text.toString());
                } else {
                    Toast.makeText(MainActivity.this, R.string.empty_word_error, Toast.LENGTH_SHORT).show();
                }
            }
        }, this);
        searchDialog.showDialog();
    }

    private void checkDuplicateAndThenAddInDB(final String s) {
        final String word = checkIsThisALetters(s);

        if (word.compareTo("") != 0) {
            // check db word duplicate task
            BackgroundManager.getInstance().doBackgroundTask(
                    new IBackgroundTask<Boolean>() {
                        @Override
                        public Boolean execute() {
                            SQLiteDatabase db = WordusDatabaseHelper.getReadableDB(WordusApp.getCurrentActivity().getApplicationContext());
                            Boolean result = false;
                            if (db != null) {
                                result = WordusDatabaseHelper.isDBContainAWord(db, word);
                            }
                            return result;
                        }
                    },
                    new IBackgroundCallback<Boolean>() {
                        @Override
                        public void doOnSuccess(Boolean result) {
                            if (!result) { //TODO if false do valid statement? A bit strange...
                                addInDBFinal(word);
                            } else {
                                Toast.makeText(WordusApp.getCurrentActivity(), R.string.word_already_contains_in_db, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void doOnError(Exception e) {
                            Toast.makeText(WordusApp.getCurrentActivity(), R.string.database_unavailable, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(WordusApp.getCurrentActivity(), R.string.empty_word_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void addInDBFinal(final String word) {
        // add word in db task
        BackgroundManager.getInstance().doBackgroundTask(new IBackgroundTask<Boolean>() {
                                                             @Override
                                                             public Boolean execute() {
                                                                 SQLiteDatabase db = WordusDatabaseHelper.getWritableDB(WordusApp.getCurrentActivity().getApplicationContext());
                                                                 WordusDatabaseHelper.addInDB(db, word);
                                                                 return true;
                                                             }
                                                         },
                new IBackgroundCallback<Boolean>() {
                    @Override
                    public void doOnSuccess(Boolean result) {
                        Toast.makeText(WordusApp.getCurrentActivity(), getString(R.string.word_) + " " + word + " " + getString(R.string._successfully_added_in_db), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void doOnError(Exception e) {
                        Toast.makeText(WordusApp.getCurrentActivity(), R.string.database_unavailable, Toast.LENGTH_SHORT).show();
                    }
                });

        //adding word in RecyclerView
        //TODO I guess it would be better to add word in RV after it was saved into DB (After callback). Move this into  separate method.
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
