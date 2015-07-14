package com.converter;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ido on 26/06/15.
 */
public class Form implements ActionListener{
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
    private RateCalculatable calc;
    private int index = 0;

    public Form() {
        parser = new BOIParser();
        calc = new RateCalculator(parser.buildMap());
        countries = parser.getCountries();
        labels = parser.getLabels();
        mainFrame = new JFrame();
        northPanel = new JPanel();
        southPanel = new JPanel();
        midPanel = new JPanel();
        toBox = new JComboBox(labels);
        fromBox = new JComboBox(labels);
        fromText = new JTextField("1");
        toText = new JTextField();
        convertButton = new JButton("Convert");
        switchButton = new JButton();
    }

    private void initForm() {
        //Init comboBoxes
        initLists(fromBox);
        initLists(toBox);
        //North
        northPanel.setLayout(new GridLayout(1, 3));
        northPanel.setPreferredSize(new Dimension(650, 35));
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
        mainFrame.setSize(650, 300);
        mainFrame.setMinimumSize(new Dimension(650, 300));
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        mainFrame.add(northPanel);
        mainFrame.add(midPanel);
        mainFrame.add(southPanel);
        //Buttons
        switchButton.setIcon(new ImageIcon("graphics/swap_32.png"));
        switchButton.setPreferredSize(new Dimension(32, 32));
        //Listeners
        fromBox.addActionListener(this);
        toBox.addActionListener(this);
        switchButton.addActionListener(this);
        convertButton.addActionListener(this);
        // Text-Box
        toText.setEditable(false);
    }

    private void initLists(JComboBox list) {
        list.setFont(new Font("Halvetica", Font.PLAIN, 18));
        list.setSelectedIndex(index);
        index = 11;
        list.setRenderer(new IconListRenderer(createIconMap()));
        list.setUI(new MetalComboBoxUI());
    }


    private Map<String,Icon> createIconMap() {
        Map<String,Icon> map = new HashMap<String, Icon>();
        int i=0;
        for (String s: countries) {
            Icon ic = new ImageIcon("flag/"+s+".png");
            map.put(labels[i++],ic);
        }
        return map;
    }



    public void start (){
        initForm();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        Form f = new Form();
        f.start();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object eventSource = evt.getSource();
        if(eventSource == switchButton) {
            //TODO: automate convert
            int fromIndex = fromBox.getSelectedIndex();
            int toIndex =   toBox.getSelectedIndex();
            fromBox.setSelectedIndex(toIndex);
            toBox.setSelectedIndex(fromIndex);
            convertButtonEvent();
        }
        else if(eventSource == convertButton) {
            convertButtonEvent();
        }
    }

    void convertButtonEvent() {
        int fromIndex = fromBox.getSelectedIndex();
        int toIndex =   toBox.getSelectedIndex();
        String fromCountry = countries[fromIndex];
        String toCountry = countries[toIndex];
        double valToConvert = Double.parseDouble(fromText.getText());
        double res = calc.calcRate(fromCountry, toCountry, valToConvert);
        System.out.println(valToConvert + "  " + fromCountry + "  to " + toCountry + "  " + res);
        toText.setText(new DecimalFormat("#0.00").format(res));
    }
}
