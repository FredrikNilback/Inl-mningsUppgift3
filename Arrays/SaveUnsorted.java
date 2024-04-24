package Arrays;

public class SaveUnsorted extends SuperSave {
    
    public SaveUnsorted(String[] itemsToSave) {

        super("UnsortedArrays.csv");

        writeToCSV(itemsToSave, -1);
    }
}
