package com.zhuandian.express.business.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuandian.base.BaseFragment;
import com.zhuandian.express.R;
import com.zhuandian.express.adapter.ExpressAdapter;
import com.zhuandian.express.business.utils.BaseRecyclerView;
import com.zhuandian.express.entity.ExpressEntity;
import com.zhuandian.express.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * desc :
 * author：xiedong
 * date：2020/03/21
 */
public class ExpressFragment extends BaseFragment {
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
        return R.layout.fragment_express;
    }

    @Override
    protected void initView() {
        ivBack.setVisibility(View.GONE);
        tvTitle.setText("快递列表");
        expressAdapter = new ExpressAdapter(mDatas, actitity);
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
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        query.order("-updatedAt");
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(UserEntity.class).getObjectId());
//        query.addWhereEqualTo("state",1);
        query.setLimit(10);
        query.setSkip(currentCount);
        //这里查询规则有点绕，备注一下，如果userEntity不为null代表需要检索指定用户发布的二手商品信息，如果为null则检索全部
        //如果userEntity为当前登录用户，则需要有对商品下架的权限，否则只有查看权限

        query.findObjects(new FindListener<ExpressEntity>() {
            @Override
            public void done(List<ExpressEntity> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        mDatas.add(list.get(i));
                    }
                    expressAdapter.notifyDataSetChanged();
                    brvGoodsList.setRefreshLayoutState(false);
                } else {
                    brvGoodsList.setRefreshLayoutState(false);
                }
            }
        });
    }

}
