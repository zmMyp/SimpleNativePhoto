package myp.zm.simplenativephotolib;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by qianjian on 2017/6/14.
 */

public class SimpleNativePhoto {
    /**
     * 图片的uri，其实就是地址。eg:/storage/sdcard0/Tencent/QQ_Images/-1935240a504f548c.jpg
     */
    public Uri uri;

    /**
     * 图像需要旋转的角度。方向不正确的图像可以根据这个进行旋转操作
     */
    public int degree;

    /**
     * 图像的bitmap
     */
    public Bitmap bitmap;
}
