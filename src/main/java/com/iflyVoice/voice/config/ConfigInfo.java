package com.iflyVoice.voice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ConfigInfo {

    @Value("${spring.datasource.url:未设置}")
    private String dbUrl;

    @Value("${server.port:未设置}")
    private String port;

    @Value("${dubbo.registry.group:未设置}")
    private String zkGroup;

    @Value("${server.ssl.enabled:未设置}")
    private String enableSSL;

    @Value("${dd.shiro.redis.expire:未设置}")
    private String cacheExpire;

    @Value("${dandan.quartz.enable:false}")
    private String enableJob;

    @Value("${spring.application.name:未设置}")
    private String appName;

    @Value("${dubbo.protocol.host:未设置}")
    private String serviceHost;

    @Value("${socket.port:未设置，默认使用10101}")
    private String socketPort;

    public String toString(){
        StringBuilder sb = new StringBuilder();
            sb.append("")
                    .append("\n*****************************************************")
                    .append("\n***          服务 : ").append(appName)
                    .append("\n***          端口 : ").append(port)
                    .append("\n***   启用定时服务 : ").append(enableJob)
                    .append("\n***   缓存过期时间 : ").append(cacheExpire).append(" 毫秒")
                    .append("\n***       启用SSL : ").append(enableSSL)
                    .append("\n***          数据 : ").append(dbUrl)
                    .append("\n******************************************************")
                    .append("\n")
                    .append("\n***   服务启动完毕  --> ");//.append(DateUtil.getNowString()).append("\n\n");
        return sb.toString();
    }
}
