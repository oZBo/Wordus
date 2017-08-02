package braincollaboration.wordus.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import braincollaboration.wordus.R;

/**
 * Created by evhenii on 02.08.17.
 */

public class RecyclerViewWithFAB extends RecyclerView {

    private FloatingActionButton fab;
    private int fabId;

    public RecyclerViewWithFAB(Context context) {
        this(context, null);
    }

    public RecyclerViewWithFAB(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RecyclerViewWithFAB(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAttributes(attrs);
        configureRecyclerView();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (fabId != -1) {
            fab = (FloatingActionButton) getRootView().findViewById(fabId);
        }
    }

    private void applyAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.RecyclerViewWithFAB, 0, 0);
        try {
            fabId = a.getResourceId(R.styleable.RecyclerViewWithFAB_fabId, -1);
        } finally {
            a.recycle();
        }
    }

    private void configureRecyclerView() {
        this.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        this.setLayoutManager(new LinearLayoutManager(getContext()));
        this.setItemAnimator(new DefaultItemAnimator());
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (fab != null) {
                    if (dy > 0 && fab.isShown()) {
                        fab.hide();
                    } else if (dy < 0 && !fab.isShown()) {
                        fab.show();
                    }
                }
            }
        });
    }

}
