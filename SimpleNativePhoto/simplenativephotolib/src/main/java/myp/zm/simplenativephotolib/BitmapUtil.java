package myp.zm.simplenativephotolib;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by qianjian on 2017/6/14.
 */

public class BitmapUtil {


    public static Bitmap rotateBitmap(Bitmap bm,int degree){
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {

        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;

    }
}
