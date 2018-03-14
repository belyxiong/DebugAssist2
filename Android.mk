LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4

#LOCAL_JAVA_LIBRARIES := MediaPlayer

LOCAL_JAVA_LIBRARIES += info3
LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := DebugAssist2

LOCAL_CERTIFICATE := platform

# Pick correct res dir based on MODEL_YEAR	 

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, res)

LOCAL_DEX_PREOPT := false

include $(BUILD_PACKAGE)
