package com.feizhang.share;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zf08526 on 2016/4/13.
 */
public class ShareResult implements Serializable{
    public int shareToId;
    public ShareResultEnum shareResult;
    public Map<String, String> resultInfo = new HashMap<>();

    public ShareResult(int shareToId, ShareResultEnum shareResult){
        this.shareToId = shareToId;
        this.shareResult = shareResult;
    }

    public void addResultInfo(String key, String value){
        this.resultInfo.put(key, value);
    }

    public String getResultInfo(String key){
        return resultInfo.get(key);
    }
}
