package com.iflyVoice.voice.service.impl;

import com.alibaba.fastjson.JSON;
import com.iflyVoice.voice.service.IflyService;
import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;
import com.iflytek.msp.cpdb.lfasr.model.LfasrType;
import com.iflytek.msp.cpdb.lfasr.model.Message;

import java.util.HashMap;

/**
 * Created by weilin5 on 2018/7/3.
 */
public class IflyServiceImpl implements IflyService {


    private static LfasrClientImp lc;


    private static LfasrClientImp doTest() {
        try {
        	lc = LfasrClientImp.initLfasrClient("5b3ad436","0acf33a1b36a641d4cee55c3b038df1f");
            System.out.print("1111111");
        } catch (LfasrException e) {
            // 初始化异常，解析异常描述信息
            Message initMsg = JSON.parseObject(e.getMessage(), Message.class);
            System.out.println("ecode=" + initMsg.getErr_no());
            System.out.println("failed=" + initMsg.getFailed());
            // 用户可根据错误码及错误描述进行相关处理-多为配置参数问题
        }
        return lc;
    }

    public static void main(String[] args) {
        doTest();
        loadVoiceByPath("");

    }




    public  static void loadVoiceByPath(String path) {
        String task_id = "";
        try {
            // 上传音频文件
            String local_file = "D:\\demo\\test.mp3";
            HashMap<String, String> params = new HashMap<>();
            params.put("has_participle", "true");
            params.put("max_alternatives", "3");
            Message uploadMsg = lc.lfasrUpload(local_file, LfasrType.LFASR_STANDARD_RECORDED_AUDIO, params);

            // 判断返回值
            int ok = uploadMsg.getOk();
            if (ok == 0) {
                // 创建任务成功
                task_id = uploadMsg.getData();
                System.out.println("task_id=" + task_id);
            } else {
                // 创建任务失败-服务端异常
                System.out.println("ecode=" + uploadMsg.getErr_no());
                System.out.println("failed=" + uploadMsg.getFailed());
            }
        } catch (LfasrException e) {
            // 上传异常，解析异常描述信息
            Message uploadMsg = JSON.parseObject(e.getMessage(), Message.class);
            System.out.println("ecode=" + uploadMsg.getErr_no());
            System.out.println("failed=" + uploadMsg.getFailed());
            // 用户可根据错误码及错误描述进行相关处理-多为客户端传递参数问题
        }
    }


}
