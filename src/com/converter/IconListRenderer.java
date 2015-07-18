package com.converter;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * The IconListRenderer class is used to create the Map of label keys, icon values
 * @version 20 Jul 2015
 * @author Ido Algom
 * @author Dassi Rosen
 */

public class IconListRenderer extends DefaultListCellRenderer {

    private Map<String,Icon> icons = null;
    public IconListRenderer(Map<String, Icon> icons) {
        this.icons = icons;
    }

    @Override
    public Component getListCellRendererComponent
            (JList list, Object value, int index,boolean isSelected,boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
        label.setPreferredSize(new Dimension(170,38));
        Icon icon = icons.get(value);
        label.setIcon(icon);
        return label;
    }
}