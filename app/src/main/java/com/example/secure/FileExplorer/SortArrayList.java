package com.example.secure.FileExplorer;

import java.util.LinkedList;

public class SortArrayList {

    /* The main function that implements QuickSort()
  arr[] --> Array to be sorted,
  low  --> Starting index,
  high  --> Ending index */
    public static void quicksort(LinkedList<Model> data, int low, int high)
    {
        if (low < high)
        {
            int pi = partition(data, low, high);

            quicksort(data, low, pi-1);
            quicksort(data, pi+1, high);
        }
    }

    private static int partition(LinkedList<Model> data, int low, int high)
    {
        Model pivot = data.get(high);
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
        {
            // If current element is smaller than the pivot
            if (data.get(j).getItemName().compareToIgnoreCase(pivot.getItemName()) < 0)
            {
                i++;

                // swap arr[i] and arr[j]
                Model placeHolder = data.get(i);
                data.set(i, data.get(j));
                data.set(j, placeHolder);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        Model temp = data.get(i+1);
        data.set(i+1, data.get(high));
        data.set(high, temp);

        return i+1;
    }

    static void merge(LinkedList<Model> data, int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        LinkedList<Model> L = new LinkedList<>();
        LinkedList<Model> R = new LinkedList<>();

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L.add(i, data.get(l + i));

        for (int j = 0; j < n2; ++j)
            R.add(j, data.get(m + 1 + j));

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2) {
            if (L.get(i).getItemName().compareToIgnoreCase(R.get(j).getItemName()) < 0) {
                data.set(k, L.get(i));
                i++;
            }
            else {
                data.set(k, R.get(j));
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            data.set(k, L.get(i));
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            data.set(k, R.get(j));
            j++;
            k++;
        }
    }

    // Main function that sorts arr[l..r] using
    // merge()
    static void mergesort(LinkedList<Model> data, int l, int r)
    {
        if (l < r) {
            // Find the middle point
            int m = (l + r) / 2;

            // Sort first and second halves
            mergesort(data, l, m);
            mergesort(data, m + 1, r);

            // Merge the sorted halves
            merge(data, l, m, r);
        }
    }
}
