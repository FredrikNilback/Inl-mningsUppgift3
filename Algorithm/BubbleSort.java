package Algorithm;

import View.Panel;

public class BubbleSort extends SuperSort implements ISort {
    
    public BubbleSort(int[] originalArray, Panel panel) {

        super(originalArray, panel);
    }

    @Override
    public void sort() {
        
        panel.setBogo(false);
        panel.setProgress(0);
        boolean swapped = true;
        int approximatedProgress = 0;
        int passes = 0;

        int[] arrayCopy = unsortedArray.clone();
        long beforeSortTime = System.nanoTime();
        while(true) {
            
            swapped = false; 
            for(int i = 0; i < arrayCopy.length - 1; i++) {
    
                if(arrayCopy[i] > arrayCopy[i + 1]) {
                    int temp = arrayCopy[i];
                    arrayCopy[i] = arrayCopy[i + 1];
                    arrayCopy[i + 1] = temp;
                    swapped = true;
                }
            }

            if(!swapped) {
                break;
            }

            passes++;
            approximatedProgress = Math.min(9, (passes * 9) / arrayCopy.length);
            panel.setProgress(approximatedProgress);
        }

        sortedArray = arrayCopy;
        panel.setProgress(9);

        long nanosToSort = System.nanoTime() - beforeSortTime;
        double secondsToSort = (double)nanosToSort / 1000000000;
        String secondsToSortString = "" + secondsToSort;
        panel.setStatisticsMessage(secondsToSortString, 1);  

        panel.setSortedArray(sortedArray);
        
    }
}
