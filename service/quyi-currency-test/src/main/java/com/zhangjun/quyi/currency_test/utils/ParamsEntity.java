package com.zhangjun.quyi.currency_test.utils;

public class ParamsEntity {

    private String useName;
    private String from;
    private String keyName;
    private Object keyValue;

    public ParamsEntity(String useName, String from, String keyName, Object keyValue) {
        this.useName = useName;
        this.from = from;
        this.keyName = keyName;
        this.keyValue = keyValue;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Object getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(Object keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public String toString() {
        return "ParamsMap{" +
                "useName='" + useName + '\'' +
                ", from='" + from + '\'' +
                ", keyName='" + keyName + '\'' +
                ", keyValue=" + keyValue +
                '}';
    }


}
