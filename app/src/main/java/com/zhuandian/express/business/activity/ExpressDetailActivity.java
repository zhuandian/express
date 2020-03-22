package com.zhuandian.express.business.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.express.R;
import com.zhuandian.express.entity.ExpressEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 物流详情页
 */
public class ExpressDetailActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_express_title)
    TextView tvExpressTitle;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_local)
    TextView tvLocal;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_fetch_express)
    TextView tvFetchExpress;
    @BindView(R.id.et_expressId)
    EditText etExpressId;
    private ExpressEntity expressEntity;
    private long timeIntval;  //逾期时间

    @Override
    protected int getLayoutId() {
        return R.layout.activity_express_detail;
    }

    @Override
    protected void setUpView() {
        expressEntity = (ExpressEntity) getIntent().getSerializableExtra("entity");
        tvExpressTitle.setText(expressEntity.getTitle());
        tvType.setText(expressEntity.getType());
        tvContent.setText(expressEntity.getContent());
        tvTime.setText("取件时间：" + expressEntity.getCreatedAt().split(" ")[0]);
        tvType.setText(expressEntity.getType());
        tvLocal.setText("地点：" + expressEntity.getLocal());
        isOverdue();
    }

    /**
     * 判断当前快递是否逾期
     *
     * @return
     */
    private void isOverdue() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date endTime = sdf.parse(expressEntity.getEndTime());
            Date currentTime = new Date();
            timeIntval = (endTime.getTime() - currentTime.getTime()) / (1000L * 3600L * 24L);

            if (timeIntval < 0) {
                new AlertDialog.Builder(ExpressDetailActivity.this)
                        .setTitle("快递逾期")
                        .setMessage("您的快递已经超时逾期 " + (timeIntval * -1) + " 天\n请您支付超期管理费后完成取件！！")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        } catch (Exception e) {

        }
    }


    @OnClick({R.id.iv_back, R.id.tv_fetch_express})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_fetch_express:
                fetchExpress();
                break;
        }
    }

    /**
     * 执行取快递操作
     */
    private void fetchExpress() {
        String etFetchCode = etExpressId.getText().toString();
        if (TextUtils.isEmpty(etFetchCode)) {
            Toast.makeText(this, "取件码不允许为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etFetchCode.equals(expressEntity.getPassword())) { //取件码一致，允许取件
            expressEntity.setState(2);
            expressEntity.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        new AlertDialog.Builder(ExpressDetailActivity.this)
                                .setTitle("成功")
                                .setMessage("您已经完成取件")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).show();
                    }
                }
            });
        } else {
            //取件码错误
            Toast.makeText(this, "您输入的取件码有误，请重新输入", Toast.LENGTH_SHORT).show();
        }

    }
}
