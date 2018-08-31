package myp.zm.simplenativephotolib;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by qianjian on 2017/6/14.
 */

public class IntentUtil {

    public static boolean isBundleEmpty(Intent intent){
        boolean isEmpty=true;
        if(intent!=null){
            Bundle bundle=intent.getExtras();
            if(bundle!=null){
                return false;
            }
        }
        return  isEmpty;

    }
}
