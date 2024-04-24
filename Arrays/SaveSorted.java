package Arrays;

public class SaveSorted extends SuperSave{
    
    public SaveSorted(String[] itemsToSave, int index) {

        super("SortedArrays.csv");

        writeToCSV(itemsToSave, index);
    }
}
