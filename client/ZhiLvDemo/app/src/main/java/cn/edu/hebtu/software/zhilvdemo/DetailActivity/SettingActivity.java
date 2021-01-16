package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.Activity.ForgetPwdActivity;
import cn.edu.hebtu.software.zhilvdemo.R;

/**
 * @ProjectName:    ZhiLv
 * @Description:    设置界面
 * @Author:         张璐婷
 * @CreateDate:     2020/12/14$ 20:17$
 * @Version:        1.0
 */
public class SettingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout changeSelfMsg;
    private LinearLayout changePhone;
    private LinearLayout changeEmail;
    private LinearLayout changePassword;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getViews();
        registListener();
    }

    private void getViews(){
        toolbar = findViewById(R.id.setting_toolbar);
        changeSelfMsg = findViewById(R.id.changeSelfMsg);
        changePhone = findViewById(R.id.changePhone);
        changeEmail = findViewById(R.id.changeEmail);
        changePassword = findViewById(R.id.changePassword);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void registListener(){
        CustomListener listener = new CustomListener();
        changePassword.setOnClickListener(listener);
        changePhone.setOnClickListener(listener);
        changeEmail.setOnClickListener(listener);
        changeSelfMsg.setOnClickListener(listener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    class CustomListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.changeSelfMsg:
                    intent = new Intent(SettingActivity.this,UserInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.changePhone:
                    break;
                case R.id.changeEmail:
                    break;
                case R.id.changePassword:
                    intent = new Intent(SettingActivity.this, ForgetPwdActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了返回键
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            finish();
        }
        return true;
    }
}
