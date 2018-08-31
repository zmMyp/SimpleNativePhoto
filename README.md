# SimpleNativePhoto
android  简单快速调用本地相机和相册获取照片的工具库，基本适配各版本

不需要麻烦地在onActivityResult中处理各种问题，喜欢的给星，谢啦

1.在工程总gradle中配置

  allprojects {
  
      repositories {
      
        ...
        maven { url 'https://jitpack.io' }
	
      }
      
    }
    
  
 2.在app的gradle中导入库
 
  dependencies {
  
	        implementation 'com.github.zmMyp:SimpleNativePhoto:1.02'
		
	}
  
 3.AndroidMainfest.xml中配置相机，文件系统权限，不多说
 
 4.初始化工具类
 
 
  SimpleNativePhotoHelper simpleNativePhotoHelper=SimpleNativePhotoHelper.getPhotoHelperInstance(this);
  
5.直接调用获取系统拍照或相册的照片

   （1）. //不裁剪返回系统相机或相册的bitmap
   
         //SimpleNativePhotoHelper.FROM_ALBUM     获取图库图片
	 
         simpleNativePhotoHelper.choicePhoto(SimpleNativePhotoHelper.FROM_CAMERA, imgPath,onSelectedPhotoListener);
   
   （2）.裁剪返回系统相机或相册的bitmap
   
        //创建一个裁剪尺寸，前两个参数是比例，后面是保存图片文件的大小
	
         SimpleNativePhotoCropParam  simpleNativePhotoCropParam= new SimpleNativePhotoCropParam(1,2,100,200);
            simpleNativePhotoHelper.choicePhoto(SimpleNativePhotoHelper.FROM_CAMERA,imgPath,onSelectedPhotoListener,simpleNativePhotoCropParam);
 
 
 注意 不穿图片路径是不会保存图片的，只返回bitmap 
 
   需要一个 SimpleNativePhotoHelper.OnSelectedPhotoListener接收返回的bitmap
   
    //返回的照片此处接受
    
    SimpleNativePhotoHelper.OnSelectedPhotoListener onSelectedPhotoListener=new SimpleNativePhotoHelper.OnSelectedPhotoListener(){
        @Override
        public void onSelectedPhoto(int way, SimpleNativePhoto photo) {
            //返回的bitmap
            if(photo.bitmap!=null){
                iv.setImageBitmap(photo.bitmap);
            }

        }

        @Override
        public void onSelectPhotoCancle() {


        }
    };
   
