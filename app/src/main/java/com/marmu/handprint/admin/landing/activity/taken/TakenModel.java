package com.marmu.handprint.admin.landing.activity.taken;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by azharuddin on 30/6/17.
 */

class TakenModel implements Serializable {
    private String key;
    private HashMap<String, Object> takenMap;

    TakenModel(String key, HashMap<String, Object> takenMap) {
        this.key = key;
        this.takenMap = takenMap;
    }

    public String getKey() {
        return key;
    }

    HashMap<String, Object> getTakenMap() {
        return takenMap;
    }
}
