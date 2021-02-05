package cn.edu.hebtu.software.zhilvdemo.Util.BaiduMapUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.R;


public class RouteLineAdapter extends RecyclerView.Adapter<RouteLineAdapter.NodeViewHolder> {

    private List<? extends RouteLine> mRouteLines;
    private LayoutInflater mLayoutInflater;

    public RouteLineAdapter(Context context, List<?extends RouteLine> routeLines) {
        this.mRouteLines = routeLines;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.activity_transit_item, null);
        return new NodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NodeViewHolder holder, int position) {
        DrivingRouteLine drivingRouteLine = (DrivingRouteLine) mRouteLines.get(position);
        holder.name.setText("路线" + (position + 1));
        int time = drivingRouteLine.getDuration();
        if ( time / 3600 == 0 ) {
            holder.lightNum.setText( + time / 60 + "分钟" );
        } else {
            holder.lightNum.setText( + time / 3600 + "小时" + (time % 3600) / 60 + "分钟" );
        }
        int distance = drivingRouteLine.getDistance();
        if(distance >= 1000){
            holder.dis.setText( + distance / 1000 + "."+distance % 1000 / 100 + "公里");
        }else{
            holder.dis.setText( + distance + "米");
        }
    }

    @Override
    public int getItemCount() {
        if(null != mRouteLines){
            return mRouteLines.size();
        }else{
            return 0;
        }
    }

    public class NodeViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView lightNum;
        private TextView dis;

        public NodeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.transitName);
            lightNum = itemView.findViewById(R.id.lightNum);
            dis = itemView.findViewById(R.id.dis);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
                        onItemClickListener.onItemClick(getLayoutPosition());
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

    private RouteLineAdapter.OnItemClickListener onItemClickListener;

    //写一个公共的方法
    public void setOnItemClickListener(RouteLineAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
