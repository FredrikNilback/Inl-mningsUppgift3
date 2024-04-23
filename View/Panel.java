package View;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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

public class Panel extends JPanel {

    JButton loadButton;
    JButton[] button;
    Dimension dimension;
    int[] arrayToBeSorted;

    public Panel(int width, int height) {

        dimension = new Dimension(width, height);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        setLayout(null);
        makeButtons();

    }

    private void makeButtons() {

        loadButton = new JButton();
        makeLoadButton();
        this.add(loadButton);

        button = new JButton[5];

        for (int i = 0; i < button.length; i++) {
            
            button[i] = new JButton();
            makeButton(i, button[i]);
            this.add(button[i]);
        }
    }

    private void makeLoadButton() {
        final int buttonWidth = 256;
        final int buttonHeight = 64;
        final int[] bounds = new int[] {((int)(this.dimension.getWidth()) - buttonWidth) / 2, 450};

        loadButton.setBounds(bounds[0], bounds[1], buttonWidth, buttonHeight);
        loadButton.setIcon(new ImageIcon(getClass().getResource("ButtonImages/LoadButton.png")));
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final int buttonSize = 32;
                final int yDivider = 8;
                final int xBound = 16;
                final int yBound = 16;

                JFrame loadFrame = new JFrame();
                JPanel loadPanel = new JPanel();
                loadPanel.setLayout(null);
                ArrayList<int[]> arrayMatrix = loadCSV();
                int numberOfButtons = 0;

                for (int i = 0; i < arrayMatrix.size(); i++) {

                    JButton buttonToAdd = new JButton();
                    buttonToAdd.setBounds(xBound, yBound + yDivider * i + buttonSize * i, buttonSize, buttonSize);

                    loadPanel.add(buttonToAdd);
                    numberOfButtons++;
                }

                Dimension loadPanelDimension = new Dimension(128, yBound * 2 + yDivider * numberOfButtons + buttonSize * numberOfButtons);
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
            // Handle exception appropriately
        }
        return null;
    }

    private void makeButton(int buttonIndex, JButton button) {

        final int buttonSize = 64;
        final int divider = 16;
        final int[] bounds = new int[] {((int)(this.dimension.getWidth()) - ((buttonSize + divider) * 5 - divider)) / 2, 550};

        button.setBounds(bounds[0] + buttonIndex * buttonSize + buttonIndex * divider, bounds[1], buttonSize, buttonSize);

        InputStream inputStream = getClass().getResourceAsStream("ButtonImages/Buttons.png");
        BufferedImage buttonImage;
        try {
            buttonImage = ImageIO.read(inputStream);
            button.setIcon(new ImageIcon((buttonImage.getSubimage(buttonIndex * buttonSize, 0, buttonSize, buttonSize))));
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

        setButtonAction(buttonIndex, button);
    }

    private void setButtonAction(int buttonIndex, JButton button) {

        switch (buttonIndex) {
            case 0:
                button.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // TODO Auto-generated method stub
                        
                    }
                });
                break;
        
            default:
                break;
        }
    }

    private static class ButtonFunctionality {

        protected final int QUICK_SORT = 0;
        protected final int MERGE_SORT = 1;
        protected final int BUBBLE_SORT = 2;
        protected final int SELECTION_SORT = 3;
        protected final int BOGO_SORT = 4;

    }
}
