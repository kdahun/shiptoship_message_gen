package com.nsone.generator.ui.util;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.nsone.generator.system.constant.SystemConstMessage;
import com.nsone.generator.ui.tab.ais.renderer.CustomTableCellRenderer;

public class JTableUtil {
	//
//	public static void changeCellColors(JTable table, int findRow, int findColumn, Color color) {
//		//
//		CustomTableCellRenderer renderer = (CustomTableCellRenderer) table.getDefaultRenderer(Object.class);
//		renderer.setCellColor(findRow, findColumn, color);
//		table.repaint();
//	}
	
//	public static void setFontSize(JTable table, int fontSize) {
//		//
//		CustomTableCellRenderer renderer = (CustomTableCellRenderer) table.getDefaultRenderer(Object.class);
//		renderer.setFontSize(fontSize);
//		table.repaint();
//	}
	
//	public static void makeDefaultCellData(JTable jTable, boolean legType) {
//		//
//		CustomTableCellRenderer renderer = (CustomTableCellRenderer) jTable.getDefaultRenderer(Object.class);
//		int term = 0;
//		for(int aa = 0 ; aa < 13; aa++) {
//    		//
//    		for (int a = 0; a < 6; a++) {
//                //
//                for (int i = 0; i < 32; i++) {
//                	//
//                    if (i >= 1 && i <= 15){
//                    	//
//                    	int slotNumber = a + ((i-1)*6)+(aa*180);
//                    	int row = a+(aa*6)+term;
//                    	
//                        renderer.setInit(row, i, String.valueOf(slotNumber), SystemConstMessage.defaultCellColor);
//                        if(a == 0 && (i == 1 || i == 2 || i == 3 )) {
//                        	//
//                        	renderer.setBBSC_RAC_ASC_DSCH(row, i, String.valueOf(slotNumber), SystemConstMessage.BBSC_Color);
//                        }
//                        if(a != 0 && i == 15) {
//                        	//
//                        	renderer.setBBSC_RAC_ASC_DSCH(row, i, String.valueOf(slotNumber), SystemConstMessage.DSCH_Color);
//                        }
//                        if(a == 0 && i > 3) {
//                        	//
//                        	if(legType) {
//                        		if (i % 2 == 0) {
//                            		//짝수
//                                    renderer.setBBSC_RAC_ASC_DSCH(row, i, String.valueOf(slotNumber), SystemConstMessage.RAC_Color);
//                                }else {
//                                	//홀수
//                                	renderer.setBBSC_RAC_ASC_DSCH(row, i, String.valueOf(slotNumber), SystemConstMessage.ASC_Color);
//                                }
//                        	}else {
//                        		//
//                        		renderer.setBBSC_RAC_ASC_DSCH(row, i, String.valueOf(slotNumber), SystemConstMessage.RAC_Color);
//                        	}
//                        }
//                    } else if (i >= 17){
//                    	//
//                    	int cut = a + ((i-2)*6)+(aa*180);
//                    	int row = a+(aa*6)+term;
//                        if(cut < 2250) {
//                        	//
//                        	renderer.setInit(row, i, String.valueOf(cut), SystemConstMessage.defaultCellColor);
//                        	if(a == 0 && (i == 17 || i == 18 || i == 19 )) {
//                            	//
//                        		renderer.setBBSC_RAC_ASC_DSCH(row, i, String.valueOf(cut), SystemConstMessage.BBSC_Color);
//                            }
//                        	if(a != 0 && i == 31) {
//                            	//
//                            	renderer.setBBSC_RAC_ASC_DSCH(row, i, String.valueOf(cut), SystemConstMessage.DSCH_Color);
//                            }
//                        	if(a == 0 && i > 19) {
//                            	//
//                        		if(legType) {
//                            		if (i % 2 == 0) {
//                                		//짝수
//                                        renderer.setBBSC_RAC_ASC_DSCH(row, i, String.valueOf(cut), SystemConstMessage.RAC_Color);
//                                    }else {
//                                    	//홀수
//                                    	renderer.setBBSC_RAC_ASC_DSCH(row, i, String.valueOf(cut), SystemConstMessage.ASC_Color);
//                                    }
//                            	}else {
//                            		//
//                            		renderer.setBBSC_RAC_ASC_DSCH(row, i, String.valueOf(cut), SystemConstMessage.RAC_Color);
//                            	}
//                            }
//                        }
//                    }
//                }
//            }
//    		term =  term +1;
//    	}
//	}
	
//	public static void defaultBBSCColor(JTable targetTable) {
//		//
//		int[] rows = {0, 7, 14, 21, 28, 35, 42, 49, 56, 63, 70, 77, 84};
//	    int[] columns = {1, 2, 3, 17, 18, 19};
//
//	    for (int row : rows) {
//	        for (int column : columns) {
//	        	//
//	        	if (row == 84 && !(column >= 1 && column <= 3)) {
//	                continue; // Skip columns other than 1, 2, and 3 when row is 84
//	            }
//	            JTableUtil.changeCellColors(targetTable, row, column, SystemConstMessage.BBSC_Color);
//	        }
//	    }
//	}
	
//	public static void defaultTooltip(JTable targetTable) {
//		//
//		CustomTableCellRenderer renderer = (CustomTableCellRenderer) targetTable.getDefaultRenderer(Object.class);
//		renderer.setCellTooltip(0, 1, "1");
//		renderer.setCellTooltip(0, 2, "6");
//		
//		for(int aa = 0 ; aa < 13; aa++) {
//    		//
//    		for (int a = 0; a < 6; a++) {
////                Object[] rowData = new Object[33];
//                for (int i = 0; i < 32; i++) {
//                	//
//                	int cut = a + ((i-2)*6)+(aa*180);
//                    if (i == 0) {
//                    	//
////                    	rowData[i] = "TDMA" + a;
//                    } else if (i == 16){
//                    	//
//                    	if(aa < 12) {
////                    		rowData[i] = "TDMA" + a;
//                    	}
//                    	
//                    } else if (i >= 1 && i <= 15){
//                    	//
////                        rowData[i] = a + ((i-1)*6)+(aa*180);
//                    	renderer.setCellTooltip(i*aa, i, String.valueOf(i));
//                    } else if (i >= 17){
//                        if(cut < 2250) {
////                        	rowData[i] = cut;
//                        	renderer.setCellTooltip(i*aa, i, String.valueOf(cut));
//                        }
//                    }
//                }
////                tableModel.addRow(rowData);
//            }
//    		if(aa < 12) {
//    			//
////    			tableModel.addRow(new Object[32]);
//    		}
//            
//    	}
//		
//	}
}
