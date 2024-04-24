package Algorithm;

import View.Panel;

public class QuickSort extends SuperSort implements ISort {
    
    public QuickSort(int[] originalArray, Panel panel) {
        super(originalArray, panel);
    }

    @Override
    public void sort() {

        if(unsortedArray == null || unsortedArray.length <= 1) {
            return;
        }
        
        quickSort(unsortedArray, 0, unsortedArray.length - 1);

        sortedArray = unsortedArray;
        panel.setProgressIndex(0);
        panel.setProgress(9);

        panel.setSortedArray(sortedArray);
    }

    private void quickSort(int[]) {

    }
}
