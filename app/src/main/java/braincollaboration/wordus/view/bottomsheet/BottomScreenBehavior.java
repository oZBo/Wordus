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
        if (BottomSheetBehavior.STATE_EXPANDED == newState) {
            floatingActionButton.setVisibility(View.GONE);
        } else {
            floatingActionButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        if (slideOffset >= 0) {
            floatingActionButton.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
        }
    }
}
