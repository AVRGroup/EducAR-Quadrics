/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class edu_dhbw_andar_ARToolkit */

#ifndef _Included_edu_dhbw_andar_ARToolkit
#define _Included_edu_dhbw_andar_ARToolkit
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     edu_dhbw_andar_ARToolkit
 * Method:    addObject
 * Signature: (ILedu/dhbw/andar/ARObject;Ljava/lang/String;D[D)V
 */
JNIEXPORT void JNICALL Java_edu_dhbw_andar_ARToolkit_addObject
  (JNIEnv *, jobject, jint, jobject, jstring, jdouble, jdoubleArray);

/*
 * Class:     edu_dhbw_andar_ARToolkit
 * Method:    removeObject
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_edu_dhbw_andar_ARToolkit_removeObject
  (JNIEnv *, jobject, jint, jobject);

/*
 * Class:     edu_dhbw_andar_ARToolkit
 * Method:    artoolkit_init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_edu_dhbw_andar_ARToolkit_artoolkit_1init__
  (JNIEnv *, jobject);

/*
 * Class:     edu_dhbw_andar_ARToolkit
 * Method:    artoolkit_init
 * Signature: (Ljava/lang/String;IIII)V
 */
JNIEXPORT void JNICALL Java_edu_dhbw_andar_ARToolkit_artoolkit_1init__Ljava_lang_String_2IIII
  (JNIEnv *, jobject, jstring, jint, jint, jint, jint);

/*
 * Class:     edu_dhbw_andar_ARToolkit
 * Method:    artoolkit_detectmarkers
 * Signature: ([BLjava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_edu_dhbw_andar_ARToolkit_artoolkit_1detectmarkers
  (JNIEnv *, jobject, jbyteArray, jobject);

/*
 * Class:     edu_dhbw_andar_ARToolkit
 * Method:    arUtilMatInv
 * Signature: ([D[D)I
 */
JNIEXPORT jint JNICALL Java_edu_dhbw_andar_ARToolkit_arUtilMatInv
  (JNIEnv *, jclass, jdoubleArray, jdoubleArray);

/*
 * Class:     edu_dhbw_andar_ARToolkit
 * Method:    arUtilMatMul
 * Signature: ([D[D[D)I
 */
JNIEXPORT jint JNICALL Java_edu_dhbw_andar_ARToolkit_arUtilMatMul
  (JNIEnv *, jclass, jdoubleArray, jdoubleArray, jdoubleArray);

#ifdef __cplusplus
}
#endif
#endif
/* Header for class edu_dhbw_andar_ARToolkit_DetectMarkerWorker */

#ifndef _Included_edu_dhbw_andar_ARToolkit_DetectMarkerWorker
#define _Included_edu_dhbw_andar_ARToolkit_DetectMarkerWorker
#ifdef __cplusplus
extern "C" {
#endif
#undef edu_dhbw_andar_ARToolkit_DetectMarkerWorker_MIN_PRIORITY
#define edu_dhbw_andar_ARToolkit_DetectMarkerWorker_MIN_PRIORITY 1L
#undef edu_dhbw_andar_ARToolkit_DetectMarkerWorker_NORM_PRIORITY
#define edu_dhbw_andar_ARToolkit_DetectMarkerWorker_NORM_PRIORITY 5L
#undef edu_dhbw_andar_ARToolkit_DetectMarkerWorker_MAX_PRIORITY
#define edu_dhbw_andar_ARToolkit_DetectMarkerWorker_MAX_PRIORITY 10L
#ifdef __cplusplus
}
#endif
#endif
