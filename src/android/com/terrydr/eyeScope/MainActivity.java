package com.terrydr.eyeScope;

import com.terrydr.eyeScope.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements OnClickListener {
	
	private final static String TAG = "MainActivity";
	private String infos;

    private static final int SHARE_SMS_CODE = 1001;
    
    private static final int SHARE_MAIL_CODE = 1002;
	
	public static final String APP_ID = "wx81f40406b3c59044";
	public static final String APP_SECRET = "9b444a462359330cc77fafc36910cf12";
	
	public static final String QQ_APP_ID = "1105336069";
	public static final String QQ_APP_KEY = "xS2EmpHKqD2cVH8"; 
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    // 设置分享内容
    private String shareTitle = "这是分享标题！";
    
    private String shareText = "这是分享内容！";
    
    private String shareUrl = "http://www.baidu.com";
    private UMImage localImage = null;
    
    private Button shareWXFriend,shareWXMoments,shareQQ,shareQQZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		localImage = new UMImage(MainActivity.this, R.drawable.ic_launcher);
        configPlatfrom();
        setShareContext();
        
        shareWXFriend = (Button)findViewById(R.id.shareWXFriend);
        shareWXMoments = (Button)findViewById(R.id.shareWXMoments);
        shareQQ = (Button)findViewById(R.id.shareQQ);
        shareQQZone = (Button)findViewById(R.id.shareQQZone);
        shareWXFriend.setOnClickListener(this);
        shareWXMoments.setOnClickListener(this);
        shareQQ.setOnClickListener(this);
        shareQQZone.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shareWXFriend:
			shareWXFriend();
			break;
		case R.id.shareWXMoments:
			shareWXMoments();
			break;
		case R.id.shareQQ:
			shareQQ();
			break;
		case R.id.shareQQZone:
			shareQQZone();
			break;
		default:
			break;
		}

	}
    
    /**
	 * 分享微信好友
	 */
	private void shareWXFriend() {
		clickShare(true, SHARE_MEDIA.WEIXIN);
	}
    /**
	 * 分享微信朋友圈
	 */
	private void shareWXMoments() {
		clickShare(true, SHARE_MEDIA.WEIXIN_CIRCLE);
	}
    /**
	 * 分享QQ好友
	 */
	private void shareQQ() {
		clickShare(true, SHARE_MEDIA.QQ);
	}
    /**
	 * 分享QQ空间
	 */
	private void shareQQZone() {
		clickShare(true, SHARE_MEDIA.QZONE);
	}

	/**
	 * 点击分享
	 * 
	 * @param isDirectShare
	 *            是否有编辑框
	 * @param platform
	 *            分享平台
	 */
	private void clickShare(boolean isDirectShare, SHARE_MEDIA platform) {
		if (isDirectShare) {
			// 调用直接分享
			mController.directShare(MainActivity.this, platform,mShareListener);
		} else {
			// 调用直接分享, 但是在分享前用户可以编辑要分享的内容
			mController.postShare(MainActivity.this, platform,mShareListener);
		}
	}

	/**
	 * 分享监听器
	 */
	private SnsPostListener mShareListener = new SnsPostListener() {

		@Override
		public void onStart() {
		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int stCode,SocializeEntity entity) {
			if (stCode == 200) {
				Log.e(TAG,"分享成功");
				Toast.makeText(MainActivity.this, "分享成功",Toast.LENGTH_SHORT).show();
			} else {
				Log.e(TAG,"分享失败");
				Toast.makeText(MainActivity.this, "分享失败",Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	/**
     * 配置分享的平台
     */
    private void configPlatfrom(){
        // QQ
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MainActivity.this, QQ_APP_ID, null);
        qqSsoHandler.addToSocialSDK();
        
        // QQ空间
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(MainActivity.this, QQ_APP_ID, null);
        qZoneSsoHandler.addToSocialSDK();
        
        // 微信
        UMWXHandler wxHandler = new UMWXHandler(MainActivity.this, APP_ID, APP_SECRET);
        wxHandler.addToSocialSDK();

        // 朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(MainActivity.this, APP_ID, APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }
	
	/**
     * 设置分享的内容
     */
    private void setShareContext(){
        // 设置微信好友分享
        String wxUrl = shareUrl;
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(shareText + wxUrl);
        weixinContent.setTitle(shareTitle);
        weixinContent.setTargetUrl(wxUrl);
        weixinContent.setShareMedia(localImage);
       
        mController.setShareMedia(weixinContent);
        
        // 设置朋友圈分享
        String circleUrl = shareUrl;
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(shareText + circleUrl);
        circleMedia.setTitle(shareTitle);
        circleMedia.setShareMedia(localImage);
        circleMedia.setTargetUrl(circleUrl);
        mController.setShareMedia(circleMedia);
        
        // 设置QQ空间分享内容
        String qZoneUrl = shareUrl;
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(shareText + qZoneUrl);
        qzone.setTargetUrl(qZoneUrl);
        qzone.setTitle(shareTitle);
        qzone.setShareMedia(localImage);
        mController.setShareMedia(qzone);
        
        // 设置QQ分享
        String qqUrl = shareUrl;
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(shareText + qqUrl);
        qqShareContent.setTitle(shareTitle);
        qqShareContent.setShareMedia(localImage);
        qqShareContent.setTargetUrl(qqUrl);
        mController.setShareMedia(qqShareContent);
        
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		if (requestCode == SHARE_MAIL_CODE || requestCode == SHARE_SMS_CODE) {
			Toast.makeText(MainActivity.this, "分享成功1", Toast.LENGTH_SHORT).show();
			return;
		}

		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
