package com.zyt.util;

import com.zyt.App;
import com.zyt.base.City;
import com.zyt.base.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chenweiqi on 2018/5/30.
 */

public class LocationUtil {
    private static LocationUtil locationUtil = new LocationUtil();

    List<Province> provinces = new ArrayList<>();

    public static LocationUtil getInstance(){
        return locationUtil;
    }

    public void init(){
        try {
            InputStream is = App.getInstance().getAssets().open("allcitys.js");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer, "utf-8");

            JSONObject jsonObject = new JSONObject(text);

            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()){
                JSONObject province = jsonObject.getJSONObject(keys.next());

                String provinceName = province.getString("name");

                Province provinceBean = new Province();
                List<City> cities = new ArrayList<>();

                if (provinceName.endsWith("市")){                //直辖市
                    cities.add(new City(""));
                }else {
                    JSONObject city = province.getJSONObject("child");
                    Iterator<String> cityKeys = city.keys();
                    while (cityKeys.hasNext()){
                        String  cityName = city.getJSONObject(cityKeys.next()).getString("name");

                        cities.add(new City(cityName));
                    }
                }

                provinceBean.setCities(cities);

                provinceBean.setName(provinceName);

                provinces.add(provinceBean);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public List<Province> getProvinces() {
        return provinces;
    }
}
