package braincollaboration.wordus.view.dialog.base;

import android.content.DialogInterface;

/**
 * Created by evhenii on 03.08.17.
 */

public abstract class DefaultDialogCallback<Result> implements IDoubleButtonCallback<Result> {

    private Result outputResult;

    @Override
    public void onPositiveButtonClickedWithResult(Result result) {

    }

    @Override
    public void onPositiveButtonClicked() {

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    public void setResult(Result result) {
        outputResult = result;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (outputResult != null) {
                    onPositiveButtonClickedWithResult(outputResult);
                } else {
                    onPositiveButtonClicked();
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                onNegativeButtonClicked();
                break;
        }
    }
}
