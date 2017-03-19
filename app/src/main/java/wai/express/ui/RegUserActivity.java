package wai.express.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.tsz.afinal.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import wai.express.BaseActivity;
import wai.express.R;
import wai.express.method.Utils;
import wai.express.model.UserModel;

/**
 * 注册页面
 * Created by Finder丶畅畅 on 2017/2/27 09:23
 * QQ群481606175
 */

public class RegUserActivity extends BaseActivity {
    @Bind(R.id.tel_et)
    EditText telEt;
    @Bind(R.id.pwd_et)
    EditText pwdEt;
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.reg_btn)
    Button regBtn;
    String admin = "0";
    @Bind(R.id.name_et)
    EditText nameEt;

    @Override
    public void initViews() {
        admin = Utils.getCache("is_course");
        if (admin.equals("2")) {
            nameEt.setVisibility(View.VISIBLE);
            telEt.setHint("请输入骑士编号");
        } else {
            admin = "0";
        }
    }

    @Override
    public void initEvents() {
        regBtn.setOnClickListener(view -> {
            String tel = telEt.getText().toString();
            String pwd = pwdEt.getText().toString();

            if (TextUtils.isEmpty(tel)) {
                ToastShort("请输入手机号码");
            } else if (!Utils.isMobileNo(tel) && !admin.equals("2")) {//管理员才可以注册)
                ToastShort("请输入正确的手机号");
            } else if (TextUtils.isEmpty(pwd)) {
                ToastShort("请输入密码");
            } else {
                UserModel user = new UserModel();
                user.setUsername(tel);
                user.setPassword(pwd);
                user.setRealName(nameEt.getText().toString().trim());
                if (admin.equals("2")) {
                    admin = "1";
                }
                user.setUserType(admin);
                user.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            ToastShort("注册成功");
                            Intent intent = new Intent();
                            intent.putExtra("tel", tel);
                            intent.putExtra("pwd", pwd);
                            setResult(33, intent);
                            finish();
                        } else if (e.getErrorCode() == 202) {
                            ToastShort("当前手机号已存在，请直接登录");
                            Intent intent = new Intent();
                            intent.putExtra("tel", tel);
                            setResult(33, intent);
                            finish();
                        } else {
                            ToastShort("请检查网络连接");
                        }
                    }
                });
            }
        });
        toolbar.setLeftClick(() -> finish());//点击返回关闭当前页面
    }

    @Override
    public int setLayout() {
        return R.layout.activity_reg_user;
    }
}
