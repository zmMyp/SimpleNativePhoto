package myp.zm.simplenativephoto;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import myp.zm.simplenativephotolib.SimpleNativePhoto;
import myp.zm.simplenativephotolib.SimpleNativePhotoCropParam;
import myp.zm.simplenativephotolib.SimpleNativePhotoHelper;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;


    private SimpleNativePhotoHelper simpleNativePhotoHelper=null;
    private PermissionUtil permissionUtil=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定activity
        ButterKnife.bind(this);
        permissionUtil=PermissionUtil.getInstance();

        //初始化
        simpleNativePhotoHelper=SimpleNativePhotoHelper.getPhotoHelperInstance(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }






    @OnClick({R.id.btn1, R.id.btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn1:
                getPhoto(false);
                break;
            case R.id.btn2:
                getPhoto(true);
                break;
        }
    }

    //获取照片
    private void getPhoto(final boolean needCrop){

        PermissionUtil.onPermissionGentedListener listener = new PermissionUtil.onPermissionGentedListener() {
            @Override
            public void onGented() {


                String imgPath=Environment.getExternalStorageDirectory().getPath()+File.separator+new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date())+".png";
                try {
                    if(needCrop){
                        //SimpleNativePhotoHelper.FROM_ALBUM     来自图库
                        //裁剪返回系统相机或相册的bitmap
                        SimpleNativePhotoCropParam  simpleNativePhotoCropParam= new SimpleNativePhotoCropParam(1,2,100,200);
                        //如果除了想回去bitmap。还想把图片文件也保存下来，请在第二个参数输入自己的路径，不传不保存图片，只返回bitmap
                        simpleNativePhotoHelper.choicePhoto(SimpleNativePhotoHelper.FROM_CAMERA, imgPath,onSelectedPhotoListener,simpleNativePhotoCropParam);
                    }else{
                        //不裁剪返回系统相机或相册的bitmap
                        simpleNativePhotoHelper.choicePhoto(SimpleNativePhotoHelper.FROM_CAMERA, imgPath,onSelectedPhotoListener);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFalied() {

            }
        };
        permissionUtil.setListener(listener);
        permissionUtil.cameraTask();
    }

    //返回的照片此处接受
    SimpleNativePhotoHelper.OnSelectedPhotoListener onSelectedPhotoListener=new SimpleNativePhotoHelper.OnSelectedPhotoListener(){
        @Override
        public void onSelectedPhoto(int way, SimpleNativePhoto photo) {

            if(photo.bitmap!=null){
                iv.setImageBitmap(photo.bitmap);
            }

        }

        @Override
        public void onSelectPhotoCancle() {

        }
    };
}
