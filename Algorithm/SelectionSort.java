package Algorithm;

import View.Panel;

public class SelectionSort extends SuperSort implements ISort {
    
    public SelectionSort(int[] originalArray, Panel panel) {
        super(originalArray, panel);
    }

    @Override
    public void sort() {

        panel.setProgressIndex(0);
        panel.setProgress(0);
        sortedArray = new int[unsortedArray.length];
        boolean[] indicesUsed = new boolean[unsortedArray.length]; 

        for(int i = 0; i < sortedArray.length; i++) {
            int minimumValueFound = Integer.MAX_VALUE;
            int minimumIndex = -1; 
    
            for(int j = 0; j < unsortedArray.length; j++) {
                if(indicesUsed[j]) {
                    continue;
                }
    
                if(unsortedArray[j] < minimumValueFound) {
                    minimumValueFound = unsortedArray[j];
                    minimumIndex = j;
                }
            }
            
            sortedArray[i] = minimumValueFound;
            indicesUsed[minimumIndex] = true; 
    
            double completionPercentage = ((double)(i + 1) / sortedArray.length) * 100;
            int completionNumber = (int)(completionPercentage / 10);
            panel.setProgress(completionNumber);
        }

        panel.setProgressIndex(0);
        panel.setProgress(9);

        panel.setSortedArray(sortedArray);

    }
}
