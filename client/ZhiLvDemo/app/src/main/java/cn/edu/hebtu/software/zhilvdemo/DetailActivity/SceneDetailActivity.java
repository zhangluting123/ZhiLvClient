package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SceneDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title;
    private TextView content;
    private TextView rule;
    private TextView openTime;
    private TextView traffic;
    private TextView ticket;
    private TextView costTime;
    private TextView phone;
    private LinearLayout website;
    private TextView updateSceneByUser;
    private TextView updateSceneTime;
    Time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_detail);
        getViews();
        addMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.baidu.com");    //设置跳转的网站
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    
    private void getViews(){
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.scene_tv_title);
        content = findViewById(R.id.scene_tv_content);
        rule = findViewById(R.id.scene_tv_rule);
        openTime = findViewById(R.id.scene_tv_openTime);
        traffic = findViewById(R.id.scene_tv_traffic);
        ticket= findViewById(R.id.scene_tv_ticket);
        costTime = findViewById(R.id.scene_tv_costTime);
        phone = findViewById(R.id.scene_tv_phone);
        website = findViewById(R.id.scene_ll_website);
        updateSceneByUser = findViewById(R.id.scene_tv_updateSceneByUser);
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
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }
}