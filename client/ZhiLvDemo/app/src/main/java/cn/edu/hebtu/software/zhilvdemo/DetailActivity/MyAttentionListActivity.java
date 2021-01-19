package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Adapter.MyAttentionListAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.R;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
/**
 * @ProjectName:    ZhiLv
 * @Description:    我的关注列表
 * @Author:         张璐婷
 * @CreateDate:     2020/12/18 14:02
 * @Version:        1.0
 */
public class MyAttentionListActivity extends AppCompatActivity {
    private List<User> mDatas;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention_list);

        Toolbar toolbar = findViewById(R.id.attention_toolbar);
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
        Intent intent = getIntent();
        mDatas = intent.getParcelableArrayListExtra("attenList");
    }

    private void initView(){
        RecyclerView rl = findViewById(R.id.attention_recycler);

        //纵向列表
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        rl.setLayoutManager(manager);
        //设置适配器
        MyAttentionListAdapter adapter = new MyAttentionListAdapter(this, R.layout.item_my_attention_list, mDatas,true);
        adapter.setOnItemClickListener(new MyAttentionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MyAttentionListActivity.this, OtherUserInfoActivity.class);
                intent.putExtra("other", mDatas.get(position));
                startActivity(intent);
            }
        });
        rl.setAdapter(adapter);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            finish();
        }
        return true;
    }

}