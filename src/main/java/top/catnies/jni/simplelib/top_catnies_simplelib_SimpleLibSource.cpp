#include "top_catnies_simplelib_SimpleLibSource.h"

JNIEXPORT jint JNICALL Java_top_catnies_simplelib_SimpleLibSource_add(JNIEnv *env, jobject obj, jint a, jint b) {
  return a + b;
}