//package com.all4land.generator.ui.tab.ais.editor;
//
//import java.awt.Component;
//
//import javax.swing.AbstractCellEditor;
//import javax.swing.JButton;
//import javax.swing.JTable;
//import javax.swing.table.TableCellEditor;
//
//public class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor {
//    private JButton button;
//
//    public ButtonCellEditor() {
//        this.button = new JButton("클릭");
//        this.button.addActionListener(e -> fireEditingStopped());
//    }
//
//    @Override
//    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        return button;
//    }
//
//    @Override
//    public Object getCellEditorValue() {
//        return null;
//    }
//}