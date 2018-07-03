package com.iflyVoice.voice.service.impl;

import com.alibaba.fastjson.JSON;
import com.iflyVoice.voice.service.IflyService;
import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
import com.iflytek.msp.cpdb.lfasr.model.Message;

/**
 * Created by weilin5 on 2018/7/3.
 */
public class IflyServiceImpl implements IflyService {


    @Override
    public String doTest() {
        // 初始化LFASR实例
        LfasrClientImp lc = null;
        try {
            lc = LfasrClientImp.initLfasrClient();
        } catch (LfasrException e) {
            // 初始化异常，解析异常描述信息
            Message initMsg = JSON.parseObject(e.getMessage(), Message.class);
            System.out.println("ecode=" + initMsg.getErr_no());
            System.out.println("failed=" + initMsg.getFailed());

            // 用户可根据错误码及错误描述进行相关处理-多为配置参数问题
        }
        return null;
    }



}
