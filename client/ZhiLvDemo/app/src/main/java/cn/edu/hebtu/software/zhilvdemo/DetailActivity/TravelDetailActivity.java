package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.zhilvdemo.Adapter.GuidePageAdapter;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.ViewCustom.InnerScrollListView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
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
    private InnerScrollListView commentList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);

        data = (MyApplication)getApplication();

        getViews();
        registListener();
        //加载ViewPager
        initViewPager();
        //加载底部圆点
        initPoint();
        //判断是否是当前用户，是用户则有菜单功能
        addMenu();


        //获得评论
//        getComments();
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
                        Intent intent = new Intent(TravelDetailActivity.this, UpdateTravelActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menu_delete:

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


    /**
     * 加载底部圆点
     */
    private void initPoint() {
        //根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
//        int size = note.getImgList().size();
        //TODO ============
        int size = 3;
        for (int i = 0;i<size;i++){
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
        //TODO ========================
        for (int i = 0; i < 3; ++i) {
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
        }


//        if(note.getImgList().size() <= 0){
//            ImageView imageView = new ImageView(this);
//            Glide.with(getApplicationContext())
//                    .asBitmap()
//                    .load(R.mipmap.default_bg)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            Drawable drawable = new BitmapDrawable(resource);
//                            imageView.setBackground(drawable);    //设置背景
//                        }
//                    });
//
//            //将ImageView加入到集合中
//            viewList.add(imageView);
//        }else{
//            for (int i = 0;i < note.getImgList().size();i++){
//                //new ImageView并设置图片资源
//                ImageView imageView = new ImageView(this);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                Glide.with(getApplicationContext())
//                        .asBitmap()
//                        .load("http://"+ ip +":8080/MoJi/"+note.getImgList().get(i))
//                        .into(imageView);
//
//                //将ImageView加入到集合中
//                viewList.add(imageView);
//            }
//    }


        //View集合初始化好后，设置Adapter
        viewPager.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        viewPager.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //循环设置当前页的标记图
//        int length = note.getImgList().size();
        int length = 3;
        for (int i = 0;i<length;i++){
            ivPointArray[position].setBackgroundResource(R.mipmap.ic_page_indicator_focused);
            if (position != i){
                ivPointArray[i].setBackgroundResource(R.mipmap.ic_page_indicator);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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