package braincollaboration.wordus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import braincollaboration.wordus.adapter.WordAdapter;
import braincollaboration.wordus.model.Word;

public class MainActivity extends AppCompatActivity {

    RecyclerView wordsListView;
    ArrayList<Word> words = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillStubList();
        initWidgets();
    }

    private void initWidgets() {
        wordsListView = (RecyclerView) findViewById(R.id.recycler_view);
        wordsListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        WordAdapter adapter = new WordAdapter();
        wordsListView.setAdapter(adapter);
        adapter.setItemList(words);
    }

    private void fillStubList() {
        for (int i = 0; i < 50; i++) {
            Word word = new Word();
            word.setWordName("Aaa");
            if (i >= 10) {
                word.setWordName("Bbbb");
            }
            if (i >= 20) {
                word.setWordName("Gggg");
            }
            if (i >= 30) {
                word.setWordName("Hhhhh");
            }
            if (i >= 40) {
                word.setWordName("Ooo");
            }
            words.add(word);
        }
    }
}
