package cn.edu.hebtu.software.zhilvdemo.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.zhilvdemo.R;
import cn.edu.hebtu.software.zhilvdemo.Setting.MyApplication;
import cn.edu.hebtu.software.zhilvdemo.Util.BaiduMapUtil.DrivingRouteOverlay;
import cn.edu.hebtu.software.zhilvdemo.Util.BaiduMapUtil.RouteLineAdapter;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
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

import java.util.List;


public class MapRouteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnMoreRoutePlan;
    private TextureMapView mapView;
    private BaiduMap baiduMap;
    private RoutePlanSearch routePlanSearch;
    private DrivingRoutePlanOption option;
    private Marker marker;
    private MyApplication data;
    private static final String TAG = "百度地图";

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
        //设置mark覆盖物拖拽监听器
        baiduMap.setOnMarkerDragListener(new MyMarkerDragListener());
        //设置mark覆盖物点击监听器
        baiduMap.setOnMarkerClickListener(new MyMarkerClickListener());

        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                clearOverlay();
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {
                String POIName = mapPoi.getName();//POI点名称
                LatLng POIPosition = mapPoi.getPosition();//POI点坐标
                //下面就是自己随便应用了
                initOverlay(POIPosition);
                //将该POI点设置为地图中心
//                baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(POIPosition));
            }
        });
    }

    //初始化添加覆盖物mark
    private void initOverlay(LatLng latLng) {
        Log.e(TAG, "Start initOverlay");

        //设置覆盖物添加的方式与效果
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)//mark出现的位置
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka))       //mark图标
                .draggable(true)//mark可拖拽
//                .animateType(MarkerOptions.MarkerAnimateType.drop)//从天而降的方式
                .animateType(MarkerOptions.MarkerAnimateType.grow)//从地生长的方式
                ;
        //添加mark
        marker = (Marker) (baiduMap.addOverlay(markerOptions));//地图上添加mark

        //弹出View(气泡，意即在地图中显示一个信息窗口)，显示当前mark位置信息
        setPopupTipsInfo(marker);

        Log.e(TAG,"End initOverlay");
    }

    //清除覆盖物
    private void clearOverlay(){
        baiduMap.clear();
        marker = null;
    }

    //覆盖物拖拽监听器
    public class MyMarkerDragListener implements BaiduMap.OnMarkerDragListener {

        @Override
        public void onMarkerDrag(Marker marker) {

        }

        //拖拽结束，调用方法，弹出View(气泡，意即在地图中显示一个信息窗口)，显示当前mark位置信息
        @Override
        public void onMarkerDragEnd(Marker marker) {
            setPopupTipsInfo(marker);
        }

        @Override
        public void onMarkerDragStart(Marker marker) {

        }
    }

    //覆盖物点击监听器
    public class MyMarkerClickListener implements BaiduMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            //调用方法,弹出View(气泡，意即在地图中显示一个信息窗口)，显示当前mark位置信息
            setPopupTipsInfo(marker);
            return false;
        }
    }

    //想根据Mark中的经纬度信息，获取当前的位置语义化结果，需要使用地理编码查询和地理反编码请求
    //在地图中显示一个信息窗口
    private void setPopupTipsInfo(Marker marker){
        //获取当前经纬度信息
        final LatLng latLng = marker.getPosition();
        final String[] addr = new String[1];
        //实例化一个地理编码查询对象
        GeoCoder geoCoder = GeoCoder.newInstance();
        //为地理编码查询对象设置一个请求结果监听器
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                Log.e(TAG, "地理编码信息 ---> \nLocation : " + geoCodeResult.getLocation()
                        + "\ntoString : " + geoCodeResult.toString()
                        + "\ndescribeContents : " + geoCodeResult.describeContents());
            }

            //当获取到反编码信息结果的时候会调用
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                //获取地理反编码位置信息
                addr[0] = reverseGeoCodeResult.getAddress();
                //获取地址的详细内容对象，此类表示地址解析结果的层次化地址信息。
                ReverseGeoCodeResult.AddressComponent addressDetail = reverseGeoCodeResult.getAddressDetail();
                Log.e(TAG, "反地理编码信息 ---> \nAddress : " + addr[0]
                        + "\nBusinessCircle : " + reverseGeoCodeResult.getBusinessCircle()//位置所属商圈名称
                        + "\ncity : " + addressDetail.city  //所在城市名称
                        + "\ndistrict : " + addressDetail.district  //区县名称
                        + "\nprovince : " + addressDetail.province  //省份名称
                        + "\nstreet : " + addressDetail.street      //街道名
                        + "\nstreetNumber : " + addressDetail.streetNumber);//街道（门牌）号码

                StringBuilder poiInfoBuilder = new StringBuilder();
                //poiInfo信息
                List<PoiInfo> poiInfoList = reverseGeoCodeResult.getPoiList();
                if(poiInfoList != null) {
                    poiInfoBuilder.append("\nPoilist size : " + poiInfoList.size());
                    for (PoiInfo p : poiInfoList) {
                        poiInfoBuilder.append("\n\taddress: " + p.address);//地址信息
                        poiInfoBuilder.append(" name: " + p.name + " postCode: " + p.postCode);//名称、邮编
                        //还有其他的一些信息，我这里就不打印了，请参考API
                    }
                }
                Log.e(TAG,"poiInfo --> " + poiInfoBuilder.toString());

                //动态创建一个View用于显示位置信息
                Button button = new Button(getApplicationContext());
                button.setTextColor(Color.BLACK);
                button.setPadding(10,0 ,10,0);
                //设置view是背景图片
                button.setBackgroundResource(R.drawable.location_tips);
                //设置view的内容（位置信息）
                button.setText(poiInfoList.get(0).name + "\n" + addr[0]);
                //在地图中显示一个信息窗口，可以设置一个View作为该窗口的内容，也可以设置一个 BitmapDescriptor 作为该窗口的内容
                InfoWindow infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), latLng, -47, new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        //当InfoWindow被点击后隐藏
                        baiduMap.hideInfoWindow();
                    }
                });
                //显示信息窗口
                baiduMap.showInfoWindow(infoWindow);
            }
        });
        //发起反地理编码请求
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    private void getViews(){
        recyclerView = findViewById(R.id.transitRecyclerView);
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
        option = new DrivingRoutePlanOption();
        //默认时间优先策略
//        option.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_TIME_FIRST);
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
                if(drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR){
                    if (drivingRouteResult.getRouteLines().size() > 0) {
                        //横向列表
                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        manager.setOrientation(RecyclerView.HORIZONTAL);
                        recyclerView.setLayoutManager(manager);
                        //设置适配器
                        RouteLineAdapter adapter = new RouteLineAdapter(MapRouteActivity.this, drivingRouteResult.getRouteLines());
                        adapter.setOnItemClickListener(new RouteLineAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                // 获取选中的路线
                                baiduMap.clear();
                                DrivingRouteOverlay overlay1 = new DrivingRouteOverlay(baiduMap);
                                overlay1.setData(drivingRouteResult.getRouteLines().get(position));
                                overlay1.addToMap();
                                overlay1.zoomToSpan();
                            }
                        });
                        recyclerView.setAdapter(adapter);
                        baiduMap.clear();
                        DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
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
        routePlanSearch.drivingSearch(option.from(stNode).to(enNode));
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
        clearOverlay();
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