package com.terrydr.share.wxapi;

import android.widget.Toast;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends WXCallbackActivity {

    @Override  
    public void onReq(BaseReq arg0) { }  
  
    @Override  
    public void onResp(BaseResp resp) {  
//        Toast.makeText(this, "resp.errCode:" + resp.errCode + ",resp.errStr:"  
//                + resp.errStr, Toast.LENGTH_LONG).show();  
        switch (resp.errCode) {  
        case BaseResp.ErrCode.ERR_OK:  
            //分享成功  
//        	Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();  
        	finish();
            break;  
        case BaseResp.ErrCode.ERR_USER_CANCEL:  
//        	Toast.makeText(this, "分享取消 ", Toast.LENGTH_LONG).show();  
        	finish();
            //分享取消  
            break;  
        case BaseResp.ErrCode.ERR_AUTH_DENIED:  
//        	Toast.makeText(this, "分享拒绝", Toast.LENGTH_LONG).show();  
        	finish();
            //分享拒绝  
            break;  
        default:
            finish();
        }  
    }  
    
    
}
