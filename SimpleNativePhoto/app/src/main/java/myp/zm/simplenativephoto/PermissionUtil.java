package myp.zm.simplenativephoto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Junnan on 2017/3/6.
 */

public class PermissionUtil {

    private static final String TAG = "PermissionUtil";
    private static final int RC_CAMERA_PERM = 123;         //打开相机和文件读取权限
    private static final int RC_CAMERA_OPEN = 124;      //单打开相机
    private static final int RC_LOCATION_CONTACTS_PERM = 125;
    private static final int RC_PHONE_READ_STATUS = 126;       //读取手机权限和文件读写权限
    private static final int RC_PHONE_CALL = 127;       //拨打电话权限
    private static final int RC_LOCATION = 128;       //获取定位信息
    private static final int RC_WRITE = 129;        //文件读写权限
    private static final int RC_ALL_PERMISSION = 130;       //获取所有权限
    private static final int RC_SHARE_PERMISSION=131;//分享需要的权限
    private static final int RC_GET_CONTACTS=132;//获取读取联系人权限


    public onPermissionGentedListener listener;

    private static PermissionUtil instance;


    private PermissionUtil(){

    }

    public static synchronized PermissionUtil getInstance(){
        if (instance == null){
            instance = new PermissionUtil();
        }
        return instance;
    }


    public interface onPermissionGentedListener{
        public void onGented();
        public void onFalied();
    }

    public void setListener(onPermissionGentedListener listener){
        this.listener = listener;
    }

    /**
     * Api 版本判断
     */
    private boolean isAndroidM(){
        //判断当前api是否为23以上版本
        Log.v("api", Build.VERSION.SDK_INT+"");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return true;
        }
        return false;
    }

    @AfterPermissionGranted(RC_SHARE_PERMISSION)
    public void ShareTask(){
        String[] perms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
        //不是6.0以上的版本 直接运行
        if (!isAndroidM()){
            if (listener != null){
                listener.onGented();
            }
            return;
        }
        if (EasyPermissions.hasPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(),perms)) {
            if (listener != null){
                listener.onGented();
            }
        } else {
            //获取多个权限
            ActivityCompat.requestPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), perms,RC_SHARE_PERMISSION);
        }

    }

    @AfterPermissionGranted(RC_LOCATION)
    public void GetLocationTask() {
        //不是6.0以上的版本 直接运行
        if (!isAndroidM()){
            if (listener != null){
                listener.onGented();
            }
            return;
        }
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION };
        if (EasyPermissions.hasPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(),perms)) {
            if (listener != null){
                listener.onGented();
            }
        } else {
            //获取多个权限
            ActivityCompat.requestPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},RC_LOCATION);
        }
    }
    //获取联系人权限
    @AfterPermissionGranted(RC_GET_CONTACTS)
    public void GetContactsTask() {
        //不是6.0以上的版本 直接运行
        if (!isAndroidM()){
            if (listener != null){
                listener.onGented();
            }
            return;
        }
        String[] perms = {Manifest.permission.READ_CONTACTS };
        if (EasyPermissions.hasPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(),perms)) {
            if (listener != null){
                listener.onGented();
            }
        } else {
            //获取多个权限
            ActivityCompat.requestPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), new String[]{Manifest.permission.READ_CONTACTS},RC_GET_CONTACTS);
        }
    }

    /**
     * 拍照权限和读取本地文件权限
     */
    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        //不是6.0以上的版本 直接运行
        if (!isAndroidM()){
            if (listener != null){
                listener.onGented();
            }
            return;
        }
        //Log.v("camera","two permission");
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA };
        //两个权限同时满足才执行接下去的任务
        if (EasyPermissions.hasPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(),perms)) {
            // Have permission, do the thing!
            //Log.v("camera","two permission");
            if (listener != null){
                listener.onGented();
            }
        } else {
            //获取多个权限
            ActivityCompat.requestPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(),perms, RC_CAMERA_PERM);
        }
    }

    @AfterPermissionGranted(RC_CAMERA_OPEN)
    public void cameraOpenTask() {
        //不是6.0以上的版本 直接运行
        if (!isAndroidM()){
            if (listener != null){
                listener.onGented();
            }
            return;
        }
        String[] perms = {Manifest.permission.CAMERA };
        if (EasyPermissions.hasPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(),perms)) {
            if (listener != null){
                listener.onGented();
            }
        } else {
            //获取多个权限
            ActivityCompat.requestPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), new String[]{Manifest.permission.CAMERA},RC_CAMERA_OPEN);
        }
    }
    /**
     * status权限和读取本地文件权限
     */
    @AfterPermissionGranted(RC_PHONE_READ_STATUS)
    public void statusAndRead() {
        //不是6.0以上的版本 直接运行
        if (!isAndroidM()){
            if (listener != null){
                listener.onGented();
            }
            return;
        }
        String[] perms = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE };
        //两个权限同时满足才执行接下去的任务
        if (EasyPermissions.hasPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(),perms)) {
            // Have permission, do the thing!
            //Log.v("camera","two permission");
            if (listener != null){
                listener.onGented();
            }
        } else {
            //获取多个权限
            ActivityCompat.requestPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PHONE_READ_STATUS);
        }
    }

    /**
     * 位置权限 （蓝牙相关）
     */
    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void locationAndContactsTask() {
        //不是6.0以上的版本 直接运行
        if (!isAndroidM()){
            if (listener != null){
                listener.onGented();
            }
            return;
        }
        String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), perms)) {
            // Have permissions, do the thing!
            if (listener != null){
                listener.onGented();
            }
        } else {
            // Ask for both permissions
            ActivityCompat.requestPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RC_LOCATION_CONTACTS_PERM);
        }
    }

    /**
     * 手机权限 （拨打电话相关）
     */
    @AfterPermissionGranted(RC_PHONE_CALL)
    public void PhoneCallTask() {
        //不是6.0以上的版本 直接运行
        if (!isAndroidM()){
            if (listener != null){
                listener.onGented();
            }
            return;
        }
        String[] perms = { Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), perms)) {
            // Have permissions, do the thing!
            if (listener != null){
                listener.onGented();
            }
        } else {
            // Ask for both permissions
            ActivityCompat.requestPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), new String[]{Manifest.permission.CALL_PHONE}, RC_PHONE_CALL);
        }
    }


    /**
     * 读取权限
     */
    @AfterPermissionGranted(RC_WRITE)
    public void writeContactsTask() {
        //不是6.0以上的版本 直接运行
        if (!isAndroidM()){
            if (listener != null){
                listener.onGented();
            }
            return;
        }
        String[] perms = { Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), perms)) {
            // Have permissions, do the thing!
            if (listener != null){
                listener.onGented();
            }
        } else {
            // Ask for both permissions
            ActivityCompat.requestPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_WRITE);
        }
    }

    /**
     * 读取权限
     */
    @AfterPermissionGranted(RC_ALL_PERMISSION)
    public void allContactsTask() {
        //不是6.0以上的版本 直接运行
        if (!isAndroidM()){
            if (listener != null){
                listener.onGented();
            }
            return;
        }
        String[] perms = { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA };
        if (EasyPermissions.hasPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), perms)) {
            // Have permissions, do the thing!
            if (listener != null){
                listener.onGented();
            }
        } else {
            // Ask for both permissions
            ActivityCompat.requestPermissions(ActivityManagerUtil.getInstance().getCurrentActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA}, RC_ALL_PERMISSION);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //ActivityManagerUtil.getInstance().getCurrentActivity().onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onPermissionsResult:" );
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        //单次被拒绝
        if (grantResults != null && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            if (listener != null){
                listener.onFalied();
            }
        }
    }

    /*@Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
*/
        /*if(listener!=null){
            listener.onGented();
        }*/
        //权限获取后自动执行接下去的指令
//        if (requestCode == RC_CAMERA_PERM && perms.size() == 2){            //说明两个权限都已满足
//            if (listener != null){
//                listener.onGented();
//            }
//        }else if (requestCode == RC_CAMERA_OPEN){
//            if (listener != null){
//                listener.onGented();
//            }
//        }else if (requestCode == RC_PHONE_READ_STATUS && perms.size() == 2){
//            if (listener != null){
//                listener.onGented();
//            }
//        }else if (requestCode == RC_LOCATION_CONTACTS_PERM){
//            if (listener != null){
//                listener.onGented();
//            }
//        }
    //}

    /*@Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(ActivityManagerUtil.getInstance().getCurrentActivity(), perms) && requestCode == RC_CAMERA_PERM) {
            new_msg AppSettingsDialog.Builder(ActivityManagerUtil.getInstance().getCurrentActivity(),"权限已被拒绝，要想使用该功能请在设置-权限中打开\"相机\"和\"存储空间\"权限").setTitle("获取权限").build().show();
        }else if (EasyPermissions.somePermissionPermanentlyDenied(ActivityManagerUtil.getInstance().getCurrentActivity(), perms) && requestCode == RC_CAMERA_OPEN){
            new_msg AppSettingsDialog.Builder(ActivityManagerUtil.getInstance().getCurrentActivity(),"权限已被拒绝，要想使用该功能请在设置-权限中打开\"相机\"权限").setTitle("获取权限").build().show();
        }else if (EasyPermissions.somePermissionPermanentlyDenied(ActivityManagerUtil.getInstance().getCurrentActivity(), perms) && requestCode == RC_PHONE_READ_STATUS){
            new_msg AppSettingsDialog.Builder(ActivityManagerUtil.getInstance().getCurrentActivity(),"权限已被拒绝，要想使用该功能请在设置-权限中打开\"存储空间\"和\"电话\"权限").setTitle("获取权限").build().show();
        }else if (EasyPermissions.somePermissionPermanentlyDenied(ActivityManagerUtil.getInstance().getCurrentActivity(), perms) && requestCode == RC_LOCATION_CONTACTS_PERM) {
            new_msg AppSettingsDialog.Builder(ActivityManagerUtil.getInstance().getCurrentActivity(),"权限已被拒绝，要想使用该功能请在设置-权限中打开\"位置信息\"权限").setTitle("获取权限").build().show();
        }else if (EasyPermissions.somePermissionPermanentlyDenied(ActivityManagerUtil.getInstance().getCurrentActivity(), perms) && requestCode == RC_PHONE_CALL){
            new_msg AppSettingsDialog.Builder(ActivityManagerUtil.getInstance().getCurrentActivity(),"权限已被拒绝，要想使用该功能请在设置-权限中打开\"电话\"权限").setTitle("获取权限").build().show();
        }else if (EasyPermissions.somePermissionPermanentlyDenied(ActivityManagerUtil.getInstance().getCurrentActivity(), perms) && requestCode == RC_WRITE){
            new_msg AppSettingsDialog.Builder(ActivityManagerUtil.getInstance().getCurrentActivity(),"权限已被拒绝，要想使用该功能请在设置-权限中打开\"存储空间\"权限").setTitle("获取权限").build().show();
        }else if (EasyPermissions.somePermissionPermanentlyDenied(ActivityManagerUtil.getInstance().getCurrentActivity(), perms) && requestCode == RC_ALL_PERMISSION){
            new_msg AppSettingsDialog.Builder(ActivityManagerUtil.getInstance().getCurrentActivity(),"权限已被拒绝，要想使用该功能请在设置-权限中打开所有权限").setTitle("获取权限").build().show();
        }
        if (listener != null){
            listener.onFalied();
        }
    }*/
}
