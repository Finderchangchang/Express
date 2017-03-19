package wai.express.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.tsz.afinal.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import wai.express.BaseActivity;
import wai.express.R;
import wai.express.method.Utils;
import wai.express.model.UserModel;

/**
 * 登录页面
 * Created by Finder丶畅畅 on 2017/2/27 09:23
 * QQ群481606175
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.tel_et)
    EditText telEt;
    @Bind(R.id.pwd_et)
    EditText pwdEt;
    @Bind(R.id.login_btn)
    Button loginBtn;
    @Bind(R.id.reg_tv)
    TextView regTv;
    @Bind(R.id.toolbar)
    TitleBar toolbar;

    @Override
    public void initViews() {
        toolbar.setLeftClick(() -> finish());
    }

    @Override
    public void initEvents() {
        loginBtn.setOnClickListener(view -> {
            String tel = telEt.getText().toString().trim();
            String pwd = pwdEt.getText().toString().trim();
            if (TextUtils.isEmpty(tel)) {
                ToastShort("请输入手机号码");
            } else if (TextUtils.isEmpty(pwd)) {
                ToastShort("请输入密码");
            } else {
                UserModel.loginByAccount(tel, pwd, new LogInListener<UserModel>() {
                    @Override
                    public void done(UserModel user, BmobException e) {
                        if (user != null) {
                            Map<String, String> map = new HashMap<>();
                            map.put("user_id", user.getObjectId());
                            map.put("user_tel", user.getUsername());
                            map.put("user_pwd", pwd);
                            map.put("is_course", user.getUserType());//普通用户
                            Utils.putCache(map);
                            setResult(22);
                            finish();
                        }
                    }
                });
            }
        });
        regTv.setOnClickListener(view -> startActivityForResult(new Intent(LoginActivity.this, RegUserActivity.class), 99));
    }

    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 33) {
            String tel = data.getStringExtra("tel");
            if (!TextUtils.isEmpty(tel)) {
                telEt.setText(tel);
                telEt.setSelection(tel.length());
            }
            pwdEt.setText(data.getStringExtra("pwd"));
        }
    }
}
