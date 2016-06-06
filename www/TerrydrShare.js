var tdShare = {
terrydrShareRegister: function(successCallback, errorCallback) {
    console.log("terrydrShareRegister");
    cordova.exec(
                 successCallback,
                 errorCallback,
                 "TerrydrShare",
                 "registerAppId",
                 []
                 );
    
}
    ,
    
terrydrQQShare: function(successCallback, errorCallback) {
    console.log("terrydrQQShare");
    cordova.exec(
                 successCallback,
                 errorCallback,
                 "TerrydrShare",
                 "terrydrQQShare",
                 []
                 );
    
}
    ,
 
terrydrWeixinShare: function(successCallback, errorCallback) {
    console.log("terrydrWeixinShare");
    cordova.exec(
                 successCallback,
                 errorCallback,
                 "TerrydrShare",
                 "shareWeinxinMessage",
                 []
                 );
    
}
    
}


module.exports = jrCamera;