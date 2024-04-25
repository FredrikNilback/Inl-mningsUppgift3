package Algorithm;

import java.util.Random;

import View.Panel;

public class BogoSort extends SuperSort implements ISort{
    
    boolean unsorted;

    public BogoSort(int[] originalArray, Panel panel) {
        
        super(originalArray, panel);

    }

    @Override
    public void sort() {

        unsorted = true;
        panel.setBogo(true);
        int progress = 0;
        panel.setProgress(0);

        long lastIndexUpdate = System.currentTimeMillis();
        final int millisPerUpdate = 100;
        
        while(unsorted) {
            shuffle();
            checkIfSorted();
            
            if (System.currentTimeMillis() - lastIndexUpdate >= millisPerUpdate) {
                lastIndexUpdate = System.currentTimeMillis();
                progress++;
                if (progress >= 16) {
                    progress = 0;
                }
                panel.setProgress(progress);
            }
        }

        sortedArray = unsortedArray;
        panel.setBogo(false);
        panel.setProgress(9);
        panel.setSortedArray(sortedArray);
    }

    private void shuffle() {

        Random random = new Random();
        for (int i = unsortedArray.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = unsortedArray[index];
            unsortedArray[index] = unsortedArray[i];
            unsortedArray[i] = temp;
        }
    }

    private void checkIfSorted() {

        int previous = Integer.MIN_VALUE;
        unsorted = false;
        for (int i = 0; i < unsortedArray.length; i++) {
            if (unsortedArray[i] < previous) {
                unsorted = true;
                break;
            }
            previous = unsortedArray[i];
        }
    }
}
