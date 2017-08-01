package braincollaboration.wordus.view.bottomsheet;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.view.View;


public class BottomScreenBehavior extends BottomSheetBehavior.BottomSheetCallback {

    private FloatingActionButton floatingActionButton;

    public BottomScreenBehavior(FloatingActionButton fab) {
        this.floatingActionButton = fab;
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
//        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//            floatingActionButton.setVisibility(View.VISIBLE);
//            floatingActionButton.show();
//        } else {
//            floatingActionButton.setVisibility(View.GONE);
//            floatingActionButton.hide();
//        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

    }
}
