package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Data.UserGood;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;

/**
 * @ProjectName:    ZhiLv
 * @Description:    
 * @Author:         张璐婷
 * @CreateDate:     2021/1/30  16:25
 * @Version:        1.0
 */
public class PraisedAdapter extends RecyclerView.Adapter<PraisedAdapter.ViewHolder> {
    private List<UserGood> goodList = new ArrayList<>();
    private int itemLayoutId;
    private Context context;
    private MyApplication data;

    public PraisedAdapter(List<UserGood> goodList, int itemLayoutId, Context context) {
        this.goodList = goodList;
        this.itemLayoutId = itemLayoutId;
        this.context = context;
        data = (MyApplication)context.getApplicationContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(itemLayoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userName.setText(goodList.get(position).getUser().getUserName());
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(context).load("http://" + data.getIp() + ":8080/ZhiLvProject/" + goodList.get(position).getUser().getUserHead()).apply(options).into(holder.userHead);
        holder.date.setText(DateUtil.showTime(goodList.get(position).getTime()));
        if(null != goodList.get(position).getTravels()){
            holder.title.setText(goodList.get(position).getTravels().getTitle());
        }else{
            holder.title.setText(goodList.get(position).getVideo().getTitle());
        }
    }

    @Override
    public int getItemCount() {
        if(null != goodList){
            return goodList.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userHead;
        private TextView userName;
        private TextView date;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userHead = itemView.findViewById(R.id.iv_userHead);
            userName = itemView.findViewById(R.id.tv_userName);
            date = itemView.findViewById(R.id.tv_date);
            title = itemView.findViewById(R.id.tv_title);
        }
    }

    public void refresh(List<UserGood> goodList){
        this.goodList = goodList;
        notifyDataSetChanged();
    }
}
