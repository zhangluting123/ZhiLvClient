package cn.edu.hebtu.software.zhilvdemo.Adapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


/**
 * @ProjectName:    ZhiLv
 * @Description:    导航栏页面适配器
 * @Author:         张璐婷
 * @CreateDate:     2020/12/14$ 12:01$
 * @Version:        1.0
 */
public class ChannelPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList;
    private final String[] titles ;

    public ChannelPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles){
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
