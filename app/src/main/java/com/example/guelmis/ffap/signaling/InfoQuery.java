package com.example.guelmis.ffap.signaling;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mario on 09/25/15.
 */
public class InfoQuery {
    protected ArrayList<String> brands;
    protected HashMap<String, ArrayList<String>> brand_model_map;
    protected ArrayList<String> years;

    protected InfoQuery(ArrayList<String> _brands, HashMap<String, ArrayList<String>> _brand_model_map, ArrayList<String> _years) {
        brands = _brands;
        brand_model_map = _brand_model_map;
        years = _years;
    }

    public HashMap<String, ArrayList<String>> getModels() {
        return brand_model_map;
    }

    public ArrayList<String> getBrands() {
        return brands;
    }

    public ArrayList<String> getYears() {
        return years;
    }

}
