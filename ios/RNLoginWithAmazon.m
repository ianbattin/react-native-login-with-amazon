
#import "RNLoginWithAmazon.h"
#import <React/RCTLog.h>

@implementation RNLoginWithAmazon

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()

// Called when the user taps the Login with Amazon Button
// Redirects to the Amazon sign on page
RCT_EXPORT_METHOD(login:(RCTResponseSenderBlock)callback)
{
    // Build an authorize request.
    AMZNAuthorizeRequest *request = [[AMZNAuthorizeRequest alloc] init];
    request.scopes = [NSArray arrayWithObjects:
                      [AMZNProfileScope profile],
                      [AMZNProfileScope postalCode], nil];
    
    // Make an Authorize call to the Login with Amazon SDK.
    [[AMZNAuthorizationManager sharedManager] authorize:request
                                            withHandler:^(AMZNAuthorizeResult *result, BOOL
                                                          userDidCancel, NSError *error) {
                                                if (error) {
                                                    callback(@[error]);
                                                } else if (userDidCancel) {
                                                    NSMutableDictionary* details = [NSMutableDictionary dictionary];
                                                    [details setValue:@"User cancelled" forKey:NSLocalizedDescriptionKey];
                                                    NSError *cancelledError = [NSError errorWithDomain:@"loginWithAmazon" code:1 userInfo:details];
                                                    callback(@[cancelledError]);
                                                } else {
                                                    // Authentication was successful.
                                                    // Obtain the access token and user profile data.
                                                    NSString *accessToken = result.token;
                                                    AMZNUser *user = result.user;
                                                    callback(@[[NSNull null], accessToken, user.profileData]);
                                                }
                                            }];
}

// Call to fetch the user's account information as long as they are already logged in/authorized
RCT_EXPORT_METHOD(fetchUserData:(RCTResponseSenderBlock)callback)
{
    [AMZNUser fetch:^(AMZNUser *user, NSError *error) {
        if (error) {
            callback(@[error]);
        } else if (user) {
            callback(@[[NSNull null], user.profileData]);
        }
    }];
}

// Logs the user out
RCT_EXPORT_METHOD(logout:(RCTResponseSenderBlock)callback)
{
    [[AMZNAuthorizationManager sharedManager] signOut:^(NSError * _Nullable error) {
        if (!error) {
            RCTLogInfo(@"Error occured!");
            callback(@[error]);
        }
        
        callback(@[[NSNull null]]);
    }];
}

@end
  
