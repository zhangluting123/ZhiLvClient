package cn.edu.hebtu.software.zhilvdemo.Util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ProjectName:    ZhiLv
 * @Description:    获取照片
 * @Author:         张璐婷
 * @CreateDate:     2020/12/15 22:11
 * @Version:        1.0
 */
public class GetPhotoUtil {

    public static void choosePhoto(Activity activity) {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intentToPickPic, FinalVariableUtil.RC_CHOOSE_PHOTO);
    }

    public static void chooseVideo(Activity activity) {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        activity.startActivityForResult(intentToPickPic, FinalVariableUtil.RC_CHOOSE_VIDEO);
    }


    public static String takePhoto(Activity activity) {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(date) + ".jpg";

        File photoFile = new File(fileDir, fileName);
        String mTempPhotoPath = photoFile.getAbsolutePath();//获取照片路径
        Log.e("图片路径", mTempPhotoPath);
        Uri imageUri = FileProvider7.getUriForFile(activity.getApplicationContext(), photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Log.e("uri", imageUri + "");
        activity.startActivityForResult(intentToTakePhoto, FinalVariableUtil.RC_TAKE_PHOTO);

        return mTempPhotoPath;
    }
}
