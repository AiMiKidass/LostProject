package com.example.alex.newtestproject.gaode;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.gaode.tool.LocationTool;
import com.example.alex.newtestproject.utils.MapToastUtil;

/**
 * Created by Administrator on 2017/7/4.
 * <p>
 * 测试编写
 * <p>
 * -- 实现mapview
 * -- 实现小蓝点功能
 * -- 实现自定义小蓝点
 * -- 实现定位Location
 * -- 实现添加marker
 * -- 实现自定义marker(带布局,点击事件等等)
 */
public class MineMapActivity extends Activity implements AMapLocationListener {

    private static final int FILL_COLOR = Color.argb(100, 0, 0, 180);
    private static final int STROKE_COLOR = Color.BLACK;


    private MapView mMapview;
    private AMap mAmap;
    private LocationSource.OnLocationChangedListener mLocationChangedListener;
    private LocationTool mLocationTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.test_map);


        mMapview = (MapView) findViewById(R.id.map);
        mMapview.onCreate(savedInstanceState);

        findViewById(R.id.geoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationTool.startLocation();
            }
        });

        init(mMapview);
        initData();

    }

    private void init(MapView mapview) {
        // initGlobalParams
        if (mLocationTool == null) {
            mLocationTool = new LocationTool(MineMapActivity.this, MineMapActivity.this);
        }
        if (mAmap == null) {
            mAmap = mapview.getMap();
            initAmap();

            initMarker();
        }
    }

    private void initMarker() {
        /*
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
         */
        mAmap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                MapToastUtil.show(MineMapActivity.this, "infoWindow-marker被点击了");
            }
        });// 设置点击infoWindow事件监听器

        // 往地图上添加marker
        LatLng latlng = new LatLng(39.761, 116.434);

        // 想切换marker的图片的话,用
        // BitmapDescriptorFactory.fromResource(R.drawable.arrow_refact)
        Marker marker = getMarker(latlng);
        // 设置默认显示一个infowinfow
        marker.showInfoWindow();
    }

    private Marker getMarker(LatLng latlng) {
        return mAmap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("催收人员")
                .snippet("第1号")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));
    }

    private void initAmap() {
        // 设置点击事件
        mAmap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //latView.setText(String.valueOf(arg0.latitude));
                //lngView.setText(String.valueOf(arg0.longitude));
                MapToastUtil.show(MineMapActivity.this, "map被点击了");
            }
        });

        // 定位监听
        mAmap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener l) {
                mLocationChangedListener = l;
                mLocationTool.startLocation();
            }

            @Override
            public void deactivate() {
                mLocationChangedListener = null;
                mLocationTool.stopLocation();
            }
        });

        // 显示定位按钮
        mAmap.getUiSettings().setMyLocationButtonEnabled(true);
        mAmap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();
    }

    /**
     * 设置样式
     */
    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.location_marker));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAmap.setMyLocationStyle(myLocationStyle);
    }

    private void initData() {
    }

    @Override
    /**
     * 定位监听 在这里可获得具体的地理位置,经纬度等等
     * 可参考LocationTool中的默认实现
     */
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mLocationChangedListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //mLocationErrText.setVisibility(View.GONE);
                mLocationChangedListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                mAmap.moveCamera(CameraUpdateFactory.zoomTo(18));
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                //mLocationErrText.setVisibility(View.VISIBLE);
                //mLocationErrText.setText(errText);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapview.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapview.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapview.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationTool.destroyLocation();
        mMapview.onDestroy();
    }


}
