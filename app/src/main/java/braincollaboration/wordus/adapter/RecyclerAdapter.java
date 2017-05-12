package braincollaboration.wordus.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import braincollaboration.wordus.MainActivity;
import braincollaboration.wordus.R;
import braincollaboration.wordus.model.Word;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Word> mDataset;
    private Context context;


    // класс view holder-а с помощью которого мы получаем ссылку на каждый элемент
    // отдельного пункта списка
    class ViewHolder extends RecyclerView.ViewHolder {
        // наш пункт состоит только из одного TextView
        TextView mTextView;
        Button deleteButton;
        RelativeLayout relativeLayout;

        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.recycler_item_headline_text);
            deleteButton = (Button) v.findViewById(R.id.recyclerViewItemDeleteButton);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.recycler_item_relativeLayout);
        }
    }

    // Конструктор
    public RecyclerAdapter(List<Word> dataset, Context context) {
        mDataset = dataset;
        this.context = context;
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)

        return new ViewHolder(v);
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).getWordName());

        // if word description is empty makes recycler_item background color == red
        if (mDataset.get(position).getWordDescription() == null) {
            holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.unFoundDescriptionColor));
        }
    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}