package com.nsone.generator.ui.tab.ais.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class BoldTextHeaderRenderer extends DefaultTableCellRenderer {
    //
	private static final long serialVersionUID = -4951629302950063378L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		//
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 텍스트를 볼드체로 설정
        label.setFont(label.getFont().deriveFont(Font.BOLD));

        // 배경 색상을 블루로 설정
        label.setBackground(new Color(69, 68, 107)); //new Color(0, 122, 255)
        label.setForeground(Color.WHITE); // 텍스트 색상을 화이트로 설정 (선택적)

        // 텍스트를 가운데 정렬
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        return label;
    }
}