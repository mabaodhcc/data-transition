package com.dhcc.entity;

public class EcifLoginLog {
    private String 网银客户号;
    private String 登录时间;
    private String 服务Id;
    private String log_state;
    private String 日志信息;

    public String get网银客户号() {
        return 网银客户号;
    }

    public void set网银客户号(String 网银客户号) {
        this.网银客户号 = 网银客户号;
    }

    public String get登录时间() {
        return 登录时间;
    }

    public void set登录时间(String 登录时间) {
        this.登录时间 = 登录时间;
    }

    public String get服务Id() {
        return 服务Id;
    }

    public void set服务Id(String 服务Id) {
        this.服务Id = 服务Id;
    }

    public String getLog_state() {
        return log_state;
    }

    public void setLog_state(String log_state) {
        this.log_state = log_state;
    }

    public String get日志信息() {
        return 日志信息;
    }

    public void set日志信息(String 日志信息) {
        this.日志信息 = 日志信息;
    }

    public EcifLoginLog(String 网银客户号, String 登录时间, String 服务Id, String log_state, String 日志信息) {
        this.网银客户号 = 网银客户号;
        this.登录时间 = 登录时间;
        this.服务Id = 服务Id;
        this.log_state = log_state;
        this.日志信息 = 日志信息;
    }
}
