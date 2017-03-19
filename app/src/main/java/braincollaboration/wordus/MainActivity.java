package braincollaboration.wordus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;

import braincollaboration.wordus.adapter.WordAdapter;
import braincollaboration.wordus.api.ApiClient;
import braincollaboration.wordus.api.ApiInterface;
import braincollaboration.wordus.model.Word;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    RecyclerView wordsListView;
    Button apiCallButton;
    ArrayList<Word> wordsList = new ArrayList<>();
    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillStubList();
        initWidgets();
    }

    private void initWidgets(){
        apiCallButton = (Button) findViewById(R.id.btn_api_call);
        apiCallButton.setOnClickListener(this);
        wordsListView = (RecyclerView) findViewById(R.id.recycler_view);
        wordsListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        WordAdapter adapter = new WordAdapter();
        adapter.setItemList(wordsList);
        wordsListView.setAdapter(adapter);
    }

    private void fillStubList(){
        for(int i = 0; i < 50; i++){
            Word word = new Word();
            word.setName("Wasd");
            word.setDescription("Wasd");
            if(i >= 10){
               word.setName("Aaaa");
            } if(i >= 20){
                word.setName("bbbbb");
            } if(i >= 30){
                word.setName("Ccccc");
            } if(i >= 40){
                word.setName("Ddddd");
            }
            wordsList.add(word);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_api_call:
                Call<String> call = apiService.getBearerByToken("Basic NmI1OWY1MjYtYWNiYy00OTQyLTkxMTAtMWRmZWY3ZTZkMDlmOjViOWYyMjExYWZlNTQ2NzBhODQ5MWMwMzRlZDExMjQ5");
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("API_TAG", response.message());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("API_TAG", t.getMessage());
                    }
                });
                break;
        }
    }
}
