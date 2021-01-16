package cn.edu.hebtu.software.zhilvdemo.Fragment;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.AddMoreDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.AddVideoActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.SearchTopicActivity;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DensityUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.FileUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.FinalVariableUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.GetPhotoUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.PermissionUtil;

import static android.app.Activity.RESULT_OK;

/**
 * @ProjectName:    ZhiLv
 * @Description:    添加游记界面
 * @Author:         张璐婷
 * @CreateDate:     2020/12/14 19:46
 * @Version:        1.0
 */
public class AddTravelsFragment extends Fragment {
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

    private View view;
    private View popupView;
    private Button btnTakePhoto;
    private Button btnChooseFromAlbum;
    private Button btnCancel;
    private int i; //图片个数
    private String mTempPhotoPath;
    private List<String> imgList = new ArrayList<>();
    private MyApplication data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_travels, null);
        i = 0;

        getViews();
        registListener();

        data = (MyApplication)getActivity().getApplicationContext();
        locationText.setText(data.getProvince() + "·" + data.getCity());


        return view;
    }


    private void getViews(){
        btnVideo = view.findViewById(R.id.add_travels_btnVideo);
        btnSubmit = view.findViewById(R.id.add_travels_btnSubmit);
        uploadImage = view.findViewById(R.id.uploadImage);
        btnMoreDetail = view.findViewById(R.id.add_btn_more_detail);
        title = view.findViewById(R.id.add_travels_et_title);
        route = view.findViewById(R.id.add_travels_et_route);
        scene = view.findViewById(R.id.add_travels_et_scene);
        ticket = view.findViewById(R.id.add_travels_et_ticket);
        hotel = view.findViewById(R.id.add_travels_et_hotel);
        tips = view.findViewById(R.id.add_travels_et_tips);
        topic = view.findViewById(R.id.addTravels_rl_topic);
        location = view.findViewById(R.id.addTravels_rl_location);
        topicText = view.findViewById(R.id.add_travels_tv_topic);
        locationText = view.findViewById(R.id.add_travels_tv_location);
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
                    intent = new Intent(getActivity().getApplicationContext(), AddVideoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.add_travels_btnSubmit:
                    break;
                case R.id.add_btn_more_detail:
                    intent = new Intent(getActivity().getApplicationContext(), AddMoreDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.addTravels_rl_topic:
                    intent = new Intent(getActivity().getApplicationContext(), SearchTopicActivity.class);
                    startActivity(intent);
                    break;
                case R.id.addTravels_rl_location:
                    break;
                case R.id.take_photo:
                    if(PermissionUtil.openCameraPermission(getActivity())){
                        mTempPhotoPath = GetPhotoUtil.takePhoto(getActivity());
                    }
                    popupWindow.dismiss();
                    break;

                case R.id.choose_from_album:
                    if(PermissionUtil.openSDCardPermission(getActivity())){
                        GetPhotoUtil.choosePhoto(getActivity());
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
        popupWindow = new PopupWindow(getActivity());
        popupWindow.setContentView(popupView);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        HorizontalScrollView parent = view.findViewById(R.id.parent);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM,0,0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.OPEN_CAMEAR_REQUEST_CODE:   //拍照权限申请返回
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    mTempPhotoPath = GetPhotoUtil.takePhoto(getActivity());
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "相机权限和读写SD卡申请失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case PermissionUtil.EXTERNAL_STORAGE_REQUEST_CODE:   //相册选择照片权限申请返回
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    GetPhotoUtil.choosePhoto(getActivity());
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "读写SD卡权限申请失败", Toast.LENGTH_SHORT).show();
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
                    String southFilePath = FileUtil.getFilePathByUri(getActivity(), uri);
                    startUCrop(getActivity(),southFilePath,UCrop.REQUEST_CROP,16,9);
                    break;
                case FinalVariableUtil.RC_TAKE_PHOTO:
                    startUCrop(getActivity(),mTempPhotoPath,UCrop.REQUEST_CROP,16,9);
                    break;
                case UCrop.REQUEST_CROP: {
                    // 裁剪照片
                    final Uri croppedUri = UCrop.getOutput(data);
                    String filePath = FileUtil.getFilePathByUri(getActivity(), croppedUri);
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
        final LinearLayout layout = view.findViewById(R.id.layout_img);
        final RelativeLayout relativeLayout = new RelativeLayout(getActivity());

        ImageView img = new ImageView(getActivity());
        img.setId(i);
        Glide.with(this).load(filePath).apply(requestOptions).into(img);
        RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(DensityUtil.dip2px(getActivity().getApplicationContext(),100),DensityUtil.dip2px(getActivity().getApplicationContext(),100));
        param1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayout.addView(img,param1);

        ImageButton deleteImg = new ImageButton(getActivity());
        deleteImg.setId(i);
        deleteImg.setBackgroundResource(R.mipmap.add_travels_deleteimg);
        RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relativeLayout.addView(deleteImg,param2);

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(DensityUtil.dip2px(getActivity().getApplicationContext(),110),DensityUtil.dip2px(getActivity().getApplicationContext(),110));
        layout.addView(relativeLayout,param);
        imgList.add(filePath);
        //        Log.e("filepath=", filePath);

        //设置删除按钮的监听器
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getId();
                layout.removeView(relativeLayout);
                imgList.remove(filePath);
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
            topicText.setText(data.getTopic());
            data.setTopic(null);
        }
        super.onResume();

    }
}
