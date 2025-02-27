package com.all4land.generator.ui.tab.ais.editor;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class MmsiTableCellEditor extends DefaultCellEditor {
	
	private static final long serialVersionUID = 1L;

	public MmsiTableCellEditor(JTextField textField, boolean isEnabled) {
		//
		super(textField);
		textField.setEnabled(isEnabled); // 편집 활성화 여부 설정
	}

	public MmsiTableCellEditor(JComboBox<?> comboBox, boolean isEnabled) {
		//
		super(new JCheckBox());
		//setClickCountToStart(1); // 싱글 클릭으로 편집 시작
		comboBox.setEnabled(isEnabled);
		((JCheckBox) editorComponent).setEnabled(isEnabled);

		// 콤보박스의 선택 상태를 편집값으로 설정
		editorComponent = comboBox;
		delegate = new EditorDelegate() {
			//
			private static final long serialVersionUID = 1L;

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