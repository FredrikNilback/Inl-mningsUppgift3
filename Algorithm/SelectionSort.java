package Algorithm;

import View.Panel;

public class SelectionSort extends SuperSort implements ISort {
    
    public SelectionSort(int[] originalArray, Panel panel) {
        super(originalArray, panel);
    }

    @Override
    public void sort() {

        panel.setBogo(false);
        panel.setProgress(0);
        sortedArray = new int[unsortedArray.length];
        boolean[] indicesUsed = new boolean[unsortedArray.length]; 

        int[] arrayCopy = unsortedArray.clone();
        long beforeSortTime = System.nanoTime();
        for(int i = 0; i < sortedArray.length; i++) {
            int minimumValueFound = Integer.MAX_VALUE;
            int minimumIndex = -1; 
    
            for(int j = 0; j < arrayCopy.length; j++) {

                if(indicesUsed[j]) {
                    continue;
                }
    
                if(arrayCopy[j] < minimumValueFound) {
                    minimumValueFound = arrayCopy[j];
                    minimumIndex = j;
                }
            }
            
            sortedArray[i] = minimumValueFound;
            indicesUsed[minimumIndex] = true; 
    
            double completionPercentage = ((double)(i + 1) / sortedArray.length) * 100;
            int completionNumber = (int)(completionPercentage / 10);
            panel.setProgress(completionNumber);
        }

        panel.setProgress(9);

        long nanosToSort = System.nanoTime() - beforeSortTime;
        double secondsToSort = (double)nanosToSort / 1000000000;
        String secondsToSortString = "" + secondsToSort;
        panel.setStatisticsMessage(secondsToSortString, 1);  

        panel.setSortedArray(sortedArray);

    }
}
