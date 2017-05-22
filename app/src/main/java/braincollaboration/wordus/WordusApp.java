package braincollaboration.wordus;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by evhenii on 22.05.17.
 */

public class WordusApp extends Application {

    private static Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks();
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    private void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                setCurrentActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                setCurrentActivity(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}

