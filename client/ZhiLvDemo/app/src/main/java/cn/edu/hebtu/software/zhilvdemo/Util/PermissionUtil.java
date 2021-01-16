package cn.edu.hebtu.software.zhilvdemo.Util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;


import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @ProjectName:    ZhiLv
 * @Description:    权限申请
 * @Author:         张璐婷
 * @CreateDate:     2020/12/15 21:37
 * @Version:        1.0
 */
public class PermissionUtil {
    public static final int BAIDU_READ_PHONE_STATE = 1001;
    public static final int OPEN_CAMEAR_REQUEST_CODE = 1002;
    public static final int EXTERNAL_STORAGE_REQUEST_CODE = 1003;

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/8  10:23
     *  @Description: 开启百度地图定位权限
     */
    public static boolean openLocationPermission(Activity activity){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity.getApplicationContext(),"没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
            return false;
        }else{
            return true;
        }
    }


    /**
     *  @author: 张璐婷
     *  @time: 2020/12/15  21:40
     *  @Description: 相机权限
     */
    public static boolean openCameraPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, OPEN_CAMEAR_REQUEST_CODE);
            return false;
        }else{
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, OPEN_CAMEAR_REQUEST_CODE);
                return false;
            } else {
                return true;
            }
        }

    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/12/15  21:54
     *  @Description: 读SD卡权限（从相册选择图片）| 写SD卡权限(创建文件)
     */
    public static boolean openSDCardPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }


}
