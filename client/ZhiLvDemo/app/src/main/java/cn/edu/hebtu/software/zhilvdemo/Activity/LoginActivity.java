package cn.edu.hebtu.software.zhilvdemo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Util.JudgeStrUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText inputPhoneOrEmail;
    private EditText inputPassword;
    private ImageView ivEye;
    private Button btnLoginIn;
    private TextView fgpwd;
    private TextView signUp;

    private boolean isPhone;
    private boolean isEmail;
    private boolean isHideFirst = true;//输入框密码是否是隐藏的，默认为true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getViews();
        registListener();
    }

    private void getViews(){
        inputPhoneOrEmail = findViewById(R.id.edt_input_phoneOrEmail);
        inputPassword = findViewById(R.id.edt_input_password);
        ivEye = findViewById(R.id.iv_eye);
        btnLoginIn = findViewById(R.id.btn_login_in);
        fgpwd = findViewById(R.id.tv_fgpwd);
        signUp = findViewById(R.id.tv_sign_up);
    }

    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        ivEye.setOnClickListener(listener);
        btnLoginIn.setOnClickListener(listener);
        fgpwd.setOnClickListener(listener);
        signUp.setOnClickListener(listener);
    }

    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.iv_eye:
                    if (isHideFirst == true) {
                        ivEye.setImageResource(R.mipmap.yanjing);
                        //明文
                        HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                        inputPassword.setTransformationMethod(method1);
                        isHideFirst = false;
                    } else {
                        ivEye.setImageResource(R.mipmap.biyanjing);
                        //密文
                        TransformationMethod method = PasswordTransformationMethod.getInstance();
                        inputPassword.setTransformationMethod(method);
                        isHideFirst = true;
                    }
                    break;
                case R.id.btn_login_in:
                    isPhone = JudgeStrUtil.isPhone(inputPhoneOrEmail.getText().toString().trim());
                    isEmail = JudgeStrUtil.isEmail(inputPhoneOrEmail.getText().toString().trim());
                    String pwd = inputPassword.getText().toString().trim();
                    if(isPhone && !isEmail){
                        // 如果是手机号
                    }else if(!isPhone && isEmail){
                        //如果是邮箱
                    }else{
                        Toast.makeText(LoginActivity.this, "手机或邮箱账号有误",Toast.LENGTH_SHORT).show();
                        inputPhoneOrEmail.requestFocus();
                    }
                    break;
                case R.id.tv_fgpwd:
                    intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.tv_sign_up:
                    intent = new Intent(LoginActivity.this, RegistActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            finish();
        }
        return true;
    }
}