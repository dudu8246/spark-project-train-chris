package com.imooc.bigdata.domain;


/**
* @Description: 将UserAgent返回的结果封装
* @Param:
* @return: 
* @Author: chris
* @Date: 2019/12/24
*/
public class UserAgentInfo {

    // 浏览器名称
    private String browserName;

    // 浏览器版本号
    private String browserVersion;

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    // 操作系统名称
    private String osName ;

    // 操作系统版本号
    private String osFamily;

    @Override
    public String toString() {
        return "UserAgentInfo{" +
                "browserName='" + browserName + '\'' +
                ", browserVersion='" + browserVersion + '\'' +
                ", osName='" + osName + '\'' +
                ", osFamily='" + osFamily + '\'' +
                '}';
    }
}
