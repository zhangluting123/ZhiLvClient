package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.Data.Scene;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DensityUtil;
import cn.edu.hebtu.software.zhilvdemo.ViewCustom.WarpLinearLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class SceneDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView path;
    private TextView content;
    private TextView rule;
    private TextView openTime;
    private TextView traffic;
    private TextView ticket;
    private TextView costTime;
    private TextView phone;
    private LinearLayout website;
    private WarpLinearLayout msg;
    private LinearLayout updateSceneByUser;
    private TextView updateSceneTime;

    private Scene scene;
    private MyApplication data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_detail);
        data = (MyApplication)getApplication();
        getViews();
        if(null != data.getUser()){
            addMenu();
        }
        initData();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(scene.getWebsite());    //设置跳转的网站
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    private void initData(){
        Intent intent = getIntent();
        scene = intent.getParcelableExtra("scene");
        Glide.with(this).load("http://"+data.getIp()+":8080/ZhiLvProject/"+scene.getPath()).into(path);
        content.setText(scene.getContent());
        rule.setText(scene.getRule());
        openTime.setText(scene.getOpenTime());
        traffic.setText(scene.getTraffic());
        ticket.setText(scene.getTicket());
        costTime.setText(scene.getCostTime());
        phone.setText(scene.getPhone());
        if(null != scene.getSceneUpdate() && scene.getSceneUpdate().size() > 0) {
            int size = scene.getSceneUpdate().size();
            updateSceneTime.setText(DateUtil.getDateTimeStr(scene.getSceneUpdate().get(0).getUpdateTime()));
            for(int i = 0 ;i < size; ++i){
                User user = scene.getSceneUpdate().get(i).getUser();
                RequestOptions options = new RequestOptions().circleCrop();
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DensityUtil.dip2px(this, 18),DensityUtil.dip2px(this, 18));
                ImageView img = new ImageView(this);
                Glide.with(this).load("http://"+data.getIp()+":8080/ZhiLvProject/"+user.getUserHead()).apply(options).into(img);
                updateSceneByUser.addView(img,params);
                ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView textView = new TextView(this);
                textView.setText(user.getUserName());
                updateSceneByUser.addView(textView,params2);
            }
        }else{
            msg.setVisibility(View.GONE);
        }
    }
    private void getViews(){
        toolbar = findViewById(R.id.toolbar);
        path = findViewById(R.id.scene_iv_path);
        content = findViewById(R.id.scene_tv_content);
        rule = findViewById(R.id.scene_tv_rule);
        openTime = findViewById(R.id.scene_tv_openTime);
        traffic = findViewById(R.id.scene_tv_traffic);
        ticket= findViewById(R.id.scene_tv_ticket);
        costTime = findViewById(R.id.scene_tv_costTime);
        phone = findViewById(R.id.scene_tv_phone);
        website = findViewById(R.id.scene_ll_website);
        msg = findViewById(R.id.scene_ll_msg);
        updateSceneByUser = findViewById(R.id.scene_ll_userMsg);
        updateSceneTime = findViewById(R.id.scene_tv_updateSceneTime);
    }

    private void addMenu(){
        toolbar.inflateMenu(R.menu.menu_edit);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_update:
                        Intent intent = new Intent(SceneDetailActivity.this,EditSceneDetailActivity.class);
                        intent.putExtra("scene", scene);
                        startActivity(intent);
                        break;
                    case R.id.share:
                        shareScene(scene);
                        break;
                }
                return true;
            }
        });
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/12/23  16:07
     *  @Description: 分享景点
     */
    private void shareScene(Scene scene) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String path  = "http://"+data.getIp()+":8080/ZhiLvProject/"+scene.getPath();
        shareIntent.putExtra(Intent.EXTRA_TEXT,"知旅景点推荐: "+scene.getTitle()+"\n"
                +"简介："+scene.getContent()+"\n"
                +"规则："+scene.getRule()+"\n"
                +"开放时间"+scene.getOpenTime()+"\n"
                +"图片链接："+path);
        startActivity(shareIntent);
    }
}