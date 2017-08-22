package braincollaboration.wordus.view.dialog;

import android.content.Context;
import android.view.View;

import braincollaboration.wordus.R;
import braincollaboration.wordus.view.dialog.base.DefaultDialogCallback;
import braincollaboration.wordus.view.dialog.base.DialogBase;

/**
 * Created by evhenii on 03.08.17.
 */

public class ConfirmationDialog extends DialogBase {

    public ConfirmationDialog(Context context, DefaultDialogCallback callback) {
        super(context);
        initDoubleButtonDialog(R.string.ok, R.string.cancel, callback);
    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void initWidgets(View root) {

    }
}
