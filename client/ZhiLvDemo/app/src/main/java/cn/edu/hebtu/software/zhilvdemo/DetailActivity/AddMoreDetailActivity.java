package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Util.DensityUtil;

import android.app.Dialog;;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMoreDetailActivity extends AppCompatActivity {
    private TimePickerView pvTime; //时间选择器对象
    private Button btnSubmit;
    private EditText destination;
    private TextView traffic;
    private TextView beginDate;
    private EditText days;
    private TextView people;
    private EditText money;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_detail);

        getViews();
        registListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getViews(){
        btnSubmit = findViewById(R.id.add_more_detail_btnSubmit);
        destination = findViewById(R.id.add_more_detail_et_destination);
        traffic = findViewById(R.id.add_more_detail_et_traffic);
        beginDate = findViewById(R.id.add_more_detail_et_beginDate);
        days = findViewById(R.id.add_more_detail_et_days);
        people = findViewById(R.id.add_more_detail_et_people);
        money = findViewById(R.id.add_more_detail_et_money);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        btnSubmit.setOnClickListener(listener);
        traffic.setOnClickListener(listener);
        beginDate.setOnClickListener(listener);
        people.setOnClickListener(listener);
    }

    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_more_detail_btnSubmit:
                    break;
                case R.id.add_more_detail_et_beginDate:
                    initTimePicker();
                    pvTime.show();
                    break;
                case R.id.add_more_detail_et_people:
                    showCustomDialog(R.layout.dialog_choose_people);
                    break;
                case R.id.add_more_detail_et_traffic:
                    showCustomDialog(R.layout.dialog_choose_traffic);
                    break;
            }
        }
    }



    private void showCustomDialog(int layoutRes){
        Dialog bottomDialog = new Dialog(this, R.style.DialogTheme);
        View contentView = LayoutInflater.from(this).inflate(layoutRes, null);
        bottomDialog.setContentView(contentView);
        WindowManager.LayoutParams lp = bottomDialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = DensityUtil.dip2px(this, 180);
        bottomDialog.getWindow().setAttributes(lp);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.AnimBottom);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.setCancelable(true);

        LinearLayout choose = contentView.findViewById(R.id.dialog_ll_choose);
        if(layoutRes == R.layout.dialog_choose_traffic){
            for(int i = 0; i < choose.getChildCount(); ++i){
                int finalI = i;
                choose.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        traffic.setText((String)choose.getChildAt(finalI).getTag());
                        bottomDialog.dismiss();
                    }
                });
            }
        }else{
            for(int i = 0; i < choose.getChildCount(); ++i){
                int finalI = i;
                choose.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        people.setText((String)choose.getChildAt(finalI).getTag());
                        bottomDialog.dismiss();
                    }
                });
            }
        }
        bottomDialog.show();

    }

    //初始化时间选择器
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);//起始时间
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 12, 31);//结束时间
        pvTime = new TimePickerView.Builder(this,
                new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        //选中事件回调
                        //mTvMyBirthday 这个组件就是个TextView用来显示日期 如2020-09-08
                        beginDate.setText(getTimes(date));
                    }
                })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "", "")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    //格式化时间
    private String getTimes(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

}