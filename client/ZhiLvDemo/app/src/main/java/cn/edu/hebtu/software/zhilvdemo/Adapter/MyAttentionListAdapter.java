package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.R;

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
    private List<String> mDatas = new ArrayList<String>();

    public MyAttentionListAdapter(Context context, int itemLayout, List<String> mDatas) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public MyAttentionListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(itemLayout, parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAttentionListAdapter.ViewHolder holder, int position) {
        holder.userSign.setText(mDatas.get(position));
        //TODO 设置数据

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
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
                        btnRemove.setText("已关注");
                    }else{
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


    public void replaceAll(List<String> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }


}
