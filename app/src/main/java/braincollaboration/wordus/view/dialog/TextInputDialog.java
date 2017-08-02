package braincollaboration.wordus.view.dialog;

import android.content.Context;
import android.content.DialogInterface;

import braincollaboration.wordus.R;

/**
 * Created by evhenii on 02.08.17.
 */

public class TextInputDialog extends DialogBase {


    public TextInputDialog(Context context) {
        super(context);
        initSingleButtonDialog(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.alert_dialog_text_input;
    }
}
