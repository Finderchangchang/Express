package wai.express.ui;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wai.express.BaseActivity;
import wai.express.R;
import wai.express.model.service;
import wai.express.view.DatePickerDialog;

/**
 * Created by Administrator on 2017/3/13.
 */

public class QISHIActivity extends BaseActivity {
    @Bind(R.id.qishi_search)
    EditText qishi_search;
    @Bind(R.id.search_start_ll)
    LinearLayout search_start_ll;
    @Bind(R.id.et_search_seal_starttime)
    TextView et_search_seal_starttime;
    @Bind(R.id.ll_search_end)
    LinearLayout ll_search_end;
    @Bind(R.id.et_search_seal_endtime)
    TextView et_search_seal_endtime;
    private DatePickerDialog dateDialog;//时间选择控件
    @Bind(R.id.btn_qishi_search)
    Button btn_qishi_search;

    @Override
    public int setLayout() {
        return R.layout.ac_qishi_list;
    }

    @Override
    public void initViews() {
        et_search_seal_endtime.setText(getNowDate());
        et_search_seal_starttime.setText(getNowDateBefor(-10));
    }

    @Override
    public void initEvents() {
        btn_qishi_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(QISHIActivity.this,MainActivity.class);
                intent.putExtra("username",qishi_search.getText().toString());
                intent.putExtra("createdAt",et_search_seal_starttime.getText().toString());
                intent.putExtra("createdend",et_search_seal_endtime.getText().toString());
                setResult(12,intent);
                finish();
//                BmobQuery<service> query = new BmobQuery<service>();
//                query.include("courier");
//                query.addWhereEqualTo("username", qishi_search.getText().toString());
//                query.addWhereGreaterThan("createdAt", new BmobDate(getDate(et_search_seal_starttime.getText().toString())));//之後
//                query.addWhereLessThan("createdAt", new BmobDate(getDate(et_search_seal_starttime.getText().toString())));//之前
//                query.findObjects(new FindListener<service>() {
//                    @Override
//                    public void done(List<service> list, BmobException e) {
//
//                    }
//                });
            }
        });
        et_search_seal_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog = new DatePickerDialog(QISHIActivity.this, et_search_seal_starttime.getText().toString());
                dateDialog.datePickerDialog(et_search_seal_starttime);//直接改变edittext的控件的值
            }
        });
        et_search_seal_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog = new DatePickerDialog(QISHIActivity.this, et_search_seal_endtime.getText().toString());
                dateDialog.datePickerDialog(et_search_seal_endtime);//直接改变edittext的控件的值
            }
        });
    }

    //前几天  day -1
    public String getNowDateBefor(int day) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.add(Calendar.DAY_OF_YEAR, day);
        Date mydate = ca.getTime();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("mydate::" + sf.format(mydate));
        return sf.format(mydate);
    }



    //获取当前日期
    public String getNowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

}
