package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.zhilvdemo.Adapter.CommentAdapter;
import cn.edu.hebtu.software.zhilvdemo.Adapter.GuidePageAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.Comment;
import cn.edu.hebtu.software.zhilvdemo.Data.MoreDetail;
import cn.edu.hebtu.software.zhilvdemo.Data.Travels;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;
import cn.edu.hebtu.software.zhilvdemo.Util.SoftKeyBoardListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mob.tools.RxMob;

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

public class TravelDetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private Toolbar toolbar;
    private TextView time;
    private TextView title;
    private ImageView userHeadImg;
    private TextView userName;
    private TextView location;
    private TextView topic;
    private TextView route;
    private TextView scene;
    private TextView ticket;
    private TextView hotel;
    private TextView tips;
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
    // viewpager img
    private ViewPager viewPager;
    private LinearLayout vg;
    private List<View> viewList;
    private ImageView point;
    private ImageView[] ivPointArray;

    private MyApplication data;
    private Travels travels;
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);

        data = (MyApplication)getApplication();
        getViews();
        registListener();
        //判断是否是当前用户，是则有菜单功能
        if(null != data.getUser()){
            addMenu();
        }

        initData();
        //获得评论
        getComments();
    }

    private void initData(){
        Intent intent = getIntent();
        travels = intent.getParcelableExtra("travels");
        time.setText(DateUtil.getDateTimeStr(travels.getUploadTime()));
        title.setText(travels.getTitle());
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this).load("http://"+data.getIp()+":8080/ZhiLvProject/"+travels.getUser().getUserHead()).apply(options).into(userHeadImg);
        userName.setText(travels.getUser().getUserName());
        location.setText(travels.getLocation());
        if(null != travels.getTopic()) {
            topic.setText("#"+travels.getTopic().getTitle()+"#");
        }
        route.setText(travels.getRoute());
        scene.setText(travels.getScene());
        ticket.setText(travels.getTicket());
        hotel.setText(travels.getHotel());
        tips.setText(travels.getTips());
        //more detail
        MoreDetail detail = travels.getDetail();
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
        //img
        //加载ViewPager
        initViewPager();
        //加载底部圆点
        if(travels.getImgList().size() > 1){
            initPoint();
        }
    }

    private void getViews(){
        toolbar = findViewById(R.id.travel_detail_toolbar);
        time = findViewById(R.id.travel_detail_tv_time);
        title = findViewById(R.id.travel_detail_tv_title);
        userHeadImg = findViewById(R.id.travel_detail_iv_headImg);
        userName = findViewById(R.id.travel_detail_tv_userName);
        location = findViewById(R.id.travel_detail_tv_location);
        topic = findViewById(R.id.travel_detail_tv_topic);
        route = findViewById(R.id.travel_detail_tv_route);
        scene = findViewById(R.id.travel_detail_tv_scene);
        ticket = findViewById(R.id.travel_detail_tv_ticket);
        hotel = findViewById(R.id.travel_detail_tv_hotel);
        tips = findViewById(R.id.travel_detail_tv_tips);
        //viewpager img
        viewPager = findViewById(R.id.travel_detail_vp_glide);
        vg = findViewById(R.id.travel_detail_ll_point);
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
                        Intent intent = new Intent(TravelDetailActivity.this, UpdateTravelActivity.class);
                        intent.putExtra("travels",travels);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menu_delete:
                        deleteTravels();
                        break;
                }
                return true;
            }
        });
    }

    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        userHeadImg.setOnClickListener(listener);
        ivComment.setOnClickListener(listener);
        ivGood.setOnClickListener(listener);
        ivStar.setOnClickListener(listener);
        submitComment.setOnClickListener(listener);
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
                case R.id.travel_detail_iv_headImg:
                    Intent intent = new Intent(TravelDetailActivity.this, OtherUserInfoActivity.class);
                    intent.putExtra("other", travels.getUser());
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
                    if(null != data.getUser()) {
                        String content = edtInsertComment.getText().toString().trim();
                        if (content.length() == 0) {
                            Toast.makeText(TravelDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
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
        SoftKeyBoardListener.setListener(TravelDetailActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
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

    /**
     * 加载底部圆点
     */
    private void initPoint() {
        //根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        int size = travels.getImgList().size();
        for (int i = 0;i < size; i++){
            point = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30,30);
            //第一个页面需要设置为选中状态，这里采用两张不同的图片
            if (i == 0){
                point.setBackgroundResource(R.mipmap.ic_page_indicator_focused);
            }else{
                layoutParams.leftMargin=20;
                point.setBackgroundResource(R.mipmap.ic_page_indicator);
            }
            point.setLayoutParams(layoutParams);
            point.setPadding(40,0,40,0);//left,top,right,bottom
            ivPointArray[i] = point;

            //将数组中的ImageView加入到LinearLayout中
            vg.addView(ivPointArray[i]);
        }
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/2  15:05
     *  @Description: 加载图片
     */
    private void initViewPager() {
        viewList = new ArrayList<>();
        if(travels.getImgList().size() <= 0){
            ImageView imageView = new ImageView(this);
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(R.mipmap.default_bg)
                    .into(new SimpleTarget<Bitmap>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(resource);
                            imageView.setBackground(drawable);    //设置背景
                        }
                    });

            //将ImageView加入到集合中
            viewList.add(imageView);
        }else{
            for (int i = 0;i < travels.getImgList().size();i++){
                //new ImageView并设置图片资源
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load("http://"+data.getIp()+":8080/ZhiLvProject/"+travels.getImgList().get(i).getPath())
                        .into(imageView);

                //将ImageView加入到集合中
                viewList.add(imageView);
            }
        }
        //View集合初始化好后，设置Adapter
        viewPager.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) {
        //循环设置当前页的标记图
        int length = travels.getImgList().size();
        for (int i = 0; i < length; i++){
            ivPointArray[position].setBackgroundResource(R.mipmap.ic_page_indicator_focused);
            if (position != i){
                ivPointArray[i].setBackgroundResource(R.mipmap.ic_page_indicator);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

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

    private void deleteTravels(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    if(DetermineConnServer.isConnByHttp(TravelDetailActivity.this)) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/travels/delete?travelsId="+travels.getTravelsId() );
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
                        URL url = new URL("http://"+data.getIp()+":8080/ZhiLvProject/comment/add?travelsId="+travels.getTravelsId()+"&userId="+data.getUser().getUserId()+"&commentContent="+content);
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
                        URL url = new URL("http://"+data.getIp()+":8080/ZhiLvProject/comment/list?travelsId="+travels.getTravelsId());
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
}