package braincollaboration.wordus.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import java.lang.NullPointerException;

import braincollaboration.wordus.R;
import braincollaboration.wordus.utils.Constants;

public class SearchDialog {

    private AlertDialog.Builder dialogBuilder;
    private Dialog dialog;

    private SearchDialogCallback dialogCallback;

    public SearchDialog(SearchDialogCallback dialogCallback, Context context) {
        this.dialogCallback = dialogCallback;
        initDialog(context);
    }

    private AlertDialog.Builder initDialog(Context context) {
        // get our search_dialog layout
        LayoutInflater li = LayoutInflater.from(context);
        View searchView = li.inflate(R.layout.search_dialog, null);

        dialogBuilder = new AlertDialog.Builder(context);

        // set the dialog view with our layout.xml
        dialogBuilder.setView(searchView);

        final EditText userInput = (EditText) searchView.findViewById(R.id.input_text);
        userInput.setLines(1);

        dialogBuilder
                .setCancelable(true)
                .setPositiveButton(R.string.search,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Вводим текст и отображаем в строке ввода на основном экране:
                                dialogCallback.findAWord(userInput.getText());
                            }
                        });

        return dialogBuilder;
    }

    public void showDialog() {
        if (dialogBuilder != null) {
            dialog = dialogBuilder.create();
            // to show soft-key by opening
            try {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            } catch (NullPointerException e) {
                Log.d(Constants.LOG_TAG, "softInput in dialog error");
            }
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
