package wai.express.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import net.tsz.afinal.view.TitleBar;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.iwf.photopicker.PhotoPicker;
import wai.express.BaseActivity;
import wai.express.R;
import wai.express.model.AdvertModel;

/**
 * Created by Administrator on 2017/3/11.
 */

public class AddWenziActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    TitleBar titleBar;
    @Bind(R.id.et_content)
    EditText et_content;

    @Override
    public int setLayout() {
        return R.layout.ac_add_wenzi;
    }

    @Override
    public void initViews() {
        titleBar = (TitleBar) findViewById(R.id.toolbar);
    }

    @Override
    public void initEvents() {
        titleBar.setLeftClick(() -> finish());
        titleBar.setRightClick(() -> ADD());
    }

    //保存文字信息
    private void ADD() {
        AdvertModel advertModel = new AdvertModel();
        advertModel.setContent(et_content.getText().toString());
        advertModel.setType("1");
        advertModel.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    ToastShort("保存成功");
                    Intent intent = new Intent(AddWenziActivity.this, WenZiListActivity.class);
                    setResult(11, intent);
                    finish();
                } else if (e.getErrorCode() == 9010) {
                    ToastShort(getResources().getString(R.string.chaoshi));
                } else if (e.getErrorCode() == 9016) {
                    ToastShort(getResources().getString(R.string.wuwang));
                } else {
                    ToastShort(getResources().getString(R.string.neibu));
                }
            }
        });
    }


}
