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

# R8: Missing class com.google.errorprone.annotations.Immutable (referenced from: com.google.crypto.tink.KeyTemplate and 4 other contexts)
-dontwarn com.google.errorprone.annotations.Immutable

# SQLCipher
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.* { *; }
