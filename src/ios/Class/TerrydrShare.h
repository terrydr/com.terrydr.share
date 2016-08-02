//
//  TerrydrShare.h
//  JRCamera
//
//  Created by 路亮亮 on 16/6/3.
//
//

#import <Cordova/CDVPlugin.h>

@interface TerrydrShare : CDVPlugin

- (void)registerAppId:(CDVInvokedUrlCommand*)command;
- (void)terrydrQQShare:(CDVInvokedUrlCommand*)command;
- (void)terrydrQZoneShare:(CDVInvokedUrlCommand*)command;
- (void)terrydrWeixinShare:(CDVInvokedUrlCommand*)command;
- (void)terrydrWeixinCircleShare:(CDVInvokedUrlCommand*)command;

@end
