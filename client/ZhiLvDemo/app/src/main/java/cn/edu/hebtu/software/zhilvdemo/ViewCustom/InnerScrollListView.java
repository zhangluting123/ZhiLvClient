package cn.edu.hebtu.software.zhilvdemo.ViewCustom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 *  @author: 张璐婷
 *  @time: 2020/4/14  11:35
 *  @Description: 自定义ListView
 */
public class InnerScrollListView extends ListView {
    public InnerScrollListView(Context context) {
        super(context);
    }

    public InnerScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
