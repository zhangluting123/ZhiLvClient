package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import cn.edu.hebtu.software.zhilvdemo.Data.MoreDetail;
import cn.edu.hebtu.software.zhilvdemo.Data.Travels;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.UploadAndDownload.UploadTravelsTask;
import cn.edu.hebtu.software.zhilvdemo.Util.DensityUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.FileUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.FinalVariableUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.GetPhotoUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.PermissionUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * @ProjectName:    ZhiLv
 * @Description:    添加游记界面
 * @Author:         张璐婷
 * @CreateDate:     2021/1/23 12:00
 * @Version:        1.0
 */
public class AddTravelsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnVideo;
    private Button btnSubmit;
    private ImageButton uploadImage;
    private Button btnMoreDetail;
    private EditText title;
    private EditText route;
    private EditText scene;
    private EditText ticket;
    private EditText hotel;
    private EditText tips;
    private RelativeLayout topic;
    private RelativeLayout location;
    private TextView topicText;
    private TextView locationText;

    private PopupWindow popupWindow;

    private View popupView;
    private Button btnTakePhoto;
    private Button btnChooseFromAlbum;
    private Button btnCancel;
    private int i; //图片个数
    private String mTempPhotoPath;
    private List<String> imgPathList= new ArrayList<>();
    private MyApplication data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travels);

        i = 0;

        getViews();
        registListener();

        data = (MyApplication)getApplication();
        locationText.setText(data.getProvince() + "·" + data.getCity());
        data.setTopic(null);
        data.setMoreDetail(null);
    }

    private void getViews(){
        toolbar = findViewById(R.id.toolbar);
        btnVideo = findViewById(R.id.add_travels_btnVideo);
        btnSubmit = findViewById(R.id.add_travels_btnSubmit);
        uploadImage = findViewById(R.id.uploadImage);
        btnMoreDetail = findViewById(R.id.add_btn_more_detail);
        title = findViewById(R.id.add_travels_et_title);
        route = findViewById(R.id.add_travels_et_route);
        scene =findViewById(R.id.add_travels_et_scene);
        ticket = findViewById(R.id.add_travels_et_ticket);
        hotel = findViewById(R.id.add_travels_et_hotel);
        tips = findViewById(R.id.add_travels_et_tips);
        topic = findViewById(R.id.addTravels_rl_topic);
        location =findViewById(R.id.addTravels_rl_location);
        topicText = findViewById(R.id.add_travels_tv_topic);
        locationText = findViewById(R.id.add_travels_tv_location);
        LayoutInflater inflater = getLayoutInflater();
        popupView = inflater.inflate(R.layout.popup_image_layout, null);
        btnTakePhoto = popupView.findViewById(R.id.take_photo);
        btnChooseFromAlbum = popupView.findViewById(R.id.choose_from_album);
        btnCancel = popupView.findViewById(R.id.btn_cancel);
    }

    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        uploadImage.setOnClickListener(listener);
        btnVideo.setOnClickListener(listener);
        btnSubmit.setOnClickListener(listener);
        btnMoreDetail.setOnClickListener(listener);
        topic.setOnClickListener(listener);
        location.setOnClickListener(listener);
        btnTakePhoto.setOnClickListener(listener);
        btnChooseFromAlbum.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.uploadImage:
                    showPopupWindow();
                    break;
                case R.id.add_travels_btnVideo:
                    intent = new Intent(getApplicationContext(), AddVideoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.add_travels_btnSubmit:
                    Travels travels = new Travels();
                    travels.setUser(data.getUser());
                    travels.setHotel(hotel.getText().toString());
                    travels.setImgPathList(imgPathList);
                    travels.setLocation(locationText.getText().toString());
                    travels.setRoute(route.getText().toString());
                    travels.setScene(scene.getText().toString());
                    travels.setTicket(ticket.getText().toString());
                    travels.setTips(tips.getText().toString());
                    travels.setTitle(title.getText().toString());
                    travels.setTopic(data.getTopic());
                    travels.setDetail(data.getMoreDetail());
                    UploadTravelsTask task = new UploadTravelsTask(AddTravelsActivity.this, travels);
                    task.execute("http://"+data.getIp()+":8080/ZhiLvProject/audit/travels/add");
                    break;
                case R.id.add_btn_more_detail:
                    intent = new Intent(getApplicationContext(), AddMoreDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.addTravels_rl_topic:
                    intent = new Intent(getApplicationContext(), SearchTopicActivity.class);
                    startActivity(intent);
                    break;
                case R.id.addTravels_rl_location:
                    break;
                case R.id.take_photo:
                    if(PermissionUtil.openCameraPermission(AddTravelsActivity.this)){
                        mTempPhotoPath = GetPhotoUtil.takePhoto(AddTravelsActivity.this);
                    }
                    popupWindow.dismiss();
                    break;

                case R.id.choose_from_album:
                    if(PermissionUtil.openSDCardPermission(AddTravelsActivity.this)){
                        GetPhotoUtil.choosePhoto(AddTravelsActivity.this);
                    }
                    popupWindow.dismiss();
                    break;
                case R.id.btn_cancel:
                    popupWindow.dismiss();
                    break;

            }
        }
    }


    public void showPopupWindow(){
        popupWindow = new PopupWindow(this);
        popupWindow.setContentView(popupView);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        HorizontalScrollView parent = findViewById(R.id.parent);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM,0,0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.OPEN_CAMEAR_REQUEST_CODE:   //拍照权限申请返回
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    mTempPhotoPath = GetPhotoUtil.takePhoto(AddTravelsActivity.this);
                }else{
                    Toast.makeText(getApplicationContext(), "相机权限和读写SD卡申请失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionUtil.EXTERNAL_STORAGE_REQUEST_CODE:   //相册选择照片权限申请返回
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    GetPhotoUtil.choosePhoto(AddTravelsActivity.this);
                }else{
                    Toast.makeText(getApplicationContext(), "读写SD卡权限申请失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FinalVariableUtil.RC_CHOOSE_PHOTO:
                    Uri uri = data.getData();
                    String southFilePath = FileUtil.getFilePathByUri(this, uri);
                    startUCrop(this,southFilePath, UCrop.REQUEST_CROP,16,9);
                    break;
                case FinalVariableUtil.RC_TAKE_PHOTO:
                    startUCrop(this,mTempPhotoPath,UCrop.REQUEST_CROP,16,9);
                    break;
                case UCrop.REQUEST_CROP: {
                    // 裁剪照片
                    final Uri croppedUri = UCrop.getOutput(data);
                    String filePath = FileUtil.getFilePathByUri(this, croppedUri);
                    if (!TextUtils.isEmpty(filePath)) {
                        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                        //将照片显示在 ivImage上
                        insertPhoto(requestOptions1,filePath);
                    }
                    break;
                }

                case UCrop.RESULT_ERROR: {
                    final Throwable cropError = UCrop.getError(data);
                    Log.i("RESULT_ERROR","UCrop_RESULT_ERROR");
                    break;
                }
            }
        }
    }

    private void insertPhoto(RequestOptions requestOptions, final String filePath){
        final LinearLayout layout = findViewById(R.id.layout_img);
        final RelativeLayout relativeLayout = new RelativeLayout(this);

        ImageView img = new ImageView(this);
        img.setId(i);
        Glide.with(this).load(filePath).apply(requestOptions).into(img);
        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(),100),DensityUtil.dip2px(getApplicationContext(),100));
        param1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayout.addView(img,param1);

        ImageButton deleteImg = new ImageButton(this);
        deleteImg.setId(i);
        deleteImg.setBackgroundResource(R.mipmap.add_travels_deleteimg);
        RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relativeLayout.addView(deleteImg,param2);

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(),110),DensityUtil.dip2px(getApplicationContext(),110));
        layout.addView(relativeLayout,param);
        //添加图片
        imgPathList.add(filePath);

        //设置删除按钮的监听器
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getId();
                layout.removeView(relativeLayout);
                imgPathList.remove(filePath);
            }
        });
        i++;
    }

    /**
     * 启动裁剪
     * @param activity 上下文
     * @param sourceFilePath 需要裁剪图片的绝对路径
     * @param requestCode 比如：UCrop.REQUEST_CROP
     * @param aspectRatioX 裁剪图片宽高比
     * @param aspectRatioY 裁剪图片宽高比
     * @return
     */
    public static void startUCrop(Activity activity, String sourceFilePath,
                                  int requestCode, float aspectRatioX, float aspectRatioY) {
        Uri sourceUri = Uri.fromFile(new File(sourceFilePath));
        File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
        //裁剪后图片的绝对路径
//        cameraScalePath = outFile.getAbsolutePath();
        Uri destinationUri = Uri.fromFile(outFile);
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //是否隐藏底部容器，默认显示
//        options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.MyThemeColor));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.MyThemeColor));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        //UCrop配置
        uCrop.withOptions(options);
        //设置裁剪图片的宽高比，比如16：9
        uCrop.withAspectRatio(aspectRatioX, aspectRatioY);
        //uCrop.useSourceImageAspectRatio();
        //跳转裁剪页面
        uCrop.start(activity, requestCode);
    }

    @Override
    public void onResume() {
        if(null != data.getTopic()) {
            topicText.setText(data.getTopic().getTitle());
        }
        super.onResume();

    }
}