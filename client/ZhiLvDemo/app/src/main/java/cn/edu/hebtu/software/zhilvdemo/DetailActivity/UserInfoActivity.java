package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.UploadAndDownload.UploadUserMsg;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.FileUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.FinalVariableUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.GetPhotoUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.PermissionUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ProjectName:    ZhiLv
 * @Description:    修改个人信息页
 * @Author:         张璐婷
 * @CreateDate:     2020/12/15 15:35
 * @Version:        1.0
 */
public class UserInfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button submit;
    private LinearLayout changeHead;
    private ImageView userHead;
    private EditText userName;
    private RadioGroup radioGroup;
    private RadioButton girl;
    private RadioButton boy;
    private TextView userBirth;
    private EditText userSign;
    private Button btnTakePhoto;
    private Button btnChooseFromAlbum;
    private Button btnCancel;

    private View v;
    private TimePickerView pvTime; //时间选择器对象
    private PopupWindow popupWindow;
    private String mTempPhotoPath;
    private MyApplication data;
    private User currentUser;
    private User user;
    private String sex;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        data = (MyApplication)getApplication();
        getViews();
        registListener();
        currentUser = data.getUser();
        user = new User();
        user.setUserId(currentUser.getUserId());
        user.setUserName(currentUser.getUserName());
        user.setBirth(currentUser.getBirth());
        user.setSex(currentUser.getSex());
        user.setSignature(currentUser.getSignature());

        userName.setText(user.getUserName());
        userSign.setText(user.getSignature());
        userBirth.setText(DateUtil.getDateStr(user.getBirth()));
        if ("girl".equals(user.getSex())) {
            girl.setSelected(true);
            girl.setChecked(true);
            sex = "girl";
        } else if ("boy".equals(user.getSex())) {
            boy.setSelected(true);
            boy.setChecked(true);
            sex = "boy";
        }
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this).load("http://" + data.getIp() + ":8080/ZhiLvProject/" + currentUser.getUserHead()).apply(options).into(userHead);

    }

    private void getViews(){
        toolbar = findViewById(R.id.info_toolbar);
        submit = findViewById(R.id.info_btnSubmit);
        changeHead = findViewById(R.id.info_changeHeader);
        userHead = findViewById(R.id.info_iv_userHead);
        userName = findViewById(R.id.info_edt_userName);
        radioGroup = findViewById(R.id.radioGroup);
        girl = findViewById(R.id.info_rb_girl);
        boy = findViewById(R.id.info_rb_boy);
        userBirth = findViewById(R.id.info_userBirth);
        userSign = findViewById(R.id.info_userSign);
        LayoutInflater inflater = getLayoutInflater();
        v = inflater.inflate(R.layout.popup_image_layout, null);
        btnTakePhoto = v.findViewById(R.id.take_photo);
        btnChooseFromAlbum = v.findViewById(R.id.choose_from_album);
        btnCancel = v.findViewById(R.id.btn_cancel);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void registListener(){
        CustomListener listener = new CustomListener();
        submit.setOnClickListener(listener);
        changeHead.setOnClickListener(listener);
        userBirth.setOnClickListener(listener);
        btnTakePhoto.setOnClickListener(listener);
        btnChooseFromAlbum.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                sex = radioButton.getText().toString();
            }
        });
    }

    class CustomListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.info_btnSubmit:
                    user.setUserName(userName.getText().toString());
                    user.setSignature(userSign.getText().toString());
                    user.setBirth(DateUtil.getDate(userBirth.getText().toString()));
                    user.setSex(sex);
                    if (!currentUser.getUserHead().equals(mTempPhotoPath)) {
                        user.setUserHead(mTempPhotoPath);
                    }
                    UploadUserMsg task = new UploadUserMsg(getApplicationContext(), user);
                    task.execute("http://" + data.getIp() + ":8080/ZhiLvProject/user/changeMsg");
                    break;
                case R.id.info_changeHeader:
                    showPopupWindow();
                    break;
                case R.id.info_userBirth:
                    initTimePicker();
                    pvTime.show();
                    break;
                case R.id.take_photo:
                    if(PermissionUtil.openCameraPermission(UserInfoActivity.this)){
                        mTempPhotoPath = GetPhotoUtil.takePhoto(UserInfoActivity.this);
                        user.setUserHead(mTempPhotoPath);
                    }
                    popupWindow.dismiss();
                    break;

                case R.id.choose_from_album:
                    if(PermissionUtil.openSDCardPermission(UserInfoActivity.this)){
                        GetPhotoUtil.choosePhoto(UserInfoActivity.this);
                    }
                    popupWindow.dismiss();
                    break;
                case R.id.btn_cancel:
                    popupWindow.dismiss();
                    break;
            }
        }
    }

    //初始化时间选择器
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);//起始时间
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 12, 31);//结束时间
        pvTime = new TimePickerView.Builder(this,
                new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        //选中事件回调
                        //mTvMyBirthday 这个组件就是个TextView用来显示日期 如2020-09-08
                        userBirth.setText(getTimes(date));
                    }
                })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "", "")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    //格式化时间
    private String getTimes(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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
                    String filePath = FileUtil.getFilePathByUri(this, uri);
                    mTempPhotoPath = filePath;
                    if (!TextUtils.isEmpty(filePath)) {
                        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop();
                        //将相册图片显示在 userHead上
                        Glide.with(this).load(filePath).apply(requestOptions1).into(userHead);
                    }
                    break;
                case FinalVariableUtil.RC_TAKE_PHOTO:
                    RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).circleCrop();
                    //将照片显示在userHead上
                    Glide.with(this).load(mTempPhotoPath).apply(requestOptions).into(userHead);
                    break;
            }
        }

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