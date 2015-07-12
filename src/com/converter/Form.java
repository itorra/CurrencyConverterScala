package com.converter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ido on 26/06/15.
 */
public class Form {

    private JFrame mainFrame;
    private JPanel westPanel;
    private JPanel eastPanel;
    private JPanel midPanel;
    private JTextField fromText;
    private JTextField toText;
    private JButton convertButton;
    private JButton switchButtaon;
    private JComboBox toBox;
    private JComboBox fromBox;
    private DefaultListModel currencies;


    public Form() {
        String countries[] = {"Australia","Great Britain","Norway","Canada","Israel","South Africa","Denmark","Japan","Sweden","Egypt","Jordan","Switzerland","Euro","Lebanon","USA"};
        mainFrame = new JFrame();
        westPanel = new JPanel();
        eastPanel = new JPanel();
        midPanel = new JPanel();
        toBox = new JComboBox(countries);
        fromBox = new JComboBox(countries);
        fromText = new JTextField("1");
        toText = new JTextField();
        convertButton = new JButton("Convert");
        switchButtaon = new JButton("Switch");
    }

    private void initForm() {
        //// Init Panels
        // East
        initLists(toBox);
        eastPanel.setLayout(new FlowLayout());
        eastPanel.add(toBox);
        // West
        initLists(fromBox);
        westPanel.setLayout(new FlowLayout());
        westPanel.add(fromBox);
        // Mid
        midPanel.setLayout(new GridLayout(2,2));
        midPanel.add(fromText);
        midPanel.add(toText);
        midPanel.add(convertButton);
        midPanel.add(switchButtaon);

        //// Init MainFrame
        mainFrame.setSize(400, 400);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        mainFrame.add(BorderLayout.WEST,westPanel);
        mainFrame.add(BorderLayout.EAST,eastPanel);
        mainFrame.add(BorderLayout.CENTER,midPanel);
    }

    private void initLists(JComboBox list) {
        Dimension size = list.getMinimumSize();
        size.height = 40;
        System.out.println(size.height + "  " + size.width);
        list.setMinimumSize(size);
        list.setFont(new Font("Halvetica", Font.PLAIN, 22));
        list.setSelectedIndex(0);
        list.setRenderer(new IconListRenderer(createIconMap()));
    }


    private Map<String,Icon> createIconMap() {
        Map<String,Icon> map = new HashMap<String, Icon>();
        String countries[] = {"Australia","Great Britain","Norway","Canada","Israel","South Africa","Denmark","Japan","Sweden","Egypt","Jordan","Switzerland","Euro","Lebanon","USA"};
        for (String s: countries) {
            Icon i = new ImageIcon("flag/"+s+".png");
            map.put(s,i);
        }
        return map;
    }


//    private Object[] prepareList() {
//        currencies = new DefaultListModel();
//        ImageIcon eur = new ImageIcon("graphics/euro.gif");
//        JLabel l = new JLabel(eur);
//        currencies.addElement("USD");
//        currencies.addElement(l);
//        currencies.addElement("YEN");
//        currencies.addElement("GBP");

//        Icon dollarI = new ImageIcon("graphics/Dollar.gif");
//        Icon euroI = new ImageIcon("graphics/Euro.gif");
//
//        JLabel dollarL = new JLabel("USD", dollarI, JLabel.LEFT);
//        JLabel euroL = new JLabel("EUR",euroI, JLabel.LEFT);
//
//        JPanel dollarP = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        JPanel euroP = new JPanel(new FlowLayout(FlowLayout.LEFT));
//
//        dollarP.add(dollarL);
//        euroP.add(euroL);
//
//        Object[] panels = {dollarP, euroP};
//
//
//        return panels;
//    }

    public void start (){
        initForm();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        Form f = new Form();
        f.start();
    }
}
