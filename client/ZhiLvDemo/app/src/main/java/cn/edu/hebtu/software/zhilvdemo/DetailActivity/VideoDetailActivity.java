package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.Data.MoreDetail;
import cn.edu.hebtu.software.zhilvdemo.Data.Video;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.AssetsUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;
import cn.edu.hebtu.software.zhilvdemo.ViewCustom.InnerScrollListView;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.CaseMap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
    private Video video;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    finish();
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        data = (MyApplication)getApplication();

        getViews();
        registListener();
        if(null != data.getUser()){
            addMenu();
        }
        initData();


    }

    private void initData(){
        Intent intent = getIntent();
        video = intent.getParcelableExtra("video");
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this).load("http://"+data.getIp()+":8080/ZhiLvProject/"+video.getUser().getUserHead()).apply(options).into(userHeadImg);
        userName.setText(video.getUser().getUserName());
        videoTitle.setText(video.getTitle());
        videoContent.setText(video.getContent());
        videoLocation.setText(video.getLocation());
        if(null != video.getTopic()) {
            videoTopic.setText("#"+video.getTopic().getTitle()+"#");
        }
        //more detail
        MoreDetail detail = video.getDetail();
        destination.setText(detail.getDestination());
        traffic.setText(detail.getTraffic());
        beginDate.setText(DateUtil.getDateStr(detail.getBeginDate()));
        if(null != detail.getDays()){
            days.setText(detail.getDays() +"");
        }
        people.setText(detail.getPeople());
        if(null != detail.getMoney()) {
            money.setText(detail.getMoney() + "");
        }
        //视频
        //解决退出后仍播放问题
        isPlayResume = true;
        //点击全屏实现横屏
        JZVideoPlayerStandard.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        videoPlayer.setUp("http://"+data.getIp()+":8080/ZhiLvProject/"+video.getPath(),
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                "");
//        videoPlayer.thumbImageView.setImageBitma();
        //实现重力感应下横屏切换
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new JZVideoPlayer.JZAutoFullscreenListener();
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
                        intent.putExtra("video", video);
                        startActivity(intent);
                        break;
                    case R.id.menu_delete:
                        deleteVideo();
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

    private void deleteVideo(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    if(DetermineConnServer.isConnByHttp(VideoDetailActivity.this)) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/video/delete?videoId="+video.getVideoId() );
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String info = reader.readLine();
                        if("OK".equals(info)){
                            msg.what = 1002;
                        }else{
                            msg.obj = "删除失败";
                        }
                    }else {
                        msg.obj = "未连接到服务器";
                    }
                    mHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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