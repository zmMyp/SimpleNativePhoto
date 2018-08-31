package myp.zm.simplenativephoto;

import android.app.Activity;




import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * Created by wangdong on 2017/3/14.
 */

public class ActivityManagerUtil {
    private static ActivityManagerUtil sInstance = new ActivityManagerUtil();
    private WeakReference<Activity> sCurrentActivityWeakRef;
    private static Stack<Activity> activityStack = new Stack<Activity>();


    private ActivityManagerUtil() {

    }

    public static ActivityManagerUtil getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

    public static void addActivity(Activity activity){
        activityStack.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityStack.remove(activity);
    }

    /**
     //     * 关闭所有的activity
     //     */
    public static void finishAll() {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            try {
                Activity activity = activityStack.get(i);

                activity.finish();
                activityStack.remove(activity);
            } catch (Exception e) {

            }

        }
    }

    public static void finishAllWithOutHome() {

        for (int i = activityStack.size() - 1; i >= 0; i--) {
            try {
                Activity activity = activityStack.get(i);

                activity.finish();
                activityStack.remove(activity);
            } catch (Exception e) {

            }
        }
    }

    /**
     //     * 关闭所有的activity，并关闭程序
     //     */
    public static void exitApp() {

        for (int i = activityStack.size() - 1; i >= 0; i--) {
            try {
                Activity activity = activityStack.get(i);
                activity.finish();
                activityStack.remove(activity);
            } catch (Exception e) {

            }

        }
//        System.exit(0);
    }






}
