# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http:#//developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
##//--- 基础混淆配置 ---
##//指定代码的压缩级别
-optimizationpasses 5

-allowaccessmodification  ##//优化时允许访问并修改有修饰符的类和类的成员

-dontusemixedcaseclassnames  ##//不使用大小写混合

-dontskipnonpubliclibraryclasses  ##//指定不去忽略非公共库的类

-verbose    ##//混淆时是否记录日志

-ignorewarnings  ##//忽略警告，避免打包时某些警告出现，没有这个的话，构建报错

-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*  ##//混淆时所采用的算法

-keepattributes *Annotation* ##//不混淆注解相关

-keepclasseswithmembernames class * {  ##//保持 native 方法不被混淆
    native <methods>;
}

-keepclassmembers enum * {  ##//保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

##//不混淆Parcelable
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}
#
##//不混淆Serializable
-keep class * implements java.io.Serializable {*;}
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {*;}



-keepclassmembers class **.R$* { ##//不混淆R文件
    public static <fields>;
}

##//不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify


-keepattributes Signature  ##//过滤泛型  出现类型转换错误时，启用这个


##//--- 不能被混淆的基类 ---
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep class org.xmlpull.v1.** { *; }



##//--- 不混淆android-support-v4包 ---
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class * extends android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v4.widget
-keep class * extends android.support.v4.app.** {*;}
-keep class * extends android.support.v4.view.** {*;}
-keep public class * extends android.support.v4.app.Fragment


##//不混淆继承的support类
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**


##//不混淆log
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}


#//保持Activity中参数类型为View的所有方法
-keepclassmembers class * extends android.app.Activity {
          public void *(android.view.View);
    }



#//--- 不混淆第三方库 这个可以去相关的第三方库官网找寻混淆代码 如果被混淆了会无法使用 ---


#//Gson
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.idea.fifaalarmclock.entity.***
-keep class com.google.gson.stream.** { *; }
-keep class com.zwonline.top28.bean.** { *; }


#//OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**


#//Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions


#//RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


#//微信
 -keep class com.tencent.mm.** {*;}


#//Glide图片库
 -keep class com.bumptech.glide.**{*;}


 #//友盟
 -keepclassmembers class * {
         public <init> (org.json.JSONObject);
 }

 -keep class com.umeng.onlineconfig.OnlineConfigAgent {
         public <fields>;
         public <methods>;
 }

 -keep class com.umeng.onlineconfig.OnlineConfigLog {
         public <fields>;
         public <methods>;
 }

 -keep interface com.umeng.onlineconfig.UmengOnlineConfigureListener {
         public <methods>;
 }


#//Testin
-dontwarn com.testin.agent.**
-keep class com.testin.agent.** {*;}


#//--- 一些特殊的混淆配置 ---


 #//有用到WEBView的JS调用接口不被混淆
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
        public *;
   }


#//对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
       void *(**On*Event);
       void *(**On*Listener);
   }


#//抛出异常时保留代码行号 方便测试
-keepattributes SourceFile,LineNumberTable


#//不混淆我们自定义控件（继承自View）
 -keep public class * extends android.view.View{
     *** get*();
     void set*(***);
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }

# 云信
-dontwarn com.netease.**
-keep class com.netease.** {*;}
-keep class com.zwonline.top28.nim.** {*;}
#如果你使用全文检索插件，需要加入
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}