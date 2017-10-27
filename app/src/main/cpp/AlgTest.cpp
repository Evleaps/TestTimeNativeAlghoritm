//
// Created by RAymaletdin on 10/27/2017.
//

#include "AlgTest.h"
#include <ctime>

AlgTest::AlgTest() {
    arr = NULL;// new double[size];
}

double AlgTest::calc(int n) {
    delete arr;
    double startTime = clock();
    arr = new double[n];

    for(int i = 0; i < n; i++) {
        if (i == 0 || i == 1)
            arr[i] = 1;
        else
            arr[i] = arr[i - 1] + arr[i - 2];
    }

    for(int i = 1; i < n; i++)
        arr[i] /= arr[i - 1];

    return (clock() - startTime) /CLOCKS_PER_SEC;
}

AlgTest::~AlgTest() {
    delete arr;
    arr = NULL;
};