package com.zhuandian.express.business.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.express.R;
import com.zhuandian.express.entity.ExpressEntity;
import com.zhuandian.express.entity.UserEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 帮他人代取快递
 */
public class HelpOtherAcitvity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_expressId)
    EditText etExpressId;
    @BindView(R.id.tv_fetch_express)
    TextView tvFetchExpress;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help_other_acitvity;
    }

    @Override
    protected void setUpView() {
        tvTitle.setText("帮他人取件");
    }


    @OnClick({R.id.iv_back, R.id.tv_fetch_express})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_fetch_express:
                searchExpress();
                break;
        }
    }

    /**
     * 搜索快递
     */
    private void searchExpress() {
        Toast.makeText(HelpOtherAcitvity.this, "查询中...", Toast.LENGTH_SHORT).show();
        String expressId = etExpressId.getText().toString();
        if (TextUtils.isEmpty(expressId)) {
            Toast.makeText(this, "请输入快递编号后检索快递", Toast.LENGTH_SHORT).show();
            return;
        }

        BmobQuery<ExpressEntity> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.addWhereEqualTo("expressId", expressId);
        query.findObjects(new FindListener<ExpressEntity>() {
            @Override
            public void done(List<ExpressEntity> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    Intent intent = new Intent(HelpOtherAcitvity.this, ExpressDetailActivity.class);
                    intent.putExtra("entity", list.get(0));
                    startActivity(intent);
                } else {
                    Toast.makeText(HelpOtherAcitvity.this, "暂时没有你要帮别人去的快递", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}