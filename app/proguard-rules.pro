# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/aluxian/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
-keepclasseswithmembernames class * { @butterknife.* <methods>; }

# ActiveAndroid
-keep class com.activeandroid.** { *; }
-keep class * extends com.activeandroid.Model { *; }
-keep class * extends com.activeandroid.serializer.TypeSerializer { *; }
-keepattributes Column, Table

# Picasso, OkHttp
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-dontwarn com.google.appengine.api.urlfetch.*
-dontwarn com.squareup.okhttp.**
-dontwarn java.nio.file.*
-dontwarn org.codehaus.**
-dontwarn rx.**

# Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class sun.misc.Unsafe { *; }
-keepattributes Expose, SerializedName, Since, Until
-keepclasseswithmembers class * { @com.google.gson.annotations.Expose <fields>; }

# Retrolambda
-dontwarn java.lang.invoke.*
