package braincollaboration.wordus.adapter;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import braincollaboration.wordus.R;
import braincollaboration.wordus.model.Word;

public class WordAdapter extends SectionedAdapterBase<Word> {

    private Context context;
    private IWordAdapterCallback actionsCallback;
    private List<Word> wordsList = new ArrayList<>();

    public WordAdapter(Context context, @LayoutRes int layoutResId, List<Word> wordList, @NonNull IWordAdapterCallback callback) {
        super.setItemList(wordList);
        this.context = context;
        this.actionsCallback = callback;
        this.wordsList = wordList;
        setCustomHeaderLayout(layoutResId);
    }

    public void refreshWordList(List<Word> words) {
        wordsList = words;
        super.setItemList(words);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final Word item, @ViewType int viewType) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.wordName.setText(item.getWordName());

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionsCallback.onItemClicked(item);
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordsList.remove(item);
                WordAdapter.super.setItemList(wordsList);
                actionsCallback.onItemDeleteButtonClicked(item);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, @ViewType int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView wordName;
        private Button deleteButton;
        private RelativeLayout rootView;

        ViewHolder(View itemView) {
            super(itemView);
            wordName = (TextView) itemView.findViewById(R.id.recycler_item_headline_text);
            deleteButton = (Button) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
            rootView = (RelativeLayout) itemView.findViewById(R.id.recycler_item_relativeLayout);
        }
    }
}
