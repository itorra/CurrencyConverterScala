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

public class Form implements ActionListener{
    static Logger logger = Logger.getLogger("FormLogger.props");
    private String countries[];
    private String labels[];
    private JFrame mainFrame;
    private JPanel midPanel;
    private JPanel northPanel;
    private JPanel southPanel;
    private JTextField fromText;
    private JTextField toText;
    private JButton convertButton;
    private JButton switchButton;
    private JComboBox<String> toBox;
    private JComboBox<String> fromBox;
    private BOIParser parser;
    private IRateCalculatorModel calc;
    private int index = 11;

    /**
     * Sole client GUI constructor
     */

    protected Form() {
        parser = new BOIParser();
//        Thread t1 = new Thread(parser);
//        t1.start();
        parser.downloadFile();
        calc = new RateCalculator(parser.buildMap());
        countries = parser.getCountries();
        labels = parser.getLabels();
        mainFrame = new JFrame();
        northPanel = new JPanel();
        southPanel = new JPanel();
        midPanel = new JPanel();
        toBox = new JComboBox(labels);
        fromBox = new JComboBox(labels);
        fromText = new JTextField("1.0");
        toText = new JTextField();
        convertButton = new JButton("Convert");
        switchButton = new JButton();
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
        southPanel.setLayout(new GridLayout(1, 3));
        southPanel.add(convertButton);
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
        //Buttons
        switchButton.setIcon(new ImageIcon("graphics/swap_32.png"));
        switchButton.setPreferredSize(new Dimension(32, 32));
        switchButton.setPreferredSize(new Dimension(50,50));
        switchButton.setToolTipText("Swap currencies");
        //Listeners
        fromBox.addActionListener(this);
        toBox.addActionListener(this);
        switchButton.addActionListener(this);
        convertButton.addActionListener(this);
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
        mainFrame.setVisible(true);
    }

    /**
     * Instantiates a Form object and initiates the program
     */

    public static void main(String[] args) {
        Form f = new Form();
        BasicConfigurator.configure(); // set formLogger configuration to default
        logger.info("client GUI was created successfully");
        f.start();
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
}
