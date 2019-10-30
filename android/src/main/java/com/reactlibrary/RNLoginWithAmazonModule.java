
package com.reactlibrary;

import android.app.Activity;
import android.telecom.Call;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.amazon.identity.auth.device.api.authorization.*;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.facebook.react.bridge.WritableMap;

public class RNLoginWithAmazonModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

  private final ReactApplicationContext reactContext;
  private RequestContext requestContext;
  private Callback authCallback;

  //Need to check authCallback so we don't execute the same callback multiple times
  //this will crash the app
  private void executeAuthCallback(Object... args) {
    if (authCallback != null) {
      authCallback.invoke(args);
    }
    authCallback = null;
  }

  public RNLoginWithAmazonModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    this.requestContext = RequestContext.create(reactContext);
    this.requestContext.registerListener(new AuthorizeListener() {

      /* Authorization was completed successfully. */
      @Override
      public void onSuccess(AuthorizeResult result) {
        /* Your app is now authorized for the requested scopes */
        WritableMap userInfo = Arguments.createMap();
        userInfo.putString("email", result.getUser().getUserEmail());
        userInfo.putString("name", result.getUser().getUserName());
        userInfo.putString("user_id", result.getUser().getUserId());
        executeAuthCallback(null, result.getAccessToken(), userInfo);
      }

      /* There was an error during the attempt to authorize the
      application. */
      @Override
      public void onError(AuthError ae) {
        /* Inform the user of the error */
        executeAuthCallback(ae.toString());
      }

      /* Authorization was cancelled before it could be completed. */
      @Override
      public void onCancel(AuthCancellation cancellation) {
        /* Reset the UI to a ready-to-login state */
        executeAuthCallback("The user cancelled the operation.");
      }
    });
  }

  @Override
  public String getName() {
    return "RNLoginWithAmazon";
  }

  @Override
  public void onHostResume() {
    this.requestContext.onResume();
  }

  @Override
  public void onHostPause() {
  }

  @Override
  public void onHostDestroy() {
  }
  
  @ReactMethod
  public void login(final Callback callback) {
    authCallback = callback;
    AuthorizationManager.authorize(new AuthorizeRequest
      .Builder(requestContext)
      .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
      .build()
    );
  }

  @ReactMethod
  public void fetchUserData(final Callback callback) {
    Scope[] scopes = {
            ProfileScope.profile(),
            ProfileScope.postalCode()
    };

    AuthorizationManager.getToken(this.reactContext, scopes, new Listener < AuthorizeResult, AuthError > () {

      @Override
      public void onSuccess(AuthorizeResult result) {
        if (result.getAccessToken() != null) {
          /* The user is signed in */
          callback.invoke(null, result.getUser().getUserInfo());
        } else {
          callback.invoke("User not signed in");
        }
      }

      @Override
      public void onError(AuthError ae) {
        /* The user is not signed in */
        callback.invoke("User not signed in");
      }
    });
  }

  @ReactMethod
  public void logout(final Callback callback) {
    AuthorizationManager.signOut(getReactApplicationContext(), new Listener < Void, AuthError > () {
      @Override
      public void onSuccess(Void response) {
        // Set logged out state in UI
        callback.invoke();
      }
      @Override
      public void onError(AuthError authError) {
        // Log the error
        callback.invoke(authError.toString());
      }
    });
  }
}