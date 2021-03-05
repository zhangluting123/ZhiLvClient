package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.Adapter.CommentAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.Comment;
import cn.edu.hebtu.software.zhilvdemo.Data.MoreDetail;
import cn.edu.hebtu.software.zhilvdemo.Data.Video;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;
import cn.edu.hebtu.software.zhilvdemo.Util.SoftKeyBoardListener;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private CommentAdapter commentAdapter;
    private Integer commentId;//当前选中评论ID
    private List<Comment> commentList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1010:
                    finish();
                    break;
                case 1002:
                    commentList = (List<Comment>)msg.obj;
                    initComment();
                    break;
                case 1003:
                    if(commentList.size() <= 0) { //之前没有评论，初始化adapter
                        noComment.setVisibility(View.GONE);
                        showCommentList();
                    }
                    Comment comment = new Comment();
                    comment.setId((Integer.parseInt((String) msg.obj)));
                    comment.setUser(data.getUser());
                    comment.setContent(edtInsertComment.getText().toString().trim());
                    comment.setTime(new Date(System.currentTimeMillis()));
                    comment.setReplyCount(0);
                    commentList.add(comment);
                    commentAdapter.flush(commentList);
                    int b = Integer.parseInt(commentCount.getText().toString());
                    b++;
                    commentCount.setText(b+"");
                    clearInput();
                    break;
                case 1004:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    for(int i = 0;i < commentList.size();++i){
                        if(commentList.get(i).getId().equals(commentId)){
                            Integer a = commentList.get(i).getReplyCount();
                            commentList.get(i).setReplyCount(++a);
                            break;
                        }
                    }
                    commentAdapter.flush(commentList);
                    clearInput();
                    break;
                case 1101:
                    ivGood.setTag("good");
                    ivGood.setImageResource(R.drawable.good_selected);
                    break;
                case 1102:
                    ivStar.setTag("star");
                    ivStar.setImageResource(R.drawable.star_selected);
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
        initData();
        if(null != data.getUser() && data.getUser().getUserId() == video.getUser().getUserId()){
            addMenu();
        }
        getComments();
        if(null != data.getUser()){
            ifGoodOrCollect("good/ifGood");
            ifGoodOrCollect("collection/ifCollect");
        }
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
                        AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(VideoDetailActivity.this);
                        normalDialog.setIcon(R.mipmap.logo_clock);
                        normalDialog.setTitle("知旅提示");
                        normalDialog.setMessage("您确定要删除视频吗?");
                        normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteVideo();
                                    }
                                });
                        normalDialog.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                        normalDialog.show();
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
                    intent.putExtra("other", video.getUser());
                    startActivity(intent);
                    break;
                case R.id.iv_comment:
                    llComment.setVisibility(View.VISIBLE);
                    llClick.setVisibility(View.GONE);
                    break;
                case R.id.iv_good:
                    if(null != data.getUser()){
                        if(ivGood.getTag().equals("good")){
                            ivGood.setTag("nogood");
                            ivGood.setImageResource(R.drawable.good_noselected);
                            deleteGoodOrCollect("good/delete");
                        }else{
                            ivGood.setTag("good");
                            ivGood.setImageResource(R.drawable.good_selected);
                            addGoodOrCollect("good/add");
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.iv_star:
                    if(null != data.getUser()){
                        if(ivStar.getTag().equals("star")){
                            ivStar.setTag("nostar");
                            ivStar.setImageResource(R.drawable.star_noselected);
                            deleteGoodOrCollect("collection/delete");
                        }else{
                            ivStar.setTag("star");
                            ivStar.setImageResource(R.drawable.star_selected);
                            addGoodOrCollect("collection/add");
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_submitComment:
                    if(null != data.getUser()) {
                        String content = edtInsertComment.getText().toString().trim();
                        if (content.length() == 0) {
                            Toast.makeText(VideoDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            if(edtInsertComment.getHint().equals("请输入评论")) {
                                insertComment(content);
                            }else{
                                //回复评论
                                insertReplyComment();
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/28  12:25
     *  @Description: 在页面添加获取的评论
     */
    private void initComment(){
        if(commentList.size() <= 0){
            commentCount.setText("0");
            noComment();
        }else{
            commentCount.setText(commentList.size()+"");
            showCommentList();
        }
    }
    /**
     *  @author: 张璐婷
     *  @time: 2021/1/28  12:25
     *  @Description: 没有评论
     */
    private void noComment(){
        noComment.setVisibility(View.VISIBLE);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/28  12:25
     *  @Description: 展示评论列表
     */
    private void showCommentList(){
        ListView listView = findViewById(R.id.comment_list);
        commentAdapter = new CommentAdapter(commentList, R.layout.item_comment, getApplicationContext());
        listView.setAdapter(commentAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                llComment.setVisibility(View.VISIBLE);
                llClick.setVisibility(View.GONE);
                edtInsertComment.setHint("回复@"+commentList.get(position).getUser().getUserName());
                edtInsertComment.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
                commentId = commentList.get(position).getId();
                edtInsertComment.setSelection(0);
                edtInsertComment.setFocusable(true);
                //键盘如果关闭弹出
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/28  12:27
     *  @Description: 触屏操作
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onKeyBoardListener();
        return super.dispatchTouchEvent(ev);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/28  12:27
     *  @Description:  清空输入内容,键盘隐藏
     */
    private void clearInput(){
        edtInsertComment.setText("");
        edtInsertComment.setHint("请输入评论");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/28  12:27
     *  @Description: 监听键盘是否弹起
     */
    private void onKeyBoardListener(){
        SoftKeyBoardListener.setListener(VideoDetailActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
//                Log.e("软键盘显示高度", height+"");
            }

            @Override
            public void keyBoardHide(int height) {
//                Log.e("软键盘隐藏高度", height+"");
                if(!edtInsertComment.getHint().equals("请输入评论")) {
                    edtInsertComment.setHint("请输入评论");
                }
            }
        });
    }

    private void ifGoodOrCollect(String str){
        new Thread(){
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    if(DetermineConnServer.isConnByHttp(VideoDetailActivity.this)) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/"+str+"?userId="+data.getUser().getUserId()+"&videoId="+video.getVideoId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String info = reader.readLine();
                        if(str.contains("good") && "TRUE".equals(info)){
                            msg.what = 1101;
                        }else if(str.contains("collection") && "TRUE".equals(info)){
                            msg.what = 1102;
                        }
                    }else {
                        msg.what = 1001;
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

    private void addGoodOrCollect(String str){
        new Thread(){
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    if(DetermineConnServer.isConnByHttp(VideoDetailActivity.this)) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/"+str+"?userId="+data.getUser().getUserId()+"&videoId="+video.getVideoId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String info = reader.readLine();
                        if("ERROR".equals(info)){
                            msg.what = 1001;
                            msg.obj = "添加失败";
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

    private void deleteGoodOrCollect(String str){
        new Thread(){
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    if(DetermineConnServer.isConnByHttp(VideoDetailActivity.this)) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/"+str+"?userId="+data.getUser().getUserId()+"&videoId="+video.getVideoId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String info = reader.readLine();
                        if("ERROR".equals(info)){
                            msg.what = 1001;
                            msg.obj = "取消失败";
                        }
                    }else {
                        msg.what = 1001;
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
                            msg.what = 1010;
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

    /**
     *  @author: 张璐婷
     *  @time: 2019/12/11  18:39
     *  @Description: 插入评论
     */
    private void insertComment(String content){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        URL url = new URL("http://"+data.getIp()+":8080/ZhiLvProject/comment/add?videoId="+video.getVideoId()+"&userId="+data.getUser().getUserId()+"&commentContent="+content);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = reader.readLine();
                        Message msg = Message.obtain();
                        String[] strings = str.split(",");
                        if("OK".equals(strings[0])){
                            msg.what = 1003;
                            msg.obj = strings[1];
                            mHandler.sendMessage(msg);
                        }else{
                            msg.obj = "评论发布失败";
                            msg.what = 1001;
                            mHandler.sendMessage(msg);
                        }

                        in.close();
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Message message  = Message.obtain();
                    message.obj = "未连接到服务器";
                    message.what = 1001;
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     *  @author: 张璐婷
     *  @time: 2019/12/11  18:39
     *  @Description: 获得评论
     */
    private void getComments(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        List<Comment> list = new ArrayList<>();
                        URL url = new URL("http://"+data.getIp()+":8080/ZhiLvProject/comment/list?videoId="+video.getVideoId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = null;
                        if((str = reader.readLine()) != null){
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Type type = new TypeToken<List<Comment>>(){}.getType();
                            list = gson.fromJson(str,type);
                        }
                        Message msg = Message.obtain();
                        msg.what = 1002;
                        msg.obj = list;
                        mHandler.sendMessage(msg);

                        in.close();
                        reader.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    Message message  = Message.obtain();
                    message.what = 1001;
                    message.obj = "未连接到服务器";
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/20  16:37
     *  @Description: 回复评论
     */
    public void insertReplyComment(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        URL url = new URL("http://"+data.getIp()+":8080/ZhiLvProject/reply/add?replyContent="+edtInsertComment.getText().toString()+"&commentId="+commentId+"&replyUserId="+data.getUser().getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        if(reader.readLine().equals("OK")){
                            Message message = Message.obtain();
                            message.what = 1004;
                            message.obj = "回复成功";
                            mHandler.sendMessage(message);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Message message  = Message.obtain();
                    message.what = 1001;
                    message.obj = "未连接到服务器";
                    mHandler.sendMessage(message);
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