//package com.all4land.generator.ui.tab.ais.editor;
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.AbstractCellEditor;
//import javax.swing.JButton;
//import javax.swing.JColorChooser;
//import javax.swing.JTable;
//import javax.swing.table.TableCellEditor;
//import javax.swing.table.TableCellRenderer;
//
//public class MmsiColorButtonEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
//    //
//	private static final long serialVersionUID = 6991092000648410935L;
//	private JButton colorButton;
//    private Color selectedColor;
//
//    public MmsiColorButtonEditor() {
//        colorButton = new JButton();
//        colorButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                selectedColor = JColorChooser.showDialog(colorButton, "Choose Color", selectedColor);
//                fireEditingStopped();
//            }
//        });
//    }
//
//    @Override
//    public Object getCellEditorValue() {
//        return selectedColor;
//    }
//
//    @Override
//    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        selectedColor = (Color) value;
//        colorButton.setBackground(selectedColor);
//        return colorButton;
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        selectedColor = JColorChooser.showDialog(colorButton, "Choose Color", selectedColor);
//        fireEditingStopped();
//    }
//
//    @Override
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        JButton button = new JButton();
//        Color cellColor = (Color) value;
//        button.setBackground(cellColor);
//        return button;
//    }
//}