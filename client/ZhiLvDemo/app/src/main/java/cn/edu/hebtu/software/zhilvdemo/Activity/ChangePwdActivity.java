package cn.edu.hebtu.software.zhilvdemo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.zhilvdemo.Data.User;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.UploadAndDownload.UploadUserMsg;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePwdActivity extends AppCompatActivity {
    private EditText edtOldPwd;
    private EditText edtNewFirstPwd;
    private EditText edtNewSecondPwd;
    private Button btnSubmit;
    private MyApplication data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        data = (MyApplication)getApplication();
        getViews();

        edtOldPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){ //失去焦点
                    if(!edtOldPwd.getText().toString().equals(data.getUser().getPassword())){
                        Toast.makeText(ChangePwdActivity.this, "原密码输入错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtNewFirstPwd.getText().toString().equals(edtNewSecondPwd.getText().toString())){
                    Toast.makeText(ChangePwdActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                }else{
                    User user = new User();
                    user.setUserId(data.getUser().getUserId());
                    user.setPassword(edtNewSecondPwd.getText().toString());
                    UploadUserMsg uploadUserMsg = new UploadUserMsg(getApplicationContext(), user);
                    uploadUserMsg.execute("http://"+ data.getIp() +":8080/ZhiLvProject/user/changeMsg");
                }
            }
        });
    }

    private void getViews(){
        edtOldPwd = findViewById(R.id.edt_old_pwd);
        edtNewFirstPwd = findViewById(R.id.edt_new_pwd1);
        edtNewSecondPwd = findViewById(R.id.edt_new_pwd2);
        btnSubmit = findViewById(R.id.btn_submit);
    }


}