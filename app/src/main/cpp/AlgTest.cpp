//
// Created by RAymaletdin on 10/27/2017.
//

#include "AlgTest.h"
#include <ctime>

AlgTest::AlgTest() {
    arr = NULL;// new double[size];
}

void AlgTest::calc(int n) {
    delete arr;
    arr = new double[n];

    for(int i = 0; i < n; i++) {
        if (i == 0 || i == 1)
            arr[i] = 1;
        else
            arr[i] = arr[i - 1] + arr[i - 2];
    }

    for(int i = 1; i < n; i++) {
        arr[i] /= arr[i - 1];
        for(int k = 1; k < n; k++) {
            arr[k] /= arr[k - 1];
        }
    }
}

AlgTest::~AlgTest() {
    delete arr;
    arr = NULL;
};