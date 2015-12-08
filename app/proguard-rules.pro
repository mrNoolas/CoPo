# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/david/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**
#-keep class android.net.http.AndroidHttpClient
