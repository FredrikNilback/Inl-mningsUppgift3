package Algorithm;

import java.util.Random;

public class BogoSort {
    
    int[] originalArray;
    int[] unsortedArray;
    int[] sortedArray;
    boolean unsorted = true;

    public BogoSort(int[] originalArray) {
        
        this.originalArray = originalArray;
        this.unsortedArray = originalArray;

        while(unsorted) {
            shuffle();
            checkIfSorted();
        }
        sortedArray = unsortedArray;

        for (int i = 0; i < sortedArray.length; i++) {
            System.out.print(sortedArray[i] + ", ");
        }
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
        for (int i = 0; i < originalArray.length; i++) {
            if (unsortedArray[i] < previous) {
                unsorted = true;
                break;
            }
            previous = unsortedArray[i];
        }
    }
}
