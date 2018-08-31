package myp.zm.simplenativephotolib;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by qianjian on 2017/6/14.
 */

public class SimpleNativePhotoActivity extends Activity {

    private class RequestCode {

        public static final int ALBUM_OK = 1, ALBUM_OK_KITKAT = 2, CAMERA_OK = 3,CROP_OK=4;
    }

    /**
     * 准备通过什么样的方式来获取图片
     */
    public static final String KEY_FROM_WAY = "key_from_way";
    /**
     * 裁剪比例
     */

    public static final String KEY_CROP_PARAM="key_crop_param";
    /**
     * 图片的全部路径
     */
    public static final String KEY_PHOTO_PATH = "key_photo_path";


    public static final int VALUE_FROM_ALBUM = 54345;

    public static final int VALUE_FROM_CAMERA = 46632;

    //没有传路径时的默认路径名称
    public static final String TEMP_PHOTO_FILE_NAME = "zm_myp_temp_photo.jpg";
    public static final String TEMP_CROP_PHOTO_FILE_NAME = "zm_crop_photo.jpg";

    private File tempPicFile;

    private boolean needCrop=false;
    SimpleNativePhotoCropParam cropParam=null;
    Uri saveCropUri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("","");

        if(this.getIntent()!=null&&this.getIntent().hasExtra(KEY_CROP_PARAM)){
            cropParam=(SimpleNativePhotoCropParam) this.getIntent().getSerializableExtra(KEY_CROP_PARAM);
            needCrop=true;
        }
        if (!IntentUtil.isBundleEmpty(getIntent())) {

            Bundle bundle = getIntent().getExtras();

            if (bundle.getInt(KEY_FROM_WAY, VALUE_FROM_ALBUM) == VALUE_FROM_ALBUM) {
                // 进行版本判断 see:http://blog.csdn.net/tempersitu/article/details/20557383
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    SimpleNativePhotoUtil.choicePicFromAlbum_kitkat(this, RequestCode.ALBUM_OK_KITKAT);
                } else {
                    // 4.4以下
                    SimpleNativePhotoUtil.choicePicFromAlbum(this, RequestCode.ALBUM_OK);
                }
            } else {

                if (bundle.getString(KEY_PHOTO_PATH) == null) {
                    // 照相得到的图片默认的保存路径，用完后会自动删除
                    tempPicFile = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
                } else {
                    // 自定义照相得到的图片的保存路径，不会自动删除
                    tempPicFile = new File(bundle.getString(KEY_PHOTO_PATH));
                }
                tempPicFile.delete();// 清空之前的文件
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    SimpleNativePhotoUtil.choicePicFromCamera_N(this, tempPicFile, RequestCode.CAMERA_OK);
                }else{
                    SimpleNativePhotoUtil.choicePicFromCamera(this, tempPicFile, RequestCode.CAMERA_OK);
                }
            }
            saveCropUri=Uri.parse(Environment.getExternalStorageDirectory()+File.separator+TEMP_CROP_PHOTO_FILE_NAME);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        if(resultCode==0){
            SimpleNativePhotoHelper.getPhotoHelperInstance(this).getSelectedPhotoCancle();
            finish();
            return;
        }
        switch (requestCode) {
            // 如果是直接从相册获取
            case RequestCode.ALBUM_OK:
                //从相册中获取到图片
                if (data != null) {
                    uri = data.getData();
                    if(needCrop){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            SimpleNativePhotoUtil.cropPic_N(this,cropParam,uri,saveCropUri, RequestCode.CROP_OK);
                        }else{
                            SimpleNativePhotoUtil.cropPic(this,cropParam,uri,saveCropUri, RequestCode.CROP_OK);
                        }
                    }else{
                        finishAndReturnBitmap(uri);
                    }

                } else {
                    finish();
                }
                break;
            case RequestCode.ALBUM_OK_KITKAT:
                if (data != null) {
                    uri = Uri.parse(SimpleNativePhotoUtil.getPath(this, data.getData()));
                    if(needCrop){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            SimpleNativePhotoUtil.cropPic_N(this,cropParam,uri,saveCropUri, RequestCode.CROP_OK);
                        }else{
                            SimpleNativePhotoUtil.cropPic(this,cropParam,uri,saveCropUri, RequestCode.CROP_OK);
                        }
                    }else{

                        finishAndReturnBitmap(uri);
                    }

                } else {
                    finish();
                }
                break;
            // 如果是调用相机拍照时
            case RequestCode.CAMERA_OK:
                Log.i("","");
                // 当拍照到照片时操作
                if (tempPicFile.exists()) {
                    uri = Uri.parse(tempPicFile.getAbsolutePath());
                    if(needCrop){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            SimpleNativePhotoUtil.cropPic_N(this,cropParam,uri,saveCropUri, RequestCode.CROP_OK);
                        }else{
                            SimpleNativePhotoUtil.cropPic(this,cropParam,uri,saveCropUri, RequestCode.CROP_OK);
                        }
                    }else{
                        finishAndReturnBitmap(uri);
                    }
                } else {
                    finish();
                }
                break;

            //裁剪
            case RequestCode.CROP_OK:

                if(saveCropUri!=null){
                    finishAndReturnBitmap(saveCropUri);
                }
                break;
            default:
                SimpleNativePhotoHelper.getPhotoHelperInstance(this).getSelectedPhotoCancle();
                finish();
                break;


        }



    }

    public void finishAndReturnBitmap(Uri uri) {
        File f=new File(uri.toString());

        SimpleNativePhotoHelper.getPhotoHelperInstance(this).getSelectedPhoto(uri);
        finish();
    }

}
