package braincollaboration.wordus.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

import braincollaboration.wordus.R;
import braincollaboration.wordus.adapter.IWordAdapterCallback;
import braincollaboration.wordus.adapter.WordAdapter;
import braincollaboration.wordus.background.DefaultBackgroundCallback;
import braincollaboration.wordus.background.broadcast.IInternetStatusCallback;
import braincollaboration.wordus.background.broadcast.InternetStatusBroadcastReceiver;
import braincollaboration.wordus.background.broadcast.InternetStatusGCM;
import braincollaboration.wordus.manager.DatabaseManager;
import braincollaboration.wordus.manager.RetrofitManager;
import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.utils.InternetUtil;
import braincollaboration.wordus.utils.MyNotification;
import braincollaboration.wordus.utils.StringUtils;
import braincollaboration.wordus.view.RecyclerViewWithFAB;
import braincollaboration.wordus.view.bottomsheet.BottomScreenBehavior;
import braincollaboration.wordus.view.dialog.ConfirmationDialog;
import braincollaboration.wordus.view.dialog.TextInputDialog;
import braincollaboration.wordus.view.dialog.base.DefaultDialogCallback;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, IWordAdapterCallback, IInternetStatusCallback {

    private RecyclerViewWithFAB recyclerView;
    private FloatingActionButton fab;
    private List<Word> mDataSet;
    private WordAdapter adapter;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private TextView wordDescriptionTextView;
    private TextView wordNameTextView;
    private InternetStatusBroadcastReceiver mBroadcastReceiver;

    private static int wordsSizeForNotification;
    private static int wordsCountForNotification;
    private static ArrayList<String> wordsListForNotification = new ArrayList<>();
    private static boolean isOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        loadDataFromDB();
    }

    private void loadDataFromDB() {
        DatabaseManager.getInstance().getWordsList(new DefaultBackgroundCallback<List<Word>>() {
            @Override
            public void doOnSuccess(List<Word> result) {
                if (mDataSet == null) {
                    initWidgets();
                    configureBottomSheet();
                    initBackgroundSearch();
                    initRecyclerView(result);
                } else {
                    adapter.refreshWordList(result);
                }
            }
        });
    }

    private void initWidgets() {
        wordDescriptionTextView = (TextView) findViewById(R.id.bottom_sheet_content_text);
        wordNameTextView = (TextView) findViewById(R.id.bottom_sheet_title_text);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = (RecyclerViewWithFAB) findViewById(R.id.recycler_view);
    }

    private void configureBottomSheet() {
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomScreenBehavior(fab));
    }

    private void initBackgroundSearch() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            initGCM();
        } else {
            initBroadcastReceiver();
        }
    }

    private void initGCM() {
        InternetStatusGCM.scheduleSync(this, this);
    }

    private void initBroadcastReceiver() {
        mBroadcastReceiver = new InternetStatusBroadcastReceiver(this);
        this.registerReceiver(mBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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

    @Override
    public void onClick(View v) {
        TextInputDialog inputDialog = new TextInputDialog(MainActivity.this, new DefaultDialogCallback<String>() {
            @Override
            public void onPositiveButtonClickedWithResult(String s) {
                addWord(StringUtils.checkIsThisALetters(s));
            }
        });
        inputDialog.show();
    }

    private void addWord(final String wordName) {
        if (!wordName.equals("")) {
            final Word word = new Word();
            word.setWordName(wordName);
            DatabaseManager.getInstance().addWordNameInDB(word, new DefaultBackgroundCallback<Boolean>() {
                @Override
                public void doOnSuccess(Boolean result) {
                    if (result) {
                        addWordToListView(word);
                        if (InternetUtil.isInternetTurnOn(MainActivity.this)) {
                            searchWordDescriptionRetrofit(word);
                        } else {
                            Toast.makeText(MainActivity.this, R.string.no_connection_now_will_be_found_later, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, R.string.word_already_contains_in_db, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, R.string.empty_word_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void addWordToListView(Word word) {
        //to not doubled already existed object (after added in db but before retrofit search)
        for (int i = 0; i < mDataSet.size(); i++) {
            Word w = mDataSet.get(i);
            if (word.getWordName().equals(w.getWordName())) {
                mDataSet.remove(w);
            }
        }
        mDataSet.add(word);
        adapter.refreshWordList(mDataSet);
    }

    @Override
    public void onItemClicked(Word word) {
        wordNameTextView.setText(word.getWordName());

        if (word.getWordDescription() == null) {
            if (!word.getHasLookedFor()) {
                wordDescriptionTextView.setText(getString(R.string.empty_word_description_not_found));
            } else {
                wordDescriptionTextView.setText(getString(R.string.empty_word_description_not_exist));
            }
        } else {
            wordDescriptionTextView.setText(word.getWordDescription());
        }

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
                        mDataSet.remove(word);
                        adapter.refreshWordList(mDataSet);
                    }
                });
            }
        });
        confirmationDialog.show();
    }

    private void searchWordDescriptionRetrofit(final Word word) {
        if (isOnPause) {
            wordsCountForNotification++;
        }
        RetrofitManager.getInstance().searchWordDescription(word, new DefaultBackgroundCallback<Word>() {
            @Override
            public void doOnSuccess(Word result) {
                if (result != null) {
                    addWordDescriptionInDB(result);

                    if (isOnPause) {
                        wordsListForNotification.add(result.getWordName());
                        if (wordsCountForNotification == wordsSizeForNotification) {
                            notifyCauseOnPause();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.no_connection_now_will_be_found_later), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void notifyCauseOnPause() {
        MyNotification.sendNotification(MainActivity.this, wordsListForNotification, wordsSizeForNotification);
        wordsCountForNotification = 0;
        wordsSizeForNotification = 0;
        wordsListForNotification = new ArrayList<>();

    }

    private void addWordDescriptionInDB(final Word word) {
        DatabaseManager.getInstance().addWordDescriptionInDB(MainActivity.this, word, new DefaultBackgroundCallback<Boolean>() {
            @Override
            public void doOnSuccess(Boolean result) {
                if (result) {
                    addWordToListView(word);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregisters BroadcastReceiver when app is destroyed.
        if (mBroadcastReceiver != null) {
            this.unregisterReceiver(mBroadcastReceiver);
        }
    }

    @Override
    public void internetIsOn() {
        // to search words which have been added without internet
        DatabaseManager.getInstance().getNotFoundWordsList(MainActivity.this, new DefaultBackgroundCallback<List<Word>>() {
            @Override
            public void doOnSuccess(List<Word> result) {
                if (!result.isEmpty()) {
                    if (isOnPause) {
                        wordsSizeForNotification = result.size();
                    }
                    for (Word word : result) {
                        searchWordDescriptionRetrofit(word);
                    }
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        isOnPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        isOnPause = false;
    }

    private void cancelGCMTask() {
        InternetStatusGCM.cancelGCMTask(this);
    }
}
