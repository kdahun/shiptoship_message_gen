package com.all4land.generator.ui.tab.ais.model;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.springframework.stereotype.Component;

import com.all4land.generator.ui.tab.ais.entity.GlobalEntityManager;
import com.all4land.generator.ui.tab.ais.entity.MmsiEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MmsiTableModel extends AbstractTableModel {
	//
	private static final long serialVersionUID = -4704374296534917846L;

	//
	private static final String[] columnNames = new String[]{"", "mmsi", "ASM", "VDE"};

    private List<MmsiEntity> mmsiEntityLists;
    private GlobalEntityManager globalEntityManager;

	public MmsiTableModel() {
	}
    public GlobalEntityManager getGlobalEntityManager() {
		return globalEntityManager;
	}

	public void setGlobalEntityManager(GlobalEntityManager globalEntityManager) {
		this.globalEntityManager = globalEntityManager;
		this.mmsiEntityLists = this.globalEntityManager.getMmsiEntityLists();
	}

	@Override
    public int getRowCount() {
        return mmsiEntityLists.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MmsiEntity mmsiEntity = mmsiEntityLists.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return mmsiEntity.isChk();
            case 1:
                return mmsiEntity.getMmsi();
            case 2:
                return mmsiEntity.isAsm();
            case 3:
                return mmsiEntity.isVde();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // 원하는 컬럼 번호를 지정하여 해당 컬럼만 수정 가능하도록 설정
        return columnIndex == 0 || columnIndex == 2 || columnIndex == 3;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 1:
                return Long.class;
            case 2:
                return Boolean.class;
            case 3:
                return Boolean.class;
            default:
                return Object.class;
        }
    }

    public void setData() {
    	//
        this.mmsiEntityLists = this.globalEntityManager.getMmsiEntityLists();
        fireTableDataChanged();
    }

	public List<MmsiEntity> getMmsiEntityLists() {
		// TODO Auto-generated method stub
		return this.mmsiEntityLists;
	}
	
	public MmsiEntity getMmsiEntity(int row) {
		// TODO Auto-generated method stub
		return this.mmsiEntityLists.get(row);
	}
	
	@Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            // 체크박스 컬럼의 값 변경 처리
            mmsiEntityLists.get(rowIndex).setChk((Boolean) value);
        } else if (columnIndex == 1) {
            // mmsi 컬럼의 값 변경 처리
            mmsiEntityLists.get(rowIndex).setMmsi((Long) value);
        } else if (columnIndex == 2) {
            // ASM
        	mmsiEntityLists.get(rowIndex).setAsm((Boolean) value);
        } else if (columnIndex == 3) {
            // VDE
        	mmsiEntityLists.get(rowIndex).setVde((Boolean) value);
        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }
	
}
