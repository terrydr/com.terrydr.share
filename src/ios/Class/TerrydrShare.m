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
    [self shareQQMessage];
}

- (void)terrydrWeixinShare:(CDVInvokedUrlCommand*)command{
    _callbackId = command.callbackId;
    [self shareWeinxinMessage];
}

- (void)shareQQMessage{
    if ([QQApiInterface isQQInstalled]) {
        if ([QQApiInterface isQQSupportApi]) {
            NSString *path = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:@"news.jpg"];
            NSData* data = [NSData dataWithContentsOfFile:path];
            NSURL* url = [NSURL URLWithString:@"http://sports.qq.com/a/20120510/000650.htm"];
            
            QQApiNewsObject* img = [QQApiNewsObject objectWithURL:url title:@"天公作美伦敦奥运圣火点燃成功 火炬传递开启" description:@"腾讯体育讯 当地时间5月10日中午，阳光和全世界的目光聚焦于希腊最高女祭司手中的火炬上，5秒钟内世界屏住呼吸。火焰骤然升腾的瞬间，古老的号角声随之从赫拉神庙传出——第30届伦敦夏季奥运会圣火在古奥林匹亚遗址点燃。取火仪式前，国际奥委会主席罗格、希腊奥委会主席卡普拉洛斯和伦敦奥组委主席塞巴斯蒂安-科互赠礼物，男祭司继北京奥运会后，再度出现在采火仪式中。" previewImageData:data];
            SendMessageToQQReq* req = [SendMessageToQQReq reqWithContent:img];
            [QQApiInterface sendReq:req];
        }
    }else{
        
    }
}

- (void)shareWeinxinMessage{
    if ([WXApi isWXAppInstalled]) {
        
    }else{
        if ([WXApi isWXAppSupportApi]) {
            WXMediaMessage *message = [WXMediaMessage message];
            message.title = @"专访张小龙：产品之上的世界观";
            message.description = @"微信的平台化发展方向是否真的会让这个原本简洁的产品变得臃肿？在国际化发展方向上，微信面临的问题真的是文化差异壁垒吗？腾讯高级副总裁、微信产品负责人张小龙给出了自己的回复。";
            [message setThumbImage:[UIImage imageNamed:@"res2.png"]];
            
            WXWebpageObject *ext = [WXWebpageObject object];
            ext.webpageUrl = @"http://tech.qq.com/zt2012/tmtdecode/252.htm";
            
            message.mediaObject = ext;
            
            SendMessageToWXReq* req = [[SendMessageToWXReq alloc] init];
            req.bText = NO;
            req.message = message;
            req.scene = WXSceneSession;
            
            [WXApi sendReq:req];
        }
    }
}

@end
