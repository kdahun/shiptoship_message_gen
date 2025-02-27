package com.all4land.generator.ui.tab.ais.renderer;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorButtonCellRenderer extends DefaultTableCellRenderer {
	//
    private JButton button;
    
    public ColorButtonCellRenderer() {
        this.button = new JButton("");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return button;
    }
}