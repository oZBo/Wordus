package braincollaboration.wordus.adapter;


import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import braincollaboration.wordus.MainActivity;
import braincollaboration.wordus.R;
import braincollaboration.wordus.asyncTask.DeleteWordFromDBCallback;
import braincollaboration.wordus.asyncTask.DeleteWordFromDBTask;
import braincollaboration.wordus.dialog.DeleteDialog;
import braincollaboration.wordus.dialog.DeleteDialogCallback;
import braincollaboration.wordus.model.Word;

public class WordAdapter extends SectionedAdapterBase<Word> {
    private Context context;
    private WordAdapterCallback wordAdapterCallback;

    public WordAdapter(@LayoutRes int layoutResID, Context context, WordAdapterCallback wordAdapterCallback) {
        setCustomHeaderLayout(layoutResID);
        this.context = context;
        this.wordAdapterCallback = wordAdapterCallback;
    }

    public void refreshAWordList(List<Word> words) {
        super.setItemList(words);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, Word item, @ViewType int viewType) {

        if (item instanceof Categorizable) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.wordName.setText(item.getWordName());

            // if word description is empty makes recycler_item background color == red
            if (item.getWordDescription() == null) {
                viewHolder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.unFoundDescriptionColor));
            }

            viewHolder.onItemClick.setWord(item);
            viewHolder.onDeleteButtonCLick.setWord(item);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, @ViewType int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordName;
        Button deleteButton;
        RelativeLayout relativeLayout;
        OnItemClickListener onItemClick;
        OnDeleteButtonClickListener onDeleteButtonCLick;

        ViewHolder(View itemView) {
            super(itemView);

            onItemClick = new OnItemClickListener();
            onDeleteButtonCLick = new OnDeleteButtonClickListener();

            wordName = (TextView) itemView.findViewById(R.id.recycler_item_headline_text);
            deleteButton = (Button) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.recycler_item_relativeLayout);

            //deleteButton.setVisibility(Button.GONE);

            deleteButton.setOnClickListener(onDeleteButtonCLick);
            itemView.setOnClickListener(onItemClick);
        }
    }

    private class OnItemClickListener implements View.OnClickListener {
        private Word word;

        @Override
        public void onClick(View v) {
            wordAdapterCallback.setTopText(word.getWordName());
            wordAdapterCallback.setBottomText(word.getWordDescription());
            wordAdapterCallback.changeBottomSheetBehaviorForExpanded();

        }

        public void setWord(Word word) {
            this.word = word;
        }
    }

    private class OnDeleteButtonClickListener implements View.OnClickListener {
        private Word word;

        @Override
        public void onClick(View v) {
            DeleteDialog deleteDialog = new DeleteDialog(context, new DeleteDialogCallback() {
                @Override
                public void delete() {

                    //delete from db
                    DeleteWordFromDBTask deleteWord = new DeleteWordFromDBTask(context, new DeleteWordFromDBCallback() {
                        @Override
                        public void dbIsUnavailable() {
                            Toast.makeText(context, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void wordWasDeleted() {
                            Toast.makeText(context, context.getString(R.string.word_) + " " + word.getWordName() + " " + context.getString(R.string._successfully_deleteed_from_db), Toast.LENGTH_SHORT).show();
                        }
                    });

                    deleteWord.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, word.getWordName());

                    // delete from rv
                    List<Word> dataSet = wordAdapterCallback.deleteWordItem(word);
                    if (dataSet != null) {
                        refreshAWordList(dataSet);
                    }
                }
            });
            deleteDialog.showDialog();
        }

        public void setWord(Word word) {
            this.word = word;
        }
    }
}
