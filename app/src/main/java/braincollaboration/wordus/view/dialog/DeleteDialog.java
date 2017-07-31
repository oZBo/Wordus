package braincollaboration.wordus.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import braincollaboration.wordus.R;

public class DeleteDialog {

    private AlertDialog.Builder dialogBuilder;
    private Dialog dialog;

    private DeleteDialogCallback deleteDialogCallback;

    public DeleteDialog(Context context, DeleteDialogCallback deleteDialogCallback) {
        this.deleteDialogCallback = deleteDialogCallback;
        initDialog(context);
    }

    private AlertDialog.Builder initDialog(Context context) {
        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setIcon(R.drawable.ic_delete_variant);
        dialogBuilder.setTitle(context.getString(R.string.confirm_title));
        dialogBuilder.setMessage(context.getString(R.string.delete_question));
        dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDialogCallback.delete();
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return dialogBuilder;
    }

    public void showDialog() {
        if (dialogBuilder != null) {
            dialog = dialogBuilder.create();
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}