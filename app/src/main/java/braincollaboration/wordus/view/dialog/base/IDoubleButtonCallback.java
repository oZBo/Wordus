package braincollaboration.wordus.view.dialog.base;

import android.content.DialogInterface;

/**
 * Created by evhenii on 03.08.17.
 */

public interface IDoubleButtonCallback<Result> extends DialogInterface.OnClickListener{

    void onPositiveButtonClickedWithResult(Result result);
    void onPositiveButtonClicked();
    void onNegativeButtonClicked();

}
