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

- (void)registerAppId:(CDVInvokedUrlCommand*)command{
    NSArray *paramArr = command.arguments;
    NSString *wxAppId = [paramArr objectAtIndex:0];
    NSString *qqAppId = [paramArr objectAtIndex:1];
    
    [WXApi registerApp:wxAppId];
    _tencentOAuth = [[TencentOAuth alloc] initWithAppId:qqAppId
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

- (void)terrydrQZoneShare:(CDVInvokedUrlCommand*)command{
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
    
    [self shareWeinxinMessageWithTitle:shareTitle shareDes:shareDes imagePath:shareImgPath shareUrl:shareUrl shareType:WXSceneSession];
}

- (void)terrydrWeixinCircleShare:(CDVInvokedUrlCommand*)command{
    _callbackId = command.callbackId;
    NSArray *paramArr = command.arguments;
    NSString *shareTitle = [paramArr objectAtIndex:0];
    NSString *shareDes = [paramArr objectAtIndex:1];
    NSString *shareImgPath = [paramArr objectAtIndex:2];
    NSString *shareUrl = [paramArr objectAtIndex:3];
    
    [self shareWeinxinMessageWithTitle:shareTitle shareDes:shareDes imagePath:shareImgPath shareUrl:shareUrl shareType:WXSceneTimeline];
}

- (void)shareQQMessageWithTitle:(NSString *)title shareDes:(NSString *)des imagePath:(NSString *)imgPath shareUrl:(NSString *)urlStr{
    if ([QQApiInterface isQQInstalled]) {
        if ([QQApiInterface isQQSupportApi]) {
            NSData* data = [NSData dataWithContentsOfURL:[NSURL URLWithString:imgPath]];
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

- (void)shareWeinxinMessageWithTitle:(NSString *)title shareDes:(NSString *)des imagePath:(NSString *)imgPath shareUrl:(NSString *)urlStr shareType:(int)scene{
    if ([WXApi isWXAppInstalled]) {
        if ([WXApi isWXAppSupportApi]) {
            WXMediaMessage *message = [WXMediaMessage message];
            message.title = title;
            message.description = des;
            NSData* data = [NSData dataWithContentsOfURL:[NSURL URLWithString:imgPath]];
            UIImage *originImage = [UIImage imageWithData:data];
            UIImage *shareImage = [self compressImage:originImage toMaxFileSize:8*1024];
            [message setThumbImage:shareImage];
            
            WXWebpageObject *ext = [WXWebpageObject object];
            ext.webpageUrl = urlStr;
            
            message.mediaObject = ext;
            
            SendMessageToWXReq* req = [[SendMessageToWXReq alloc] init];
            req.bText = NO;
            req.message = message;
            req.scene = scene;
            
            [WXApi sendReq:req];
        }
    }else{
        //0:未安装
        CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"0"];
        [self.commandDelegate sendPluginResult:result callbackId:_callbackId];
    }
}

- (UIImage *)compressImage:(UIImage *)image toMaxFileSize:(NSInteger)maxFileSize {
    CGFloat compression = 0.9f;
    CGFloat maxCompression = 0.1f;
    NSData *imageData = UIImageJPEGRepresentation(image, compression);
    while ([imageData length] > maxFileSize && compression > maxCompression) {
        compression -= 0.1;
        imageData = UIImageJPEGRepresentation(image, compression);
    }
    
    UIImage *compressedImage = [UIImage imageWithData:imageData];
    return compressedImage;
}

@end
