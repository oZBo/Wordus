package braincollaboration.wordus.adapter;


import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import braincollaboration.wordus.R;
import braincollaboration.wordus.WordusApp;
import braincollaboration.wordus.model.Word;
import braincollaboration.wordus.utils.Constants;

public class WordAdapter extends SectionedAdapterBase<Word> {

    private IWordAdapterCallback actionsCallback;

    public WordAdapter(@LayoutRes int layoutResId, List<Word> wordList, @NonNull IWordAdapterCallback callback) {
        super.setItemList(wordList);
        this.actionsCallback = callback;
        setCustomHeaderLayout(layoutResId);
    }

    public void refreshWordList(List<Word> words) {
        super.setItemList(words);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final Word item, @ViewType int viewType) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.wordName.setText(item.getWordName());

        Typeface face;
        int wordNameColor;
        int wordStateColor;

        // not opened new words
        if (item.getEverShown() == 1) {
            wordNameColor = WordusApp.getCurrentActivity().getResources().getColor(R.color.almostBlackColorText);
            face = Typeface.createFromAsset(WordusApp.getCurrentActivity().getApplicationContext().getAssets(), Constants.CUSTOM_FONT_BOLD);
        } else {
            wordNameColor = WordusApp.getCurrentActivity().getResources().getColor(R.color.mainColorOfText);
            face = Typeface.createFromAsset(WordusApp.getCurrentActivity().getApplicationContext().getAssets(), Constants.CUSTOM_FONT_REGULAR);
        }

        if (item.getWordDescription() == null) {
            wordStateColor = WordusApp.getCurrentActivity().getResources().getColor(R.color.noDescriptionColor);
            // to apply font to unfounded words
            if (item.getHasLookedFor()) {
                face = Typeface.createFromAsset(WordusApp.getCurrentActivity().getApplicationContext().getAssets(), Constants.CUSTOM_FONT_LIGHT);
            }
        } else {
            wordStateColor = WordusApp.getCurrentActivity().getResources().getColor(R.color.hasDescriptionColor);
        }

        viewHolder.wordStateLabel.setBackgroundColor(wordStateColor);
        viewHolder.wordName.setTextColor(wordNameColor);
        viewHolder.wordName.setTypeface(face);

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionsCallback.onItemClicked(item);
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        private ImageButton deleteButton;
        private RelativeLayout rootView;
        private View wordStateLabel;

        ViewHolder(View itemView) {
            super(itemView);

            wordName = (TextView) itemView.findViewById(R.id.recycler_item_headline_text);
            wordStateLabel = itemView.findViewById(R.id.word_state_label);
            deleteButton = (ImageButton) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
            rootView = (RelativeLayout) itemView.findViewById(R.id.recycler_item_relativeLayout);
        }
    }
}
