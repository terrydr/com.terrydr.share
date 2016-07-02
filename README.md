cordova-plugin-terrydrShare
--------------------------------------------------
####微信,QQ分享

支持平台：IOS Android

安装：cordova plugin add https://github.com/terrydr/com.terrydr.share.git

卸载：cordova plugin rm cordova-plugin-terrydrShare

示例：
    //分享插件初始化 微信key  qqkey app初始化调用
    tdShare.terrydrShareRegister("wx81f40406b3c59044", "1105336069"); 

    //分享
    tdShare.terrydrWeixinShare(title, description, imagePath, url, function(res) {
        if (res == 0) {
        popup.alert("分享到微信需要安装微信客户端");
        }
    }, function() {
        //分享成功
    });
