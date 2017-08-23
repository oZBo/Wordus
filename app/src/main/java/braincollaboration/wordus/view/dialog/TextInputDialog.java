package braincollaboration.wordus.view.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import braincollaboration.wordus.R;
import braincollaboration.wordus.WordusApp;
import braincollaboration.wordus.utils.Constants;
import braincollaboration.wordus.view.dialog.base.DefaultDialogCallback;
import braincollaboration.wordus.view.dialog.base.DialogBase;

/**
 * Created by evhenii on 02.08.17.
 */

public class TextInputDialog extends DialogBase {

    private DefaultDialogCallback<String> callback;

    public TextInputDialog(Context context, DefaultDialogCallback<String> callback) {
        super(context);
        this.callback = callback;
        initSingleButtonDialog(R.string.ok, callback);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.alert_dialog_text_input;
    }

    @Override
    protected void initWidgets(View root) {
        FrameLayout frameLayout = (FrameLayout) root.findViewById(R.id.dialog_header);
        TextView textView = (TextView) frameLayout.findViewById(R.id.dialog_header_text_view);
        Typeface face = Typeface.createFromAsset(WordusApp.getCurrentActivity().getApplicationContext().getAssets(), Constants.CUSTOM_FONT_BOLD);
        textView.setTypeface(face);

        EditText editText = (EditText) root.findViewById(R.id.text_input);
        face = Typeface.createFromAsset(WordusApp.getCurrentActivity().getApplicationContext().getAssets(), Constants.CUSTOM_FONT_REGULAR);
        editText.setTypeface(face);

        editText.requestFocus();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                callback.setResult(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
