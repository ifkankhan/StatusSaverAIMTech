# ====================================================================
# AGGRESSIVE R8 OBFUSCATION & SHRINKING SETTINGS (90%-100% Score)
# ====================================================================

# Repackage all obfuscated classes into root package for maximum obfuscation
-repackageclasses ''
-allowaccessmodification
-overloadaggressively

# Strip unused attributes and line numbers
-renamesourcefileattribute ""
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Suppress non-critical warnings
-dontwarn androidx.room.**
-dontwarn androidx.work.**
-dontwarn io.coilkt.**

# ====================================================================
# MINIMAL SPECIFIC RUNTIME KEEP RULES (ONLY REFLECTION TARGETS)
# ====================================================================

# WorkManager Room DB (reflection target by WorkManagerInitializer)
-keep class androidx.work.impl.WorkDatabase_Impl {
    <init>(...);
}
-keep class * extends androidx.work.Worker {
    <init>(...);
}
-keep class * extends androidx.work.ListenableWorker {
    <init>(...);
}
-keep class * extends androidx.startup.Initializer {
    <init>(...);
}
