//
//  TerrydrShare.m
//  JRCamera
//
//  Created by 路亮亮 on 16/6/3.
//
//

#import "TerrydrShare.h"
#import "WXApi.h"
#import <TencentOpenAPI/TencentOAuth.h>
#import "TencentOpenAPI/QQApiInterface.h"

@interface TerrydrShare (){
    NSString *_callbackId;
    TencentOAuth *_tencentOAuth;
}

@end

@implementation TerrydrShare

- (void)registerAppId{
    [WXApi registerApp:@"wx1f505150fdaa042e"];
    _tencentOAuth = [[TencentOAuth alloc] initWithAppId:@"1105372473"
                                            andDelegate:nil];
}

- (void)terrydrQQShare:(CDVInvokedUrlCommand*)command{
    _callbackId = command.callbackId;
    NSArray *paramArr = command.arguments;
    NSString *shareTitle = [paramArr objectAtIndex:0];
    NSString *shareDes = [paramArr objectAtIndex:1];
    NSString *shareImgPath = [paramArr objectAtIndex:2];
    NSString *shareUrl = [paramArr objectAtIndex:3];
    
    [self shareQQMessageWithTitle:shareTitle shareDes:shareDes imagePath:shareImgPath shareUrl:shareUrl];
}

- (void)terrydrWeixinShare:(CDVInvokedUrlCommand*)command{
    _callbackId = command.callbackId;
    NSArray *paramArr = command.arguments;
    NSString *shareTitle = [paramArr objectAtIndex:0];
    NSString *shareDes = [paramArr objectAtIndex:1];
    NSString *shareImgPath = [paramArr objectAtIndex:2];
    NSString *shareUrl = [paramArr objectAtIndex:3];
    
    [self shareWeinxinMessageWithTitle:shareTitle shareDes:shareDes imagePath:shareImgPath shareUrl:shareUrl];
}

- (void)shareQQMessageWithTitle:(NSString *)title shareDes:(NSString *)des imagePath:(NSString *)imgPath shareUrl:(NSString *)urlStr{
    if ([QQApiInterface isQQInstalled]) {
        if ([QQApiInterface isQQSupportApi]) {
            NSData* data = [NSData dataWithContentsOfFile:imgPath];
            NSURL* url = [NSURL URLWithString:urlStr];
            
            QQApiNewsObject* img = [QQApiNewsObject objectWithURL:url title:title description:des previewImageData:data];
            SendMessageToQQReq* req = [SendMessageToQQReq reqWithContent:img];
            [QQApiInterface sendReq:req];
        }
    }else{
        //0:未安装
        CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"0"];
        [self.commandDelegate sendPluginResult:result callbackId:_callbackId];
    }
}

- (void)shareWeinxinMessageWithTitle:(NSString *)title shareDes:(NSString *)des imagePath:(NSString *)imgPath shareUrl:(NSString *)urlStr{
    if ([WXApi isWXAppInstalled]) {
        if ([WXApi isWXAppSupportApi]) {
            WXMediaMessage *message = [WXMediaMessage message];
            message.title = title;
            message.description = des;
            NSData* data = [NSData dataWithContentsOfFile:imgPath];
            [message setThumbImage:[UIImage imageWithData:data]];
            
            WXWebpageObject *ext = [WXWebpageObject object];
            ext.webpageUrl = urlStr;
            
            message.mediaObject = ext;
            
            SendMessageToWXReq* req = [[SendMessageToWXReq alloc] init];
            req.bText = NO;
            req.message = message;
            req.scene = WXSceneSession;
            
            [WXApi sendReq:req];
        }
    }else{
        //0:未安装
        CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"0"];
        [self.commandDelegate sendPluginResult:result callbackId:_callbackId];
    }
}

@end
