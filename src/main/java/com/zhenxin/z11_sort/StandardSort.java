package com.zhenxin.z11_sort;

import java.util.Arrays;
import java.util.Random;

/**
 * @Created: 11:06 19/12/2018
 * @Author: xzhen
 * @Description: TODO
 */
public class StandardSort {

    private static void bubblingSort(int[] arr) {
        int len = arr.length;
        for (int i = len - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    private static void insertSort(int[] arr) {
        int len = arr.length;
        for (int i = 1; i < len; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private static int[] mergeSort(int[] arr, int left, int right) {

        int[] leftArr = null;
        int[] rightArr = null;
        if (right - left > 1) {
            int t = (right - left) / 2;
            leftArr = mergeSort(arr, left, left + t);
            rightArr = mergeSort(arr, left + t , right);
        }

        if (leftArr == null || rightArr == null) {
            return new int[]{arr[left]};
        }
        int len = leftArr.length > rightArr.length ? leftArr.length : rightArr.length;
        int[] res = new int[len];
        int l = 0, r = 0;

        for (int i = 0; i < len; i++) {
            if (l < leftArr.length && r < rightArr.length) {
                if (leftArr[l] > rightArr[r]) {

                }
//                res[i] =
            }
        }
        return null;

    }


    public static void main(String[] args) {
        Random random = new Random();
        int len = random.nextInt(10) + 10;
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = random.nextInt(50);
        }
        System.out.println("len: " + len);
        System.out.println(Arrays.toString(arr));
//        Arrays.sort(arr);

//        bubblingSort(arr);
        insertSort(arr);


        System.out.println(Arrays.toString(arr));
    }
}
