package Algorithm;

import View.Panel;

public class QuickSort extends SuperSort implements ISort {
    
    public QuickSort(int[] originalArray, Panel panel) {
        super(originalArray, panel);
    }

    @Override
    public void sort() {

        panel.setBogo(false);
        panel.setProgress(0);

        long beforeSortTime = System.nanoTime();
        if(unsortedArray == null || unsortedArray.length <= 1) {

            sortedArray = unsortedArray;
            panel.setProgress(9);
            panel.setSortedArray(sortedArray);
            return;
        }
        
        quickSort(unsortedArray, 0, unsortedArray.length - 1);

        sortedArray = unsortedArray;
        panel.setProgress(9);

        long nanosToSort = System.nanoTime() - beforeSortTime;
        double secondsToSort = (double)nanosToSort / 1000000000;
        System.out.println("Sort Time: " + secondsToSort + " seconds");  

        panel.setSortedArray(sortedArray);
    }

    private void quickSort(int[] arrayToBeSorted, int leftIndex, int rightIndex) {

        if(leftIndex < rightIndex) {

            int pivotPoint = pivot(arrayToBeSorted, leftIndex, rightIndex);
            quickSort(arrayToBeSorted, leftIndex, pivotPoint - 1);
            quickSort(arrayToBeSorted, pivotPoint + 1, rightIndex);

            int totalElements = rightIndex - leftIndex + 1;
            int elementsProcessed = pivotPoint - leftIndex + rightIndex - pivotPoint;
            double progress = (double) elementsProcessed / totalElements * 9;
            panel.setProgress((int) Math.ceil(progress));
        }

        
    }

    private int pivot(int[] arrayToBeSorted, int leftIndex, int rightIndex) {

        int pivotPoint = arrayToBeSorted[rightIndex];
        int iterator = leftIndex - 1;

        for(int i = leftIndex; i < rightIndex; i++) {
            
            if(arrayToBeSorted[i] < pivotPoint) {

                iterator++;
                swapPlace(arrayToBeSorted, iterator, i);
            }
        }

        swapPlace(arrayToBeSorted, iterator + 1, rightIndex);
        return iterator + 1;
    }

    private void swapPlace(int[] swapArray, int i, int j) {

        int temp = swapArray[i];
        swapArray[i] = swapArray[j];
        swapArray[j] = temp;
    }
}
