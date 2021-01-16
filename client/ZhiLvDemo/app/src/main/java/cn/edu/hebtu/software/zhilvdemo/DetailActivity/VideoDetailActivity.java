package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.AssetsUtil;
import cn.edu.hebtu.software.zhilvdemo.ViewCustom.InnerScrollListView;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private JZVideoPlayerStandard videoPlayer;
    private TextView videoTitle;
    private TextView videoContent;
    private ImageView userHeadImg;
    private TextView userName;
    private TextView videoTopic;
    private TextView videoLocation;
    //more detail
    private TextView destination;
    private TextView traffic;
    private TextView beginDate;
    private TextView days;
    private TextView people;
    private TextView money;
    //comment list
    private TextView commentCount;
    private LinearLayout noComment;
    private InnerScrollListView commentList;
    //click msg
    private ImageView ivComment;
    private ImageView ivGood;
    private ImageView ivStar;
    private LinearLayout llComment;
    private LinearLayout llClick;
    private EditText edtInsertComment;
    private Button submitComment;

    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Boolean isPlayResume ;

    private MyApplication data;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        data = (MyApplication)getApplication();

        getViews();
        registListener();
        addMenu();

        //将assets目录文件放到file文件夹中
        AssetsUtil.copyAssetsDB(this);// "//data//data//cn.edu.hebtu.software.zhilvdemo//files"

        //解决退出后仍播放问题
        isPlayResume = true;
        //点击全屏实现横屏
        JZVideoPlayerStandard.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        videoPlayer.setUp("//data//data//cn.edu.hebtu.software.zhilvdemo//files//test-video.mp4",
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                "");
        videoPlayer.thumbImageView.setImageBitmap(getVideoThumbnail(null));
        //实现重力感应下横屏切换
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JZVideoPlayer.JZAutoFullscreenListener();
    }


    // 获取本地视频第一帧图片
    public Bitmap getVideoThumbnail(String url) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource("//data//data//cn.edu.hebtu.software.zhilvdemo//files//test-video.mp4");
            //获得第一帧原尺寸图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            Log.e("getVideoThumbnail", "catch");
            e.printStackTrace();
        } finally {
            retriever.release();
        }
// 生成视频缩略图
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(this, Uri.parse("//data//data//cn.edu.hebtu.software.zhilvdemo//files//test-video.mp4"));
//        bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        return bitmap;
    }

    private void getViews(){
        videoPlayer = findViewById(R.id.videoplayer);
        videoTitle = findViewById(R.id.video_detail_tv_title);
        videoContent = findViewById(R.id.video_detail_tv_content);
        userHeadImg = findViewById(R.id.video_detail_iv_headImg);
        userName = findViewById(R.id.video_detail_tv_userName);
        videoTopic = findViewById(R.id.video_detail_tv_topic);
        videoLocation = findViewById(R.id.video_detail_tv_location);
        //more detail
        destination = findViewById(R.id.more_detail_tv_destination);
        traffic = findViewById(R.id.more_detail_tv_traffic);
        beginDate = findViewById(R.id.more_detail_tv_beginDate);
        days = findViewById(R.id.more_detail_tv_days);
        people = findViewById(R.id.more_detail_tv_people);
        money = findViewById(R.id.more_detail_days_money);
        //comment list
        commentCount = findViewById(R.id.tv_commentCount);
        noComment = findViewById(R.id.ll_noComment);
        commentList = findViewById(R.id.comment_list);
        //click msg
        ivComment = findViewById(R.id.iv_comment);
        ivGood = findViewById(R.id.iv_good);
        ivStar = findViewById(R.id.iv_star);
        llComment = findViewById(R.id.ll_comment);
        llClick = findViewById(R.id.ll_click);
        edtInsertComment = findViewById(R.id.edt_insertComment);
        submitComment = findViewById(R.id.btn_submitComment);
    }

    private void addMenu(){
        toolbar.inflateMenu(R.menu.menu_drawer);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_update:
                        Intent intent = new Intent(VideoDetailActivity.this, UpdateVideoActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_delete:

                        break;
                }
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        userHeadImg.setOnClickListener(listener);
        ivComment.setOnClickListener(listener);
        ivGood.setOnClickListener(listener);
        ivStar.setOnClickListener(listener);
        submitComment.setOnClickListener(listener);
        toolbar = findViewById(R.id.toolbar);
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
            switch (v.getId()){
                case R.id.video_detail_iv_headImg:
                    Intent intent = new Intent(VideoDetailActivity.this, OtherUserInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_comment:
                    llComment.setVisibility(View.VISIBLE);
                    llClick.setVisibility(View.GONE);
                    break;
                case R.id.iv_good:
                    if(ivGood.getTag().equals("good")){
                        ivGood.setTag("nogood");
                        ivGood.setImageResource(R.drawable.good_noselected);
                    }else{
                        ivGood.setTag("good");
                        ivGood.setImageResource(R.drawable.good_selected);
                    }
                    break;
                case R.id.iv_star:
                    if(ivStar.getTag().equals("star")){
                        ivStar.setTag("nostar");
                        ivStar.setImageResource(R.drawable.star_noselected);
                    }else{
                        ivStar.setTag("star");
                        ivStar.setImageResource(R.drawable.star_selected);
                    }
                    break;
                case R.id.btn_submitComment:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayerStandard.goOnPlayOnPause();
        isPlayResume = true;
        sensorManager.unregisterListener(sensorEventListener);
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlayResume) {
            JZVideoPlayerStandard.goOnPlayOnResume();
            isPlayResume = false;
        }
        //播放器重力感应
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoPlayer != null) {
            videoPlayer.release();
        }
        JZVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(llComment.getVisibility() == View.VISIBLE){
                llClick.setVisibility(View.VISIBLE);
                llComment.setVisibility(View.GONE);
            }else{
                finish();
            }
        }
        return true;
    }
}