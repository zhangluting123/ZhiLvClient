package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.BaiduMapUtil.DrivingRouteOverlay;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;


public class MapRouteActivity extends AppCompatActivity {
    private Button btnMoreRoutePlan;
    private TextureMapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private LocationClientOption locationClientOption;
    private PoiSearch poiSearch;
    private RoutePlanSearch routePlanSearch;
    private MyApplication data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_route);

        data = (MyApplication)getApplication();

        initView();

        btnMoreRoutePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreRoutePlanFromBaiduMapAPP();
            }
        });
    }

    private void initView(){
        getViews();
        //驾车路线规划
        DrivingRoutePlan();
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
        //不显示指南针
        baiduMap.setCompassEnable(false);


    }

    private void getViews(){
        btnMoreRoutePlan = findViewById(R.id.btn_more_route_plan);
        mapView = findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
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
        baiduMap.setMapStatus(msu);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/4  18:44
     *  @Description: 比例尺操作
     */
    private void zoomLevelOp() {
        //设置允许放大和缩小的比例范围
        baiduMap.setMaxAndMinZoomLevel(8, 19);
        //设置默认比例为100m
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(19);
        baiduMap.setMapStatus(msu);
    }


    /**
     *  @author: 张璐婷
     *  @time: 2021/1/7  17:55
     *  @Description: 驾车路线规划
     */
    private void DrivingRoutePlan(){
        routePlanSearch = RoutePlanSearch.newInstance();
        //创建路线规划结果监听器
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                if (drivingRouteResult == null || drivingRouteResult.error !=   SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MapRouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//                    drivingRouteResult.getSuggestAddrInfo();
//                    return;
//                }
                if(drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR){
                    baiduMap.clear();
                    //创建DrivingRouteOverlay实例
                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
                    if (drivingRouteResult.getRouteLines().size() > 0) {
                        //获取路径规划数据,(以返回的第一条路线为例）
                        //为DrivingRouteOverlay实例设置数据
                        overlay.setData(drivingRouteResult.getRouteLines().get(0));
                        //在地图上绘制DrivingRouteOverlay
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };
        routePlanSearch.setOnGetRoutePlanResultListener(listener);
        //准备起终点信息
        LatLng st = new LatLng(data.getLatitude(), data.getLongitude());
        LatLng en = new LatLng(data.getSearchLatitude(), data.getSearchLongitude());
        PlanNode stNode = PlanNode.withLocation(st);
        PlanNode enNode = PlanNode.withLocation(en);
        routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
    }

    /**
     *  @author: 张璐婷
     *  @time: 2021/1/7  17:55
     *  @Description: 更多路线规划，调用百度地图APP
     */
    private void MoreRoutePlanFromBaiduMapAPP(){
        //定义起终点坐标
        LatLng startPoint = new LatLng(data.getLatitude(), data.getLongitude());
        LatLng endPoint = new LatLng(data.getSearchLatitude(), data.getSearchLongitude());

        //构建RouteParaOption参数以及策略
        //也可以通过startName和endName来构造
        RouteParaOption paraOption = new RouteParaOption()
                .startPoint(startPoint)
                .endPoint(endPoint)
                .busStrategyType(RouteParaOption.EBusStrategyType.bus_recommend_way);
        //调起百度地图
        try {
            BaiduMapRoutePlan.openBaiduMapTransitRoute(paraOption, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
}