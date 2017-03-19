package braincollaboration.wordus.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import braincollaboration.wordus.R;
import braincollaboration.wordus.model.Word;

public class WordAdapter extends SectionedAdapterBase<Word> {

    public WordAdapter(){
        setCustomHeaderLayout(R.layout.section_header);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, Word item, @ViewType int viewType) {
        WordViewHolder myHolder = (WordViewHolder) holder;
        myHolder.wordName.setText(item.getWordName());
        myHolder.wordDescription.setText(item.getWordDescription());
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, @ViewType int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false);
        return new WordViewHolder(itemView);
    }

    private class WordViewHolder extends RecyclerView.ViewHolder {

        TextView wordName;
        TextView wordDescription;

        WordViewHolder(View itemView) {
            super(itemView);
            wordName = (TextView) itemView.findViewById(R.id.item_word_text_view);
            wordDescription = (TextView) itemView.findViewById(R.id.item_word_description);
        }
    }

}
