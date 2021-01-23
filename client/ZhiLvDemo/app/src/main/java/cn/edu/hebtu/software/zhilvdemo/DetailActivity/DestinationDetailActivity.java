package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.edu.hebtu.software.zhilvdemo.Adapter.StaggeredGridAdapter;
import cn.edu.hebtu.software.zhilvdemo.Data.Note;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.BaiduMapUtil.PoiOverlay;
import cn.edu.hebtu.software.zhilvdemo.Util.DensityUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DestinationDetailActivity extends AppCompatActivity {
    private RelativeLayout head;
    private SearchView searchView;
    private TextView location;
    private TextView locationTitle;
    private Button btnLocationDetail;
    private TextView locationIntroduce;
    private RecyclerView recyclerView;

    private TextureMapView mapView;
    private BaiduMap baiduMap;
    private PoiSearch poiSearch;
    private SuggestionSearch suggestionSearch;
    private ListPopupWindow listPopupWindow;

    private List<Note> mDatas;

    private MyApplication data;
    private List<Map<String,String>> sugList;
    private boolean submitFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_detail);

        data = (MyApplication)getApplication();

        initView();
        registListener();

        location.setText(data.getCity());
        searchView.setQueryHint(data.getSearchText());
    }

    private void getViews(){
        head = findViewById(R.id.des_head);
        searchView = findViewById(R.id.searchView);
        location = findViewById(R.id.des_location);
        locationTitle = findViewById(R.id.des_tv_location_title);
        locationIntroduce = findViewById(R.id.des_tv_location_introduce);
        btnLocationDetail = findViewById(R.id.des_detail_btn_locationDetail);
        recyclerView = findViewById(R.id.des_detail_recycler);
        mapView = findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
    }
    private void initView(){
        getViews();
//        initData();
        //显示俯视图
        showOverLook();
        //比例尺操作
        zoomLevelOp();
        //设置图层定位
        baiduMap.setMyLocationEnabled(true);
        //隐藏百度的LOGO
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        // 不显示地图上比例尺
        mapView.showScaleControl(false);
        // 不显示地图缩放控件（按钮控制栏）
        mapView.showZoomControls(false);
        //不显示指南针
        baiduMap.setCompassEnable(false);

        //查询目标地址
        POISearch();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        StaggeredGridAdapter adapter = new StaggeredGridAdapter(mDatas,this);
        adapter.setOnItemClickListener(new StaggeredGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(!mDatas.get(position).isFlag()){
                    Intent intent = new Intent(DestinationDetailActivity.this, VideoDetailActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(DestinationDetailActivity.this ,TravelDetailActivity.class);
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }


    private void registListener(){
        btnLocationDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestinationDetailActivity.this, SceneDetailActivity.class);
                startActivity(intent);
            }
        });
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);//让键盘的回车键设置成搜索
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("searchTopic", "submit");
                //清除焦点，收软键盘
                searchView.clearFocus();
                data.setSearchText(query);
                searchView.setQueryHint(query);
                searchView.setQuery(null, false);
                Intent intent = new Intent(DestinationDetailActivity.this, DestinationDetailActivity.class);
                startActivity(intent);
                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("searchTopic", "change");
                String text =  searchView.getQuery().toString();
                if(null != text && !submitFlag){
                    SugSearch();
                }
                submitFlag = false;
                return false;
            }
        });

        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(DestinationDetailActivity.this,MapRouteActivity.class);
                startActivity(intent);
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });
    }


    /**
     *  @author: 张璐婷
     *  @time: 2021/1/4  18:42
     *  @Description: 显示俯视图效果
     */
    private void showOverLook() {
        MapStatus mapStatus = new MapStatus.Builder()
                        .overlook(0)//0 —— -45度
                        .build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
        //应用地图状态
        baiduMap.setMapStatus(msu);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/4  18:44
     *  @Description: 比例尺操作
     */
    private void zoomLevelOp() {
        //设置允许放大和缩小的比例范围
        baiduMap.setMaxAndMinZoomLevel(8, 8);
        //设置默认比例为100m
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(8);
        baiduMap.setMapStatus(msu);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/4  18:33
     *  @Description: POI检索地点
     */
    private void POISearch(){
        poiSearch = PoiSearch.newInstance();
        //创建POI检索监听器
        OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(DestinationDetailActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
                    return;
                }
                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                    baiduMap.clear();
                    MyPoiOverlay poiOverlay = new MyPoiOverlay(baiduMap);
                    poiOverlay.setData(poiResult);// 设置POI数据
                    baiduMap.setOnMarkerClickListener(poiOverlay);
                    poiOverlay.addToMap();// 将所有的overlay添加到地图上到地图上
                    poiOverlay.zoomToSpan();
                    //设置查找城市为全局变量
                    data.setSearchCity(poiResult.getAllPoi().get(0).getCity());
                    data.setSearchLatitude(poiResult.getAllPoi().get(0).location.latitude);
                    data.setSearchLongitude(poiResult.getAllPoi().get(0).location.longitude);

                    //设置到屏幕中心
                    LatLng latLng = poiResult.getAllPoi().get(0).getLocation();
                    showLocOnMap(latLng.latitude,latLng.longitude);

                    for(int i = 0; i < poiResult.getAllPoi().size(); ++i){
                        Log.e("city", poiResult.getAllPoi().get(i).getCity());
                    }

                }
            }
            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }
            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) { //废弃

            }
        };
        //设置POI监听器
        poiSearch.setOnGetPoiSearchResultListener(listener);
        /**
         *  PoiCiySearchOption 设置检索属性
         *  city 检索城市
         *  keyword 检索内容关键字
         *  pageNum 分页页码
         */
        PoiCitySearchOption option = new PoiCitySearchOption();
        option.city(data.getCity())
                .keyword(data.getSearchText())
                .pageCapacity(1)
                .pageNum(0);
        option.cityLimit(false);
        poiSearch.searchInCity(option);
    }

    class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap arg0) {
            super(arg0);
        }
        @Override
        public boolean onPoiClick(int arg0) {
            super.onPoiClick(arg0);
            PoiInfo poiInfo = getPoiResult().getAllPoi().get(arg0);
            // 检索poi详细信息
            poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poiInfo.uid));
            return true;
        }
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/4  20:47
     *  @Description: 显示定位
     */
    private void showLocOnMap(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        //移动到中心位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(msu);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/5  17:03
     *  @Description: Sug检索关键字
     */
    private void SugSearch(){
        suggestionSearch = SuggestionSearch.newInstance();
        //Sug检索监听器
        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                //处理sug检索结果
                List<SuggestionResult.SuggestionInfo> suggestAllList = suggestionResult.getAllSuggestions();
                if(null == suggestAllList) return;
                sugList = new ArrayList<Map<String,String>>();
                for(int i = 0; i < suggestAllList.size(); ++i){
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("name", suggestAllList.get(i).key);
                    map.put("address", suggestAllList.get(i).address);
                    sugList.add(map);
                }
                //显示弹出框
                if(null != listPopupWindow && listPopupWindow.isShowing()){
                    listPopupWindow.dismiss();
                }
                showListPopupWindow();

            }
        };
        suggestionSearch.setOnGetSuggestionResultListener(listener);
        //在您的项目中，keyword为随您的输入变化的值
        suggestionSearch.requestSuggestion(new SuggestionSearchOption()
                .city(data.getCity())
                .keyword(searchView.getQuery().toString()));
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/5  16:20
     *  @Description: 设置目的地的下拉搜索提示框
     */

    private void showListPopupWindow(){
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setHeight(DensityUtil.dip2px(this, 300));
        listPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setAnchorView(head);//关联控件
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                sugList,
                R.layout.item_searchlocation,
                new String[]{"name","address"},
                new int[]{R.id.search_name,R.id.search_address});
        listPopupWindow.setAdapter(simpleAdapter);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();
                submitFlag = true;
                searchView.setQuery(sugList.get(position).get("name"),true);
            }
        });
        listPopupWindow.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        poiSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}