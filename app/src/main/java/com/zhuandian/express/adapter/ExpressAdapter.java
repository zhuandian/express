package com.zhuandian.express.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.base.BaseAdapter;
import com.zhuandian.base.BaseViewHolder;
import com.zhuandian.express.R;
import com.zhuandian.express.business.activity.ExpressDetailActivity;
import com.zhuandian.express.business.activity.OverdueDetailActivity;
import com.zhuandian.express.entity.ExpressEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * desc :
 * author：xiedong
 * date：2020/03/21
 */
public class ExpressAdapter extends BaseAdapter<ExpressEntity, BaseViewHolder> {
    @BindView(R.id.tv_express_title)
    TextView tvExpressTitle;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_local)
    TextView tvLocal;
    private List<ExpressEntity> expressEntities;
    private Context context;

    public ExpressAdapter(List<ExpressEntity> mDatas, Context context) {
        super(mDatas, context);
    }


    @Override
    protected void converData(BaseViewHolder myViewHolder, ExpressEntity expressEntity, int position) {
        ButterKnife.bind(this, myViewHolder.itemView);
        tvExpressTitle.setText(expressEntity.getTitle());
        tvType.setText(expressEntity.getType());
        tvContent.setText(expressEntity.getContent());
        tvTime.setText("取件时间：" + expressEntity.getCreatedAt().split(" ")[0]);
        tvType.setText(expressEntity.getType());
        tvLocal.setText("地点：" + expressEntity.getLocal());

        initEndTime(expressEntity, myViewHolder.itemView);
    }

    private void initEndTime(ExpressEntity expressEntity, View itemView) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateCreate = sdf.parse(expressEntity.getCreatedAt());
            Date endTime = sdf.parse(expressEntity.getEndTime());
            Date currentTime = new Date();
//            sdf.format(currentTime);

            long timeIntval = (endTime.getTime() - currentTime.getTime()) / (1000L * 3600L * 24L);


            if (expressEntity.getState() == 2) {
                tvEndTime.setText("已完成取件");
            } else {
                if (timeIntval >= 0) {
                    tvEndTime.setText(String.format("距离取件时间还是%d天", Math.abs(timeIntval)));
                } else {
                    tvEndTime.setText("逾期未取件");

                }

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expressEntity.getState() == 2) {
                        Toast.makeText(mContext, "当前快递已完成取件，不允许重复操作", Toast.LENGTH_SHORT).show();
                    } else {
                        if (timeIntval >= 0) {
                            Intent intent = new Intent(mContext, ExpressDetailActivity.class);
                            intent.putExtra("entity", expressEntity);
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, OverdueDetailActivity.class);
                            intent.putExtra("entity", expressEntity);
                            mContext.startActivity(intent);
                        }

                    }
                }
            });


        } catch (Exception e) {

        }
    }


    @Override
    public int getItemLayoutId() {
        return R.layout.item_express;
    }
}
