package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Data.Scene;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;

/**
 * @ProjectName:    ZhiLv
 * @Description:    
 * @Author:         张璐婷
 * @CreateDate:     2021/2/9  14:12
 * @Version:        1.0
 */
public class LocalSceneAdapter extends RecyclerView.Adapter<LocalSceneAdapter.ViewHolder> {
    private Context context;
    private List<Scene> mDatas;
    private MyApplication data;

    public LocalSceneAdapter(Context context, List<Scene> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        data = (MyApplication)context.getApplicationContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_local_scene, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(mDatas.get(position).getTitle());
        Glide.with(context).load("http://"+ data.getIp()+":8080/ZhiLvProject/"+mDatas.get(position).getPath()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(null != mDatas){
            return mDatas.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.destination_iv_scene);
            textView = itemView.findViewById(R.id.destination_tv_scene);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getLayoutPosition());
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

    public void refresh(List<Scene> scenes){
        this.mDatas = scenes;
        notifyDataSetChanged();
    }
}
