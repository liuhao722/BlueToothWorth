package com.worth.framework.base.network.bean;

import java.util.Arrays;

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    5/30/21 --> 8:21 PM
 * Description: This is ResultData
 */
public class ResultData {
    public ResultData(){}
    private String sessionId;
    private String logId;
    private String result;
    private String actionType;
    private Object slotOriginalWords;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Object getSlotOriginalWords() {
        return slotOriginalWords;
    }

    public void setSlotOriginalWords(Object slotOriginalWords) {
        this.slotOriginalWords = slotOriginalWords;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "sessionId='" + sessionId + '\'' +
                ", logId='" + logId + '\'' +
                ", result='" + result + '\'' +
                ", actionType='" + actionType + '\'' +
                ", slotOriginalWords=" + slotOriginalWords +
                '}';
    }
}
