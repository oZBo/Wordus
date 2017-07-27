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
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import braincollaboration.wordus.R;
import braincollaboration.wordus.background.DefaultBackgroundCallback;
import braincollaboration.wordus.dialog.DeleteDialog;
import braincollaboration.wordus.dialog.DeleteDialogCallback;
import braincollaboration.wordus.manager.DatabaseManager;
import braincollaboration.wordus.model.Word;

public class WordAdapter extends SectionedAdapterBase<Word> implements SectionIndexer {
    private Context context;
    private WordAdapterCallback wordAdapterCallback;
    private ArrayList<Integer> mSectionPositions;

    public WordAdapter(@LayoutRes int layoutResID, Context context, WordAdapterCallback wordAdapterCallback) {
        setCustomHeaderLayout(layoutResID);
        this.context = context;
        this.wordAdapterCallback = wordAdapterCallback;
    }

    public void refreshAWordList(List<Word> words) {
        super.setItemList(words);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final Word item, @ViewType int viewType) {

        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.wordName.setText(item.getWordName());

        // if word description is empty makes recycler_item background color == red
        if (item.getWordDescription() == null) {
            viewHolder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.unFoundDescriptionColor));
        }

        viewHolder.onItemClick.setWord(item);
        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog deleteDialog = new DeleteDialog(context, new DeleteDialogCallback() {
                    @Override
                    public void delete() {
                        DatabaseManager.getInstance().deleteWord(item, new DefaultBackgroundCallback<Void>() {
                            @Override
                            public void doOnSuccess(Void result) {
                                Toast.makeText(context, context.getString(R.string.word_) + " " + item.getWordName() + " " + context.getString(R.string._successfully_deleteed_from_db), Toast.LENGTH_SHORT).show();
                            }
                        });

                        // delete from rv
                        List<Word> dataSet = wordAdapterCallback.deleteWordItem(item);
                        if (dataSet != null) {
                            refreshAWordList(dataSet);
                        }
                    }
                });
                deleteDialog.showDialog();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, @ViewType int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = wordAdapterCallback.getDataSet().size(); i < size; i++) {
            String section = String.valueOf(wordAdapterCallback.getDataSet().get(i).getWordName().charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex != 0) {
            return mSectionPositions.get(sectionIndex);
        }
        return 1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordName;
        Button deleteButton;
        RelativeLayout relativeLayout;
        OnItemClickListener onItemClick;

        ViewHolder(View itemView) {
            super(itemView);

            onItemClick = new OnItemClickListener();

            wordName = (TextView) itemView.findViewById(R.id.recycler_item_headline_text);
            deleteButton = (Button) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.recycler_item_relativeLayout);

            itemView.setOnClickListener(new OnItemClickListener());
            itemView.setOnClickListener(onItemClick);
        }
    }

    private class OnItemClickListener implements View.OnClickListener {
        private Word word;

        @Override
        public void onClick(View v) {
            wordAdapterCallback.setTopTextOfTheBottomSheet(word.getWordName());
            wordAdapterCallback.setBottomTextOfTheBottomSheet(word.getWordDescription());
            wordAdapterCallback.changeBottomSheetBehaviorForExpanded();

        }

        void setWord(Word word) {
            this.word = word;
        }
    }
}
