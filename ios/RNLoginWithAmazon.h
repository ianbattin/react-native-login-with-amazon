
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#import <LoginWithAmazon/LoginWithAmazon.h>

@interface RNLoginWithAmazon : NSObject <RCTBridgeModule>

@end
  
