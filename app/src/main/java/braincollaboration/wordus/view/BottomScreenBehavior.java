package braincollaboration.wordus.view;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.view.View;


public class BottomScreenBehavior extends BottomSheetBehavior.BottomSheetCallback {

    private FloatingActionButton fab;

    public BottomScreenBehavior(FloatingActionButton fab) {
        this.fab = fab;
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (BottomSheetBehavior.STATE_EXPANDED == newState) {
            fab.setVisibility(View.GONE);
        } else if (BottomSheetBehavior.STATE_HIDDEN == newState) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        if (slideOffset >= 0) {
            fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
        }
    }
}
