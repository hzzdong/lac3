package com.linkallcloud.core.lang.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkallcloud.core.json.Json;

public class ClassMeta {

    public String type;
    public Map<String, List<String>> paramNames = new HashMap<String, List<String>>();
    public Map<String, Integer> methodLines = new HashMap<String, Integer>();
    
    public String toString() {
        return Json.toJson(this);
    }
}
