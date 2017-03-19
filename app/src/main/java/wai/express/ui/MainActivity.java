package wai.express.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paradoxie.autoscrolltextview.VerticalTextview;
import com.youth.banner.Banner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.AppVersion;
import wai.express.BaseActivity;
import wai.express.R;
import wai.express.method.CommonAdapter;
import wai.express.method.CommonViewHolder;
import wai.express.method.Utils;
import wai.express.model.AdvertModel;
import wai.express.model.service;

/**
 * 主页
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.main_lv)
    ListView mainLv;
    View view;
    CommonAdapter<service> commonAdapter;
    List<service> list;
    @Bind(R.id.user_center_tv)
    TextView user_center_tv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;
    @Bind(R.id.yz_srl)
    SwipeRefreshLayout yzSrl;
    public static MainActivity install;
    List<String> images;
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.seal_tv1)
    TextView sealTv1;
    @Bind(R.id.pt_tv)
    TextView ptTv;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.pt_ll)
    LinearLayout ptLl;
    @Bind(R.id.seal_tv2)
    TextView sealTv2;
    @Bind(R.id.jd_tv)
    TextView jdTv;
    @Bind(R.id.jd_desc_tv)
    TextView jdDescTv;
    @Bind(R.id.jd_type_tv)
    TextView jdTypeTv;
    @Bind(R.id.jd_ll)
    LinearLayout jdLl;
    @Bind(R.id.seal_tv3)
    TextView sealTv3;
    @Bind(R.id.cd_tv)
    TextView cdTv;
    @Bind(R.id.cd_desc_tv)
    TextView cdDescTv;
    @Bind(R.id.cd_price_tv)
    TextView cdPriceTv;
    @Bind(R.id.cd_ll)
    LinearLayout cdLl;
    @Bind(R.id.user_ll)
    LinearLayout user_ll;
    @Bind(R.id.main_tag_tv)
    VerticalTextview main_tag_tv;
    ArrayList<String> titleList;
    List<String> prices;
    @Bind(R.id.tv_main_search)
    TextView tv_main_search;

    @Override
    public int setLayout() {
        install = this;
        titleList = new ArrayList<>();
        prices = new ArrayList<>();
//        titleList.add("拉阿拉");
//        titleList.add("哦哦哦");
        images = new ArrayList<>();
//        images.add("https://img6.bdstatic.com/img/image/smallpic/2.jpg");
//        images.add("https://img6.bdstatic.com/img/image/smallpic/3.jpg");
        list = new ArrayList<>();
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        Banner banner = (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        getNum();
        BmobQuery<AppVersion> query = new BmobQuery<AppVersion>();
        query.findObjects(new FindListener<AppVersion>() {
            @Override
            public void done(List<AppVersion> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        int now = Integer.parseInt(Utils.getVersion().replace(".", ""));
                        int news = Integer.parseInt(list.get(0).getVersion().replace(".", ""));
                        if (news > now) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.install);
                            builder.setTitle("提示");
                            builder.setMessage(list.get(0).getUpdate_log());
                            builder.setNegativeButton("确定", (dialogInterface, i) -> {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(list.get(0).getAndroid_url());
                                intent.setData(content_url);
                                startActivity(intent);
                            });
                            builder.setPositiveButton("取消", null);
                            builder.show();
                        }
                    }
                }
            }
        });
        commonAdapter = new CommonAdapter<service>(this, list, R.layout.item_new) {
            @Override
            public void convert(CommonViewHolder holder, service service, int position) {
                holder.setText(R.id.express_id_tv, service.getExpress_name());
                holder.setText(R.id.express_price_tv, service.getGood_price() + "元");
                holder.setText(R.id.content_tv, service.getExpress_address() + "   ");
                holder.setText(R.id.express_state_tv, service.getState());
                holder.setText(R.id.time_tv, service.getCreatedAt().substring(5, 16));
                holder.setText(R.id.desc_tv, service.getRemark());
                holder.setVisible(R.id.desc_tv, true);

                holder.setText(R.id.diver_name_tv, "骑士编码：" + service.getCourier().getUsername());
                if (Utils.getCache("is_course").equals("1")) {
                    holder.setVisible(R.id.get_order_btn, true);
                } else {
                    holder.setVisible(R.id.get_order_btn, false);
                }
                holder.setOnClickListener(R.id.get_order_btn, view12 -> {
                    BmobUser user = new BmobUser();
                    user.setObjectId(Utils.getCache("user_id"));
                    service.setCourier(user);
                    service.setState("已接单");
                    service.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                list = new ArrayList<>();
                                MainActivity.install.refresh();
                                ToastShort("抢单成功");
                            } else {
                                ToastShort("抢单失败");
                            }
                        }
                    });
                });
            }
        };
        //BmobUser bmobUser = BmobUser.getCurrentUser();
        refresh();
        mainLv.setAdapter(commonAdapter);
        mainLv.setOnItemClickListener((adapterView, view13, i, l) -> {
            service model = list.get(i);
            Utils.IntentPost(OrderDetailActivity.class, intent -> intent.putExtra("model", model));
        });
        view = View.inflate(this, R.layout.item_empty, null);
        view.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(Utils.getCache("user_id"))) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 99);
            }
        });
        mainLv.setEmptyView(view);
        user_center_tv.setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(Utils.getCache("user_id"))) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 99);
            } else {
                Utils.IntentPost(UserCenterActivity.class);
            }
        });//跳转到个人中心页面
        yzSrl.setOnRefreshListener(() -> {
            list = new ArrayList<>();
            refresh();
        });
        BmobQuery<AdvertModel> qq = new BmobQuery<>();
        qq.findObjects(new FindListener<AdvertModel>() {
            @Override
            public void done(List<AdvertModel> list, BmobException e) {
                if (e == null) {
                    for (AdvertModel model : list) {
                        switch (model.getType()) {
                            case "1":
                                titleList.add(model.getContent());
                                break;
                            case "3":
                                prices.add(model.getContent());
                                break;
                            default:
                                images.add(model.getContent());
                                break;
                        }
                    }
                    if (prices.size() == 3) {
                        textView2.setText(prices.get(0) + "元/件");
                        jdTypeTv.setText(prices.get(1) + "元/件");
                        cdPriceTv.setText(prices.get(2) + "元/件");
                    }
                    main_tag_tv.setTextList(titleList);//加入显示内容,集合类型
                    banner.setImages(images);
                    banner.start();
                }
            }
        });

        ptLl.setOnClickListener(view1 -> {
            if (prices.size() == 3) {
                input(prices.get(0), "q9CB111F");
            }
        });//普通
        jdLl.setOnClickListener(view1 -> {
            if (prices.size() == 3) {
                input(prices.get(1), "vKnxGGGK");
            }
        });//较大
        cdLl.setOnClickListener(view1 -> {
            if (prices.size() == 3) {
                input(prices.get(2), "hi3U777I");
            }
        });//超大

        main_tag_tv.setText(18, 5, Color.BLACK);//设置属性,具体跟踪源码
        main_tag_tv.setTextStillTime(3000);//设置停留时长间隔
        main_tag_tv.setAnimTime(300);//设置进入和退出的时间间隔
    }

    private void input(String position, String type) {
        if (TextUtils.isEmpty(Utils.getCache("user_id"))) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 99);
        } else {
            Intent intent = new Intent(MainActivity.this, AddExpressActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("position", position);
            startActivityForResult(intent, 99);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getNum();
    }

    private void getNum() {
        BmobQuery<service> query_count = new BmobQuery<>();
        query_count.addWhereEqualTo("priceType", "q9CB111F");
        query_count.count(service.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                sealTv1.setText("销量：" + (386 + integer) + "件");
            }
        });
        query_count = new BmobQuery<>();
        query_count.addWhereEqualTo("priceType", "vKnxGGGK");
        query_count.count(service.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                sealTv2.setText("销量：" + (228 + integer) + "件");
            }
        });
        query_count = new BmobQuery<>();
        query_count.addWhereEqualTo("priceType", "hi3U777I");
        query_count.count(service.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                sealTv3.setText("销量：" + (108 + integer) + "件");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 22) {
            if (!TextUtils.isEmpty(Utils.getCache("user_id"))) {
                refresh();
            }
        }
        if (requestCode == 11 && resultCode == 12) {
            List<BmobQuery<service>> and = new ArrayList<BmobQuery<service>>();
            String una = data.getStringExtra("username");
            String datastart = data.getStringExtra("createdAt");
            String createdend = data.getStringExtra("createdend");
            BmobQuery<service> query = new BmobQuery<service>();
            query.include("courier");
            if (!una.equals("")) {
                query.addWhereEqualTo("username", una);
            }
            // query.addWhereGreaterThan("createdAt", new BmobDate(getDate(datastart)));//之後
            // query.addWhereLessThan("createdAt", new BmobDate(getDate(createdend)));//之前

            BmobQuery<service> q1 = new BmobQuery<>();
            q1.addWhereGreaterThan("createdAt", new BmobDate(getDate(datastart)));//之後
            and.add(q1);
            BmobQuery<service> q2 = new BmobQuery<>();
            q2.addWhereLessThan("createdAt", new BmobDate(getDate(createdend)));//之前
            and.add(q2);
            query.and(and);
            query.findObjects(new FindListener<service>() {
                @Override
                public void done(List<service> list, BmobException e) {
                    if (e == null) {
                        commonAdapter.refresh(list);
                        if (list.size() == 0) {
                            empty_tv.setVisibility(View.VISIBLE);
                            empty_tv.setText("当前无新订单~~");
                        } else {
                            empty_tv.setVisibility(View.GONE);
                        }
                    } else {
                        empty_tv.setVisibility(View.VISIBLE);
                        empty_tv.setText("网络错误");
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        main_tag_tv.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        main_tag_tv.stopAutoScroll();
    }

    @Override
    public void initEvents() {
        tv_main_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QISHIActivity.class);
                startActivityForResult(intent, 11);
            }
        });
    }

    public Date getDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(s);
            return d1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void refresh() {
        list = new ArrayList<>();
        BmobQuery<service> query = new BmobQuery<>();
        query.include("user,courier");
        String is = Utils.getCache("is_course");
        if (!TextUtils.isEmpty(is) && !is.equals("0")) {

            user_ll.setVisibility(View.GONE);
            yzSrl.setVisibility(View.VISIBLE);
            switch (Utils.getCache("is_course")) {
                case "1":
                    query.addWhereEqualTo("state", "已支付");
                    break;
                case "2":
                    tv_main_search.setVisibility(View.VISIBLE);
                    query.order("-state");
                    break;
            }
            query.findObjects(new FindListener<service>() {
                @Override
                public void done(List<service> services, BmobException e) {
                    if (e == null) {
                        commonAdapter.refresh(services);
                        list = services;
                        if (services.size() == 0) {
                            empty_tv.setVisibility(View.VISIBLE);
                            empty_tv.setText("当前无新订单~~");
                        } else {
                            empty_tv.setVisibility(View.GONE);
                        }
                    }
                }
            });
            if (yzSrl.isRefreshing()) {
                yzSrl.setRefreshing(false);
            }
        } else {
            user_ll.setVisibility(View.VISIBLE);
            yzSrl.setVisibility(View.GONE);
        }
    }

    private void LoadSearchList() {

    }
}
