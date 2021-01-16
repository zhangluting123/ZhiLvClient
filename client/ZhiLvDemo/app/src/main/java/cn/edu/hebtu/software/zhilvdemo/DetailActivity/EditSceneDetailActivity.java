package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class EditSceneDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText title;
    private EditText content;
    private EditText rule;
    private EditText openTime;
    private EditText traffic;
    private EditText ticket;
    private EditText costTime;
    private EditText phone;
    private EditText website;
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scene_detail);
        getViews();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getViews(){
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.scene_et_title);
        content = findViewById(R.id.scene_et_content);
        rule = findViewById(R.id.scene_et_rule);
        openTime = findViewById(R.id.scene_et_openTime);
        traffic = findViewById(R.id.scene_et_traffic);
        ticket= findViewById(R.id.scene_et_ticket);
        costTime = findViewById(R.id.scene_et_costTime);
        phone = findViewById(R.id.scene_et_phone);
        website = findViewById(R.id.scene_et_website);
        btnSubmit = findViewById(R.id.scene_btn_submit);
    }
}