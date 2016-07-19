package com.terrydr.share;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

public class Share_plugin_intent extends CordovaPlugin {
	private final static String TAG = "Plugin_intent";
	private static final int SHARE_SMS_CODE = 1001;
	private static final int SHARE_MAIL_CODE = 1002;
	public String APP_ID;
	public String QQ_APP_ID;
	public static final String APP_SECRET = "9b444a462359330cc77fafc36910cf12";
	public static final String QQ_APP_KEY = "xS2EmpHKqD2cVH8";
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	//分享图片
	private UMImage localImage = null; 

	// private CallbackContext callbackContext;

	@Override
	public boolean execute(String action, org.json.JSONArray args,
			CallbackContext callbackContext) throws org.json.JSONException {
		// this.callbackContext = callbackContext;
		if (action.equals("registerAppId")) { // 注册
			String appid = args.getString(0);
			String qqappid = args.getString(1);
			configPlatfrom(appid, qqappid);
			return true;
		} else if (action.equals("terrydrWeixinShare")) { // 分享微信好友
			String title = args.getString(0);
			String descirption = args.getString(1);
			String imagePath = args.getString(2);
			String url = args.getString(3);
			boolean isWeiXin = isWeixinAvilible(cordova.getActivity());
			if (!isWeiXin) {
				callbackContext.success(0);
				return true;
			}
			this.shareWXFriend(title, descirption, imagePath, url);
			return true;
		} else if (action.equals("terrydrWeixinCircleShare")) { // 分享微信朋友圈
			String title = args.getString(0);
			String descirption = args.getString(1);
			String imagePath = args.getString(2);
			String url = args.getString(3);
			// LOG.e(TAG, "title:" + title);
			// LOG.e(TAG, "descirption:" + descirption);
			// LOG.e(TAG, "imagePath:" + imagePath);
			// LOG.e(TAG, "url:" + url);
			boolean isWeiXin = isWeixinAvilible(cordova.getActivity());
			if (!isWeiXin) {
				// LOG.e(TAG, "isWeiXin:" + false);
				callbackContext.success(0);
				return true;
			}
			this.shareWXCircle(title, descirption, imagePath, url);
			return true;
		} else if (action.equals("terrydrQQShare")) { // 分享QQ好友
			String title = args.getString(0);
			String descirption = args.getString(1);
			String imagePath = args.getString(2);
			String url = args.getString(3);
			boolean isQQ = isQQClientAvailable(cordova.getActivity());
			if (!isQQ) {
				callbackContext.success(0);
				return true;
			}
			this.shareQQ(title, descirption, imagePath, url);
			return true;
		}else if (action.equals("terrydrQZoneShare")) { // 分享QQ好友
			String title = args.getString(0);
			String descirption = args.getString(1);
			String imagePath = args.getString(2);
			String url = args.getString(3);
			boolean isQQ = isQQClientAvailable(cordova.getActivity());
			if (!isQQ) {
				callbackContext.success(0);
				return true;
			}
			this.shareQZone(title, descirption, imagePath, url);
			return true;
		}
		return false;
	}

	/**
	 * 配置分享的平台
	 * 
	 * @param appid
	 *            微信开发平台注册应用的AppID
	 * @param qq_app_id
	 *            QQ开发平台注册应用的AppID
	 */
	private void configPlatfrom(String appid, String qq_app_id) {
		// 微信
		UMWXHandler wxHandler = new UMWXHandler(cordova.getActivity(), appid,
				APP_SECRET);
		wxHandler.addToSocialSDK();

		// 朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(cordova.getActivity(),
				appid, APP_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// QQ
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(cordova.getActivity(),
				qq_app_id, QQ_APP_KEY);
		qqSsoHandler.addToSocialSDK();

		// QQ空间
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(cordova.getActivity(), 
				qq_app_id, QQ_APP_KEY);
		qZoneSsoHandler.addToSocialSDK();

	}

	/**
	 * 设置微信好友分享内容
	 * 
	 * @param title
	 *            设置title
	 * @param descirption
	 *            设置分享文字
	 * @param imagePath
	 *            设置分享图片
	 * @param url
	 *            设置分享内容跳转URL
	 */
	private void shareWXFriend(String title, String descirption,
			String imagePath, String url) {
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(descirption);
		weixinContent.setTitle(title);
		weixinContent.setTargetUrl(url);
		localImage = new UMImage(cordova.getActivity(),
				getBitMBitmap(imagePath));
		weixinContent.setShareMedia(localImage);
		mController.setShareMedia(weixinContent);
		clickShare(true, SHARE_MEDIA.WEIXIN);
	}

	/**
	 * 设置分享微信朋友圈内容
	 * 
	 * @param title
	 *            设置title
	 * @param descirption
	 *            设置分享文字
	 * @param imagePath
	 *            设置分享图片
	 * @param url
	 *            设置分享内容跳转URL
	 */
	private void shareWXCircle(String title, String descirption,
			String imagePath, String url) {
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(descirption);
		circleMedia.setTitle(title);
		localImage = new UMImage(cordova.getActivity(),
				getBitMBitmap(imagePath));
		circleMedia.setShareMedia(localImage);
		circleMedia.setTargetUrl(url);
		mController.setShareMedia(circleMedia);
		clickShare(true, SHARE_MEDIA.WEIXIN_CIRCLE);
	}

	/**
	 * 设置QQ好友分享内容
	 * 
	 * @param title
	 *            设置title
	 * @param descirption
	 *            设置分享文字
	 * @param imagePath
	 *            设置分享图片
	 * @param url
	 *            设置分享内容跳转URL
	 */
	private void shareQQ(String title, String descirption, String imagePath,
			String url) {
		// 设置QQ分享
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(descirption);
		qqShareContent.setTitle(title);
		localImage = new UMImage(cordova.getActivity(),
				getBitMBitmap(imagePath));
		qqShareContent.setShareMedia(localImage);
		qqShareContent.setTargetUrl(url);
		mController.setShareMedia(qqShareContent);
		clickShare(true, SHARE_MEDIA.QQ);
	}
	
	/**
	 * 设置QQ空间分享内容
	 * 
	 * @param title
	 *            设置title
	 * @param descirption
	 *            设置分享文字
	 * @param imagePath
	 *            设置分享图片
	 * @param url
	 *            设置分享内容跳转URL
	 */
	private void shareQZone(String title, String descirption, String imagePath,
			String url){
        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(descirption);
        qzone.setTargetUrl(url);
        qzone.setTitle(title);
        localImage = new UMImage(cordova.getActivity(),
				getBitMBitmap(imagePath));
        qzone.setShareMedia(localImage);
        mController.setShareMedia(qzone);
        clickShare(true, SHARE_MEDIA.QZONE);
	}

	/**
	 * 根据url地址获取图片信息
	 * 
	 * @param urlpath
	 *            图片url地址
	 * @return Bitmap 根据图片url获取图片对象
	 */
	public Bitmap getBitMBitmap(String urlpath) {
		Bitmap map = null;
		InputStream in;
		try {
			if (urlpath == null || "".equals(urlpath)) {
				String notImgPath = "img" + File.separator + "notimg.jpg";
				in = cordova.getActivity().getResources().getAssets()
						.open(notImgPath);
			} else {
				URL url = new URL(urlpath);
				URLConnection conn = url.openConnection();
				conn.connect();
				in = conn.getInputStream();
			}
			map = BitmapFactory.decodeStream(in);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.e(TAG, "IOException:" + e);
		}
		return map;
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
		mController.getConfig().closeToast();
		if (isDirectShare) {
			// 调用直接分享
			mController.directShare(cordova.getActivity(), platform,
					mShareListener);
		} else {
			// 调用直接分享, 但是在分享前用户可以编辑要分享的内容
			mController.postShare(cordova.getActivity(), platform,
					mShareListener);
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
		public void onComplete(SHARE_MEDIA platform, int stCode,
				SocializeEntity entity) {
			if (stCode == 200) {
				Log.e(TAG, "分享成功");
//				Toast.makeText(cordova.getActivity(), "分享成功",
//						Toast.LENGTH_SHORT).show();
			} else {
				Log.e(TAG, "分享失败");
//				Toast.makeText(cordova.getActivity(), "分享失败",
//						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		if (requestCode == SHARE_MAIL_CODE || requestCode == SHARE_SMS_CODE) {
//			Toast.makeText(cordova.getActivity(), "分享成功", Toast.LENGTH_SHORT)
//					.show();
			return;
		}
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 判断微信是否可用
	 * 
	 * @param context
	 *            上下文
	 * @return 安装：true,否则:false
	 */
	private static boolean isWeixinAvilible(Context context) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断QQ是否可用
	 * 
	 * @param context
	 *            上下文
	 * @return 安装：true,否则:false
	 */
	private static boolean isQQClientAvailable(Context context) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mobileqq")) {
					return true;
				}
			}
		}
		return false;
	}
}
