package cn.edu.hebtu.software.zhilvdemo.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Activity.MainActivity;
import cn.edu.hebtu.software.zhilvdemo.Adapter.MessageCommentAdapter;
import cn.edu.hebtu.software.zhilvdemo.Adapter.MyAttentionListAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.MailMyComment;
import cn.edu.hebtu.software.zhilvdemo.DetailActivity.PraisedActivity;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

/**
 * @ProjectName:    ZhiLv
 * @Description:    消息界面
 * @Author:         张璐婷
 * @CreateDate:     2020/12/14 19:48
 * @Version:        1.0
 */
public class MessageFragment extends Fragment {
    private RelativeLayout notification;
    private RelativeLayout good;
    private RelativeLayout chat;
    private View view;
    private MessageCommentAdapter adapter;
    private List<MailMyComment> myComments;

    private MyApplication data;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    myComments = (List<MailMyComment>)msg.obj;
                    initView();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container,false);
        data = (MyApplication)getActivity().getApplication();
        registListener();
        if(null != data.getUser()){
            getMailList();
        }
        return view;
    }

    private void initView(){
        RecyclerView recyclerView = view.findViewById(R.id.message_commend_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new MessageCommentAdapter(myComments, R.layout.item_mycomment, getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        notification = view.findViewById(R.id.message_rl_notification);
        good = view.findViewById(R.id.message_rl_good);
        chat = view.findViewById(R.id.message_rl_chat);
        notification.setOnClickListener(listener);
        good.setOnClickListener(listener);
        chat.setOnClickListener(listener);
    }


    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.message_rl_notification:
                    break;
                case R.id.message_rl_good:
                    Intent intent = new Intent(getActivity(), PraisedActivity.class);
                    startActivity(intent);
                    break;
                case R.id.message_rl_chat:
                    break;
            }
        }
    }

    private void getMailList(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())){
                    List<MailMyComment> mails = new ArrayList<>();
                    try {
                        URL url = new URL("http://"+ data.getIp() +":8080/ZhiLvProject/mailmycomment/list?userId=" + data.getUser().getUserId() );
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String str = null;
                        if((str = reader.readLine()) != null){
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Type type = new TypeToken<List<MailMyComment>>(){}.getType();
                            mails = gson.fromJson(str, type);
                            Message msg = Message.obtain();
                            msg.what = 1002;
                            msg.obj = mails;
                            mHandler.sendMessage(msg);
                        }
                        reader.close();
                        in.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Message msg = Message.obtain();
                    msg.what = 1001;
                    msg.obj = "未连接到服务器";
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != data.getUser()){
            getMailList();
        }
    }
}
