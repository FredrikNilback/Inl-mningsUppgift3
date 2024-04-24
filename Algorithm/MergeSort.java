package Algorithm;

import View.Panel;

public class MergeSort extends SuperSort implements ISort {
    
    public MergeSort(int[] originalArray, Panel panel) {
        super(originalArray, panel);


    }

    @Override
    public void sort() {

        if(unsortedArray == null || unsortedArray.length <= 1) {
            return;
        }
        
        mergeSort(unsortedArray, 0, unsortedArray.length - 1);

        sortedArray = unsortedArray;
        panel.setProgressIndex(0);
        panel.setProgress(9);

        panel.setSortedArray(sortedArray);
    }

    private void mergeSort(int[] divideArray, int leftIndex, int rightIndex) {

        if(leftIndex < rightIndex) {

            int middleIndex = (leftIndex + rightIndex) / 2;
            mergeSort(divideArray, leftIndex, middleIndex);
            mergeSort(divideArray, middleIndex + 1, rightIndex);
            
            merge(divideArray, leftIndex, middleIndex, rightIndex);
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
        
        int i = 0, j = 0;
        int k = leftIndex;
        
        while (i < leftArraySize && j < rightArraySize) {

            if (leftArray[i] <= rightArray[j]) {

                mergeArray[k] = leftArray[i];
                i++;
            } 
            else {

                mergeArray[k] = rightArray[j];
                j++;
            }
            k++;
        }
        
        while (i < leftArraySize) {

            mergeArray[k] = leftArray[i];
            i++;
            k++;
        }
        
        while (j < rightArraySize) {

            mergeArray[k] = rightArray[j];
            j++;
            k++;
        }
    }


}
