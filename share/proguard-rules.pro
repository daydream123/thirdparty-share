# For Gson
-keep public class com.google.gson.**{*;}

# For Picasso
-keep class com.squareup.picasso.**{*;}

# For EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}

# wechat share
-keep class com.tencent.** {
   *;
}

-keep class com.tencent.** {*;}
