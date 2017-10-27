#include <jni.h>
#include "AlgTest.h"

extern "C"
//указать тип возвр
JNIEXPORT jdouble

JNICALL
Java_com_example_raymaletdin_testtimenativealghoritm_MainActivity_calc(
        JNIEnv *env,
        jobject /* this */,
        jint n) {
    //вызвать метод класса и передать в метод тип из конструктора выше
    return AlgTest().calc(n);
}
