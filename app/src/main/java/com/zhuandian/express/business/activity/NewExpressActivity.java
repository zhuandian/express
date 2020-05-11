package com.zhuandian.express.business.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.zhuandian.base.BaseActivity;
import com.zhuandian.express.R;
import com.zhuandian.express.entity.ExpressEntity;
import com.zhuandian.express.entity.UserEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class NewExpressActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.et_type)
    EditText etType;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_local)
    EditText etLocal;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_express;
    }

    @Override
    protected void setUpView() {
        tvTitle.setText("录入快递");
    }


    @OnClick({R.id.iv_back, R.id.tv_user, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_user:
                showAllUser();
                break;
            case R.id.tv_submit:
                submitExpress();
                break;
        }
    }

    private void showAllUser() {
        BmobQuery<UserEntity> query = new BmobQuery<>();
        query.addWhereNotEqualTo("type",1);
        query.findObjects(new FindListener<UserEntity>() {
            @Override
            public void done(List<UserEntity> list, BmobException e) {
                String userArray[] = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    userArray[i] = list.get(i).getUsername();
                }

                new AlertDialog.Builder(NewExpressActivity.this)
                        .setTitle("选择收件人")
                        .setSingleChoiceItems(userArray, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userId = list.get(which).getObjectId();
                                tvUser.setText(list.get(which).getUsername());
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void submitExpress() {
        String content = etContent.getText().toString();
        String title = etName.getText().toString();
        String local = etLocal.getText().toString();
        String password = etPassword.getText().toString();
        String type = etType.getText().toString();


        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(title) || TextUtils.isEmpty(userId) ||
                TextUtils.isEmpty(local) || TextUtils.isEmpty(password) || TextUtils.isEmpty(type)) {
            Toast.makeText(this, "请完善所有信息", Toast.LENGTH_SHORT).show();
            return;
        }


        long endTimeMillis = System.currentTimeMillis() + (1000 * 60 * 60 * 24*3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String endTime = sdf.format(new Date(endTimeMillis));

        ExpressEntity expressEntity = new ExpressEntity();
        expressEntity.setType(type);
        expressEntity.setTitle(title);
        expressEntity.setContent(content);
        expressEntity.setState(1);
        expressEntity.setEndTime(endTime);
        expressEntity.setUserId(userId);
        expressEntity.setPassword(password);
        expressEntity.setLocal(local);

        expressEntity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(NewExpressActivity.this, "录入成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewExpressActivity.this, NewExpressActivity.class));
                    finish();
                }
            }
        });
    }
}
