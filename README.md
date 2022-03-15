<img src="https://avatars2.githubusercontent.com/u/25844347?s=200&v=4"/>

# **Fanap's PodNotification Android SDK**
a push notification android sdk

## Installation

#### Project build.gradle

```
allprojects {
    repositories {
        jcenter()
        google()
        maven { url "https://s3.amazonaws.com/repo.commonsware.com" }
        maven {
            name = "notif_sdk"
            url = uri("https://maven.pkg.github.com/FanapSoft/pod-notif-android-sdk")
            credentials {
                username = System.getenv('GITHUB_USER')
                password = System.getenv('GITHUB_PERSONAL_ACCESS_TOKEN')
            }
        }
     }
    }
```

#### App build.gradle ![Build Status](https://img.shields.io/bintray/v/farhad7d7/maven/chat?style=plastic)

```
implementation("ir.fanap.sdk_notif:sdk_notif:0.0.6.4")
```

#


#### Initial without LOGIN

```
            pushSdk = new PushSdk.Builder()
                    .setContext(MainActivity.this)
                    .setAppId(appId)
                    .setHandleNotification(false)
                    .setResponseListener(new ResponseListener() {
                        @Override
                        public void onSubscribe(JSONObject jsonObject) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnSubmitWithoutLogin.stopLoading();
                                    lblResponse.setText(jsonObject.optString("fcmToken"));
                                    lblResponse.setTextColor(Color.GREEN);
                                }
                            });
                        }

                        @Override
                        public void onUnsubscribe() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnExit.stopLoading();
                                    lblResponse.setText("Logout!!!");
                                    lblResponse.setTextColor(Color.GREEN);
                                }
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnExit.stopLoading();
                                    btnSubmitWithoutLogin.stopLoading();
                                    lblResponse.setText(e.getMessage());
                                    lblResponse.setTextColor(Color.RED);
                                }
                            });
                        }
                    })
                    .build();
```

#


#### Initial with SSOID

```
            pushSdk = new PushSdk.Builder()
                    .setContext(MainActivity.this)
                    .setAppId(appId)
                    .setSsoId(ssoId)
                    .setHandleNotification(false)
                    .setResponseListener(new ResponseListener() {
                        @Override
                        public void onSubscribe(JSONObject jsonObject) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnSubmitWithLogin.stopLoading();
                                    lblResponse.setText(jsonObject.optString("fcmToken"));
                                    lblResponse.setTextColor(Color.GREEN);
                                }
                            });
                        }

                        @Override
                        public void onUnsubscribe() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnExit.stopLoading();
                                    lblResponse.setText("Logout!!!");
                                    lblResponse.setTextColor(Color.GREEN);
                                }
                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnExit.stopLoading();
                                    btnSubmitWithLogin.stopLoading();
                                    lblResponse.setText(e.getMessage());
                                    lblResponse.setTextColor(Color.RED);
                                }
                            });
                        }
                    })
                    .build();
```

#


#### Customize notificaiotn

> You can get data from notification and parse it

```
            pushSdk.setNotificationListener(new NotificationListener() {
                @Override
                public void getNotification(JSONObject object) {
                    //TODO: get notification data and customize it, then show notification
                }
            });

```

