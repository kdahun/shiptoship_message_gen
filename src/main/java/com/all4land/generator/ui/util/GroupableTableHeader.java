package com.all4land.generator.ui.util;


import javax.swing.plaf.TableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.all4land.generator.ui.util.GroupableColumnModel.IColumnGroup;

/**
 *
 * @author shane
 */
public class GroupableTableHeader extends JTableHeader {

	private IColumnGroup draggedGroup;
	
	public GroupableTableHeader(TableColumnModel model) {
		super(model);
		super.setUI(new GroupableTableHeaderUI());
		setReorderingAllowed(true);
	}

	@Override
	public void setUI(TableHeaderUI ui) {
	}

	public void setDraggedGroup(IColumnGroup columnGroup) {
		
		draggedGroup = columnGroup;
		
	}

	public IColumnGroup getDraggedGroup() {

		return draggedGroup;
		
	}
	
}