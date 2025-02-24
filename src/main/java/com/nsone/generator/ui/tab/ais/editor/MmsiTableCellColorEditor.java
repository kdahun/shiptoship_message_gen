//package com.nsone.generator.ui.tab.ais.editor;
//
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Random;
//
//import javax.swing.DefaultCellEditor;
//import javax.swing.JButton;
//import javax.swing.JTable;
//import javax.swing.JTextField;
//
//import com.nsone.generator.ui.tab.ais.entity.MmsiEntity;
//import com.nsone.generator.ui.tab.ais.model.MmsiTableModel2;
//
//public class MmsiTableCellColorEditor extends DefaultCellEditor {
//	
//	private static final long serialVersionUID = 1L;
//	//
//    private JButton button;
//    private Color color;
//
//    public MmsiTableCellColorEditor() {
//    	//
//        super(new JTextField());
//        button = new JButton();
//        button.setOpaque(true);
//        button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	//
//            	// 랜덤한 RGB 값으로 Color 객체 생성
//            	//Random random = new Random();
//                //color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
////                button.setBackground(color);
//                
//                // 색상 열에 해당하는 MmsiEntity의 color 속성 업데이트
//                JTable table = (JTable) e.getSource();
//                MmsiTableModel2 tableModel = (MmsiTableModel2) table.getModel();
//                int row = table.getEditingRow();
//                MmsiEntity mmsiEntity = tableModel.getMmsiEntityLists().get(row);
////                mmsiEntity.setColor(color);
//                tableModel.fireTableCellUpdated(row, table.getEditingColumn());
//                
//            }
//        });
//    }
//
//    @Override
//    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        return button;
//    }
//}