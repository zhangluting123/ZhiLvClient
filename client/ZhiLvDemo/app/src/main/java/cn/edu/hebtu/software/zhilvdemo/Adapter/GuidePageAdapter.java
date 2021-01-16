package cn.edu.hebtu.software.zhilvdemo.Adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * @ProjectName:    MoJi
 * @Description:    引导页Adapter
 * @Author:         张璐婷
 * @CreateDate:     2019/11/26 15:48
 * @Version:        1.0
 */
public class GuidePageAdapter extends PagerAdapter {
    private List<View> viewList;

    public GuidePageAdapter(List<View> viewList){
        this.viewList = viewList;
    }

    /**
     * 返回页面个数
     * @return
     */
    @Override
    public int getCount() {
        if(viewList != null){
            return viewList.size();
        }
        return 0;
    }

    /**
     * 判断对象是否生成接界面
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * 初始化position位置的界面
     * @param container
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewList.get(position));
    }
}
