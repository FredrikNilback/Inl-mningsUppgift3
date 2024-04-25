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

        long beforeSortTime = System.nanoTime();
        while(true) {
            
            swapped = false; 
            for(int i = 0; i < unsortedArray.length - 1; i++) {
    
                if(unsortedArray[i] > unsortedArray[i + 1]) {
                    int temp = unsortedArray[i];
                    unsortedArray[i] = unsortedArray[i + 1];
                    unsortedArray[i + 1] = temp;
                    swapped = true;
                }
            }

            if(!swapped) {
                break;
            }

            passes++;
            approximatedProgress = Math.min(9, (passes * 9) / unsortedArray.length);
            panel.setProgress(approximatedProgress);
        }

        sortedArray = unsortedArray;
        panel.setProgress(9);

        long nanosToSort = System.nanoTime() - beforeSortTime;
        double secondsToSort = (double)nanosToSort / 1000000000;
        System.out.println("Sort Time: " + secondsToSort + " seconds"); 

        panel.setSortedArray(sortedArray);
        
    }
}
