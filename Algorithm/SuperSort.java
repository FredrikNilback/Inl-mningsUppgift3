package Algorithm;

import View.Panel;

public class SuperSort {
    
    int[] unsortedArray, sortedArray;
    Panel panel;

    public SuperSort(int[] originalArray, Panel panel) {
        this.unsortedArray = originalArray.clone();
        this.panel = panel;
    }
    
}
