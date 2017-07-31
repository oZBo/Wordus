package braincollaboration.wordus.adapter;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
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
    private List<Word> wordsList;

    public WordAdapter(Context context, @LayoutRes int layoutResID) {
        this(context, layoutResID, new ArrayList<Word>());
    }

    public WordAdapter(Context context, @LayoutRes int layoutResId, List<Word> wordsList) {
        setCustomHeaderLayout(layoutResId);
        this.context = context;
        this.wordsList = wordsList;
        super.setItemList(wordsList);
    }

    public void refreshAWordList(List<Word> words) {
        super.setItemList(words);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, Word item, @ViewType int viewType) {

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.wordName.setText(item.getWordName());

        if (item.getWordDescription() == null) {
            viewHolder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.unFoundDescriptionColor));
        }

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //YOUR ACTION HERE
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
        private RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            wordName = (TextView) itemView.findViewById(R.id.recycler_item_headline_text);
            deleteButton = (Button) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.recycler_item_relativeLayout);
        }
    }
}
