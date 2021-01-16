package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Adapter.TopicListAdapter;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class ShowTopicActivity extends AppCompatActivity {
    private SearchView searchView;
    private List<String> mDatas;
    private MyApplication data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_topic);
        initData();
        initView();
    }

    private void initView(){
        RecyclerView recyclerView = findViewById(R.id.topic_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        TopicListAdapter adapter = new TopicListAdapter(this, R.layout.item_show_topic, mDatas);
        adapter.setOnItemClickListener(new TopicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ShowTopicActivity.this,TopicDetailActivity.class);
                intent.putExtra("topic", "话题详情页");
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        //查找
        searchView = findViewById(R.id.searchView);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);//让键盘的回车键设置成搜索
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("searchTopic", "submit");
                //清除焦点，收软键盘
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("searchTopic", "change");
                return false;
            }
        });
        //返回
        ImageButton back = findViewById(R.id.topic_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){
        mDatas = new ArrayList<>();
        for(int i = 0; i < 15; ++i){
            mDatas.add("topic--->" + i);
        }
    }
}