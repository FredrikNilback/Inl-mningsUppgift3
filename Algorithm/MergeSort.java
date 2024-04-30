package Algorithm;

import View.Panel;

public class MergeSort extends SuperSort implements ISort {

    private int totalMergeSteps;
    
    public MergeSort(int[] originalArray, Panel panel) {
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
        
        int[] arrayCopy = unsortedArray.clone();
        totalMergeSteps = calculateTotalMergeSteps(arrayCopy.length);
        mergeSort(arrayCopy, 0, arrayCopy.length - 1);

        sortedArray = arrayCopy;
        panel.setProgress(9);

        long nanosToSort = System.nanoTime() - beforeSortTime;
        double secondsToSort = (double)nanosToSort / 1000000000;
        String secondsToSortString = "" + secondsToSort;
        panel.setStatisticsMessage(secondsToSortString, 1);   

        panel.setSortedArray(sortedArray);
    }

    private void mergeSort(int[] divideArray, int leftIndex, int rightIndex) {

        if(leftIndex < rightIndex) {

            int middleIndex = (leftIndex + rightIndex) / 2;
            mergeSort(divideArray, leftIndex, middleIndex);
            mergeSort(divideArray, middleIndex + 1, rightIndex);
            
            merge(divideArray, leftIndex, middleIndex, rightIndex);

            int currentMergeStep = calculateMergeStep(leftIndex, rightIndex);
            int progress = (int) ((double) currentMergeStep / totalMergeSteps * 9);
            panel.setProgress(progress);
        }
    }

    private void merge(int[] mergeArray, int leftIndex, int middleIndex, int rightIndex) {

        int leftArraySize = middleIndex - leftIndex + 1;
        int rightArraySize = rightIndex - middleIndex;
        
        int[] leftArray = new int[leftArraySize];
        int[] rightArray = new int[rightArraySize];
        for(int i = 0; i < leftArraySize; i++) {
            leftArray[i] = mergeArray[leftIndex + i];
        }
        for(int j = 0; j < rightArraySize; j++) {
            rightArray[j] = mergeArray[middleIndex + 1 + j];
        }
        
        int i = 0, j = 0, k = leftIndex;
        while(i < leftArraySize && j < rightArraySize) {

            if(leftArray[i] <= rightArray[j]) {

                mergeArray[k] = leftArray[i];
                i++;
            } 
            else {

                mergeArray[k] = rightArray[j];
                j++;
            }
            k++;
        }
        
        while(i < leftArraySize) {

            mergeArray[k] = leftArray[i];
            i++;
            k++;
        }
        
        while(j < rightArraySize) {

            mergeArray[k] = rightArray[j];
            j++;
            k++;
        }
    }

    private int calculateTotalMergeSteps(int length) {
        return (int)(Math.ceil(Math.log(length) / Math.log(2)));
    }

    private int calculateMergeStep(int leftIndex, int rightIndex) {
        return (int)(Math.log(rightIndex - leftIndex + 1) / Math.log(2));
    }


}
