package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import cn.edu.hebtu.software.zhilvdemo.Data.Scene;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.UploadAndDownload.EditSceneTask;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;


public class EditSceneDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView path;
    private EditText title;
    private EditText content;
    private EditText rule;
    private EditText openTime;
    private EditText traffic;
    private EditText ticket;
    private EditText costTime;
    private EditText phone;
    private EditText website;
    private Button btnSubmit;

    private View v;
    private Button btnTakePhoto;
    private Button btnChooseFromAlbum;
    private Button btnCancel;

    private Scene scene;
    private Scene newScene;
    private MyApplication data;
    private PopupWindow popupWindow;
    private String mTempPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scene_detail);
        data = (MyApplication)getApplication();
        getViews();
        initData();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //注册监听器
        CustomListener listener = new CustomListener();
        btnSubmit.setOnClickListener(listener);
        path.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);
        btnChooseFromAlbum.setOnClickListener(listener);
        btnTakePhoto.setOnClickListener(listener);
    }

    private void getViews(){
        toolbar = findViewById(R.id.toolbar);
        path = findViewById(R.id.scene_iv_path);
        title = findViewById(R.id.scene_et_title);
        content = findViewById(R.id.scene_et_content);
        rule = findViewById(R.id.scene_et_rule);
        openTime = findViewById(R.id.scene_et_openTime);
        traffic = findViewById(R.id.scene_et_traffic);
        ticket= findViewById(R.id.scene_et_ticket);
        costTime = findViewById(R.id.scene_et_costTime);
        phone = findViewById(R.id.scene_et_phone);
        website = findViewById(R.id.scene_et_website);
        btnSubmit = findViewById(R.id.scene_btn_submit);
        LayoutInflater inflater = getLayoutInflater();
        v = inflater.inflate(R.layout.popup_image_layout, null);
        btnTakePhoto = v.findViewById(R.id.take_photo);
        btnChooseFromAlbum = v.findViewById(R.id.choose_from_album);
        btnCancel = v.findViewById(R.id.btn_cancel);

    }

    private void initData() {
        newScene = new Scene();
        Intent intent = getIntent();
        scene = intent.getParcelableExtra("scene");
        Glide.with(this).load("http://" + data.getIp() + ":8080/ZhiLvProject/" + scene.getPath()).into(path);
        title.setText(scene.getTitle());
        content.setText(scene.getContent());
        rule.setText(scene.getRule());
        openTime.setText(scene.getOpenTime());
        traffic.setText(scene.getTraffic());
        ticket.setText(scene.getTicket());
        costTime.setText(scene.getCostTime());
        phone.setText(scene.getPhone());
        website.setText(scene.getWebsite());
    }

    class CustomListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.scene_btn_submit:
                    newScene.setSceneId(scene.getSceneId());
                    newScene.setTitle(title.getText().toString());
                    newScene.setContent(content.getText().toString());
                    newScene.setRule(rule.getText().toString());
                    newScene.setOpenTime(openTime.getText().toString());
                    newScene.setTicket(ticket.getText().toString());
                    newScene.setTraffic(traffic.getText().toString());
                    newScene.setCostTime(costTime.getText().toString());
                    newScene.setPhone(phone.getText().toString());
                    newScene.setWebsite(website.getText().toString());
                    EditSceneTask task = new EditSceneTask(EditSceneDetailActivity.this, newScene,data.getUser().getUserId() );
                    task.execute("http://" + data.getIp() + ":8080/ZhiLvProject/audit/scene/change");
                    break;
                case R.id.scene_iv_path:
                    showPopupWindow();
                    break;
                case R.id.take_photo:
                    if(PermissionUtil.openCameraPermission(EditSceneDetailActivity.this)){
                        mTempPhotoPath = GetPhotoUtil.takePhoto(EditSceneDetailActivity.this);
                    }
                    popupWindow.dismiss();
                    break;

                case R.id.choose_from_album:
                    if(PermissionUtil.openSDCardPermission(EditSceneDetailActivity.this)){
                        GetPhotoUtil.choosePhoto(EditSceneDetailActivity.this);
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
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow = new PopupWindow(this);
        popupWindow.setContentView(v);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM,0,0);
    }

    /**
     * 权限申请结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.OPEN_CAMEAR_REQUEST_CODE:   //拍照权限申请返回
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    mTempPhotoPath = GetPhotoUtil.takePhoto(this);
                }else{
                    Toast.makeText(getApplicationContext(), "相机权限和读写SD卡申请失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionUtil.EXTERNAL_STORAGE_REQUEST_CODE:   //相册选择照片权限申请返回
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    GetPhotoUtil.choosePhoto(this);
                }else{
                    Toast.makeText(getApplicationContext(), "读写SD卡权限申请失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                        RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                        //将照片显示在 ivImage上
                        Glide.with(this).load(filePath).apply(requestOptions).into(path);
                        newScene.setPath(filePath);
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
        // 设置图片压缩质量
//        options.setCompressionQuality(100);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.brown_deep));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.brown_deep));
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了返回键
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            finish();
        }
        return true;
    }
}