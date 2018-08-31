package myp.zm.simplenativephotolib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by qianjian on 2017/6/14.
 */

public class SimpleNativePhotoUtil {

    /**
     * 从相册获取图片
     */
    public static void choicePicFromAlbum(Activity activity, int requestCode) {
        // 来自相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 4.4以上版本使用
     * @see "http://blog.csdn.net/tempersitu/article/details/20557383"
     *
     * @param activity
     * @param requestCode
     */
    public static void choicePicFromAlbum_kitkat(Activity activity, int requestCode) {
        // 来自相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 7.0以上版本使用
     * @see "http://blog.csdn.net/tempersitu/article/details/20557383"
     *
     * @param activity
     * @param requestCode
     */
    public static void choicePicFromAlbum_N(Activity activity, int requestCode) {
        // 来自相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照后获取图片
     *
     * @param activity
     * @param cameraPhotoFile 照片的文件
     * @param requestCode
     */
    public static void choicePicFromCamera(Activity activity, File cameraPhotoFile, int requestCode) {
        // 来自相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径，这样通过这个uri就可以得到这个照片了
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraPhotoFile));
        activity.startActivityForResult(cameraIntent, requestCode);// CAMERA_OK是用作判断返回结果的标识
    }

    /**
     * 拍照后获取图片
     * 7.0
     * http://blog.csdn.net/fengyuzhengfan/article/details/52688232
     * @param activity
     * @param cameraPhotoFile 照片的文件
     * @param requestCode
     */
    public static void choicePicFromCamera_N(Activity activity, File cameraPhotoFile, int requestCode) {

        try {
            String pkName = activity.getPackageName();
            // 来自相机
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, pkName + ".simplephoto.fileprovider", cameraPhotoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            activity.startActivityForResult(cameraIntent, requestCode);// CAMERA_OK是用作判断返回结果的标识
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 裁剪
     * @param activity
     * @param cropParam
     * @param fromUri
     * @param saveUri
     * @param requestCode
     */
    public static void cropPic(Activity activity, SimpleNativePhotoCropParam cropParam, Uri fromUri, Uri saveUri, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");

        if(fromUri.toString().startsWith("content")){//相册
            intent.setDataAndType(fromUri, "image/*");
        }else{
            intent.setDataAndType(Uri.fromFile(new File(fromUri.toString())), "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", cropParam.getAspectX());
        intent.putExtra("aspectY",cropParam.getAspectY());
        intent.putExtra("outputX",cropParam.getOutputX());
        intent.putExtra("outputY",cropParam.getOutputY());
        intent.putExtra("scale", true);
        intent.putExtra("output", Uri.fromFile(new File(saveUri.toString())));
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
        //intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, requestCode);

    }
    /**
     * 裁剪 7.0
     * @param activity
     * @param cropParam
     * @param fromUri
     * @param saveUri
     * @param requestCode
     */
    public static void cropPic_N(Activity activity, SimpleNativePhotoCropParam cropParam, Uri fromUri, Uri saveUri, int requestCode){
        String pkName = activity.getPackageName();
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri imageUri= FileProvider.getUriForFile(activity, pkName + ".simplephoto.fileprovider", new File(fromUri.toString()));//通过FileProvider创建一个content类型的Uri
        if(fromUri.toString().startsWith("content")){//相册
            intent.setDataAndType(imageUri, "image/*");
        }else{
            intent.setDataAndType(imageUri, "image/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", cropParam.getAspectX());
        intent.putExtra("aspectY",cropParam.getAspectY());
        intent.putExtra("outputX",cropParam.getOutputX());
        intent.putExtra("outputY",cropParam.getOutputY());
        intent.putExtra("scale", true);
        File cropfile = new File(saveUri.toString());
        try {
            if (cropfile.exists()) {
                cropfile.delete();
            }
            cropfile.getParentFile().mkdirs();
            cropfile.createNewFile();
        } catch (IOException ex) {
            Log.e("io", ex.getMessage());
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropfile));
        intent.putExtra("return-data", false);// 若为false则表示不返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, requestCode);

    }
    private int aspectX = 1000;

    private int aspectY = 1000;

    private int outputX = 1;

    private int outputY = 1;

    private boolean shouldBeClip = false;

    private boolean shouldBeScale = false;

    /**
     * 设置比率，比如1:1
     */
    public void setAspectX(int aspectX, int aspectY) {
        this.aspectX = aspectX;
        this.aspectY = aspectY;
    }

    /**
     * 设置裁剪出来的图片的宽高
     */
    public void setOutputX(int outputX, int outputY) {
        this.outputX = outputX;
        this.outputY = outputY;
    }


    /**
     * 裁剪时是否保留图片的比例，如果是true那么就是保留
     */
    public void setShouldBeScale(boolean shoubeScale) {
        this.shouldBeScale = shoubeScale;
    }

    /**
     * 4.4得到的uri,需要以下方法来获取文件的路径
     *
     * @param context
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 得到图片的方向
     *
     * @param photoUri
     * @return
     */
    public static int getOrientation(final Uri photoUri) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(photoUri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
    }

    /**
     * 通过photo的uri来得到图片的角度，从而判断是否需要进行旋转操作
     *
     * @param uri
     * @return
     */
    public static int getPhotoDegreeByUri(Uri uri) {
        int degree = 0;
        int orientation = SimpleNativePhotoUtil.getOrientation(uri);
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            degree = 90;
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            degree = 180;
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            degree = 270;
        }
        return degree;
    }

    /**
     *
     *
     * @param wantedSize 想要压缩到的大小
     */
    public static Bitmap ThumbileBmp(Bitmap bitmapQb,int wantedSize) {
        Bitmap bitmap=bitmapQb;
        Bitmap resultBmp=null;
        int quality =50;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        if(baos.toByteArray().length>wantedSize*1024){
            baos.reset();
            //直接压缩50
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            //如果依然大再进入循环
            while (baos.toByteArray().length > wantedSize * 1024) {
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                quality-=10;
                Log.i("photo","0->"+baos.toByteArray().length);
                if(quality<=0){
                    baos.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                    break;
                }
            }

        }

        byte [] bitmapByte =baos.toByteArray();
        resultBmp= BitmapFactory.decodeByteArray(bitmapByte,0,bitmapByte.length);

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return resultBmp;
    }


}
