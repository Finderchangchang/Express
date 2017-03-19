package wai.express.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import wai.express.BaseActivity;
import wai.express.R;
import wai.express.model.service;

/**
 * 订单详情页面
 * Created by Administrator on 2017/3/1.
 */

public class OrderDetailActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.order_state_tv)
    TextView orderStateTv;
    @Bind(R.id.courier_tel_tv)
    TextView courierTelTv;
    @Bind(R.id.user_tel_btn)
    Button userTelBtn;
    @Bind(R.id.courier_tel_btn)
    Button courierTelBtn;
    @Bind(R.id.courier_tel_ll)
    LinearLayout courier_tel_ll;
    @Bind(R.id.remark_tv)
    TextView remark_tv;
    @Bind(R.id.express_name_tv)
    TextView express_name_tv;
    service model;
    @Bind(R.id.express_tel_tv)
    TextView expressTelTv;
    @Bind(R.id.express_address_tv)
    TextView expressAddressTv;
    @Bind(R.id.express_price_tv)
    TextView express_price_tv;

    @Override
    public void initEvents() {
        if (model != null) {
            orderStateTv.setText(model.getState());
            expressTelTv.setText(model.getUser_tel());
            expressAddressTv.setText(model.getExpress_address());
            express_name_tv.setText(model.getExpress_name());
            express_price_tv.setText(model.getGood_price() + "元");
            if (model.getCourier() != null) {
                courierTelTv.setText(model.getCourier().getUsername());
                courier_tel_ll.setVisibility(View.VISIBLE);
                courierTelBtn.setVisibility(View.VISIBLE);
            }
            userTelBtn.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + model.getUser().getUsername());
                intent.setData(data);
                startActivity(intent);
            });
            courierTelBtn.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + model.getCourier().getUsername());
                intent.setData(data);
                startActivity(intent);
            });
            remark_tv.setText(model.getRemark());
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initViews() {
        model = (service) getIntent().getSerializableExtra("model");
        toolbar.setLeftClick(() -> finish());
    }
}
