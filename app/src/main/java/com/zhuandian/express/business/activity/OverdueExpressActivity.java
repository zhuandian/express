package com.zhuandian.express.business.activity;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.express.R;
import com.zhuandian.express.adapter.ExpressAdapter;
import com.zhuandian.express.business.utils.BaseRecyclerView;
import com.zhuandian.express.entity.ExpressEntity;
import com.zhuandian.express.entity.UserEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class OverdueExpressActivity extends BaseActivity {

    @BindView(R.id.brv_list)
    BaseRecyclerView brvGoodsList;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private List<ExpressEntity> mDatas = new ArrayList<>();
    private ExpressAdapter expressAdapter;
    private int currentCount = -10;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_overdue_express;
    }

    @Override
    protected void setUpView() {
        tvTitle.setText("逾期快递");
        expressAdapter = new ExpressAdapter(mDatas, this);
        brvGoodsList.setRecyclerViewAdapter(expressAdapter);
        loadDatas();
        initRefreshListener();
    }


    private void initRefreshListener() {
        brvGoodsList.setRefreshListener(new BaseRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentCount = -10; //重新置位
                mDatas.clear();
                expressAdapter.notifyDataSetChanged();
                loadDatas();

            }
        });
        brvGoodsList.setLoadMoreListener(new BaseRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadDatas();
            }
        });


    }


    private void loadDatas() {
        currentCount = currentCount + 10;
        BmobQuery<ExpressEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.order("-updatedAt");
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(UserEntity.class).getObjectId());
//        query.addWhereEqualTo("state",1);
        query.setLimit(10);
        query.setSkip(currentCount);
        query.findObjects(new FindListener<ExpressEntity>() {
            @Override
            public void done(List<ExpressEntity> list, BmobException e) {
                if (e == null) {


                    for (int i = 0; i < list.size(); i++) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date endTime = null;
                        try {
                            endTime = sdf.parse(list.get(i).getEndTime());
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        Date currentTime = new Date();
                        long timeIntval = (endTime.getTime() - currentTime.getTime()) / (1000L * 3600L * 24L);

                        //过滤逾期快递
                        if (timeIntval < 0) {
                            mDatas.add(list.get(i));
                        }
                    }
                    expressAdapter.notifyDataSetChanged();
                    brvGoodsList.setRefreshLayoutState(false);
                } else {
                    brvGoodsList.setRefreshLayoutState(false);
                }
            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
