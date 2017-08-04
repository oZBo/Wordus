package braincollaboration.wordus.view.dialog.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;

import braincollaboration.wordus.R;

import static android.content.DialogInterface.OnClickListener;

/**
 * Created by evhenii on 02.08.17.
 */

public abstract class DialogBase {

    private AlertDialog.Builder dialogBuilder;
    private Dialog dialog;
    private Context context;
    private String positiveButtonCaption;
    private String negativeButtonCaption;

    protected abstract int getLayoutResId();

    protected abstract void initWidgets(View root);

    protected DialogBase(Context context) {
        this.context = context;
        initDialog();
    }

    private void initDialog() {
        dialogBuilder = new AlertDialog.Builder(context);

        if (getLayoutResId() != 0) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View root = inflater.inflate(getLayoutResId(), null);
            dialogBuilder.setView(root);
            initWidgets(root);
        } else {
            dialogBuilder.setIcon(android.R.drawable.ic_menu_delete);
            dialogBuilder.setTitle(R.string.confirm_title);
            dialogBuilder.setMessage(R.string.delete_question);
        }
    }

    protected void initSingleButtonDialog(@StringRes int btnTextId, OnClickListener clickListener) {
        if (dialogBuilder != null) {
            dialogBuilder.setPositiveButton(btnTextId, clickListener);
        }
    }

    protected void initDoubleButtonDialog(@StringRes int positiveBtnTextId, @StringRes int negativeBtnTextId, OnClickListener callback) {
        if (dialogBuilder != null) {
            dialogBuilder.setPositiveButton(positiveBtnTextId, callback);
            dialogBuilder.setNegativeButton(negativeBtnTextId, callback);
        }
    }

    public void show() {
        if (dialogBuilder != null) {
            dialog = dialogBuilder.create();
            dialog.show();
        }
    }

    public void hide() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
