package cn.edu.hebtu.software.zhilvdemo.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;

import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import cn.edu.hebtu.software.zhilvdemo.Data.Topic;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.DestinationDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.SceneDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.SearchTopicActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.ShowTopicActivity;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.TopicDetailActivity;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DensityUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

/**
 * @ProjectName:    ZhiLv
 * @Description:    目的地界面
 * @Author:         张璐婷
 * @CreateDate:     2020/12/14 19:48
 * @Version:        1.0
 */
public class DestinationFragment extends Fragment {
    private View view;
    private RelativeLayout head;
    private SearchView searchView;
    private TextView location;
    private TextView scene1;
    private TextView scene2;
    private TextView scene3;
    private TextView scene4;
    private Button btnMoreTopic;
    private RelativeLayout topic1;
    private TextView topicText1;
    private RelativeLayout topic2;
    private TextView topicText2;

    private MyApplication data;

    private SuggestionSearch suggestionSearch;
    private ListPopupWindow listPopupWindow;
    private boolean submitFlag = false;

    private List<Map<String,String>> sugList;
    private List<Topic> topics;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1003:
                    topics = (List<Topic>) msg.obj;
                    topicText1.setText(topics.get(0).getTitle());
                    topicText2.setText(topics.get(1).getTitle());
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == view) {
            view = inflater.inflate(R.layout.fragment_destination, null);
            data = (MyApplication)getActivity().getApplication();
            topicTwiceList();
            getViews();
            registListener();


        }

        location.setText(data.getCity());

        ViewGroup parent = (ViewGroup) view.getParent();
        if(null != parent){
            parent.removeView(view);
        }
        return view;

    }

    private void getViews(){
        head = view.findViewById(R.id.destination_head);
        searchView = view.findViewById(R.id.searchView);
        location = view.findViewById(R.id.destination_location);
        scene1 = view.findViewById(R.id.destination_tv_scene1);
        scene2 = view.findViewById(R.id.destination_tv_scene2);
        scene3 = view.findViewById(R.id.destination_tv_scene3);
        scene4 = view.findViewById(R.id.destination_tv_scene4);
        btnMoreTopic = view.findViewById(R.id.destination_btn_moreTopic);
        topic1 = view.findViewById(R.id.destination_rl_topic1);
        topicText1 = view.findViewById(R.id.destination_tv_topic1);
        topic2 = view.findViewById(R.id.destination_rl_topic2);
        topicText2 = view.findViewById(R.id.destination_tv_topic2);
    }

    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        scene1.setOnClickListener(listener);
        scene2.setOnClickListener(listener);
        scene3.setOnClickListener(listener);
        scene4.setOnClickListener(listener);
        btnMoreTopic.setOnClickListener(listener);
        topic1.setOnClickListener(listener);
        topic2.setOnClickListener(listener);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);//让键盘的回车键设置成搜索
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("searchTopic", "submit");
                //清除焦点，收软键盘
                searchView.clearFocus();
                data.setSearchText(query);
                searchView.setQueryHint(query);
                searchView.setQuery(null, false);
                Intent intent = new Intent(getActivity().getApplicationContext(), DestinationDetailActivity.class);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("searchTopic", "change");
                String text =  searchView.getQuery().toString();
                if(null != text && !text.contains("\n") && !submitFlag){
                    SugSearch();
                }
                submitFlag = false;
                return false;
            }
        });
    }

    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.destination_tv_scene1:
                    intent = new Intent(getActivity().getApplicationContext(), SceneDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.destination_tv_scene2:

                    break;
                case R.id.destination_tv_scene3:

                    break;
                case R.id.destination_tv_scene4:

                    break;
                case R.id.destination_btn_moreTopic:
                    intent = new Intent(getActivity().getApplicationContext(), ShowTopicActivity.class);
                    startActivity(intent);
                    break;
                case R.id.destination_rl_topic1:
                    intent = new Intent(getActivity().getApplicationContext(), TopicDetailActivity.class);
                    intent.putExtra("topic", topics.get(0).getTitle());
                    startActivity(intent);
                    break;
                case R.id.destination_rl_topic2:
                    intent = new Intent(getActivity().getApplicationContext(), TopicDetailActivity.class);
                    intent.putExtra("topic", topics.get(1).getTitle());
                    startActivity(intent);
                    break;
            }
        }
    }


    /**
     *  @author: 张璐婷
     *  @time: 2021/1/5  17:03
     *  @Description: Sug检索关键字
     */
    private void SugSearch(){
        suggestionSearch = SuggestionSearch.newInstance();
        //Sug检索监听器
        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                //处理sug检索结果
                List<SuggestionResult.SuggestionInfo> suggestAllList = suggestionResult.getAllSuggestions();
                if(null == suggestAllList) return;
                sugList = new ArrayList<Map<String,String>>();
                for(int i = 0; i < suggestAllList.size(); ++i){
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("name", suggestAllList.get(i).key);
                    map.put("address", suggestAllList.get(i).address);
                    sugList.add(map);
                }
                //显示弹出框
                if(null != listPopupWindow && listPopupWindow.isShowing()){
                    listPopupWindow.dismiss();
                }
                showListPopupWindow();



            }
        };
        suggestionSearch.setOnGetSuggestionResultListener(listener);
        //在您的项目中，keyword为随您的输入变化的值
        if(null != data.getCity()) {
            suggestionSearch.requestSuggestion(new SuggestionSearchOption()
                    .city(data.getCity())
                    .keyword(searchView.getQuery().toString()));
        }
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/5  16:20
     *  @Description: 设置目的地的下拉搜索提示框
     */
    private void showListPopupWindow(){
        listPopupWindow = new ListPopupWindow(getActivity().getApplicationContext());
        listPopupWindow.setHeight(DensityUtil.dip2px(getActivity().getApplicationContext(), 300));
        listPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setAnchorView(head);//关联控件
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity().getApplicationContext(),
                sugList,
                R.layout.item_searchlocation,
                new String[]{"name","address"},
                new int[]{R.id.search_name,R.id.search_address});
        listPopupWindow.setAdapter(simpleAdapter);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                submitFlag = true;
                searchView.setQuery(sugList.get(position).get("name"),true);
            }
        });
        listPopupWindow.show();
    }

    private void topicTwiceList(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Message msg = Message.obtain();
                    if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/topic/twiceUsed" );
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String info = reader.readLine();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Topic>>(){}.getType();
                        List<Topic> topicList = gson.fromJson(info,type);
                        msg.what = 1003;
                        msg.obj = topicList;
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
}
