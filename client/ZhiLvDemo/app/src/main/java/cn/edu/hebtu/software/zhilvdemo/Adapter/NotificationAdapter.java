package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.Data.Notification;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;


/**
 * @ProjectName:    ZhiLv
 * @Description:    
 * @Author:         张璐婷
 * @CreateDate:     2021/1/31  16:15
 * @Version:        1.0
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> notifications = new ArrayList<>();
    private int itemLayoutId;
    private Context context;

    public NotificationAdapter(List<Notification> notifications, int itemLayoutId, Context context) {
        this.notifications = notifications;
        this.itemLayoutId = itemLayoutId;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(itemLayoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(notifications.get(position).getTitle());
        holder.content.setText(notifications.get(position).getContent());
        holder.date.setText(DateUtil.showTime(notifications.get(position).getTime()));
    }

    @Override
    public int getItemCount() {
        if(null != notifications){
            return notifications.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView content;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);
            date = itemView.findViewById(R.id.tv_date);
        }
    }

    public void refresh(List<Notification> notifications){
        this.notifications = notifications;
        notifyDataSetChanged();
    }
}
