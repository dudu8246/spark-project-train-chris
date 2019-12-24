package com.imooc.bigdata.utils;

import com.imooc.bigdata.domain.UserAgentInfo;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import org.apache.commons.lang3.StringUtils;


import java.io.IOException;

/**
 * @program: spark-project-train
 * @description: UserAgent工具类
 * @author: chris
 * @create: 2019-12-24 17:19
 **/

public class UAUtils {

    public static UASparser parser = null;
    static {

        try {
            parser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /**
    * @Description: 将官方解析出来的UserAgentInfo 转换成自己需要的UserAgentInfo，获取需要的字段
    * @Param: [ua]
    * @return: com.imooc.bigdata.domain.UserAgentInfo
    * @Author: chris
    * @Date: 2019/12/24
    */
    public static UserAgentInfo getUserAgentInfo(String ua){
        UserAgentInfo info = null;


        try {
            if(StringUtils.isNotEmpty(ua)) {
                cz.mallat.uasparser.UserAgentInfo tmp = parser.parse(ua);
                if(tmp != null){
                    info = new UserAgentInfo();
                    info.setBrowserName(tmp.getUaFamily());
                    info.setBrowserVersion(tmp.getBrowserVersionInfo());
                    info.setOsName(tmp.getOsName());
                    info.setOsFamily(tmp.getOsFamily());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return info;
    }

    public static void main(String[] args) throws Exception{

        UserAgentInfo info = UAUtils.getUserAgentInfo("Mozilla/4.0 (compatible; MSIE 7.0; windows NT 5.1; )");

        System.out.println(info.toString());
    }


}
