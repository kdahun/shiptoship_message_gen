package com.nsone.generator.ui.tab.ais.editor;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CellEditor extends DefaultCellEditor {
	//
	private static final long serialVersionUID = -4990770406933196348L;
//	private JCheckBox checkBox;
	
	public CellEditor(JTextField textField, boolean isEnabled) {
		//
		super(textField);
//		textField.setEnabled(isEnabled); // 편집 활성화 여부 설정
	}
	
	public CellEditor(JCheckBox checkBox, boolean isEnabled) {
		//
		super(new JCheckBox());
//		checkBox.setEnabled(isEnabled);
        checkBox = (JCheckBox) editorComponent;
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.addActionListener(e -> fireEditingStopped());
	}


	public CellEditor(JComboBox<?> comboBox, boolean isEnabled) {
		//
		super(new JCheckBox());
//		comboBox.setEnabled(isEnabled);
		((JCheckBox) editorComponent).setEnabled(isEnabled);

		// 콤보박스의 선택 상태를 편집값으로 설정
		editorComponent = comboBox;
		delegate = new EditorDelegate() {
			//
			private static final long serialVersionUID = -8524864442112998401L;

			@Override
			public void setValue(Object value) {
				comboBox.setSelectedItem(value);
			}

			@Override
			public Object getCellEditorValue() {
				return comboBox.getSelectedItem();
			}
		};
	}
}
