package com.converter;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;
import org.apache.log4j.*;

/**
 * The Form class is used to create the client GUI for the program
 * @version 20 Jul 2015
 * @author Ido Algom
 * @author Dassi Rosen
 */

public class FormTest implements ActionListener, MessageConsumer{
    static Logger logger = Logger.getLogger("FormLogger.props");
    private String countries[];
    private String labels[];
    private JFrame mainFrame;
    private JPanel southPanel;
    private JPanel midPanel;
    private JPanel northPanel;
    private JPanel tablePanel;
    private JTextField fromText;
    private JTextField toText;
    private JButton convertButton;
    private JButton switchButton;
    private JTable rateTable;
    private JComboBox<String> toBox;
    private JComboBox<String> fromBox;
    private IRateCalculatorModel calc;
    private int index = 11;
    private JButton tableButton;
    private JFrame tableFrame;

    /**
     * Sole client GUI constructor
     */

    protected FormTest(IRateCalculatorModel calcModel, BOIParser boiParser) {
        this.calc = calcModel;
        countries = boiParser.getCountries();
        labels = boiParser.getLabels();
        mainFrame = new JFrame();
        tableFrame = new JFrame();
        northPanel = new JPanel();
        midPanel = new JPanel();
        tablePanel = new JPanel();
        southPanel = new JPanel();
        //create rate table
        String[] cols = new String[16];
        cols[0]="";
        for (int i=1; i<16; i++) {
            cols[i] = labels[i-1];
        }
        double[][] rateMatrix = calc.getMatrix();
        String[][] dataTable = new String[15][16];
        for(int i=0; i< rateMatrix.length; i++){
            dataTable[i][0]= labels[i];
            for(int j=0; j<rateMatrix.length; j++) {
                dataTable[i][j+1]= new DecimalFormat("#0.0000").format(rateMatrix[i][j]);
            }
        }
        rateTable = new JTable(dataTable, cols);
        toBox = new JComboBox(labels);
        fromBox = new JComboBox(labels);
        fromText = new JTextField("1.0");
        toText = new JTextField();
        convertButton = new JButton("Convert");
        switchButton = new JButton();
        tableButton = new JButton("Table");
    }

    /**
     * Creates GUI components and adds event listeners to them
     */

    private void initForm() {
        //Init comboBoxes
        fromBox.setToolTipText("Choose a currency to convert from");
        toBox.setToolTipText("Choose a currency to convert to");
        initLists(fromBox);
        initLists(toBox);
        //North
        northPanel.setLayout(new FlowLayout());
        northPanel.add(fromBox);
        northPanel.add(switchButton);
        northPanel.add(toBox);
        // Mid
        midPanel.setLayout(new GridLayout(1, 3));
        midPanel.add(fromText);
        midPanel.add(toText);
        //South
        southPanel.setLayout(new FlowLayout());
        southPanel.add(convertButton);
        southPanel.add(tableButton);

        Dimension tableDimension = rateTable.getPreferredSize();
        JScrollPane tablePane = new JScrollPane(rateTable);
        tablePane.setPreferredSize(new Dimension(tableDimension.width, rateTable.getRowHeight()*labels.length));
        tablePanel.add(tablePane);
        //// Init MainFrame
        int heightWin = 200, widthWin = 570;
        mainFrame.setSize(widthWin,heightWin);
        mainFrame.setMinimumSize(new Dimension(widthWin, heightWin));
        mainFrame.setMaximumSize(new Dimension(widthWin, heightWin));
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                logger.info("user closed the main frame and left the program");
                System.exit(0);
            }
        });
        mainFrame.add(northPanel);
        mainFrame.add(midPanel);
        mainFrame.add(southPanel);
        mainFrame.setTitle("Currency converter");
        // TableFrame
        tableFrame.add(tablePanel);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int heightTable = 310, widthTable = 1400;
        tableFrame.setSize(widthTable,heightTable);
        tableFrame.setMinimumSize(new Dimension(widthTable, heightTable));
        tableFrame.setMaximumSize(new Dimension(widthTable, heightTable));
        tableFrame.pack();
        //Buttons
        switchButton.setIcon(new ImageIcon("graphics/swap_32.png"));
        switchButton.setPreferredSize(new Dimension(50,50));
        switchButton.setToolTipText("Swap currencies");
        //Listeners
        fromBox.addActionListener(this);
        toBox.addActionListener(this);
        switchButton.addActionListener(this);
        convertButton.addActionListener(this);
        tableButton.addActionListener(this);
        convertButton.setPreferredSize(new Dimension(500,50));
        tableButton.setPreferredSize(new Dimension(70,50));
        // Text-Box
        toText.setEditable(false);
        fromText.setFont(new Font("Halvetica",Font.BOLD,14));
        toText.setFont(new Font("Halvetica",Font.BOLD,14));
        fromText.setHorizontalAlignment(JTextField.CENTER);
        toText.setHorizontalAlignment(JTextField.CENTER);
        fromText.requestFocus();
    }

    /**
     * Creates the list of currency labels and flags to be shown as a dropdown menu in a comboBox
     * @see #createIconMap
     * @see #createIconMap
     * @param list the comboBox component
     */

    private void initLists(JComboBox list) {
        list.setFont(new Font("Halvetica", Font.PLAIN, 14));
        list.setSelectedIndex(index);
        index = 0;
        list.setRenderer(new IconListRenderer(createIconMap()));
        list.setUI(new MetalComboBoxUI());
    }

    /**
     * Pairs labels with flag icons and creates
     * @see #initLists
     * @return a key, value map that holds the pairs
     */

    private Map<String,Icon> createIconMap() {
        Map<String,Icon> map = new HashMap<String, Icon>();
        int i=0;
        for (String s: countries) {
            Icon ic = new ImageIcon("flag/"+s+".png");
            map.put(labels[i++],ic);
        }
        return map;
    }

    /**
     * Makes the call to initiate the client GUI and sets it to visible
     * @see #initForm
     */

    public void start (){
        initForm();
        convertButtonEvent();
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * Calls for a Swap between source and result labels if a click on the switch button event took place
     * Calls for recalculation of the currency rate according to the new source-result set
     * @see #convertButtonEvent
     * @param evt
     */

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object eventSource = evt.getSource();
        if(eventSource == switchButton) {
            int fromIndex = fromBox.getSelectedIndex();
            int toIndex =   toBox.getSelectedIndex();
            fromBox.setSelectedIndex(toIndex);
            toBox.setSelectedIndex(fromIndex);
            convertButtonEvent();
            logger.info("user clicked on switch button");
        }
        else if(eventSource == fromBox) {
            logger.info("user choose " + fromBox.getSelectedItem() + "as currency source");
            convertButtonEvent();
        }
        else if(eventSource == toBox) {
            logger.info("user choose " + toBox.getSelectedItem() + "as currency destination");
            convertButtonEvent();
        }
        else if(eventSource == convertButton) {
            logger.info("user clicked the convert button");
            convertButtonEvent();
        } else if (eventSource == tableButton) {
            tableFrame.setVisible(true);
        }
    }

    /**
     * Calls a rate calculator with the chosen source and destination
     */

    void convertButtonEvent() {
        int fromIndex = fromBox.getSelectedIndex();
        int toIndex =   toBox.getSelectedIndex();
        String fromCountry = countries[fromIndex];
        String toCountry = countries[toIndex];
        double valToConvert = Double.parseDouble(fromText.getText());
        double res = calc.calcRate(fromCountry, toCountry, valToConvert);
        toText.setText(new DecimalFormat("#0.00").format(res));
    }

    @Override
    public void consume(String msg) {
        JOptionPane.showMessageDialog(new JPanel(),msg);
    }
}
