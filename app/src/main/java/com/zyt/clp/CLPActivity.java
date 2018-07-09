package com.zyt.clp;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zyt.HttpUtil.Bean.SFWBean.SFWHitBean;
import com.zyt.HttpUtil.Bean.SFWBean.SFWHouseBean;
import com.zyt.HttpUtil.Bean.SFWBean.SFWResultBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.R;
import com.zyt.base.City;
import com.zyt.base.Province;
import com.zyt.util.LocationUtil;
import com.zyt.util.UserInfo;
import com.zyt.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class CLPActivity extends AppCompatActivity {


    @InjectView(R.id.searchET)
    EditText searchET;
    @InjectView(R.id.keySearchResult)
    RecyclerView keySearchResult;
    @InjectView(R.id.searchResultView)
    RecyclerView searchResultView;
    @InjectView(R.id.bmapView)
    MapView bmapView;
    @InjectView(R.id.text_currentLocation)
    TextView textCurrentLocation;
    @InjectView(R.id.btn_sel_location)
    LinearLayout btnSelLocation;

    private ArrayList<SFWHouseBean> keySearchBean = new ArrayList<SFWHouseBean>();
    private KeySearchAdapter keySearchAdapter;

    private ArrayList<SFWHouseBean> searchHouseBean = new ArrayList<SFWHouseBean>();
    private PersonAdapter personAdapter;


    private List<Province> options1Items = new ArrayList<>();
    private List<List<City>> options2Items = new ArrayList<>();

    String province;
    String city;
    private String cityPy = "";

    GeoCoder mSearch;
    public LocationClient mLocationClient = null;
    public BDAbstractLocationListener myListener = new MyListener();
    OptionsPickerView pvOptions;

    MapStatus currentMapStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_clp);
        ButterKnife.inject(this);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);

        options1Items = LocationUtil.getInstance().getProvinces();

        for (int i=0;i<options1Items.size();i++){
            options2Items.add(options1Items.get(i).getCities());
        }

        pvOptions = new OptionsPickerBuilder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            province = options1Items.get(options1).getName();

            city =  options2Items.get(options1).get(option2).getPickerViewText();
            if (TextUtils.isEmpty(city)){
                city =  options1Items.get(options1).getPickerViewText();
            }
            searchET.setText("");
            textCurrentLocation.setText(city);
            mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        //没有检索到结果
                        return;
                    }
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(result.getLocation());
                    bmapView.getMap().setMapStatus(update);
                    cityPy= Util.getFirstSpell(city);
                    onMoveSearch(bmapView.getMap().getMapStatus());

                    //获取地理编码结果
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        //没有找到检索结果
                    }

                    //获取反向地理编码结果
                }
            });
            mSearch.geocode(new GeoCodeOption()
                    .city(city).address("政府"));


        }).build();
        pvOptions.setPicker(options1Items, options2Items);

        initSearchResultView();

        initMyLocation();

        initMapGest();

        initSearch();

        mSearch = GeoCoder.newInstance();

        city = UserInfo.getUserLocation();
        if (city !=null){
            textCurrentLocation.setText(city);
            cityPy = Util.getFirstSpell(city);

            mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        //没有检索到结果
                        return;
                    }
                    MyLocationData locData = new MyLocationData.Builder()
                            .direction(100).latitude(result.getLocation().latitude)
                            .longitude(result.getLocation().longitude).build();


                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(result.getLocation());
                    bmapView.getMap().setMapStatus(update);

                    onMoveSearch(bmapView.getMap().getMapStatus());
                    //获取地理编码结果
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        //没有找到检索结果
                    }

                    //获取反向地理编码结果
                }
            });
            mSearch.geocode(new GeoCodeOption()
                    .city(city).address("人民政府"));
        }else {
            mLocationClient.start();
        }
    }
    @OnClick(R.id.btn_sel_location)
    public void onSelLocationClick(){

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);

        pvOptions.show();
    }

    public void initSearch() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        keySearchResult.setLayoutManager(manager);
        keySearchResult.setAdapter(keySearchAdapter = new KeySearchAdapter());
        keySearchResult.addItemDecoration(new SpaceItemDecoration(1));

        keySearchResult.setVisibility(View.GONE);


    }

    @OnClick(R.id.searchBtn)
    public void searchBtnClick() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchET.getWindowToken(), 0);


        if (searchET.getText().length() != 0) {
            searchResultView.setVisibility(View.GONE);
            searchET.clearFocus();
            if (cityPy.length() == 0) {
                Toast.makeText(getApplicationContext(), "获取城市中...", Toast.LENGTH_SHORT).show();
            } else {

                String  url = getUrl(province,city)+"?mapmode=&district=&subwayline=&subwaystation=&price=&room=&area=&towards=&floor=&hage=&equipment=&keyword=" + searchET.getText().toString() + "&comarea=&orderby=30&isyouhui=&newCode=&houseNum=&schoolDist=&schoolid=&ecshop=&PageNo=1&zoom=18&a=ajaxSearch&city=" + cityPy + "&searchtype=";
                com.orhanobut.logger.Logger.d(url);
                OkHttpUtils.get().url(url)
                        .build()
                        .execute(new BeanCallBack<SFWResultBean<SFWHitBean<SFWHouseBean>>>() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(SFWResultBean<SFWHitBean<SFWHouseBean>> response, int id) {
                                Log.e("bee", response.toString());
                                if (response.getLoupan() != null) {
                                    if (response.getLoupan().getHit().size() != 0) {
                                        keySearchBean = new ArrayList<SFWHouseBean>();
                                        keySearchBean.addAll(response.getLoupan().getHit());
                                        keySearchAdapter.notifyDataSetChanged();
                                        keySearchResult.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        }
    }


    public void initSearchResultView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        searchResultView.setLayoutManager(manager);
        searchResultView.setAdapter(personAdapter = new PersonAdapter());

        searchResultView.setOnTouchListener((view, motionEvent) -> true);

        searchResultView.setVisibility(View.GONE);
    }

    public void initMapGest() {
        bmapView.getMap().setMapType(BaiduMap.MAP_TYPE_NORMAL);

        bmapView.getMap().setOnMarkerClickListener(marker -> {
            final int index = marker.getExtraInfo().getInt("index");
            Button button = new Button(getApplicationContext());
            button.setText("进入全景");
            InfoWindow mInfoWindow = new InfoWindow(button, marker.getPosition(), -100);

            button.setOnClickListener(view -> {
                Intent i = new Intent(CLPActivity.this, QJActivity.class);
                i.putExtra("lanti", searchHouseBean.get(index).getY());
                i.putExtra("longi", searchHouseBean.get(index).getX());
                CLPActivity.this.startActivity(i);
            });

            bmapView.getMap().showInfoWindow(mInfoWindow);
            searchResultView.scrollToPosition(index);
            return false;
        });

        BaiduMap.OnMapStatusChangeListener statusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                searchResultView.setVisibility(View.GONE);
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                searchResultView.setVisibility(View.GONE);

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                Log.e("bee", "map status change");
                currentMapStatus = mapStatus;

                if (cityPy.length() == 0) {
                    Toast.makeText(getApplicationContext(), "获取城市中...", Toast.LENGTH_SHORT).show();
                } else {
                    onMoveSearch(mapStatus);
                }
            }
        };

        bmapView.getMap().setOnMapStatusChangeListener(statusChangeListener);
    }

    public void onMoveSearch(MapStatus mapStatus){
        String url = getUrl(province,city)+"?mapmode=&district=&subwayline=&subwaystation=&price=&room=&area=&towards=&floor=&hage=&equipment=&keyword=&comarea=&orderby=&isyouhui=&x1=" + mapStatus.bound.southwest.longitude + "&y1=" + mapStatus.bound.southwest.latitude + "&x2=" + mapStatus.bound.northeast.longitude + "&y2=" + mapStatus.bound.northeast.latitude + "&newCode=&houseNum=&schoolDist=&schoolid=&PageNo=1&zoom=18&a=ajaxSearch&searchtype=";
        com.orhanobut.logger.Logger.d(url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(new BeanCallBack<SFWResultBean<SFWHitBean<SFWHouseBean>>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(SFWResultBean<SFWHitBean<SFWHouseBean>> response, int id) {
                        Log.e("bee", response.toString());
                        bmapView.getMap().clear();


                        searchHouseBean = new ArrayList<SFWHouseBean>();

                        if (response.getLoupan() == null) {
                            Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<SFWHouseBean> houseBeanList = response.getLoupan().getHit();
                        for (SFWHouseBean bean : houseBeanList) {

                            if (bean.getPrice() == null) {
                                continue;
                            }

                            Bundle dataBundle = new Bundle();
                            dataBundle.putInt("index", searchHouseBean.size());
                            searchHouseBean.add(bean);

                            BitmapDescriptor bitmap = BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_0);
                            OverlayOptions options = new MarkerOptions()
                                    .position(new LatLng(bean.getY(), bean.getX()))
                                    .icon(bitmap)
                                    .extraInfo(dataBundle);
                            bmapView.getMap().addOverlay(options);
                        }

                        if (searchHouseBean.size() != 0) {
                            searchResultView.setVisibility(View.VISIBLE);
                            personAdapter.notifyDataSetChanged();
                            searchResultView.scrollToPosition(0);
                        } else {

                        }
                    }
                });
    }


    public void initMyLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
//        mLocationClient.start();
        Log.e("bee", "start get location");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bmapView.onDestroy();
        if (mSearch!=null)
            mSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapView.onPause();
    }

    @OnClick(R.id.backBtn)
    public void onClick() {
        finish();
    }

    class MyListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.e("bee", "get location success:" + bdLocation.getCityCode() + bdLocation.getCity());

            String cityName = bdLocation.getCity();
            if (cityName.contains("市")) {
                cityName = cityName.substring(0, cityName.length() - 1);
            }
            cityPy = Util.getFirstSpell(cityName);
            Log.e("bee", cityPy);
            bmapView.getMap().setMyLocationEnabled(true);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(100).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            bmapView.getMap().setMyLocationData(locData);

            LatLng cenpt = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(18)
                    .build();

            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            bmapView.getMap().setMapStatus(mMapStatusUpdate);

            mLocationClient.stop();
        }
    }

    class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(CLPActivity.this).inflate(R.layout.search_result_layout, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.setHouseData(searchHouseBean.get(position));
        }

        @Override
        public int getItemCount() {
            return searchHouseBean.size();
        }

        class MyViewHolder extends ViewHolder {

            TextView houseName;
            TextView housePrice;
            TextView houseAddress;

            public MyViewHolder(View view) {
                super(view);
                houseName = (TextView) view.findViewById(R.id.houseName);
                housePrice = (TextView) view.findViewById(R.id.price);
                houseAddress = (TextView) view.findViewById(R.id.houseAddress);
            }

            public void setHouseData(SFWHouseBean bean) {
                houseName.setText(bean.getProjname());
                housePrice.setText(bean.getPrice() + "元/平");
                houseAddress.setText(bean.getAddress());
            }
        }

    }

    class KeySearchAdapter extends RecyclerView.Adapter<KeySearchAdapter.KeySearchHolder> {

        @Override
        public KeySearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            KeySearchHolder holder = new KeySearchHolder(LayoutInflater.from(CLPActivity.this).inflate(R.layout.search_result_layout, parent, false));

            return holder;
        }

        @Override
        public void onBindViewHolder(KeySearchHolder holder, int position) {
            holder.setHouseData(keySearchBean.get(position));
            holder.setIndex(position);
        }

        @Override
        public int getItemCount() {
            return keySearchBean.size();
        }

        class KeySearchHolder extends ViewHolder implements View.OnClickListener {

            TextView houseName;
            TextView housePrice;
            TextView houseAddress;

            int index;

            public KeySearchHolder(View view) {
                super(view);
                view.setOnClickListener(this);

                houseName = (TextView) view.findViewById(R.id.houseName);
                housePrice = (TextView) view.findViewById(R.id.price);
                houseAddress = (TextView) view.findViewById(R.id.houseAddress);
            }

            public void setHouseData(SFWHouseBean bean) {
                houseName.setText(bean.getProjname());
                housePrice.setText(bean.getPrice() + "元/平");
                houseAddress.setText(bean.getAddress());
            }

            public void setIndex(int i) {
                index = i;
            }

            @Override
            public void onClick(View view) {
                keySearchResult.setVisibility(View.GONE);
                SFWHouseBean bean = keySearchBean.get(index);

                bmapView.getMap().clear();

                LatLng cenpt = new LatLng(bean.getY(), bean.getX());
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(cenpt)
                        .zoom(18)
                        .build();

                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                bmapView.getMap().setMapStatus(mMapStatusUpdate);

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("index", 0);

                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.map_icon);
                OverlayOptions options = new MarkerOptions()
                        .position(new LatLng(bean.getY(), bean.getX()))
                        .icon(bitmap)
                        .extraInfo(dataBundle);

                bmapView.getMap().addOverlay(options);

                searchHouseBean = new ArrayList<SFWHouseBean>();
                searchHouseBean.add(bean);
                personAdapter.notifyDataSetChanged();

                searchResultView.setVisibility(View.VISIBLE);


            }
        }

    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

    private String getUrl(String province,String city){
        String url;

        String cityPy = Util.getFirstSpell(city);

        if (cityPy.equals("北京")){
            url  = "http://esf.fang.com/map/";
        }else if (city.equals("呼和浩特")){
            url = "http://esf.nm.fang.com/map/";
        }else if (city.equals("重庆")){
            url  = "http://esf.cq.fang.com/map/";
        }else if (city.equals("太原")){
            url  = "http://esf.taiyuan.fang.com/map/";
        }else if (city.equals("长春")){
            url  = "http://esf.changchun.fang.com/map/";
        }else if (city.equals("哈尔滨")){
            url  = "http://esf.hrb.fang.com/map/";
        }else if (city.equals("长沙")){
            url  = "http://esf.cs.fang.com/map/";
        }else if (city.equals("海口")){
            url  = "http://esf.hn.fang.com/map/";
        }else if (city.equals("三亚")){
            url  = "http://esf.sanya.fang.com/map/";
        }else if (city.equals("拉萨")){
            url  = "http://esf.lasa.fang.com/map/";
        }else if (city.equals("西安")){
            url  = "http://esf.xian.fang.com/map/";
        }else if (city.equals("银川")){
            url  = "http://esf.yinchuan.fang.com/map/";
        }else if (city.equals("乌鲁木齐")){
            url  = "http://esf.xj.fang.com/map/";
        }else if (city.equals("厦门")){
            url =  "http://esf.xm.fang.com/map/";
        }
        else {
            url  = "http://esf." + cityPy + ".fang.com/map/";

        }
        return url;
    }
}
