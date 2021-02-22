package com.example.esp_health_monitor;

public class model {
    private String temperature,blood_pressure,heamoglobin,sugar,heart_rate,time,remark;
    private String temp_color,bp_color,hb_color,sugar_color,bpm_color,conclusion;

    public model(String temperature, String blood_pressure, String heamoglobin, String sugar, String heart_rate, String time, String remark, String temp_color, String bp_color, String hb_color, String sugar_color, String bpm_color, String conclusion) {
        this.temperature = temperature;
        this.blood_pressure = blood_pressure;
        this.heamoglobin = heamoglobin;
        this.sugar = sugar;
        this.heart_rate = heart_rate;
        this.time = time;
        this.remark = remark;
        this.temp_color = temp_color;
        this.bp_color = bp_color;
        this.hb_color = hb_color;
        this.sugar_color = sugar_color;
        this.bpm_color = bpm_color;
        this.conclusion = conclusion;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBlood_pressure() {
        return blood_pressure;
    }

    public void setBlood_pressure(String blood_pressure) {
        this.blood_pressure = blood_pressure;
    }

    public String getHeamoglobin() {
        return heamoglobin;
    }

    public void setHeamoglobin(String heamoglobin) {
        this.heamoglobin = heamoglobin;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(String heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTemp_color() {
        return temp_color;
    }

    public void setTemp_color(String temp_color) {
        this.temp_color = temp_color;
    }

    public String getBp_color() {
        return bp_color;
    }

    public void setBp_color(String bp_color) {
        this.bp_color = bp_color;
    }

    public String getHb_color() {
        return hb_color;
    }

    public void setHb_color(String hb_color) {
        this.hb_color = hb_color;
    }

    public String getSugar_color() {
        return sugar_color;
    }

    public void setSugar_color(String sugar_color) {
        this.sugar_color = sugar_color;
    }

    public String getBpm_color() {
        return bpm_color;
    }

    public void setBpm_color(String bpm_color) {
        this.bpm_color = bpm_color;
    }
}
