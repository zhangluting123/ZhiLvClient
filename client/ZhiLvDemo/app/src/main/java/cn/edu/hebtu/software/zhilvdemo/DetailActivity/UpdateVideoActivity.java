package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.zhilvdemo.Adapter.StaggeredGridAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.MoreDetail;
import cn.edu.hebtu.software.zhilvdemo.Data.Video;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.UploadAndDownload.EditVideoTask;
import cn.edu.hebtu.software.zhilvdemo.UploadAndDownload.UploadVideoTask;
import cn.edu.hebtu.software.zhilvdemo.Util.DateUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.DensityUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.FileUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.FinalVariableUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.GetPhotoUtil;
import cn.edu.hebtu.software.zhilvdemo.Util.PermissionUtil;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ProjectName:    ZhiLv
 * @Description:    编辑视频页
 * @Author:         张璐婷
 * @CreateDate:     2021/1/8 11:23
 * @Version:        1.0
 */
public class UpdateVideoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnSubmit;
    private ImageView videoBitmap;
    private EditText destination;
    private TextView traffic;
    private TextView beginDate;
    private EditText days;
    private TextView people;
    private EditText money;
    private EditText videoTitle;
    private EditText videoContent;
    private RelativeLayout topic;
    private TextView topicText;
    private RelativeLayout location;
    private TextView locationText;

    //视频缩略图
    private Bitmap bitmap;
    private TimePickerView pvTime; //时间选择器
    private MyApplication data;
    private Video video = new Video();
    private Video beforeVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_video);

        data = (MyApplication)getApplication();
        data.setTopic(null);
        data.setMoreDetail(null);
        getViews();
        registListener();
        initData();
    }

    private void initData(){
        Intent intent = getIntent();
        beforeVideo = intent.getParcelableExtra("video");
        videoTitle.setText(beforeVideo.getTitle());
        videoContent.setText(beforeVideo.getContent());
        locationText.setText(beforeVideo.getLocation());
        if(null != beforeVideo.getTopic()) {
            topicText.setText("#"+beforeVideo.getTopic().getTitle()+"#");
        }
        //more detail
        MoreDetail detail = beforeVideo.getDetail();
        destination.setText(detail.getDestination());
        traffic.setText(detail.getTraffic());
        beginDate.setText(DateUtil.getDateStr(detail.getBeginDate()));
        if(null != detail.getDays()){
            days.setText(detail.getDays() +"");
        }
        people.setText(detail.getPeople());
        if(null != detail.getMoney()) {
            money.setText(detail.getMoney() + "");
        }
        //缩略图
        Glide.with(this).
                load("http://"+data.getIp()+":8080/ZhiLvProject/"+beforeVideo.getImg())
                .into(videoBitmap);
    }

    private void getViews(){
        toolbar = findViewById(R.id.toolbar);
        btnSubmit = findViewById(R.id.update_video_btnSubmit);
        videoBitmap = findViewById(R.id.update_video_bitmap);
        destination = findViewById(R.id.update_more_detail_et_destination);
        traffic = findViewById(R.id.update_more_detail_et_traffic);
        beginDate = findViewById(R.id.update_more_detail_et_beginDate);
        days = findViewById(R.id.update_more_detail_et_days);
        people = findViewById(R.id.update_more_detail_et_people);
        money = findViewById(R.id.update_more_detail_et_money);
        videoTitle = findViewById(R.id.update_video_et_title);
        videoContent = findViewById(R.id.update_video_et_content);
        topic = findViewById(R.id.update_video_rl_topic);
        topicText = findViewById(R.id.update_video_tv_topic);
        location = findViewById(R.id.update_video_rl_location);
        locationText = findViewById(R.id.update_video_tv_location);
    }

    private void registListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        btnSubmit.setOnClickListener(listener);
        videoBitmap.setOnClickListener(listener);
        traffic.setOnClickListener(listener);
        beginDate.setOnClickListener(listener);
        people.setOnClickListener(listener);
        topic.setOnClickListener(listener);
        location.setOnClickListener(listener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.update_video_btnSubmit:
                    video.setVideoId(beforeVideo.getVideoId());
                    video.setUser(beforeVideo.getUser());
                    video.setTitle(videoTitle.getText().toString());
                    video.setContent(videoContent.getText().toString());
                    video.setLocation(locationText.getText().toString());
                    video.setUploadTime(beforeVideo.getUploadTime());
                    if(null != data.getTopic()){
                        video.setTopic(data.getTopic());
                    }else{
                        video.setTopic(beforeVideo.getTopic());
                    }
                    MoreDetail moreDetail = new MoreDetail();
                    moreDetail.setDestination(destination.getText().toString());
                    moreDetail.setBeginDate(DateUtil.getDate(beginDate.getText().toString()));
                    moreDetail.setTraffic(traffic.getText().toString());
                    if(!"".equals(days.getText().toString())) {
                        moreDetail.setDays(Integer.parseInt(days.getText().toString()));
                    }
                    moreDetail.setPeople(people.getText().toString());
                    if(!"".equals(money.getText().toString())) {
                        moreDetail.setMoney(Integer.parseInt(money.getText().toString()));
                    }
                    video.setDetail(moreDetail);
                    EditVideoTask task = new EditVideoTask(UpdateVideoActivity.this, video);
                    task.execute("http://"+data.getIp()+":8080/ZhiLvProject/audit/video/add");
                    break;
                case R.id.update_video_bitmap:
                    if(PermissionUtil.openSDCardPermission(UpdateVideoActivity.this)){
                        GetPhotoUtil.chooseVideo(UpdateVideoActivity.this);
                    }
                    break;
                case R.id.update_more_detail_et_beginDate:
                    initTimePicker();
                    pvTime.show();
                    break;
                case R.id.update_more_detail_et_people:
                    showCustomDialog(R.layout.dialog_choose_people);
                    break;
                case R.id.update_more_detail_et_traffic:
                    showCustomDialog(R.layout.dialog_choose_traffic);
                    break;
                case R.id.update_video_rl_topic:
                    intent = new Intent(getApplicationContext(), SearchTopicActivity.class);
                    startActivity(intent);
                    break;
                case R.id.update_video_rl_location:
                    break;
                default:
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

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/1  10:44
     *  @Description: 得到选中视频中的信息
     */
    public void getVideoData(Uri uri) {
        ContentResolver cr = this.getContentResolver();
        //数据库查询操作
        //第一个参数 uri：为要查询的数据库+表的名称
        //第二个参数 projection ： 要查询的列
        //第三个参数 selection ： 查询的条件，相当于SQL where
        //第三个参数 selectionArgs ： 查询条件的参数，相当于?
        //第四个参数 sortOrder ： 结果排序
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // 视频ID:MediaStore.Audio.Media._ID
//                int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                // 视频名称：MediaStore.Audio.Media.TITLE
                String videoTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                video.setTitle(videoTitle);
                // 视频路径：MediaStore.Audio.Media.DATA
                String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                video.setPath(videoPath);
                // 视频时长（默认ms）：MediaStore.Audio.Media.DURATION
                long duration_ms = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                int duration_s = (int)Math.floor(duration_ms / 1000);
                String ds = duration_s + "";
                if (duration_s < 10){
                    ds = "0" + duration_s;
                }
                int duration_m = (int)Math.floor((duration_s % 3600) / 60);
                String dm = duration_m + "";
                if (duration_m < 10){
                    dm = "0" + duration_m;
                }
                int duration_h = duration_s / 3600;
                String dh = duration_h + "";
                if (duration_h < 10){
                    dh = "0" + duration_h;
                }
                video.setDuration(dh + ":" + dm + ":" + ds);
                // 视频大小（默认Byte）：MediaStore.Audio.Media.SIZE
                long size_byte = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                double size_MB = (size_byte * 1.0) / (1024 * 1024);
                String sm = new java.text.DecimalFormat("#.00").format(size_MB);
                String videoSize = sm + "MB";
                video.setSize(videoSize);
                // 生成视频缩略图
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(this,Uri.parse(videoPath));
                bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                video.setImg(GetPhotoUtil.bitmapToImg(bitmap));
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FinalVariableUtil.RC_CHOOSE_VIDEO:
                    Uri uri = data.getData();
                    String vPath = FileUtil.getFilePathByUri(this, uri);
                    if (!TextUtils.isEmpty(vPath)) {
                        getVideoData(uri);
                        videoBitmap.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        videoBitmap.setImageBitmap(bitmap);
                        Log.e("video", "标题：" + video.getTitle());
                        Log.e("video", "路径：" + video.getPath());
                        Log.e("video", "时长：" + video.getDuration());
                        Log.e("video", "大小：" + video.getSize());
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        if(null != data.getTopic()) {
            topicText.setText(data.getTopic().getTitle());
        }
        super.onResume();
    }
}