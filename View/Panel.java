package View;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
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
import Algorithm.MergeSort;
import Algorithm.QuickSort;
import Algorithm.SelectionSort;
import Arrays.SaveUnsorted;
import Arrays.SaveSorted;

public class Panel extends JPanel {

    private Dimension dimension;

    private JButton loadButton, saveUnsorted, saveSorted;
    private JButton[] button;
    private int[] arrayToBeSorted, sortedArray;
    private int progress;
    private JTextArea originalArrayText, sortedArrayText; 
    private JLabel progressLabel;
    private BufferedImage progressLabelImage;
    protected int index = -1;
    protected boolean changeIndex = false, skipLoading = false, bogo = false;

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
        final int[] size = new int[] {500, 506};

        originalArrayText = new JTextArea();
        originalArrayText.setText("");
        originalArrayText.setBounds(bounds, bounds, size[0], size[1]);
        originalArrayText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane originalScrollPane = new JScrollPane(originalArrayText);
        originalScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        originalScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        originalScrollPane.setBounds(8, 8, 500, 506);
        this.add(originalScrollPane);

        sortedArrayText = new JTextArea();
        sortedArrayText.setEditable(false);
        sortedArrayText.setText("");
        sortedArrayText.setBounds((int) (dimension.getWidth()) - bounds - size[0], bounds, size[0], size[1]);
        sortedArrayText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane sortedScrollPane = new JScrollPane(sortedArrayText);
        sortedScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sortedScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sortedScrollPane.setBounds((int) (dimension.getWidth()) - bounds - size[0], bounds, size[0], size[1]);
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

        loadButton = new JButton();
        makeLoadButton();
        this.add(loadButton);

        saveUnsorted = new JButton();
        saveSorted = new JButton();
        makeSaveButtons();
        this.add(saveUnsorted);
        this.add(saveSorted);

        button = new JButton[5];

        for (int i = 0; i < button.length; i++) {
            
            button[i] = new JButton();
            makeSortButton(i, button[i]);
            this.add(button[i]);
        }
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

                            setProgress(0);
                            setBogo(false);
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

    private void makeSortButton(int buttonIndex, JButton button) {

        final int buttonSize = 64;
        final int divider = 16;
        final int[] bounds = new int[] {((int)(this.dimension.getWidth()) - ((buttonSize + divider) * 5 - divider)) / 2, 550};

        button.setBounds(bounds[0] + buttonIndex * buttonSize + buttonIndex * divider, bounds[1], buttonSize, buttonSize);

        InputStream inputStream = getClass().getResourceAsStream("Images/Buttons.png");
        BufferedImage buttonImage;
        try {
            buttonImage = ImageIO.read(inputStream);
            button.setIcon(new ImageIcon((buttonImage.getSubimage(buttonIndex * buttonSize, 0, buttonSize, buttonSize))));
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        setButtonAction(buttonIndex, button);
    }

    private void setButtonAction(int buttonIndex, JButton button) {

        switch (buttonIndex) {
            case 0:
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                            @Override
                            protected Void doInBackground() throws Exception {

                                setSortedArray(new int[0]);
                                if(!skipLoading) {

                                    long beforeLoadTime = System.nanoTime();

                                    String[] stringSetArray = Panel.this.originalArrayText.getText().split((",\\s*|,|\\s+"));
                                    int[] setArray = new int[stringSetArray.length];
                                    for (int i = 0; i < stringSetArray.length; i++) {
                                        setArray[i] = Integer.parseInt(stringSetArray[i]);
                                    }
                                    setArrayToBeSorted(setArray.clone());

                                    long nanosToLoad = System.nanoTime() - beforeLoadTime;
                                    double secondsToLoad = (double)nanosToLoad / 1000000000;
                                    System.out.println("Load Time: " + secondsToLoad + " seconds"); 
                                }
                            
                                QuickSort quickSort = new QuickSort(arrayToBeSorted.clone(), Panel.this);
                                quickSort.sort();

                                if(Panel.this.changeIndex) {

                                    ArrayList<int[]> arrayMatrix = loadCSV();
                                    Panel.this.index = arrayMatrix.size() - 1;
                                }
                
                                return null;
                            }
                        };
                        worker.execute();
                    }
                });
                break;

            case 1:
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                            @Override
                            protected Void doInBackground() throws Exception {

                                setSortedArray(new int[0]);
                                if(!skipLoading) {

                                    long beforeLoadTime = System.nanoTime();

                                    String[] stringSetArray = Panel.this.originalArrayText.getText().split((",\\s*|,|\\s+"));
                                    int[] setArray = new int[stringSetArray.length];
                                    for (int i = 0; i < stringSetArray.length; i++) {
                                        setArray[i] = Integer.parseInt(stringSetArray[i]);
                                    }
                                    setArrayToBeSorted(setArray.clone());

                                    long nanosToLoad = System.nanoTime() - beforeLoadTime;
                                    double secondsToLoad = (double)nanosToLoad / 1000000000;
                                    System.out.println("Load Time: " + secondsToLoad + " seconds"); 
                                }
                                
                                MergeSort mergeSort = new MergeSort(arrayToBeSorted.clone(), Panel.this);
                                mergeSort.sort();

                                if(Panel.this.changeIndex) {

                                    ArrayList<int[]> arrayMatrix = loadCSV();
                                    Panel.this.index = arrayMatrix.size() - 1;
                                }
                    
                                return null;
                            }
                        };
                        worker.execute();
                    }
                });
                break;

            case 2:
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                            @Override
                            protected Void doInBackground() throws Exception {
                                
                                setSortedArray(new int[0]);
                                if(!skipLoading) {

                                    long beforeLoadTime = System.nanoTime();

                                    String[] stringSetArray = Panel.this.originalArrayText.getText().split((",\\s*|,|\\s+"));
                                    int[] setArray = new int[stringSetArray.length];
                                    for (int i = 0; i < stringSetArray.length; i++) {
                                        setArray[i] = Integer.parseInt(stringSetArray[i]);
                                    }
                                    setArrayToBeSorted(setArray.clone());

                                    long nanosToLoad = System.nanoTime() - beforeLoadTime;
                                    double secondsToLoad = (double)nanosToLoad / 1000000000;
                                    System.out.println("Load Time: " + secondsToLoad + " seconds"); 
                                }
                                
                                BubbleSort bubbleSort = new BubbleSort(arrayToBeSorted.clone(), Panel.this);
                                bubbleSort.sort();
                                 
                                if(Panel.this.changeIndex) {

                                    ArrayList<int[]> arrayMatrix = loadCSV();
                                    Panel.this.index = arrayMatrix.size() - 1;
                                }
                        
                                return null;
                            }
                        };
                        worker.execute();
                    }
                });
                break;
                
            case 3:
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                            @Override
                            protected Void doInBackground() throws Exception {
                                
                                setSortedArray(new int[0]);
                                if(!skipLoading) {

                                    long beforeLoadTime = System.nanoTime();

                                    String[] stringSetArray = Panel.this.originalArrayText.getText().split((",\\s*|,|\\s+"));
                                    int[] setArray = new int[stringSetArray.length];
                                    for (int i = 0; i < stringSetArray.length; i++) {
                                        setArray[i] = Integer.parseInt(stringSetArray[i]);
                                    }
                                    setArrayToBeSorted(setArray.clone());

                                    long nanosToLoad = System.nanoTime() - beforeLoadTime;
                                    double secondsToLoad = (double)nanosToLoad / 1000000000;
                                    System.out.println("Load Time: " + secondsToLoad + " seconds"); 
                                }

                                SelectionSort selectionSort = new SelectionSort(arrayToBeSorted.clone(), Panel.this);
                                selectionSort.sort();

                                if(Panel.this.changeIndex) {

                                    ArrayList<int[]> arrayMatrix = loadCSV();
                                    Panel.this.index = arrayMatrix.size() - 1;
                                }
                            
                                return null;
                            }
                        };
                        worker.execute();
                    }
                });
                break;

            case 4:
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                            @Override
                            protected Void doInBackground() throws Exception {
                                
                                setSortedArray(new int[0]);
                                if(!skipLoading) {

                                    long beforeLoadTime = System.nanoTime();

                                    String[] stringSetArray = Panel.this.originalArrayText.getText().split((",\\s*|,|\\s+"));
                                    int[] setArray = new int[stringSetArray.length];
                                    for (int i = 0; i < stringSetArray.length; i++) {
                                        setArray[i] = Integer.parseInt(stringSetArray[i]);
                                    }
                                    setArrayToBeSorted(setArray.clone());

                                    long nanosToLoad = System.nanoTime() - beforeLoadTime;
                                    double secondsToLoad = (double)nanosToLoad / 1000000000;
                                    System.out.println("Load Time: " + secondsToLoad + " seconds"); 
                                }
    
                                BogoSort bogoSort = new BogoSort(arrayToBeSorted.clone(), Panel.this);
                                bogoSort.sort();

                                if(Panel.this.changeIndex) {

                                    ArrayList<int[]> arrayMatrix = loadCSV();
                                    Panel.this.index = arrayMatrix.size() - 1;
                                }
                                
                                return null;
                            }
                        };
                        worker.execute();
                    }
                });
                break;

            default:
                break;
        }
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

    //getters & setters
    public void setProgress(int progress) {

        if (progress < 0) {
            progress = 0;
        }
        else if(progress > 9 && !bogo) {
            progress = 9;
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
            System.out.println("Print Time: " + secondsToPrint + " seconds");
        }
    }
}
