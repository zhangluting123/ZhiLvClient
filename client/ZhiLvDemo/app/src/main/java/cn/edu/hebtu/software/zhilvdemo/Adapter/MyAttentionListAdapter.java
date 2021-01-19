package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DetermineConnServer;

/**
 * @ProjectName:    ZhiLv
 * @Description:    我的关注列表| 粉丝列表适配器
 * @Author:         张璐婷
 * @CreateDate:     2020/12/18 12:04
 * @Version:        1.0
 */
public class MyAttentionListAdapter extends RecyclerView.Adapter<MyAttentionListAdapter.ViewHolder>{
    private Context context;
    private int itemLayout;
    private List<User> mDatas = new ArrayList<User>();
    private boolean flag;// flag = true 关注  flag = false 粉丝

    private MyApplication data;
    private Handler handler;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(context, (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public MyAttentionListAdapter(Context context, int itemLayout, List<User> mDatas, boolean flag) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.mDatas = mDatas;
        this.flag = flag;
        data = (MyApplication)context.getApplicationContext();
    }

    @NonNull
    @Override
    public MyAttentionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(itemLayout, parent,false);
       return new ViewHolder(view);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onBindViewHolder(@NonNull MyAttentionListAdapter.ViewHolder holder, int position) {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1002:
                        holder.btnRemove.setText((String)msg.obj);
                        break;
                }
            }
        };
        if(!flag){
            ifFollow(position);
        }
        holder.userName.setText(mDatas.get(position).getUserName());
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(context).load("http://"+ data.getIp()+":8080/ZhiLvProject/"+mDatas.get(position).getUserHead()).apply(options).into(holder.userHead);
        holder.userSign.setText(mDatas.get(position).getSignature());
    }

    @Override
    public int getItemCount() {
        if(null != mDatas) {
            return mDatas.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView userHead;
        private TextView userName;
        private TextView userSign;
        private Button btnRemove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userHead = itemView.findViewById(R.id.attention_iv_userHead);
            userName = itemView.findViewById(R.id.attention_tv_userName);
            userSign = itemView.findViewById(R.id.attention_tv_userSign);
            btnRemove = itemView.findViewById(R.id.attention_btn_remove);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
                        onItemClickListener.onItemClick(getLayoutPosition());
                    }
                }
            });
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nowMsg = btnRemove.getText().toString().trim();
                    if(nowMsg.equals("+ 关注")){
                        follow(getLayoutPosition());
                        btnRemove.setText("已关注");
                    }else{
                        noFollow(getLayoutPosition());
                        btnRemove.setText("+ 关注");
                    }
                }
            });

        }

    }

    //==============设置每一项的点击事件=========================

    //定义接口
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    //写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public void replaceAll(List<User> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public void ifFollow(int pos){
        FollowThread thread = new FollowThread(pos);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    class FollowThread extends Thread {
        private int pos;
        public FollowThread(int pos){
            this.pos = pos;
        }
        @Override
        public void run() {
            try {
                if(DetermineConnServer.isConnByHttp(context)) {
                    URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/user/ifFollow?mineId="+data.getUser().getUserId()+"&otherId="+mDatas.get(pos).getUserId());
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String info = reader.readLine();
                    Message message = Message.obtain();
                    message.what = 1002;
                    if("YES".equals(info)){
                        message.obj = "已关注";
                    }else{
                        message.obj = "+ 关注";
                    }
                    handler.sendMessage(message);
                }else{
                    Message message = Message.obtain();
                    message.what = 1001;
                    message.obj = "未连接到服务器";
                    mHandler.sendMessage(message);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void follow(int pos) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(context)) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/user/follow?mineId="+data.getUser().getUserId()+"&otherId="+mDatas.get(pos).getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message message = Message.obtain();
                        message.what = 1001;
                        if("OK".equals(info)){
                            message.obj = "关注成功";
                        }else{
                            message.obj = "关注失败";
                        }
                        mHandler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = 1001;
                        message.obj = "未连接到服务器";
                        mHandler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void noFollow(int pos) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(context)) {
                        URL url = new URL("http://" + data.getIp() + ":8080/ZhiLvProject/user/noFollow?mineId="+data.getUser().getUserId()+"&otherId="+mDatas.get(pos).getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message message = Message.obtain();
                        message.what = 1001;
                        if("OK".equals(info)){
                            message.obj = "取关成功";
                        }else{
                            message.obj = "取关失败";
                        }
                        mHandler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = 1001;
                        message.obj = "未连接到服务器";
                        mHandler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }





}
