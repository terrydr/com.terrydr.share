var tdShare = {
terrydrShareRegister: function(wxAppId,qqAppId,successCallback, errorCallback) {
    console.log("terrydrShareRegister");
    cordova.exec(
                 successCallback,
                 errorCallback,
                 "TerrydrShare",
                 "registerAppId",
                 [wxAppId,qqAppId]
                 );
    
}
    ,
    
terrydrQQShare: function(title,descirption,imagePath,url,successCallback, errorCallback) {
    console.log("terrydrQQShare");
    cordova.exec(
                 successCallback,
                 errorCallback,
                 "TerrydrShare",
                 "terrydrQQShare",
                 [title,descirption,imagePath,url]
                 );
    
}
    ,
 
terrydrWeixinShare: function(title,descirption,imagePath,url,successCallback, errorCallback) {
    console.log("terrydrWeixinShare");
    cordova.exec(
                 successCallback,
                 errorCallback,
                 "TerrydrShare",
                 "terrydrWeixinShare",
                 [title,descirption,imagePath,url]
                 );
    
}
    
}


module.exports = tdShare;