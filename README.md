
# react-native-login-with-amazon

## Getting started

`$ npm install react-native-login-with-amazon --save`

### Mostly automatic installation

`$ react-native link react-native-login-with-amazon`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-login-with-amazon` and add `RNLoginWithAmazon.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNLoginWithAmazon.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNLoginWithAmazonPackage;` to the imports at the top of the file
  - Add `new RNLoginWithAmazonPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-login-with-amazon'
  	project(':react-native-login-with-amazon').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-login-with-amazon/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-login-with-amazon')
  	```


## Usage
```javascript
import LoginWithAmazon from 'react-native-login-with-amazon';

LoginWithAmazon.login((error, accessToken, profileData) => {
  // ...
}
```
  
