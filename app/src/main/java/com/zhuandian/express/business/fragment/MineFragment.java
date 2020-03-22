package com.zhuandian.express.business.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhuandian.base.BaseFragment;
import com.zhuandian.express.R;
import com.zhuandian.express.business.PersonalDataActivity;
import com.zhuandian.express.business.activity.ExpressHistoryActivity;
import com.zhuandian.express.business.activity.OverdueExpressActivity;
import com.zhuandian.express.business.login.LoginActivity;
import com.zhuandian.express.business.utils.PictureSelectorUtils;
import com.zhuandian.express.entity.UserEntity;
import com.zhuandian.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * desc :
 * author：xiedong
 * date：2020/03/21
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    private SharedPreferences sharedPreferences;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

        sharedPreferences = actitity.getSharedPreferences("config", Context.MODE_PRIVATE);
        String headerPath = sharedPreferences.getString("header_path", "");
        if (!headerPath.isEmpty()) {
            decodePath2Bitmap(headerPath);
        }
        UserEntity userEntity = BmobUser.getCurrentUser(UserEntity.class);
        if (userEntity != null) {
            tvNickName.setText(userEntity.getNikeName() == null ? userEntity.getUsername() : userEntity.getNikeName());
        }
    }

    @OnClick({R.id.iv_header, R.id.tv_nick_name, R.id.tv_more_setting, R.id.tv_logout, R.id.tv_history, R.id.tv_overdue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                PictureSelectorUtils.selectImg(PictureSelector.create(this), 1);
                break;
            case R.id.tv_history:
                startActivity(new Intent(actitity, ExpressHistoryActivity.class));
                break;
            case R.id.tv_overdue:
                startActivity(new Intent(actitity, OverdueExpressActivity.class));
                break;
            case R.id.tv_more_setting:
                startActivity(new Intent(actitity, PersonalDataActivity.class));
                break;
            case R.id.tv_logout:
                startActivity(new Intent(actitity, LoginActivity.class));
                BmobUser.logOut();
                actitity.finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList.size() > 0) {
                    String imagePath = selectList.get(0).getCompressPath();
                    sharedPreferences = actitity.getSharedPreferences("config", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("header_path", imagePath).commit();
                    decodePath2Bitmap(imagePath);
                }
            }
        }
    }


    /**
     * 把指定路径的image资源转成Bitmap
     *
     * @param path
     */
    private void decodePath2Bitmap(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        if (bm != null) {
            ivHeader.setImageBitmap(bm);
        }
    }

}
