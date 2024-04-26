package View;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import Algorithm.BogoSort;
import Algorithm.BubbleSort;
import Algorithm.ISort;
import Algorithm.MergeSort;
import Algorithm.QuickSort;
import Algorithm.SelectionSort;
import Arrays.SaveUnsorted;
import Arrays.SaveSorted;

public class Panel extends JPanel {

    private Dimension dimension;

    private JButton loadButton, saveUnsorted, saveSorted, startSort;
    private JToggleButton[] sortingAlgortihmPickerButton;
    private int[] arrayToBeSorted, sortedArray;
    private int progress;
    private JTextArea originalArrayText, sortedArrayText, statistics; 
    private JLabel progressLabel;
    private BufferedImage progressLabelImage, buttonImage;
    private String[] finalMessage = new String[3];

    protected int index = -1;
    protected boolean changeIndex = false, skipLoading = false, bogo = false;
    protected ISort iSort;

    public Panel(int width, int height) {

        dimension = new Dimension(width, height);
        progress = 0;
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        setLayout(null);

        makeButtons();
        makeLabels();
    }

    private void makeLabels() {

        final int bounds = 8;
        final int[] arrayTextSize = new int[] {500, 506};
        final int[] statisticsSize = new int[] {256, 64};

        statistics = new JTextArea();
        statistics.setEditable(false);
        statistics.setText("");
        statistics.setBounds((int) dimension.getWidth() - 353, 650, statisticsSize[0], statisticsSize[1]);
        statistics.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(statistics);

        originalArrayText = new JTextArea();
        originalArrayText.setText("");
        originalArrayText.setBounds(bounds, bounds, arrayTextSize[0], arrayTextSize[1]);
        originalArrayText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane originalScrollPane = new JScrollPane(originalArrayText);
        originalScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        originalScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        originalScrollPane.setBounds(8, 8, 500, 506);
        this.add(originalScrollPane);

        sortedArrayText = new JTextArea();
        sortedArrayText.setEditable(false);
        sortedArrayText.setText("");
        sortedArrayText.setBounds((int) (dimension.getWidth()) - bounds - arrayTextSize[0], bounds, arrayTextSize[0], arrayTextSize[1]);
        sortedArrayText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane sortedScrollPane = new JScrollPane(sortedArrayText);
        sortedScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sortedScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sortedScrollPane.setBounds((int) (dimension.getWidth()) - bounds - arrayTextSize[0], bounds, arrayTextSize[0], arrayTextSize[1]);
        this.add(sortedScrollPane);

        final int[] progressLabelSize = new int[] {(int) (dimension.getWidth()) - bounds * 4 - originalArrayText.getWidth() - sortedArrayText.getWidth(), 425};
        progressLabel = new JLabel();
        progressLabel.setBounds(bounds * 2 + sortedArrayText.getWidth(), bounds, progressLabelSize[0], progressLabelSize[1]);

        InputStream inputStream = getClass().getResourceAsStream("Images/Progress.png");
        
        try {
            progressLabelImage = ImageIO.read(inputStream);
            int yValue = 0;
            if(this.bogo) {
                yValue = 1;
            }
            progressLabel.setIcon(new ImageIcon(progressLabelImage.getSubimage(progress * progressLabelSize[0], yValue * progressLabelSize[1], progressLabelSize[0], progressLabelSize[1])));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        this.add(progressLabel);
    }

    private void makeButtons() {

        startSort = new JButton();
        makeStartSortButton();
        this.add(startSort);

        loadButton = new JButton();
        makeLoadButton();
        this.add(loadButton);

        saveUnsorted = new JButton();
        saveSorted = new JButton();
        makeSaveButtons();
        this.add(saveUnsorted);
        this.add(saveSorted);

        sortingAlgortihmPickerButton = new JToggleButton[5];

        for (int i = 0; i < sortingAlgortihmPickerButton.length; i++) {
            
            sortingAlgortihmPickerButton[i] = new JToggleButton();
            makeSortButton(i, sortingAlgortihmPickerButton[i]);
            this.add(sortingAlgortihmPickerButton[i]);
        }
    }

    private void makeStartSortButton() {

        final int buttonWidth = 256;
        final int buttonHeight = 64;
        final int[] startSortPosition = new int[]{(int) (this.dimension.getWidth() - buttonWidth) / 2, 650};

        startSort.setBounds(startSortPosition[0], startSortPosition[1], buttonWidth, buttonHeight);
        startSort.setIcon(new ImageIcon(getClass().getResource("Images/StartSort.png")));
        startSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (Panel.this.arrayToBeSorted == null) {
                    System.out.println("lol");
                    return;
                }
               
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        if (Panel.this.iSort == null) {
                            setStatistics(new String[0], true);
                        }
                        Panel.this.iSort.sort();
                        if (Panel.this.changeIndex) {
                            ArrayList<int[]> arrayMatrix = loadCSV();
                            Panel.this.index = arrayMatrix.size() - 1;
                        }
                        return null;
                    }
                };
                worker.execute();
            }
        });
    }

    private void makeSaveButtons() {

        final int buttonWidth = 256;
        final int buttonHeight = 64;
        final int[] unsortedPosition = new int[]{97, 550};
        final int[] sortedPosition = new int[] {927, 550};

        saveUnsorted.setBounds(unsortedPosition[0], unsortedPosition[1], buttonWidth, buttonHeight);
        saveUnsorted.setIcon(new ImageIcon(getClass().getResource("Images/SaveUnsorted.png")));
        saveUnsorted.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < Panel.this.sortingAlgortihmPickerButton.length; i++) {
                    Panel.this.sortingAlgortihmPickerButton[i].setSelected(false);
                }
                Panel.this.iSort = new ISort() {
                    @Override
                    public void sort() {
                        throw new UnsupportedOperationException("Unimplemented method 'sort'");
                    }                   
                };
                Panel.this.setSortedArray(new int[0]);

                String rawString = Panel.this.originalArrayText.getText();
                String[] itemsToSave = rawString.split(",\\s*|,|\\s+");
                Panel.this.changeIndex = true;
                Panel.this.skipLoading = false;
                new SaveUnsorted(itemsToSave);
            }
        });

        saveSorted.setBounds(sortedPosition[0], sortedPosition[1], buttonWidth, buttonHeight);
        saveSorted.setIcon(new ImageIcon(getClass().getResource("Images/SaveSorted.png")));
        saveSorted.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String rawString = Panel.this.sortedArrayText.getText();
                String[] itemsToSave = rawString.split(",\\s*|,|\\s+");

                new SaveSorted(itemsToSave, Panel.this.index);
            }
        });
    }

    private void makeLoadButton() {

        final int buttonWidth = 256;
        final int buttonHeight = 64;
        final int[] bounds = new int[]{((int) (this.dimension.getWidth()) - buttonWidth) / 2, 450};
    
        loadButton.setBounds(bounds[0], bounds[1], buttonWidth, buttonHeight);
        loadButton.setIcon(new ImageIcon(getClass().getResource("Images/LoadButton.png")));
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
    
                final int buttonSize = 48;
                final int divider = 8;
                final int xBound = 16;
                final int yBound = 16;
    
                JFrame loadFrame = new JFrame();
                JPanel loadPanel = new JPanel();
                loadPanel.setLayout(null);
                ArrayList<int[]> arrayMatrix = loadCSV();

                int row = 0;
                for (int i = 0; i < arrayMatrix.size(); i++) {
                    
                    final int index = i;
                    JButton buttonToAdd = new JButton();
    
                    if (i % 16 == 0 && i != 0) {
                        row++;
                    }
                    
                    int x = xBound + (buttonSize + divider) * (i % 16);
                    int y = yBound + (buttonSize + divider) * row;
    
                    buttonToAdd.setBounds(x, y, buttonSize, buttonSize);
                    buttonToAdd.setText(String.valueOf(index + 1));
                    buttonToAdd.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            setBogo(false);
                            setProgress(0);

                            Panel.this.iSort = new ISort() {
                                @Override
                                public void sort() {
                                }  
                            };
                            for (int j = 0; j < Panel.this.sortingAlgortihmPickerButton.length; j++) {
                                Panel.this.sortingAlgortihmPickerButton[j].setSelected(false);
                            }

                            setSortedArray(new int[0]);
                            setArrayToBeSorted(arrayMatrix.get(index));
                            Panel.this.index = index;
                            Panel.this.changeIndex = false;
                            Panel.this.skipLoading = true;
                            loadFrame.dispose();
                        }
                    });
    
                    loadPanel.add(buttonToAdd);
                }
    
                int width = 16 * (buttonSize + divider) + yBound;
                int height = (row + 1) * (buttonSize + divider) + xBound;
                Dimension loadPanelDimension = new Dimension(width, height);
                loadPanel.setMinimumSize(loadPanelDimension);
                loadPanel.setPreferredSize(loadPanelDimension);
                loadPanel.setMaximumSize(loadPanelDimension);
    
                loadFrame.add(loadPanel);
                loadFrame.pack();
                loadFrame.setLocationRelativeTo(null);
                loadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                loadFrame.setResizable(false);
                loadFrame.setVisible(true);
            }
        });
    }

    private ArrayList<int[]> loadCSV() {

        ArrayList<int[]> integerArrayList = new ArrayList<>();
        String path = "Arrays/UnsortedArrays.csv"; 
        
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)))) {

            String line;
            while((line = bufferedReader.readLine()) != null) {

                String[] stringList = line.split(",");

                int[] integerArray = new int[stringList.length];
                for (int i = 0; i < stringList.length; i++) {
                    integerArray[i] = Integer.parseInt(stringList[i]);
                }
            
                integerArrayList.add(integerArray);
            }

            return integerArrayList;
        } 
        catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void makeSortButton(int buttonIndex, JToggleButton button) {

        final int buttonSize = 64;
        final int divider = 16;
        final int[] bounds = new int[] {((int)(this.dimension.getWidth()) - ((buttonSize + divider) * 5 - divider)) / 2, 550};

        button.setBounds(bounds[0] + buttonIndex * buttonSize + buttonIndex * divider, bounds[1], buttonSize, buttonSize);

        InputStream inputStream = getClass().getResourceAsStream("Images/Buttons.png");
        
        try {
            buttonImage = ImageIO.read(inputStream);
            button.setIcon(new ImageIcon((buttonImage.getSubimage(buttonIndex * buttonSize, 0, buttonSize, buttonSize))));
            button.setSelectedIcon(new ImageIcon((buttonImage.getSubimage(buttonIndex * buttonSize, buttonSize, buttonSize, buttonSize))));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        setButtonAction(buttonIndex, button);
    }

    private void setButtonAction(int buttonIndex, JToggleButton button) {

        switch (buttonIndex) {
            case 0:
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        setSortedArray(new int[0]);
                        if(!skipLoading) {
                            Panel.this.loadArrayToBeSorted(); 
                        }

                        boolean arrayIsSelected = Panel.this.arrayToBeSorted != null;
                        for(int i = 0; i < Panel.this.sortingAlgortihmPickerButton.length; i++) {
                            if (i != buttonIndex || !arrayIsSelected) {
                                Panel.this.sortingAlgortihmPickerButton[i].setSelected(false);
                            }
                        }
                        
                        if(arrayIsSelected) {
                            Panel.this.iSort = new QuickSort(arrayToBeSorted.clone(), Panel.this);            
                        }
                    }        
                });
                break;

            case 1:
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        setSortedArray(new int[0]);
                        if(!skipLoading) {
                            Panel.this.loadArrayToBeSorted(); 
                        }

                        boolean arrayIsSelected = Panel.this.arrayToBeSorted != null;
                        for(int i = 0; i < Panel.this.sortingAlgortihmPickerButton.length; i++) {
                            if (i != buttonIndex || !arrayIsSelected) {
                                Panel.this.sortingAlgortihmPickerButton[i].setSelected(false);
                            }
                        }
                        if(arrayIsSelected) {    
                            Panel.this.iSort = new MergeSort(arrayToBeSorted.clone(), Panel.this);            
                        }    
                    }
                });
                break;

            case 2:
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        setSortedArray(new int[0]);
                        if(!skipLoading) {
                            Panel.this.loadArrayToBeSorted(); 
                        }

                        boolean arrayIsSelected = Panel.this.arrayToBeSorted != null;
                        for(int i = 0; i < Panel.this.sortingAlgortihmPickerButton.length; i++) {
                            if (i != buttonIndex || !arrayIsSelected) {
                                Panel.this.sortingAlgortihmPickerButton[i].setSelected(false);
                            }
                        }
                        
                        if(arrayIsSelected) {
                            Panel.this.iSort = new BubbleSort(arrayToBeSorted.clone(), Panel.this);            
                        }
                    }
                });
                break;
                
            case 3:
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        setSortedArray(new int[0]);
                        if(!skipLoading) {
                            Panel.this.loadArrayToBeSorted(); 
                        }

                        boolean arrayIsSelected = Panel.this.arrayToBeSorted != null;
                        for(int i = 0; i < Panel.this.sortingAlgortihmPickerButton.length; i++) {
                            if (i != buttonIndex || !arrayIsSelected) {
                                Panel.this.sortingAlgortihmPickerButton[i].setSelected(false);
                            }
                        }
                        
                        if(arrayIsSelected) {
                            Panel.this.iSort = new SelectionSort(arrayToBeSorted.clone(), Panel.this);    
                        }        
                    }
                });
                break;

            case 4:
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        setSortedArray(new int[0]);
                        if(!skipLoading) {
                            Panel.this.loadArrayToBeSorted(); 
                        }

                        boolean arrayIsSelected = Panel.this.arrayToBeSorted != null;
                        for(int i = 0; i < Panel.this.sortingAlgortihmPickerButton.length; i++) {
                            if (i != buttonIndex || !arrayIsSelected) {
                                Panel.this.sortingAlgortihmPickerButton[i].setSelected(false);
                            }
                        }

                        if(arrayIsSelected) {
                            Panel.this.iSort = new BogoSort(arrayToBeSorted.clone(), Panel.this);            
                        }
                    }
                });
                break;

            default:
                break;
        }
    }

    private void loadArrayToBeSorted() {

        long beforeLoadTime = System.nanoTime();

        String[] stringSetArray = originalArrayText.getText().split((",\\s*|,|\\s+"));
        boolean errorCaught = false;
        int[] setArray = new int[stringSetArray.length];
        for (int i = 0; i < stringSetArray.length; i++) {
            try {
                setArray[i] = Integer.parseInt(stringSetArray[i]);
            } 
            catch (Exception e) {
                errorCaught = true;
            }  
        }
        if(!errorCaught) {
            setArrayToBeSorted(setArray);
        }

        long nanosToLoad = System.nanoTime() - beforeLoadTime;
        double secondsToLoad = (double)nanosToLoad / 1000000000;
        String secondsToLoadString = "" + secondsToLoad;
        setStatisticsMessage(secondsToLoadString, 0);  
    }

    private void updateProgressLabel() {

        final int bounds = 8;
        final int[] progressLabelSize = new int[] {(this.getWidth()) - bounds * 4 - 500 * 2, 425};

        int yValue = 0;
        if (bogo) {
            yValue = 1;
        }
        this.progressLabel.setIcon(new ImageIcon(progressLabelImage.getSubimage(progress * progressLabelSize[0], yValue * progressLabelSize[1], progressLabelSize[0], progressLabelSize[1])));
        revalidate();
        repaint();
    }

    private void setArrayToBeSorted(int[] array) {

        this.arrayToBeSorted = array;
        String arrayText = "";
        for(int i = 0; i < arrayToBeSorted.length - 1; i++) {

            arrayText += arrayToBeSorted[i] + ", "; 
            if(i % 15 == 0 && i != 0) {
                arrayText += "\n";
            }
        }

        arrayText += arrayToBeSorted[arrayToBeSorted.length - 1];
        this.originalArrayText.setText(arrayText);
    }

    private void setStatistics(String[] statistics, boolean complaint) {

        String output = "";
        if (complaint) {
            output = "Please Select Sorting Algorithm";
        }
        else if(statistics[0] == null) {
            output = "Load Time: ";
            output += "Preloaded.\n";
        }
        else{
            output = "Load Time: ";
            output += statistics[0] + " Seconds\n";
        }
        output += "Sort Time: " + statistics[1] + " Seconds.\n";
        output += "Print Time: " + statistics[2] + " Seconds.";

        skipLoading = true;
        setStatisticsMessage(null, 0);

        this.statistics.setText(output);
    }

    //public setters
    public void setStatisticsMessage(String messagePart, int index) {
        
        finalMessage[index] = messagePart;
        if(index == 2) {
            if(this.skipLoading) {
                finalMessage[0] = null;
            }
            setStatistics(finalMessage, false);
        }
    }

    public void setProgress(int progress) {

        if (progress < 0) {
            progress = 0;
        }
        else if(progress > 9 && !bogo) {
            progress = 9;
        }
        else if(progress > 15 && bogo) {
            progress = 15;
        }
        
        this.progress = progress;
        updateProgressLabel();  
    }

    public void setBogo(boolean bogo) {
        
        this.bogo = bogo;
        updateProgressLabel();
    }

    public void setSortedArray(int[] array) {

        this.sortedArray = array;
        String arrayText = "";
        long beforePrintTime = 0;

        if(sortedArray.length != 0) {
            beforePrintTime = System.nanoTime();
        }

        for(int i = 0; i < sortedArray.length - 1; i++) {

            arrayText += sortedArray[i] + ", "; 
            if(i % 15 == 0 && i != 0) {
                arrayText += "\n";
            }
        }
        if(sortedArray.length != 0) {
            arrayText += sortedArray[sortedArray.length - 1];
        }

        this.sortedArrayText.setText(arrayText);

        if(sortedArray.length != 0) {

            long nanosToPrint = System.nanoTime() - beforePrintTime;
            double secondsToPrint = (double) nanosToPrint / 1000000000;
            String secondsToSortString = "" + secondsToPrint;
            setStatisticsMessage(secondsToSortString, 2);  
        }
    }
}
