package myp.zm.simplenativephoto;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by qianjian on 2018/8/31.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
         registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityManagerUtil.getInstance().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                ActivityManagerUtil.getInstance().setCurrentActivity(activity);
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
                ActivityManagerUtil.getInstance().removeActivity(activity);
            }
        });
    }
}
