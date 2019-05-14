/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collections;

/**
 *
 * @author dpf
 */
public enum Sort {

    ;
    
    public static void mergeSort(Comparable[] list) {
        //If list is empty; no need to do anything
        if (list.length <= 1) {
            return;
        }

        //Split the array in half in two parts
        Comparable[] first = new Comparable[list.length / 2];
        Comparable[] second = new Comparable[list.length - first.length];
        System.arraycopy(list, 0, first, 0, first.length);
        System.arraycopy(list, first.length, second, 0, second.length);

        //Sort each half recursively
        mergeSort(first);
        mergeSort(second);

        //Merge both halves together, overwriting to original array
        merge(first, second, list);
    }

    private static void merge(Comparable[] first, Comparable[] second, Comparable[] result) {
        //Index Position in first array - starting with first element
        int iFirst = 0;

        //Index Position in second array - starting with first element
        int iSecond = 0;

        //Index Position in merged array - starting with first position
        int iMerged = 0;

        //Compare elements at iFirst and iSecond, 
        //and move smaller element at iMerged
        while (iFirst < first.length && iSecond < second.length) {
            if (first[iFirst].compareTo(second[iSecond]) < 0) {
                result[iMerged] = first[iFirst];
                iFirst++;
            } else {
                result[iMerged] = second[iSecond];
                iSecond++;
            }
            iMerged++;
        }
        //copy remaining elements from both halves - each half will have already sorted elements
        System.arraycopy(first, iFirst, result, iMerged, first.length - iFirst);
        System.arraycopy(second, iSecond, result, iMerged, second.length - iSecond);
    }

    public static void quickSort(Comparable[] list) {
        if (list == null || list.length == 0) {
            return;
        }
        quickSort(list, 0, list.length - 1);
    }

    private static void quickSort(Comparable[] arr, int low, int high) {
        if (arr == null || arr.length == 0) {
            return;
        }

        if (low >= high) {
            return;
        }

        // pick the pivot
        int middle = low + (high - low) / 2;
        Comparable pivot = arr[middle];

        // make left < pivot and right > pivot
        int i = low, j = high;
        while (i <= j) {
            while (arr[i].compareTo(pivot) < 0) {
                i++;
            }

            while (arr[j].compareTo(pivot) > 0) {
                j--;
            }

            if (i <= j) {
                Comparable temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }

        // recursively sort two sub parts
        if (low < j) {
            quickSort(arr, low, j);
        }

        if (high > i) {
            quickSort(arr, i, high);
        }
    }

    private static final int MINIMUM_THREAD_WORKLOAD = 131_072;

    public static void quickSortParallel(Comparable[] array) {
        int fromIndex = 0;
        int toIndex = array.length-1;
        int rangeLength = toIndex - fromIndex;
        int cores = Math.min(rangeLength / MINIMUM_THREAD_WORKLOAD,
                             Runtime.getRuntime().availableProcessors());
        sortImpl(array, 
                 fromIndex, 
                 toIndex, 
                 cores);
    }

    private static void sortImpl(Comparable[] array,
                                 int fromIndex, 
                                 int toIndex,
                                 int cores) {
        if (cores <= 1) {
            quickSort(array, fromIndex, toIndex);
            return;
        }

        int rangeLength = toIndex - fromIndex;
        int distance = rangeLength / 4;

        Comparable a = array[fromIndex + distance];
        Comparable b = array[fromIndex + (rangeLength >>> 1)];
        Comparable c = array[toIndex - distance];

        Comparable pivot = median(a, b, c);
        int leftPartitionLength = 0;
        int rightPartitionLength = 0;
        int index = fromIndex;

        while (index < toIndex - rightPartitionLength) {
            Comparable current = array[index];

            if (current.compareTo(pivot) > 0 ) {
                ++rightPartitionLength;
                swap(array, toIndex - rightPartitionLength, index);
            } else if (current.compareTo(pivot) < 0) {
                swap(array, fromIndex + leftPartitionLength, index);
                ++index;
                ++leftPartitionLength;
            } else {
                ++index;
            }
        }

        ParallelQuicksortThread leftThread = 
            new ParallelQuicksortThread(array,
                                        fromIndex,
                                        fromIndex + leftPartitionLength,
                                        cores / 2);
    ParallelQuicksortThread rightThread =
            new ParallelQuicksortThread(array,
                                        toIndex - rightPartitionLength,
                                        toIndex,
                                        cores - cores / 2);
        

        leftThread.start();
        rightThread.start();

        try {
            leftThread.join();
            rightThread.join();
        } catch (InterruptedException ex) {
            throw new IllegalStateException(
                    "Parallel quicksort threw an InterruptedException.");
        }
    }
    
    private static void swap(Comparable[] array, int i, int j) {
        Comparable tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    private static Comparable median(Comparable a, Comparable b, Comparable c) {
        if (a.compareTo(b) <= 0) {
            if (c.compareTo(a) <= 0) {
                return a;
            }

            return b.compareTo(c) <= 0 ? b : c;
        } 

        if (c.compareTo(b) <= 0) {
            return b;
        }

        return a.compareTo(c) <= 0 ? a : c;
    } 

    private static final class ParallelQuicksortThread extends Thread {

        private final Comparable[] array;
        private final int fromIndex;
        private final int toIndex;
        private final int cores;

        ParallelQuicksortThread(Comparable[] array, 
                                int fromIndex, 
                                int toIndex, 
                                int cores) {
            this.array = array;
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
            this.cores = cores;
        }

        @Override
        public void run() {
            sortImpl(array, fromIndex, toIndex, cores);
        }
    }

}
