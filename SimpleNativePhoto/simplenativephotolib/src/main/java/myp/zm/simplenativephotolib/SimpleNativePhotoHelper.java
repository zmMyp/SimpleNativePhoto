package myp.zm.simplenativephotolib;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.IntDef;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by qianjian on 2017/6/14.
 */

public class SimpleNativePhotoHelper {

    public static final int FROM_ALBUM = 0;

    public static final int FROM_CAMERA = 1;

    public static final int NO_THUMBLIlE=-1;//不需要压缩

    @IntDef({SimpleNativePhotoHelper.FROM_ALBUM, SimpleNativePhotoHelper.FROM_CAMERA})
    private @interface from {

    }

    private Activity mActivity;

    private String mPicFilePath;

    private int mFromWay;

    private int Thumbile=-1;//需要压缩到的大小

    private SimpleNativePhotoHelper(Activity activity) {
        mActivity = activity;
    }

    private static SimpleNativePhotoHelper instance;

    public static SimpleNativePhotoHelper getPhotoHelperInstance(Activity activity) {
        if (instance == null) {
            instance = new SimpleNativePhotoHelper(activity);
        }
        return instance;
    }

    public void choicePhoto(@from final int way, OnSelectedPhotoListener listener,SimpleNativePhotoCropParam cropParam) {
        choicePhoto(way,null,listener,cropParam);
    }

    public void choicePhoto(@from final int way, OnSelectedPhotoListener listener) {
        choicePhoto(way,null,listener,null);
    }
    public void choicePhoto(@from final int way, String picFilePath, OnSelectedPhotoListener listener) {
       choicePhoto(way,picFilePath,listener,null);
    }
    /**
     * 从相册或照相机获得一张图片
     *
     * @param way         获取图片的途径
     * @param picFilePath 如果需要保存从相机拍摄的图片，请指定保存图片的全部路径(通过相机拍照时才有效)
     *                    eg:GetPhotoHelper.choicePhoto(GetPhotoHelper.FROM_WAY.FROM_CAMERA, Environment.getExternalStorageDirectory()+ "/temp.jpg");
     */
    public void choicePhoto(@from final int way, String picFilePath, OnSelectedPhotoListener listener,SimpleNativePhotoCropParam cropParam) {
        mFromWay = way;

        mPicFilePath = picFilePath;
        if (way == FROM_ALBUM) {
            choicePhotoFromAlbum(cropParam);
        } else if (way == FROM_CAMERA) {
            choicePhotoFromCamera(picFilePath,cropParam);
        }
        mListener = listener;
    }

    /**
     * 启动相册的activity
     */
    private void choicePhotoFromAlbum(SimpleNativePhotoCropParam cropParam) {
        Intent intent = new Intent(mActivity, SimpleNativePhotoActivity.class);
        intent.putExtra(SimpleNativePhotoActivity.KEY_FROM_WAY, SimpleNativePhotoActivity.VALUE_FROM_ALBUM);
        if(cropParam!=null){
            intent.putExtra(SimpleNativePhotoActivity.KEY_CROP_PARAM,cropParam);
        }
        mActivity.startActivityForResult(intent, 0);
    }

    /**
     * 启动相机的activity
     */
    private void choicePhotoFromCamera(String picFilePath,SimpleNativePhotoCropParam cropParam) {
        Intent intent = new Intent(mActivity, SimpleNativePhotoActivity.class);
        intent.putExtra(SimpleNativePhotoActivity.KEY_FROM_WAY, SimpleNativePhotoActivity.VALUE_FROM_CAMERA);
        intent.putExtra(SimpleNativePhotoActivity.KEY_PHOTO_PATH, picFilePath);
        if(cropParam!=null){
            intent.putExtra(SimpleNativePhotoActivity.KEY_CROP_PARAM,cropParam);
        }

        mActivity.startActivityForResult(intent, 0);
    }

    /**
     * 裁剪
     */


    /**
     * 得到已经选择好的图片，这个方法必须在onActivityResult中进行回调
     *
     * @return 已经选择好的bitmap
     */
    protected void getSelectedPhoto(Uri uri) {

        try {

            String scrPath=uri.toString();
            if(scrPath.startsWith("content")){
                scrPath= SimpleNativePhotoUtil.getPath(mActivity,uri);
            }
            File imFile=new File(scrPath);
            FileInputStream fis=new FileInputStream(imFile);
            Bitmap bitmap=BitmapFactory.decodeStream(fis);
            fis.close();
            /*if (bitmap != null) {
                bitmap = BitmapUtil.rotateBitmap(bitmap, SimpleNativePhotoUtil.getPhotoDegreeByUri(uri));
            }*/
            final SimpleNativePhoto photo = new SimpleNativePhoto();
            photo.bitmap = bitmap;
            photo.uri = uri;
            photo.degree = SimpleNativePhotoUtil.getPhotoDegreeByUri(uri);

            // 如果来源是相机，而且没有指定图片保存的目录，那么使用完毕后就立刻删除相片
            if (mFromWay == FROM_CAMERA && mPicFilePath == null) {
                File tempPicFile = new File(uri.toString());
                if (tempPicFile != null) {
                    tempPicFile.delete();//设置成功后清除之前的照片文件
                }
            }
            mListener.onSelectedPhoto(mFromWay,photo);
        } catch (Exception e) {
            mListener.onSelectedPhoto(mFromWay,null);
            e.printStackTrace();
        }
    }

    public void getSelectedPhotoCancle(){
        try {
            mListener.onSelectPhotoCancle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnSelectedPhotoListener mListener;

    public interface OnSelectedPhotoListener {

        public void onSelectedPhoto(int way, SimpleNativePhoto photo);
        public void onSelectPhotoCancle();

    }
}
