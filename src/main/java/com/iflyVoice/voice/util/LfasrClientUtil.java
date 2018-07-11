package com.iflyVoice.voice.util;

import com.iflytek.msp.cpdb.lfasr.client.LfasrClientImp;
import com.iflytek.msp.cpdb.lfasr.exception.LfasrException;

/**
 *
 * @author weilin5
 * @date 2018/7/3
 */
public class LfasrClientUtil  extends LfasrClientImp {
    public LfasrClientUtil() throws LfasrException {
    }

    public LfasrClientUtil(String app_id, String secret_key) throws LfasrException {
        super(app_id, secret_key);
    }

    public LfasrClientUtil(String err_msg) throws LfasrException {
        err_msg = err_msg;
    }
}
