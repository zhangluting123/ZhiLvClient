package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Adapter.TopicListAdapter;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DensityUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SearchTopicActivity extends AppCompatActivity {
    private SearchView searchView;
    private List<String> mDatas;
    private MyApplication data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_topic);
        data = (MyApplication) getApplication();
        initData();
        initView();


    }

    private void initView(){
        RecyclerView recyclerView = findViewById(R.id.topic_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        TopicListAdapter adapter = new TopicListAdapter(this, R.layout.item_topic, mDatas);
        adapter.setOnItemClickListener(new TopicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                data.setTopic(mDatas.get(position));
                finish();
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
        //添加话题
        ImageButton addTopic = findViewById(R.id.btn_add_topic);
        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
    }

    private void initData(){
        mDatas = new ArrayList<>();
        for(int i = 0; i < 15; ++i){
            mDatas.add("topic--->" + i);
        }
    }

    private void showInputDialog() {
        //自定义View
        LinearLayout layout = new LinearLayout(this);
        EditText editText = new EditText(this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(editText);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        int len = DensityUtil.dip2px(this, 20);
        layout.setPadding (len,len, len, len);
        //设置对话框
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(this);
        inputDialog.setIcon(R.mipmap.logo_topic);
        inputDialog.setTitle("请输入要添加的话题");
        inputDialog.setView(layout);
        inputDialog.setPositiveButton("提交审核",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(SearchTopicActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}