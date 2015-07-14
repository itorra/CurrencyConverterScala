package com.converter;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by ido on 26/06/15.
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
//        label.setPreferredSize(new Dimension(200,35));
        Icon icon = icons.get(value);
        label.setIcon(icon);
        return label;
    }
}
