package braincollaboration.wordus.adapter;


import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import braincollaboration.wordus.R;
import braincollaboration.wordus.model.Word;

public class WordAdapter extends SectionedAdapterBase<Word> {

    public WordAdapter(@LayoutRes int layoutResID) {
        setCustomHeaderLayout(layoutResID);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, Word item, @ViewType int viewType) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.wordName.setText(item.getWordName());
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, @ViewType int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordName;
        Button deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            wordName = (TextView) itemView.findViewById(R.id.recycler_item_headline_text);
            deleteButton = (Button) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
        }
    }

}
