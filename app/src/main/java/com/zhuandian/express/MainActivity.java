package com.zhuandian.express;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zhuandian.base.BaseActivity;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.express.adapter.HomePageAdapter;
import com.zhuandian.express.business.fragment.HomeFragment;
import com.zhuandian.express.business.fragment.MineFragment;
import com.zhuandian.express.business.fragment.ExpressFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.vp_home)
    ViewPager vpHome;
    @BindView(R.id.tab_bottom)
    BottomNavigationView tabBottom;
    public static final int PAGE_HOME = 0;
    public static final int PAGE_EXPRESS = 1;
    public static final int PAGE_MY = 2;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUpView() {
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ExpressFragment());
        fragmentList.add(new MineFragment());
        vpHome.setAdapter(new HomePageAdapter(getSupportFragmentManager(),fragmentList));
        vpHome.setOffscreenPageLimit(4);

        vpHome.setCurrentItem(PAGE_HOME);
        initBottomTab();
    }

    public  void setCurrentPage(int position){
        vpHome.setCurrentItem(position);
    }
    private void initBottomTab() {
        vpHome.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabBottom.getMenu().getItem(position).setChecked(true);
            }
        });

        tabBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.tab_home:
                        vpHome.setCurrentItem(PAGE_HOME);
                        break;
                    case R.id.tab_express:
                        vpHome.setCurrentItem(PAGE_EXPRESS);
                        break;
                    case R.id.tab_my:
                        vpHome.setCurrentItem(PAGE_MY);
                        break;
                }

                return true;
            }
        });
    }
}
