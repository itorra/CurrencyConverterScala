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
 *
 * @author Ido Algom
 * @author Dassi Rosen
 * @version 20 Jul 2015
 */

public class View implements ActionListener, MessageConsumer {
    static Logger viewLogger = Logger.getLogger(View.class.getName());
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

    protected View(IRateCalculatorModel calcModel, BOIParser boiParser) {
        this.calc = calcModel;
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        countries = boiParser.getCountries();
        labels = boiParser.getLabels();
        mainFrame = new JFrame();
        tableFrame = new JFrame();
        northPanel = new JPanel();
        midPanel = new JPanel();
        tablePanel = new JPanel();
        southPanel = new JPanel();
        initTable(labels.length);
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
        tableButton.setToolTipText("Click here to see the full rate table");
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
        tablePane.setPreferredSize(new Dimension(tableDimension.width, rateTable.getRowHeight() * labels.length));
        tablePanel.add(tablePane);
        //// Init MainFrame
        int heightWin = 200, widthWin = 570;
        mainFrame.setSize(widthWin, heightWin);
        mainFrame.setMinimumSize(new Dimension(widthWin, heightWin));
        mainFrame.setMaximumSize(new Dimension(widthWin, heightWin));
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                viewLogger.info("user closed the main frame and left the program");
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
        tableFrame.setSize(widthTable, heightTable);
        tableFrame.setMinimumSize(new Dimension(widthTable, heightTable));
        tableFrame.setMaximumSize(new Dimension(widthTable, heightTable));
        tableFrame.pack();
        //Buttons
        switchButton.setIcon(new ImageIcon("graphics/swap_32.png"));
        switchButton.setPreferredSize(new Dimension(50, 50));
        switchButton.setToolTipText("Swap currencies");
        //Listeners
        fromBox.addActionListener(this);
        toBox.addActionListener(this);
        switchButton.addActionListener(this);
        convertButton.addActionListener(this);
        tableButton.addActionListener(this);
        convertButton.setPreferredSize(new Dimension(500, 50));
        tableButton.setPreferredSize(new Dimension(70, 50));
        // Text-Box
        toText.setEditable(false);
        fromText.setFont(new Font("Halvetica", Font.BOLD, 14));
        toText.setFont(new Font("Halvetica", Font.BOLD, 14));
        fromText.setHorizontalAlignment(JTextField.CENTER);
        toText.setHorizontalAlignment(JTextField.CENTER);
        fromText.requestFocus();
    }

    /**
     * Initialize JTable values
     */

    private void initTable(int size) {
        //create rate table
        String[] cols = new String[size+1];
        cols[0] = "";
        for (int i = 1; i < size+1; i++) {
            cols[i] = labels[i - 1];
        }
        double[][] rateMatrix = calc.getMatrix();
        String[][] dataTable = new String[size][size+1];
        for (int i = 0; i < rateMatrix.length; i++) {
            dataTable[i][0] = labels[i];
            for (int j = 0; j < rateMatrix.length; j++) {
                dataTable[i][j + 1] = new DecimalFormat("#0.0000").format(rateMatrix[i][j]);
            }
        }
        rateTable = new JTable(dataTable, cols);
    }

    /**
     * Creates the list of currency labels and flags to be shown as a dropdown menu in a comboBox
     *
     * @param list the comboBox component
     * @see #createIconMap
     * @see #createIconMap
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
     *
     * @return a key, value map that holds the pairs
     * @see #initLists
     */

    private Map<String, Icon> createIconMap() {
        Map<String, Icon> map = new HashMap<String, Icon>();
        int i = 0;
        for (String s : countries) {
            Icon ic = new ImageIcon("flag/" + s + ".png");
            map.put(labels[i++], ic);
        }
        return map;
    }

    /**
     * Makes the call to initiate the client GUI and sets it to visible
     *
     * @see #initForm
     */

    public void start() {
        initForm();
        convert();
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * Handles GUI events triggered by client interaction with the application
     * @param evt
     * @see #convert
     */

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object eventSource = evt.getSource();
        if (eventSource == switchButton) {
            int fromIndex = fromBox.getSelectedIndex();
            int toIndex = toBox.getSelectedIndex();
            fromBox.setSelectedIndex(toIndex);
            toBox.setSelectedIndex(fromIndex);
            convert();
            viewLogger.info("user clicked on switch button");
        } else if (eventSource == fromBox) {
            viewLogger.info("user chose to convert from " + fromBox.getSelectedItem());
            convert();
        } else if (eventSource == toBox) {
            viewLogger.info("user chose to convert to " + toBox.getSelectedItem());
            convert();
        } else if (eventSource == convertButton) {
            viewLogger.info("user clicked convert button");
            convert();
        } else if (eventSource == tableButton) {
            viewLogger.info("user clicked table button");
            tableFrame.setVisible(true);
        }
    }

    /**
     * Calls a rate calculator with the chosen source and destination
     */

    void convert() {
        int fromIndex = fromBox.getSelectedIndex();
        int toIndex = toBox.getSelectedIndex();
        String fromCountry = countries[fromIndex];
        String toCountry = countries[toIndex];
        double valToConvert = Double.parseDouble(fromText.getText());
        viewLogger.info("Converting " + valToConvert +" " + labels[fromIndex] + " to " + labels[toIndex] );
        double res = calc.calcRate(fromCountry, toCountry, valToConvert);
        toText.setText(new DecimalFormat("#0.00").format(res));
    }

    /**
     * Shows messages sent from the logic of the application in a popup dialog
     */
    @Override
    public void consume(String msg) {
        JOptionPane.showMessageDialog(new JPanel(), msg);
    }
}
