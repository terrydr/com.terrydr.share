# com.terrydr.share

插件名称：com.terrydr.share

功能描述：微信,QQ分享

支持平台：安卓，iOS

用于：泰瑞眼科

安装：cordova plugin add https://github.com/terrydr/com.terrydr.share.git

卸载：cordova plugin rm com.terrydr.share

示例：

//分享插件初始化 微信key  qqkey

tdShare.terrydrShareRegister("wx81f40406b3c59044", "1105336069"); 

//分享

tdShare.terrydrWeixinShare(title, description, imagePath, url, function(res) {

    if (res == 0) {
    
        popup.alert("分享到微信需要安装微信客户端");
        
    }
    
}, function() {

  //分享成功
  
});
