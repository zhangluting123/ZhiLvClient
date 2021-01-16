package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.Data.Video;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.FileUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.FinalVariableUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.GetPhotoUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.PermissionUtil;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class AddVideoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnSubmit;
    private ImageView videoImg;
    private Button btnMoreDetail;
    private EditText videoTitle;
    private EditText videoContent;
    private RelativeLayout videoTopic;
    private RelativeLayout videoLocation;
    private TextView videoLocationText;
    private TextView videoTopicText;

    //视频缩略图
    private Bitmap bitmap;

    private MyApplication data;
    private Video video = new Video();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        getViews();
        registListener();

        data = (MyApplication)getApplication();
        videoLocationText.setText(data.getProvince() + "·" + data.getCity());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getViews(){
        btnSubmit = findViewById(R.id.add_video_btnSubmit);
        videoImg = findViewById(R.id.add_video_bitmap);
        btnMoreDetail = findViewById(R.id.add_btn_more_detail);
        videoTitle = findViewById(R.id.add_video_et_title);
        videoContent = findViewById(R.id.add_video_et_content);
        videoTopic = findViewById(R.id.add_video_rl_topic);
        videoLocation = findViewById(R.id.add_video_rl_location);
        videoLocationText = findViewById(R.id.add_video_tv_location);
        videoTopicText = findViewById(R.id.add_video_tv_topic);
        toolbar = findViewById(R.id.toolbar);
    }

    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        btnSubmit.setOnClickListener(listener);
        videoImg.setOnClickListener(listener);
        btnMoreDetail.setOnClickListener(listener);
        videoTopic.setOnClickListener(listener);
        videoLocation.setOnClickListener(listener);
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
                case R.id.add_video_btnSubmit:
                    break;
                case R.id.add_video_bitmap:
                    if(PermissionUtil.openSDCardPermission(AddVideoActivity.this)){
                        GetPhotoUtil.chooseVideo(AddVideoActivity.this);
                    }
                    break;
                case R.id.add_btn_more_detail:
                    intent = new Intent(getApplicationContext(), AddMoreDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.add_video_rl_topic:
                    intent = new Intent(getApplicationContext(), SearchTopicActivity.class);
                    startActivity(intent);
                    break;
                case R.id.add_video_rl_location:
                    break;

            }
        }
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/1  10:44
     *  @Description: 得到选中视频中的信息
     */
    public void getVideoData(Uri uri) {
        ContentResolver cr = this.getContentResolver();
        //数据库查询操作
        //第一个参数 uri：为要查询的数据库+表的名称
        //第二个参数 projection ： 要查询的列
        //第三个参数 selection ： 查询的条件，相当于SQL where
        //第三个参数 selectionArgs ： 查询条件的参数，相当于?
        //第四个参数 sortOrder ： 结果排序
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // 视频ID:MediaStore.Audio.Media._ID
                int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                video.setVideoId(videoId+"");
                // 视频名称：MediaStore.Audio.Media.TITLE
                String videoTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                video.setTitle(videoTitle);
                // 视频路径：MediaStore.Audio.Media.DATA
                String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                video.setPath(videoPath);
                // 视频时长（默认ms）：MediaStore.Audio.Media.DURATION
                int duration_ms = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                int duration_s = (int)Math.floor(duration_ms / 1000);
                String ds = duration_s + "";
                if (duration_s < 10){
                    ds = "0" + duration_s;
                }
                int duration_m = (int)Math.floor((duration_s % 3600) / 60);
                String dm = duration_m + "";
                if (duration_m < 10){
                    dm = "0" + duration_m;
                }
                int duration_h = duration_s / 3600;
                String dh = duration_h + "";
                if (duration_h < 10){
                    dh = "0" + duration_h;
                }
                String videoDuration = dh + ":" + dm + ":" + ds;
                video.setDuration(videoDuration);
                // 视频大小（默认Byte）：MediaStore.Audio.Media.SIZE
                long size_byte = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                double size_MB = (size_byte * 1.0) / (1024 * 1024);
                String sm = new java.text.DecimalFormat("#.00").format(size_MB);
                String videoSize = sm + "MB";
                video.setSize(videoSize);
                // 生成视频缩略图
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(this,Uri.parse(videoPath));
                bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PermissionUtil.EXTERNAL_STORAGE_REQUEST_CODE:   //相册选择视频权限申请返回
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    GetPhotoUtil.chooseVideo(this);
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
                case FinalVariableUtil.RC_CHOOSE_VIDEO:
                    Uri uri = data.getData();
                    String vPath = FileUtil.getFilePathByUri(this, uri);
                    if (!TextUtils.isEmpty(vPath)) {
                        getVideoData(uri);
                        videoImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        videoImg.setImageBitmap(bitmap);
                        Log.e("video","ID：" + video.getVideoId());
                        Log.e("video", "标题：" + video.getTitle());
                        Log.e("video", "路径：" + video.getPath());
                        Log.e("video", "时长：" + video.getDuration());
                        Log.e("video", "大小：" + video.getSize());
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        if(null != data.getTopic()) {
            videoTopicText.setText(data.getTopic());
            data.setTopic(null);
        }
        super.onResume();
    }
}