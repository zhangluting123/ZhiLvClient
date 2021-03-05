package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Data.AuditMessage;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;

/**
 * @ProjectName:    ZhiLv
 * @Description:    
 * @Author:         张璐婷
 * @CreateDate:     2021/3/4  20:40
 * @Version:        1.0
 */
public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.ViewHolder> {
    private List<AuditMessage> mDatas = new ArrayList<>();
    private Context context;

    public AuditAdapter(List<AuditMessage> mDatas, Context context) {
        this.mDatas = mDatas;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_audit, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (mDatas.get(position).getFlag()){
            case 1:
                holder.flag.setBackgroundResource(R.mipmap.logo_tab1);
                holder.flag.setText("游记");
                break;
            case 2:
                holder.flag.setBackgroundResource(R.mipmap.logo_tab2);
                holder.flag.setText("视频");
                break;
            case 3:
                holder.flag.setBackgroundResource(R.mipmap.logo_tab3);
                holder.flag.setText("话题");
                break;
            case 4:
                holder.flag.setBackgroundResource(R.mipmap.logo_tab4);
                holder.flag.setText("景点");
                break;
            default:
                break;
        }
        switch (mDatas.get(position).getStatus()){ //0默认颜色
            case 0:
                holder.auditImg.setImageResource(R.mipmap.logo_clock);
                holder.auditText.setTextColor(context.getResources().getColor(R.color.blue));
                holder.auditText.setText("审核中");
                break;
            case 1:
                holder.auditImg.setImageResource(R.mipmap.logo_clock2);
                holder.auditText.setTextColor(context.getResources().getColor(R.color.green_deep));
                holder.auditText.setText("已通过");
                break;
            case 2:
                holder.auditImg.setImageResource(R.mipmap.logo_clock3);
                holder.auditText.setTextColor(Color.RED);
                holder.auditText.setText("不通过");
                break;
            default:
                break;
        }
        if(null != mDatas.get(position).getLastTime()) {
            holder.lastTime.setText(DateUtil.showTime(mDatas.get(position).getLastTime()));
        }
        holder.title.setText(mDatas.get(position).getTitle());
        if(null != mDatas.get(position).getContent()){
            holder.content.setText(mDatas.get(position).getContent());
        }
        if(null != mDatas.get(position).getAddress()){
            holder.address.setText(mDatas.get(position).getAddress());
        }
        if(null != mDatas.get(position).getUploadTime()){
            holder.time.setText(DateUtil.showTime(mDatas.get(position).getUploadTime()));
        }
    }

    @Override
    public int getItemCount() {
       if(null != mDatas){
           return mDatas.size();
       }else{
           return 0;
       }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView flag;
        ImageView auditImg;
        TextView auditText;
        TextView lastTime;
        TextView title;
        TextView content;
        TextView address;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.tv_flag);
            auditImg = itemView.findViewById(R.id.audit_img);
            auditText = itemView.findViewById(R.id.audit_text);
            lastTime = itemView.findViewById(R.id.audit_lastTime);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);
            address = itemView.findViewById(R.id.tv_address);
            time = itemView.findViewById(R.id.tv_time);
        }
    }

    public void refresh(List<AuditMessage> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
}
