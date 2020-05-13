package com.zhuandian.express.business.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.express.R;
import com.zhuandian.express.entity.ExpressEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class OverdueDetailActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    private ExpressEntity expressEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_overdue_detail;
    }

    @Override
    protected void setUpView() {
        expressEntity = (ExpressEntity) getIntent().getSerializableExtra("entity");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateCreate = sdf.parse(expressEntity.getCreatedAt());
            Date endTime = sdf.parse(expressEntity.getEndTime());
            Date currentTime = new Date();
//            sdf.format(currentTime);

            long timeIntval = (endTime.getTime() - currentTime.getTime()) / (1000L * 3600L * 24L);

            double totalPay = 6 * 0.5;


            tvDetail.setText("当前快递已预期 " + Math.abs(timeIntval) + " 天\n，根据快递预期收费标准，每天0.5元，最高收取3元\n 您当前应支付" + totalPay + "" +
                    "元 逾期费用，请点击下方按钮完成支付");


            double finalTotalPay = totalPay > 3 ? 3 : totalPay;


            tvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(OverdueDetailActivity.this)
                            .setTitle("支付费用")
                            .setMessage("您当前应支付\n" + finalTotalPay + "元 逾期费用，是否立即支付？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定支付", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    expressEntity.setState(2);
                                    expressEntity.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                finish();
                                                Toast.makeText(OverdueDetailActivity.this, "快递状态更新成功", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @OnClick({R.id.iv_back, R.id.tv_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.tv_pay:
                break;
        }
    }
}
