# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

# AppGallery Connect SDK
-keepclassmembers class **{
    public <init>(android.content.Context,com.huawei.agconnect.AGConnectInstance);
}
-keepclassmembers class com.huawei.agconnect.remoteconfig.internal.server.**{*;}
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class com.huawei.agconnect.**{*;}
-dontwarn com.huawei.agconnect.**
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep interface com.huawei.hms.analytics.type.HAEventType{*;}
-keep interface com.huawei.hms.analytics.type.HAParamType{*;}
-keepattributes Exceptions, Signature, InnerClasses, LineNumberTable

# Firebase
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# Flipper
-keep class com.facebook.jni.** { *; }
-keep class com.facebook.flipper.** { *; }
# Missing class com.facebook.proguard.annotations.DoNotStrip (referenced from: void com.facebook.flipper.android.EventBase.initHybrid() and 7 other contexts)\nMissing class org.slf4j.impl.StaticLoggerBinder (referenced from: void org.slf4j.LoggerFactory.bind() and 3 other contexts)","sources":[{}],"tool":"R8
-dontwarn com.facebook.proguard.annotations.DoNotStrip

# R8: Missing class com.google.errorprone.annotations.Immutable (referenced from: com.google.crypto.tink.KeyTemplate and 4 other contexts)
-dontwarn com.google.errorprone.annotations.Immutable

# SQLCipher
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.* { *; }

# https://youtrack.jetbrains.com/issue/KTOR-5528
# https://stackoverflow.com/q/76042330
-dontwarn org.slf4j.impl.StaticLoggerBinder

# Added because of
# AGPBI: {"kind":"error","text":"Missing class android.securitydiagnose.HwSecurityDiagnoseManager$StpExtraStatusInfo (referenced from: boolean com.huawei.secure.android.common.detect.b.b())","sources":[{}],"tool":"R8"}
-ignorewarnings
