# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
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

# for picasso
#   https://github.com/square/picasso#proguard
-dontwarn com.squareup.okhttp.**

# for Okio(OkHttp)
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# for moshi
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keep class com.ttvnp.ttj_asset_android_client.data.service.response.* {
  *;
}

# rx
-keepnames class io.reactivex.** { *; }
-dontwarn io.reactivex.**
-dontnote io.reactivex.**

# google
-keep class com.google.android.gms.common.GooglePlayServicesUtil { *; }
-dontwarn com.google.android.gms.common.GooglePlayServicesUtil
-dontnote com.google.android.gms.common.GooglePlayServicesUtil
-keep class com.google.errorprone.annotations.* { *; }
-dontwarn com.google.errorprone.annotations.*
-dontnote com.google.errorprone.annotations.*

# kotlin reflect / annotations
-dontwarn kotlin.reflect.jvm.internal.**

# ok
-dontwarn okhttp3.**
-dontwarn okio.**