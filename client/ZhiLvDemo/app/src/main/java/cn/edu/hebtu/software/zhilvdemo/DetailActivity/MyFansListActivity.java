package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Adapter.MyAttentionListAdapter;
import cn.edu.hebtu.software.zhilvdemo.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName:    ZhiLv
 * @Description:    我的粉丝列表
 * @Author:         张璐婷
 * @CreateDate:     2020/12/18 21:55
 * @Version:        1.0
 */
public class MyFansListActivity extends AppCompatActivity {
    private List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans_list);

        Toolbar toolbar = findViewById(R.id.fans_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
        initView();
    }

    private void initData(){
        mDatas = new ArrayList<String>();
        for (int i = 0; i < 50; i++)
        {
            mDatas.add( "List -> " + i);
        }
    }

    private void initView(){
        RecyclerView rl = findViewById(R.id.fans_recycler);

        //纵向列表
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        rl.setLayoutManager(manager);
        //设置适配器
        MyAttentionListAdapter adapter = new MyAttentionListAdapter(this, R.layout.item_my_fans_list, mDatas);
        adapter.setOnItemClickListener(new MyAttentionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MyFansListActivity.this, OtherUserInfoActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Click" + mDatas.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        rl.setAdapter(adapter);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }
}