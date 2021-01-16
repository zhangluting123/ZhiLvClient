package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.zhilvdemo.Adapter.ChannelPagerAdapter;
import cn.edu.hebtu.software.zhilvdemo.Fragment.Mine.TravelsMineFragment;
import cn.edu.hebtu.software.zhilvdemo.R;

import android.graphics.Color;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class OtherUserInfoActivity extends AppCompatActivity {
    private ChannelPagerAdapter adapter;
    private ViewPager pages;
    private PagerSlidingTabStrip tabs;
    private List<Fragment> fragmentList;
    private String[] titles = {"游记"};

    private ImageView userSex;
    private TextView userName;
    private TextView userSign;
    private Button btnAttention;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_info);
        initTabsPager();
        getViews();

        btnAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nowMsg = btnAttention.getText().toString().trim();
                if(nowMsg.equals("+ 关注")){
                    btnAttention.setText("已关注");
                }else{
                    btnAttention.setText("+ 关注");
                }
            }
        });

    }

    private void initTabsPager(){
        pages = findViewById(R.id.pager_viewpager);
        tabs = findViewById(R.id.pager_tabs);
        pages.setOffscreenPageLimit(titles.length);
        initFragment();
        adapter = new ChannelPagerAdapter(getSupportFragmentManager(),fragmentList,titles);
        pages.setAdapter(adapter);
        //每个选项卡相同权重，必须设置在setViewPager()之前
//        tabs.setShouldExpand(true);
        //标签之间分隔线的颜色
        tabs.setDividerColor(Color.TRANSPARENT);
        //滑动指示器的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.MyThemeColor));
        //滑动指示器的高度
        tabs.setIndicatorHeight(4);
        tabs.setUnderlineColor(Color.TRANSPARENT);
        tabs.setTextSize(30);
        tabs.setViewPager(pages);
    }

    private void initFragment() {
        fragmentList = new ArrayList<Fragment>();
        Fragment travelsMineFragment = new TravelsMineFragment();
        fragmentList.add(travelsMineFragment);
    }


    private void getViews(){
        userSex = findViewById(R.id.mine_user_sex);
        userName = findViewById(R.id.mine_user_name);
        userSign = findViewById(R.id.mine_user_sign);
        btnAttention = findViewById(R.id.btn_attention);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }

}