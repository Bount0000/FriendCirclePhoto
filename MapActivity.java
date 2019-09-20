package com.sinosoft.huataiejia_sit.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.sinosoft.huataiejia_sit.R;
import com.sinosoft.huataiejia_sit.utils.LogUtils;

public class MapActivity extends AppCompatActivity implements LocationSource,AMapLocationListener {
    private MapView mMapView;//显示地图的视图
    private AMap aMap;//定义AMap 地图对象的操作方法与接口。

    private OnLocationChangedListener mListener;//位置发生变化时的监听

    private AMapLocationClient mapLocationClient;//定位服务类。此类提供单次定位、持续定位、地理围栏、最后位置相关功能。
    private AMapLocationClientOption mapLocationClientOption;//定位参数设置，通过这个类可以对定位的相关参数进行设置
    //在AMapLocationClient进行定位时需要这些参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);//必须调用
        init();
    }
    //实例化Amaph对象
    public void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setConfigrationAmap();
        }
    }
    //配置Amap对象
    public void setConfigrationAmap() {
        aMap.setLocationSource(MapActivity.this);//设置定位监听
        aMap.setMyLocationEnabled(true);//设置显示定位层，并可以出发定位
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置显示定位按  钮
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);//设置定位类型
    }
    // 必须重写
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }
    // 必须重写
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }
    // 必须重写m
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }
    // 必须重写
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMapView.onDestroy();
        if(mapLocationClient!=null){
            mapLocationClient.onDestroy();
        }
    }
    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
       LogUtils.i("Tag","已经激活定位-------------activate");
        mListener=onLocationChangedListener;
        if(mapLocationClient==null){
            mapLocationClient=new AMapLocationClient(MapActivity.this);
            mapLocationClientOption=new AMapLocationClientOption();
            mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//设置定位模式为 高精度
            mapLocationClient.setLocationOption(mapLocationClientOption);//设置配置
            mapLocationClient.setLocationListener(this);//设置位置变化监听
            mapLocationClient.startLocation();
        }
    }

    //关闭定位
    @Override
    public void deactivate() {

        mListener=null;
        if(mapLocationClient!=null){
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient =null;
       LogUtils.i("Tag","已经关闭定位-------------deactivate");
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(mListener!=null&&aMapLocation!=null){
            if(aMapLocation!=null&&aMapLocation.getErrorCode()==0){
                Toast.makeText(MapActivity.this,aMapLocation.getAddress(),Toast.LENGTH_SHORT).show();
                mListener.onLocationChanged(aMapLocation);
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                LogUtils.i("Tag","========="+aMapLocation.getAddress());
                LogUtils.i("Tag","========="+ aMapLocation.getCountry());
               LogUtils.i("Tag","==========地图定位=============="+aMapLocation.getLatitude() + "----" + aMapLocation.getLongitude() + "---------" + aMapLocation.getErrorCode());
            }
        }else{
            Toast.makeText(MapActivity.this,"定位失败:"+aMapLocation.getErrorCode(),Toast.LENGTH_SHORT).show();
        }
    }


}
