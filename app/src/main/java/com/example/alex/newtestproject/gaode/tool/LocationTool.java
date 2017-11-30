package com.example.alex.newtestproject.gaode.tool;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by Alk on 2017/7/3.
 * LocationUtil 工具类
 */
public class LocationTool {

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;

    public LocationTool(Context context, AMapLocationListener locationListener) {
        if (locationListener != null) {
            this.locationListener = locationListener;
        }
        //初始化client
        locationClient = new AMapLocationClient(context);
        initLocation();
    }

    /**
     * 初始化定位
     *
     * @since 2.8.0
     */
    public void initLocation() {
        // 设置定位模式
        initLocationOption(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 初始化定位模式
     *
     * @param checkedId checkedId
     */
    public void initLocationOption(AMapLocationClientOption.AMapLocationMode checkedId) {
        if (null == locationOption) {
            locationOption = new AMapLocationClientOption();
        }

        // 分别为低功耗,设备模式,高精度模式
        if (checkedId == AMapLocationClientOption.AMapLocationMode.Battery_Saving) {
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        } else if (checkedId == AMapLocationClientOption.AMapLocationMode.Device_Sensors) {
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        } else if (checkedId == AMapLocationClientOption.AMapLocationMode.Hight_Accuracy) {
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        }

        // 初始化
        resetOption(new LocationOptionObject());
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                StringBuilder sb = new StringBuilder();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");

                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    //定位完成的时间
                    sb.append("定位时间: " + LocationUtils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                //定位之后的回调时间
                sb.append("回调时间: " + LocationUtils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                String result = sb.toString();
                //tvResult.setText(result);
            } else {
                //tvResult.setText("定位失败，loc is null");
            }
        }
    };

    /**
     * 默认的定位参数
     *
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 根据控件的选择，重新设置定位参数
     */
    public void resetOption(LocationOptionObject optionObject) {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(optionObject.isAddress);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(optionObject.isGpsFirst);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(optionObject.isCacheAble);
        // 设置是否单次定位
        locationOption.setOnceLocation(optionObject.isOnceLocation);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(optionObject.isOnceLastest);
        //设置是否使用传感器
        locationOption.setSensorEnable(optionObject.isSensorAble);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        try {
            // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
            locationOption.setInterval(optionObject.etInterval);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            // 设置网络请求超时时间
            locationOption.setHttpTimeOut(optionObject.etHttpTimeout);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // 设置定位参数
        locationClient.setLocationOption(locationOption);
    }

    /**
     * 开始定位
     * 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
     * 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
     * 在定位结束后，在合适的生命周期调用onDestroy()方法
     * 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
     *
     * @since 2.8.0
     */
    public void startLocation() {
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        // 停止定位
        if (locationClient != null) {
            locationClient.stopLocation();
        }
    }

    /**
     * 销毁定位
     *
     * @since 2.8.0
     */
    public void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            stopLocation();
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 定位配置参数
     */
    public static class LocationOptionObject {
        /**
         * 设置是否需要显示地址信息
         */
        public boolean isAddress = true;
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        public boolean isGpsFirst;
        /**
         * 设置是否开启缓存
         */
        public boolean isCacheAble = true;
        /**
         * 设置是否单次定位
         */
        public boolean isOnceLocation = true;
        /**
         * 设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
         */
        public boolean isOnceLastest;

        /**
         * 设置是否使用传感器
         */
        public boolean isSensorAble;

        /**
         * 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
         */
        public long etInterval = 2000;

        /**
         * 设置网络请求超时时间
         */
        public long etHttpTimeout = 3000;
    }
}
