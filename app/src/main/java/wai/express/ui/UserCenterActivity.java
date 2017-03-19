package wai.express.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import wai.express.BaseActivity;
import wai.express.R;
import wai.express.method.CommonAdapter;
import wai.express.method.CommonViewHolder;
import wai.express.method.Utils;
import wai.express.model.AdvertModel;
import wai.express.model.UserModel;
import wai.express.model.service;

/**
 * 个人中心页面
 * Created by Finder丶畅畅 on 2017/2/28 21:59
 * QQ群481606175
 */

public class UserCenterActivity extends BaseActivity {
    @Bind(R.id.exit_btn)
    Button exitBtn;
    @Bind(R.id.order_list_lv)
    ListView orderListLv;
    @Bind(R.id.order_list_srl)
    SwipeRefreshLayout orderListSrl;
    CommonAdapter<service> commonAdapter;
    List<service> list;
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.reg_btn)
    Button reg_btn;
    @Bind(R.id.count_tv)
    TextView count_tv;
    @Bind(R.id.user_diver_ll)
    LinearLayout user_diver_ll;
    @Bind(R.id.admin_ll)
    LinearLayout admin_ll;
    public static UserCenterActivity install;
    @Bind(R.id.tv_picture)
    TextView tv_picture;
    @Bind(R.id.tv_price)
    TextView tv_price;
    @Bind(R.id.tv_wenzi)
    TextView tv_wenzi;
    @Bind(R.id.price1_et)
    EditText price1Et;
    @Bind(R.id.price2_et)
    EditText price2Et;
    @Bind(R.id.price3_et)
    EditText price3Et;
    @Bind(R.id.tv_uc_num)
    TextView tv_uc_num;

    @Override
    public int setLayout() {
        list = new ArrayList<>();
        install = this;
        return R.layout.activity_user_center;
    }

    String val;

    @Override
    public void initViews() {
        toolbar.setLeftClick(() -> finish());
        //获取骑士数量
        getQSNum();
        if (Utils.getCache("is_course").equals("2")) {//管理员才可以注册
            reg_btn.setVisibility(View.VISIBLE);
            reg_btn.setOnClickListener(view -> Utils.IntentPost(RegUserActivity.class));
        }
        exitBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确定要退出当前账号？");
            builder.setNegativeButton("确定", (dialogInterface, i) -> {
                MainActivity.install.finish();
                Map<String, String> map = new HashMap<>();
                map.put("user_id", "");
                map.put("user_tel", "");
                map.put("is_course", "");//配送人员
                Utils.putCache(map);
                BmobUser.logOut();
                finish();
            });
            builder.setPositiveButton("取消", null);
            builder.show();
        });
        tv_wenzi.setOnClickListener(view ->
                Utils.IntentPost(WenZiListActivity.class, intent -> intent.putExtra("Type", "1"))
        );
        tv_picture.setOnClickListener(view ->
                Utils.IntentPost(WenZiListActivity.class, intent -> intent.putExtra("Type", "2"))
        );
        BmobQuery<AdvertModel> query = new BmobQuery<>();
        query.addWhereEqualTo("type", "3");
        query.findObjects(new FindListener<AdvertModel>() {
            @Override
            public void done(List<AdvertModel> list, BmobException e) {
                if (e == null) {
                    for (AdvertModel model : list) {
                        if (model.getObjectId().equals("q9CB111F")) {
                            price1Et.setText(model.getContent());
                        } else if (model.getObjectId().equals("vKnxGGGK")) {
                            price2Et.setText(model.getContent());
                        } else if (model.getObjectId().equals("hi3U777I")) {
                            price3Et.setText(model.getContent());
                        }
                    }
                } else if (e.getErrorCode() == 9010) {
                    ToastShort(getResources().getString(R.string.chaoshi));
                } else if (e.getErrorCode() == 9016) {
                    ToastShort(getResources().getString(R.string.wuwang));
                } else {
                    ToastShort(getResources().getString(R.string.neibu));
                }
            }
        });

        price1Et.setOnFocusChangeListener((view, b) -> {
            String key = price1Et.getText().toString().trim();
            if (b) {//得到焦点
                val = key;
            } else {//失去焦点
                if (!val.equals(key)) {
                    AdvertModel model = new AdvertModel();
                    model.setContent(key);
                    model.update("q9CB111F", new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastShort("修改成功");
                            }
                        }
                    });
                }
            }
        });
        price2Et.setOnFocusChangeListener((view, b) -> {
            String key = price2Et.getText().toString().trim();
            if (b) {//得到焦点
                val = key;
            } else {//失去焦点
                if (!val.equals(key)) {
                    AdvertModel model = new AdvertModel();
                    model.setContent(key);
                    model.update("vKnxGGGK", new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastShort("修改成功");
                            }
                        }
                    });
                }
            }
        });
        price3Et.setOnFocusChangeListener((view, b) -> {
            String key = price3Et.getText().toString().trim();
            if (b) {//得到焦点
                val = key;
            } else {//失去焦点
                if (!val.equals(key)) {
                    AdvertModel model = new AdvertModel();
                    model.setContent(key);
                    model.update("hi3U777I", new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastShort("修改成功");
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void initEvents() {
        commonAdapter = new CommonAdapter<service>(this, list, R.layout.item_new) {
            @Override
            public void convert(CommonViewHolder holder, service service, int position) {
                holder.setText(R.id.express_id_tv, service.getExpress_address());
                holder.setText(R.id.content_tv, service.getGood_price() + "元");
                holder.setText(R.id.express_state_tv, service.getState());
                holder.setText(R.id.time_tv, service.getUpdatedAt().substring(5, 16));
                holder.setVisible(R.id.diver_name_tv,false);
                if (Utils.getCache("is_course").equals("1")) {
                    holder.setText(R.id.get_order_btn, "完成订单");
                    holder.setVisible(R.id.get_order_btn, true);
                } else {
                    holder.setVisible(R.id.get_order_btn, false);
                }
                holder.setOnClickListener(R.id.get_order_btn, view12 -> {
                    BmobUser user = new BmobUser();
                    user.setObjectId(Utils.getCache("user_id"));
                    service.setCourier(user);
                    service.setState("已完成");
                    service.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                list = new ArrayList<>();
                                UserCenterActivity.install.refresh();
                                ToastShort("修改成功");
                            } else {
                                ToastShort("修改失败");
                            }
                        }
                    });
                });
            }
        };
        orderListLv.setAdapter(commonAdapter);
        toolbar.setCentertv("个人中心");
        refresh();
        orderListSrl.setOnRefreshListener(() -> {
            list = new ArrayList<>();
            refresh();
        });
        orderListLv.setOnItemClickListener((adapterView, view13, i, l) ->
                Utils.IntentPost(OrderDetailActivity.class, intent -> intent.putExtra("model", list.get(i)))
        );
        boolean sta = true;
        if (Utils.getCache("is_course").equals("1")) {
            count_tv.setVisibility(View.VISIBLE);
        } else {
            if (Utils.getCache("is_course").equals("2")) {
                sta = false;
            }
            count_tv.setVisibility(View.GONE);
        }
        user_diver_ll.setVisibility(sta ? View.VISIBLE : View.GONE);
        admin_ll.setVisibility(sta ? View.GONE : View.VISIBLE);
        tv_uc_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void refresh() {
        BmobQuery<service> query = new BmobQuery<>();
        query.include("type,user,courier");
        if (Utils.getCache("is_course").equals("1")) {
            BmobUser user = new BmobUser();
            user.setObjectId(Utils.getCache("user_id"));
            query.addWhereEqualTo("courier", user);
        } else if (Utils.getCache("is_course").equals("0")) {
            BmobUser user = new BmobUser();
            user.setObjectId(Utils.getCache("user_id"));
            query.addWhereEqualTo("user", user);
        }
        query.findObjects(new FindListener<service>() {
            @Override
            public void done(List<service> services, BmobException e) {
                if (e == null) {
                    commonAdapter.refresh(services);
                    list = services;
                    String result = "共" + services.size() + "单";
                    double price = 0;
                    for (service s : services) {
                        if (s.getState().equals("已完成")) {
                            price += s.getGood_price();
                        }
                    }
                    result += "   总钱数：" + price;
                    count_tv.setText(result);
                }
            }
        });
        if (orderListSrl.isRefreshing()) {
            orderListSrl.setRefreshing(false);
        }
    }

    //获取骑士数量
    private int getQSNum() {
        int num = 0;
        BmobQuery<UserModel> query = new BmobQuery<UserModel>();
        query.addWhereEqualTo("userType", "2");
        query.count(UserModel.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if (e == null) {
                    tv_uc_num.setText("骑士总数量为：" + count);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    tv_uc_num.setText("骑士总数量：" + e.getMessage());
                }
            }
        });
        return num;
    }
}
