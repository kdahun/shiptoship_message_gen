package com.nsone.generator.ui.tab.ais.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.nsone.generator.system.constant.SystemConstMessage;
import com.nsone.generator.system.util.StringMessageMaker;
import com.nsone.generator.ui.tab.ais.entity.AisCellInfos;
import com.nsone.generator.ui.tab.ais.entity.AsmCellInfos;
import com.nsone.generator.ui.tab.ais.entity.CellInfos;
import com.nsone.generator.ui.tab.ais.entity.MmsiEntity;
import com.nsone.generator.ui.tab.ais.entity.TdmaInfoEntity;
import com.nsone.generator.ui.tab.ais.entity.VdeLowerLegCellInfos;
import com.nsone.generator.ui.tab.ais.entity.VdeUpperLegCellInfos;
import com.nsone.generator.ui.util.MapUtil;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    //
	private static final long serialVersionUID = -7835298844708187784L;
	private int fontSize = 9; // 기본 폰트 사이즈
	private boolean legType;
	private int tdmaChannelSumValue = 0;
	private Map<Point, CellInfos> cellInfos = new LinkedHashMap<>();
	
	// 셀의 기본 정보가 담긴 맵 을 클리어하기위한 기본 맵
	private Map<Point, CellInfos> cellInfosDefault = new LinkedHashMap<>();
	
	private Map<Integer, Point> cellInfosBySlotNumber = new LinkedHashMap<>();
	
	private Map<Integer, Integer> tdmaFrameIndexMap = new TreeMap<>();
	private Map<TdmaInfoEntity, Integer[]> tdmaInfos = new LinkedHashMap<>();
	
	private boolean aisMsgDisplay = true;
	private boolean asmMsgDisplay = true;
	private boolean vdeMsgDisplay = true;
	
	int[] tdma0_1 = generateArray(1);
    int[] tdma0_2 = generateArray(2);
    int[] tdma0_3 = generateArray(3);
    int[] tdma0_4 = generateArray(4);
    int[] tdma0_5 = generateArray(5);
    
    int[] tdma1_1 = generateArray(91);
    int[] tdma1_2 = generateArray(92);
    int[] tdma1_3 = generateArray(93);
    int[] tdma1_4 = generateArray(94);
    int[] tdma1_5 = generateArray(95);
    
    int[] tdma2_1 = generateArray(181);
    int[] tdma2_2 = generateArray(182);
    int[] tdma2_3 = generateArray(183);
    int[] tdma2_4 = generateArray(184);
    int[] tdma2_5 = generateArray(185);
    
    int[] tdma3_1 = generateArray(271);
    int[] tdma3_2 = generateArray(272);
    int[] tdma3_3 = generateArray(273);
    int[] tdma3_4 = generateArray(274);
    int[] tdma3_5 = generateArray(275);
    
    int[] tdma4_1 = generateArray(361);
    int[] tdma4_2 = generateArray(362);
    int[] tdma4_3 = generateArray(363);
    int[] tdma4_4 = generateArray(364);
    int[] tdma4_5 = generateArray(365);
    
    int[] tdma5_1 = generateArray(451);
    int[] tdma5_2 = generateArray(452);
    int[] tdma5_3 = generateArray(453);
    int[] tdma5_4 = generateArray(454);
    int[] tdma5_5 = generateArray(455);
    

    int[] tdma6_1 = generateArray(541);
    int[] tdma6_2 = generateArray(542);
    int[] tdma6_3 = generateArray(543);
    int[] tdma6_4 = generateArray(544);
    int[] tdma6_5 = generateArray(545);
    
    int[] tdma7_1 = generateArray(631);
    int[] tdma7_2 = generateArray(632);
    int[] tdma7_3 = generateArray(633);
    int[] tdma7_4 = generateArray(634);
    int[] tdma7_5 = generateArray(635);
    
    int[] tdma8_1 = generateArray(721);
    int[] tdma8_2 = generateArray(722);
    int[] tdma8_3 = generateArray(723);
    int[] tdma8_4 = generateArray(724);
    int[] tdma8_5 = generateArray(725);
    
    int[] tdma9_1 = generateArray(811);
    int[] tdma9_2 = generateArray(812);
    int[] tdma9_3 = generateArray(813);
    int[] tdma9_4 = generateArray(814);
    int[] tdma9_5 = generateArray(815);
    

    int[] tdma10_1 = generateArray(901);
    int[] tdma10_2 = generateArray(902);
    int[] tdma10_3 = generateArray(903);
    int[] tdma10_4 = generateArray(904);
    int[] tdma10_5 = generateArray(905);
    
    int[] tdma11_1 = generateArray(991);
    int[] tdma11_2 = generateArray(992);
    int[] tdma11_3 = generateArray(993);
    int[] tdma11_4 = generateArray(994);
    int[] tdma11_5 = generateArray(995);
    
    int[] tdma12_1 = generateArray(1081);
    int[] tdma12_2 = generateArray(1082);
    int[] tdma12_3 = generateArray(1083);
    int[] tdma12_4 = generateArray(1084);
    int[] tdma12_5 = generateArray(1085);
    
    int[] tdma13_1 = generateArray(1171);
    int[] tdma13_2 = generateArray(1172);
    int[] tdma13_3 = generateArray(1173);
    int[] tdma13_4 = generateArray(1174);
    int[] tdma13_5 = generateArray(1175);
    

    int[] tdma14_1 = generateArray(1261);
    int[] tdma14_2 = generateArray(1262);
    int[] tdma14_3 = generateArray(1263);
    int[] tdma14_4 = generateArray(1264);
    int[] tdma14_5 = generateArray(1265);
    
    int[] tdma15_1 = generateArray(1351);
    int[] tdma15_2 = generateArray(1352);
    int[] tdma15_3 = generateArray(1353);
    int[] tdma15_4 = generateArray(1354);
    int[] tdma15_5 = generateArray(1355);
    
    int[] tdma16_1 = generateArray(1441);
    int[] tdma16_2 = generateArray(1442);
    int[] tdma16_3 = generateArray(1443);
    int[] tdma16_4 = generateArray(1444);
    int[] tdma16_5 = generateArray(1445);
    
    int[] tdma17_1 = generateArray(1531);
    int[] tdma17_2 = generateArray(1532);
    int[] tdma17_3 = generateArray(1533);
    int[] tdma17_4 = generateArray(1534);
    int[] tdma17_5 = generateArray(1535);
    

    int[] tdma18_1 = generateArray(1621);
    int[] tdma18_2 = generateArray(1622);
    int[] tdma18_3 = generateArray(1623);
    int[] tdma18_4 = generateArray(1624);
    int[] tdma18_5 = generateArray(1625);
    
    int[] tdma19_1 = generateArray(1711);
    int[] tdma19_2 = generateArray(1712);
    int[] tdma19_3 = generateArray(1713);
    int[] tdma19_4 = generateArray(1714);
    int[] tdma19_5 = generateArray(1715);
    
    int[] tdma20_1 = generateArray(1801);
    int[] tdma20_2 = generateArray(1802);
    int[] tdma20_3 = generateArray(1803);
    int[] tdma20_4 = generateArray(1804);
    int[] tdma20_5 = generateArray(1805);
    
    int[] tdma21_1 = generateArray(1891);
    int[] tdma21_2 = generateArray(1892);
    int[] tdma21_3 = generateArray(1893);
    int[] tdma21_4 = generateArray(1894);
    int[] tdma21_5 = generateArray(1895);
    

    int[] tdma22_1 = generateArray(1981);
    int[] tdma22_2 = generateArray(1982);
    int[] tdma22_3 = generateArray(1983);
    int[] tdma22_4 = generateArray(1984);
    int[] tdma22_5 = generateArray(1985);
    
    int[] tdma23_1 = generateArray(2071);
    int[] tdma23_2 = generateArray(2072);
    int[] tdma23_3 = generateArray(2073);
    int[] tdma23_4 = generateArray(2074);
    int[] tdma23_5 = generateArray(2075);
    
    int[] tdma24_1 = generateArray(2161);
    int[] tdma24_2 = generateArray(2162);
    int[] tdma24_3 = generateArray(2163);
    int[] tdma24_4 = generateArray(2164);
    int[] tdma24_5 = generateArray(2165);
	
    int[][] tdmaArrays = {
		    tdma0_1, tdma0_2, tdma0_3, tdma0_4, tdma0_5,
		    tdma1_1, tdma1_2, tdma1_3, tdma1_4, tdma1_5,
		    tdma2_1, tdma2_2, tdma2_3, tdma2_4, tdma2_5,
		    tdma3_1, tdma3_2, tdma3_3, tdma3_4, tdma3_5,
		    tdma4_1, tdma4_2, tdma4_3, tdma4_4, tdma4_5,
		    tdma5_1, tdma5_2, tdma5_3, tdma5_4, tdma5_5,
		    tdma6_1, tdma6_2, tdma6_3, tdma6_4, tdma6_5,
		    tdma7_1, tdma7_2, tdma7_3, tdma7_4, tdma7_5,
		    tdma8_1, tdma8_2, tdma8_3, tdma8_4, tdma8_5,
		    tdma9_1, tdma9_2, tdma9_3, tdma9_4, tdma9_5,
		    tdma10_1, tdma10_2, tdma10_3, tdma10_4, tdma10_5,
		    tdma11_1, tdma11_2, tdma11_3, tdma11_4, tdma11_5,
		    tdma12_1, tdma12_2, tdma12_3, tdma12_4, tdma12_5,
		    tdma13_1, tdma13_2, tdma13_3, tdma13_4, tdma13_5,
		    tdma14_1, tdma14_2, tdma14_3, tdma14_4, tdma14_5,
		    tdma15_1, tdma15_2, tdma15_3, tdma15_4, tdma15_5,
		    tdma16_1, tdma16_2, tdma16_3, tdma16_4, tdma16_5,
		    tdma17_1, tdma17_2, tdma17_3, tdma17_4, tdma17_5,
		    tdma18_1, tdma18_2, tdma18_3, tdma18_4, tdma18_5,
		    tdma19_1, tdma19_2, tdma19_3, tdma19_4, tdma19_5,
		    tdma20_1, tdma20_2, tdma20_3, tdma20_4, tdma20_5,
		    tdma21_1, tdma21_2, tdma21_3, tdma21_4, tdma21_5,
		    tdma22_1, tdma22_2, tdma22_3, tdma22_4, tdma22_5,
		    tdma23_1, tdma23_2, tdma23_3, tdma23_4, tdma23_5,
		    tdma24_1, tdma24_2, tdma24_3, tdma24_4, tdma24_5
		};
    
    public CustomTableCellRenderer(DefaultTableModel tableModel, boolean legType, int sum) {
    	//
    	this.legType = legType; //upper , lower
    	this.tdmaChannelSumValue = sum; 
    	this.init(tableModel, legType);
    	
    	for(Entry<Point, CellInfos> cellInfo : this.cellInfos.entrySet()) {
        	//
    		cellInfosBySlotNumber.put(cellInfo.getValue().getSlotNumber(), cellInfo.getKey());
        }
    	this.initTdmaFrameMap();
    	this.initTdmaInfos();
    }

    private void initTdmaInfos() {
    	//
    	for(int i = 0; i <= 24; i++) {
    		//
    		for(int ii = 0; ii <= 5; ii++ ) {
    			//
    			TdmaInfoEntity tdma = new TdmaInfoEntity();
    			tdma.setTdmaFrame(i);
    			tdma.setTdmaChannel(ii);
    			Integer[] slotNumbers = {((i*90)+ii), ((i*90)+ii)+6, ((i*90)+ii)+12, ((i*90)+ii)+18, ((i*90)+ii)+24, ((i*90)+ii)+30, ((i*90)+ii)+36, ((i*90)+ii)+42, 
//    					((i*90)+ii)+48, ((i*90)+ii)+54, ((i*90)+ii)+60, ((i*90)+ii)+66, ((i*90)+ii)+72, ((i*90)+ii)+78, ((i*90)+ii)+84 };
    					((i*90)+ii)+48, ((i*90)+ii)+54, ((i*90)+ii)+60, ((i*90)+ii)+66, ((i*90)+ii)+72, ((i*90)+ii)+78 };
    			//System.out.println("tdmaFrame : "+i + ", tdmaChannel : " + ii+", slots : "+ Arrays.toString(slotNumbers));
    			this.tdmaInfos.put(tdma, slotNumbers);
    		}
    	}
    }
    
    public void paintResources(int tdmaFrame, int tdmaChannel) {
    	//
    	System.out.println(tdmaFrame+","+tdmaChannel);
    	TdmaInfoEntity tdma = new TdmaInfoEntity();
    	tdma.setTdmaFrame(tdmaFrame);
		tdma.setTdmaChannel(tdmaChannel);
    	Integer[] target_slots = this.tdmaInfos.get(tdma);
    	
    	for(Integer slot : target_slots) {
    		//
    		this.paintResourceCell(slot);
    	}
    }
    
    public int getTdmaFrame(int slotNumber) {
    	//
    	return this.tdmaFrameIndexMap.get(slotNumber);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	//
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Point cellKey = new Point(row, column);
        CellInfos cellInfos = this.cellInfos.get(cellKey);
        
        if(this.cellInfos.containsKey(cellKey)) {
        	if(SystemConstMessage.AIS_Color.equals(cellInfos.getColor())) {
            	//
            	if(this.aisMsgDisplay) {
            		c.setBackground(cellInfos.getColor());
            		this.setForeground(this.getContrastColor(cellInfos.getColor()));
            	}else {
            		c.setBackground(cellInfos.getDefaultColor());
            		this.setForeground(this.getContrastColor(cellInfos.getDefaultColor()));
            	}
            }else if(SystemConstMessage.ASM_Color.equals(cellInfos.getColor())) {
            	//
            	if(this.asmMsgDisplay) {
            		c.setBackground(cellInfos.getColor());
            		this.setForeground(this.getContrastColor(cellInfos.getColor()));
            	}else {
            		c.setBackground(cellInfos.getDefaultColor());
            		this.setForeground(this.getContrastColor(cellInfos.getDefaultColor()));
            	}
            }else if(SystemConstMessage.VDE_Color.equals(cellInfos.getColor()) || SystemConstMessage.ACK_Color.equals(cellInfos.getColor())) {
            	//
            	if(this.vdeMsgDisplay) {
            		c.setBackground(cellInfos.getColor());
            		this.setForeground(this.getContrastColor(cellInfos.getColor()));
            	}else {
            		c.setBackground(cellInfos.getDefaultColor());
            		this.setForeground(this.getContrastColor(cellInfos.getDefaultColor()));
            	}
            }else {
            	c.setBackground(cellInfos.getColor());
            	this.setForeground(this.getContrastColor(cellInfos.getColor()));
            }
        }else {
        	c.setBackground(SystemConstMessage.defaultCellColor);
        }
        
        
        // 툴팁 설정
        if(this.cellInfos.containsKey(cellKey)) {
        	//
        	if (cellInfos.getSlotNumber() >= 0 && cellInfos.getSlotNumber() <= 2249) {
                setToolTipText(StringMessageMaker.tooltipMessage(cellInfos));
            }else {
            	setToolTipText(null);
            }
        }else {
        	setToolTipText(null);
        }
        
        // Set font size
        c.setFont(new Font(c.getFont().getFontName(), c.getFont().getStyle(), fontSize));
        
        // 테두리 제거
        setBorder(null);
        
        // 텍스트 가운데 정렬
        setHorizontalAlignment(JLabel.CENTER);
        
        return c;
    }

    public void clearAllCellInfos() {
    	//
    	MapUtil.copy(this.cellInfos, this.cellInfosDefault);
    }
    
    public void initTdmaFrameMap() {
    	//
    	int start = 0;
    	int end = 89;
    	for(int frame = 0; frame <= 24; frame++) {
    		//
    		for(int i = start; i <= end ; i++) {
        		//
        		this.tdmaFrameIndexMap.put(i, frame);
        	}
    		start = start + 90;
    		end = start + 90;
    	}
    }
    
    public Point getCellInfosBySlotNumber(int slotNumber) {
        return cellInfosBySlotNumber.get(slotNumber);
    }
    
    // AIS used slot check
    public synchronized boolean verifyAisAllChannelIsEmpty(int row, int column) {
    	//
    	Point cellKey = new Point(row, column);
    	if(this.cellInfos.containsKey(cellKey) && this.cellInfos.get(cellKey).getAisCellInfos() != null) {
    		return true;
    	}
    	return false;
    }
    
    // ASM used slot check
    public synchronized boolean verifyASMChannelAIsEmpty(int row, int column) {
    	//
    	Point cellKey = new Point(row, column);
    	if(this.cellInfos.containsKey(cellKey) && this.cellInfos.get(cellKey).getAsmCellInfosForA() != null) {
    		return true;
    	}
    	return false;
    }
    
    public synchronized boolean verifyASMChannelBIsEmpty(int row, int column) {
    	//
    	Point cellKey = new Point(row, column);
    	if(this.cellInfos.containsKey(cellKey) && this.cellInfos.get(cellKey).getAsmCellInfosForB() != null) {
    		return true;
    	}
    	return false;
    }
    
    public synchronized boolean verifyVDEUpperIsEmpty(int row, int column) {
    	//
    	Point cellKey = new Point(row, column);
    	if(this.cellInfos.containsKey(cellKey) && this.cellInfos.get(cellKey).getVdeUpperLegCellInfos() != null) {
    		return true;
    	}
    	return false;
    }
    
    public synchronized List<Point> findVdeSlotMin(int startIndex) {
    	//
    	List<Point> points = new ArrayList<>();
    	int min = Integer.MAX_VALUE; // 최소값을 최대 정수값으로 초기화합니다.
		for (int i = 0; i < tdmaArrays.length; i++) {
		    for (int j = 0; j < tdmaArrays[i].length; j++) {
		        if (tdmaArrays[i][j] == startIndex) {
		            // Found the desired value in array tdmaArrays[i], update min if needed
		            for (int k = 0; k < tdmaArrays[i].length; k++) {
		                if (tdmaArrays[i][k] < min) {
		                    min = tdmaArrays[i][k];
		                    
		                }
		            }
		            break; // Break out of the inner loop since we found the desired value
		        }
		    }
		}
		
		for(int i = 0; i < 15 ; i++) {
			for(Entry<Point, CellInfos> cellInfo : this.cellInfos.entrySet()) {
	        	//
	        	if(cellInfo.getValue() != null && cellInfo.getValue().getSlotNumber() == min+ (i*6) && 2250 >= min+ (i*6) ) {
	        		//
	        		points.add(cellInfo.getKey());
	        	}
	        }
		}
		
		
		return points;
    }
    
    public synchronized boolean checkVdeSlot(int startIndex) {
    	//
    	boolean rtn = false;
    	int min = Integer.MAX_VALUE; // 최소값을 최대 정수값으로 초기화합니다.
		for (int i = 0; i < tdmaArrays.length; i++) {
		    for (int j = 0; j < tdmaArrays[i].length; j++) {
		        if (tdmaArrays[i][j] == startIndex) {
		            // Found the desired value in array tdmaArrays[i], update min if needed
		            for (int k = 0; k < tdmaArrays[i].length; k++) {
		                if (tdmaArrays[i][k] < min) {
		                    min = tdmaArrays[i][k];
		                }
		            }
		            break; // Break out of the inner loop since we found the desired value
		        }
		    }
		}
		
		for(int i = 0; i < 14 ; i++) {
			for(Entry<Point, CellInfos> cellInfo : this.cellInfos.entrySet()) {
	        	//
	        	if(cellInfo.getValue() != null && cellInfo.getValue().getSlotNumber() == min) {
	        		//
	        		cellInfo.getValue().getVdeLowerLegCellInfos();
	        		if(cellInfo.getValue().getVdeLowerLegCellInfos() == null) {
	        			//
	        			rtn = false;
	        		}else {
	        			rtn = true;
	        		}
	        	}
	        }
		}
		
		
		return rtn;
    }
    
    public void setFontSize(int fontSize) {
    	//
        this.fontSize = fontSize;
    }
    
    public synchronized void setSlotTimeOut(MmsiEntity mmsiEntity) {
    	//
    	for(Entry<Point, CellInfos> cellInfo : this.cellInfos.entrySet()) {
    		//
    		if(this.cellInfos.get(cellInfo.getKey()).getAisCellInfos() != null) {
    			//
    			if(this.cellInfos.get(cellInfo.getKey()).getAisCellInfos().getMmsi() == mmsiEntity.getMmsi()) {
    				//
    				this.cellInfos.get(cellInfo.getKey()).getAisCellInfos().setSlotTimeOut(mmsiEntity.getSlotTimeOut());
    			}
    		}
    	}
    }
    
    public synchronized Map<String, Integer> getOccupancyinformation() {
    	//
    	int aisChannelACount = 0;
    	int aisChannelBCount = 0;
    	
    	int asmChannelACount = 0;
    	int asmChannelBCount = 0;
    	
    	int vdeUpperLegCount = 0;
    	int vdeLowerLegCount = 0;
    	
    	for(Entry<Point, CellInfos> cellInfo : this.cellInfos.entrySet()) {
    		//
    		if(this.cellInfos.get(cellInfo.getKey()).getAisCellInfos() != null) {
    			//
    			if(this.cellInfos.get(cellInfo.getKey()).getAisCellInfos().getChannelATime() != null) {
    				aisChannelACount = aisChannelACount +1;
    			}
    			if(this.cellInfos.get(cellInfo.getKey()).getAisCellInfos().getChannelBTime() != null) {
    				aisChannelBCount = aisChannelBCount +1;
    			}
    		}
    		
    		if(this.cellInfos.get(cellInfo.getKey()).getAsmCellInfosForA() != null 
    				&& this.cellInfos.get(cellInfo.getKey()).getAsmCellInfosForA().getTime() != null) {
    			//
    			asmChannelACount = asmChannelACount +1;
    		}
    		if(this.cellInfos.get(cellInfo.getKey()).getAsmCellInfosForB() != null 
    				&& this.cellInfos.get(cellInfo.getKey()).getAsmCellInfosForB().getTime() != null) {
    			//
    			asmChannelBCount = asmChannelBCount +1;
    		}
    		
    		if(this.cellInfos.get(cellInfo.getKey()).getVdeLowerLegCellInfos() != null 
    				&& this.cellInfos.get(cellInfo.getKey()).getVdeLowerLegCellInfos().getChannelTime() != null) {
    			//
    			vdeLowerLegCount = vdeLowerLegCount +1;
    		}
    		if(this.cellInfos.get(cellInfo.getKey()).getVdeUpperLegCellInfos() != null 
    				&& this.cellInfos.get(cellInfo.getKey()).getVdeUpperLegCellInfos().getChannelTime() != null) {
    			//
    			vdeUpperLegCount = vdeUpperLegCount +1;
    		}
    	}
    	
    	Map<String, Integer> rtnValue = new HashMap<>();
    	rtnValue.put("aisA", aisChannelACount);
    	rtnValue.put("aisB", aisChannelBCount);
    	rtnValue.put("asmA", asmChannelACount);
    	rtnValue.put("asmB", asmChannelBCount);
    	rtnValue.put("vdeUpper", vdeUpperLegCount);
    	rtnValue.put("vdeLower", vdeLowerLegCount);
    	
    	return rtnValue;
    }
    
    public synchronized void setCellInfosAis(int row, int column, MmsiEntity mmsiEntity, char channel, LocalDateTime time) {
    	//
    	Point cellKey = new Point(row, column);
    	
    	if(this.cellInfos.get(cellKey) != null && this.cellInfos.get(cellKey).getAisCellInfos() != null) {
    		// 기존에 있으면
    		if(channel == 'A') {
        		//
    			this.cellInfos.get(cellKey).getAisCellInfos().setChannelATime(time);
        	}else {
        		//
        		this.cellInfos.get(cellKey).getAisCellInfos().setChannelBTime(time);
        	}
    	}else {
    		// 기존에 없으면
    		AisCellInfos aisCellInfos = new AisCellInfos();
    		aisCellInfos.setMmsiEntity(mmsiEntity);
        	aisCellInfos.setMmsi(mmsiEntity.getMmsi());
        	aisCellInfos.setSlotTimeOut(mmsiEntity.getSlotTimeOut());
        	if(channel == 'A') {
        		//
        		aisCellInfos.setChannelATime(time);
        	}else {
        		//
        		aisCellInfos.setChannelBTime(time);
        	}
        	
        	CellInfos cellInfos = this.cellInfos.get(cellKey);
        	cellInfos.setAisCellInfos(aisCellInfos);
        	cellInfos.setColor(SystemConstMessage.AIS_Color);
    	}
    	
    }
    
    public synchronized void setCellInfosAsmForB(int row, int column, MmsiEntity mmsiEntity) {
    	//
    	Point cellKey = new Point(row, column);
    	
    	AsmCellInfos asmCellInfos = new AsmCellInfos();
    	asmCellInfos.setMmsi(mmsiEntity.getMmsi());
    	asmCellInfos.setTime(mmsiEntity.getAsmEntity().getStartTime());
    	CellInfos cellInfos = this.cellInfos.get(cellKey);
        cellInfos.setAsmCellInfosForB(asmCellInfos);
        cellInfos.setColor(SystemConstMessage.ASM_Color);
    }
    
    public synchronized void setCellInfosAsmForA(int row, int column, MmsiEntity mmsiEntity) {
    	//
    	Point cellKey = new Point(row, column);
    	
    	AsmCellInfos asmCellInfos = new AsmCellInfos();
    	asmCellInfos.setMmsi(mmsiEntity.getMmsi());
    	asmCellInfos.setTime(mmsiEntity.getAsmEntity().getStartTime());
    	CellInfos cellInfos = this.cellInfos.get(cellKey);
        cellInfos.setAsmCellInfosForA(asmCellInfos);
        cellInfos.setColor(SystemConstMessage.ASM_Color);

    }
    
    public synchronized void setCellInfosVdeForLowerLeg(Point cellKey, MmsiEntity mmsiEntity) {
    	//
    	VdeLowerLegCellInfos vdeLowerLegCellInfos = new VdeLowerLegCellInfos();
    	vdeLowerLegCellInfos.setMmsi(mmsiEntity.getMmsi());
    	vdeLowerLegCellInfos.setChannelTime(mmsiEntity.getVdeEntity().getStartTime());
    	CellInfos cellInfos = this.cellInfos.get(cellKey);
        cellInfos.setVdeLowerLegCellInfos(vdeLowerLegCellInfos);
        cellInfos.setColor(SystemConstMessage.VDE_Color);
    }
    
    public synchronized void setCellInfosVdeForLowerLegAck(Point cellKey, MmsiEntity mmsiEntity) {
    	//
    	VdeLowerLegCellInfos vdeLowerLegCellInfos = new VdeLowerLegCellInfos();
    	vdeLowerLegCellInfos.setMmsi(mmsiEntity.getMmsi());
    	vdeLowerLegCellInfos.setChannelTime(mmsiEntity.getVdeEntity().getStartTime());
    	CellInfos cellInfos = this.cellInfos.get(cellKey);
        cellInfos.setVdeLowerLegCellInfos(vdeLowerLegCellInfos);
        cellInfos.setColor(SystemConstMessage.ACK_Color);
    }
    
    public synchronized void setCellInfosVdeForUpperLeg(int row, int column, MmsiEntity mmsiEntity) {
    	//
    	Point cellKey = new Point(row, column);
    	
    	VdeUpperLegCellInfos vdeUpperLegCellInfos = new VdeUpperLegCellInfos();
    	vdeUpperLegCellInfos.setMmsi(mmsiEntity.getMmsi());
    	vdeUpperLegCellInfos.setChannelTime(mmsiEntity.getVdeEntity().getStartTime());
    	CellInfos cellInfos = this.cellInfos.get(cellKey);
        cellInfos.setVdeUpperLegCellInfos(vdeUpperLegCellInfos);
        cellInfos.setColor(SystemConstMessage.VDE_Color);
    }
    
    public synchronized void setCellInfosVdeForUpperLegAck(int row, int column, MmsiEntity mmsiEntity) {
    	//
    	Point cellKey = new Point(row, column);
    	
    	VdeUpperLegCellInfos vdeUpperLegCellInfos = new VdeUpperLegCellInfos();
    	vdeUpperLegCellInfos.setMmsi(mmsiEntity.getMmsi());
    	vdeUpperLegCellInfos.setChannelTime(mmsiEntity.getVdeEntity().getStartTime());
    	CellInfos cellInfos = this.cellInfos.get(cellKey);
        cellInfos.setVdeUpperLegCellInfos(vdeUpperLegCellInfos);
        cellInfos.setColor(SystemConstMessage.ACK_Color);
    }
    
    public String getCellSlotNumber(int row, int column) {
    	//
    	Point cellKey = new Point(row, column);
        return String.valueOf(this.cellInfos.get(cellKey).getSlotNumber());
    }
    
    // 대비되는 색을 계산하는 메서드
    private Color getContrastColor(Color color) {
        // 색상의 밝기 계산 (YIQ 모델 사용)
        double y = 0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue();

        // 색상의 밝기에 따라 텍스트 색상 결정
        return (y >= 128) ? Color.BLACK : Color.WHITE;
    }
    
    public synchronized Map<Point, CellInfos> getCellInfos() {
    	//
//    	return new HashMap<>(this.cellInfos);
    	return MapUtil.copy(this.cellInfos);
    }
    
    public synchronized void setCellInfos(Map<Point, CellInfos> cellInfos) {
    	//
    	MapUtil.copy(this.cellInfos, cellInfos);
//    	this.cellInfos = cellInfos;
    }
    
    public void paintResourceStartCell(int slotNumber) {
    	//
    	Point cellKey = this.getCellInfosBySlotNumber(slotNumber);
    	CellInfos cellInfo = this.cellInfos.get(cellKey);
    	cellInfo.setColor(SystemConstMessage.RESOURCE_REQUEST_Color);
    	System.out.println("리소스 스타트 컬러마킹 완료");
    }
    
    public void paintResourceResponseCell(int slotNumber) {
    	//
    	Point cellKey = this.getCellInfosBySlotNumber(slotNumber);
    	CellInfos cellInfo = this.cellInfos.get(cellKey);
    	cellInfo.setColor(SystemConstMessage.RESOURCE_RESPONSE_Color);
    	//System.out.println("리소스 종료 컬러마킹 완료");
    }
    
    public void paintResourceCell(int slotNumber) {
    	//
    	Point cellKey = this.getCellInfosBySlotNumber(slotNumber);
    	CellInfos cellInfo = this.cellInfos.get(cellKey);
    	cellInfo.setColor(SystemConstMessage.RESOURCE_Color);
    	VdeLowerLegCellInfos vdeLower = new VdeLowerLegCellInfos();
		vdeLower.setChannelTime(LocalDateTime.now());
		cellInfo.setVdeLowerLegCellInfos(vdeLower);
		cellInfo.setColor(SystemConstMessage.RESOURCE_Color);
    }
    
    public void paintResourceCell(int tdmaFrame, int tdmaChannel) {
    	
    }
    
    public synchronized void setInit(int row, int column, String text, Color color) {
    	//
    	Point cellKey = new Point(row, column);
    	CellInfos cellInfos = new CellInfos();
    	cellInfos.setSlotNumber(Integer.valueOf(text));
    	cellInfos.setColor(color);
    	cellInfos.setDefaultColor(color);
    	this.cellInfos.put(cellKey, cellInfos);
    	
    	CellInfos cellInfosDefault = new CellInfos();
    	cellInfosDefault.setSlotNumber(Integer.valueOf(text));
    	cellInfosDefault.setColor(color);
    	cellInfosDefault.setDefaultColor(color);
    	this.cellInfosDefault.put(new Point(row, column), cellInfosDefault);
    }
    
    public synchronized void setAisDisplayToggle(boolean v) {
    	//
    	this.aisMsgDisplay = v;
    }
    
    public synchronized void setAsmDisplayToggle(boolean v) {
    	//
    	this.asmMsgDisplay = v;
    }
    
    public synchronized void setVdeDisplayToggle(boolean v) {
    	//
    	this.vdeMsgDisplay = v;
    }
    
    public synchronized void setInit(boolean legType) {
    	//
    	int term = 0;
		for(int aa = 0 ; aa < 13; aa++) {
    		//
    		for (int a = 0; a < 6; a++) {
                //
                for (int i = 0; i < 32; i++) {
                	//
                    if (i == 0) {
                    	//
                    	int row = a+(aa*6)+term;
                    	this.setInit(row, i, "9999", SystemConstMessage.defaultCellColor);
                    } else if (i == 16){
                    	//
                    	if(aa < 12) {
                    		int row = a+(aa*6)+term;
                    		this.setInit(row, i, "9999", SystemConstMessage.defaultCellColor);
                    	}
                    	
                    } else if (i >= 1 && i <= 15){
                    	//
                    	int slotNumber = a + ((i-1)*6)+(aa*180);
                    	int row = a+(aa*6)+term;
                        this.setInit(row, i, String.valueOf(slotNumber), SystemConstMessage.defaultCellColor);
                        if(a == 0 && (i == 1 || i == 2 || i == 3 )) {
                        	//
                        	this.setInit(row, i, String.valueOf(slotNumber), SystemConstMessage.BBSC_Color);
                        }
                        if(a != 0 && i == 15) {
                        	//
                        	this.setInit(row, i, String.valueOf(slotNumber), SystemConstMessage.DSCH_Color);
                        }
                        if(a == 0 && i > 3) {
                        	//
                        	if(legType) {
                        		if (i % 2 == 0) {
                            		//짝수
                        			this.setInit(row, i, String.valueOf(slotNumber), SystemConstMessage.RAC_Color);
                                }else {
                                	//홀수
                                	this.setInit(row, i, String.valueOf(slotNumber), SystemConstMessage.ASC_Color);
                                }
                        	}else {
                        		//
                        		this.setInit(row, i, String.valueOf(slotNumber), SystemConstMessage.RAC_Color);
                        	}
                        }
                    } else if (i >= 17){
                    	//
                    	int cut = a + ((i-2)*6)+(aa*180);
                    	int row = a+(aa*6)+term;
                        if(cut < 2250) {
                        	//
                        	this.setInit(row, i, String.valueOf(cut), SystemConstMessage.defaultCellColor);
                        	if(a == 0 && (i == 17 || i == 18 || i == 19 )) {
                            	//
                        		this.setInit(row, i, String.valueOf(cut), SystemConstMessage.BBSC_Color);
                            }
                        	if(a != 0 && i == 31) {
                            	//
                        		this.setInit(row, i, String.valueOf(cut), SystemConstMessage.DSCH_Color);
                            }
                        	if(a == 0 && i > 19) {
                            	//
                        		if(legType) {
                            		if (i % 2 == 0) {
                                		//짝수
                            			this.setInit(row, i, String.valueOf(cut), SystemConstMessage.RAC_Color);
                                    }else {
                                    	//홀수
                                    	this.setInit(row, i, String.valueOf(cut), SystemConstMessage.ASC_Color);
                                    }
                            	}else {
                            		//
                            		this.setInit(row, i, String.valueOf(cut), SystemConstMessage.RAC_Color);
                            	}
                            }
                        }
                    }
                }
            }
    		term =  term +1;
    	}
    }
    
    private void init(DefaultTableModel tableModel, boolean legType) {
    	//
    	int term = 0;
    	int max = 13;
    	
    	if(this.tdmaChannelSumValue >= 10000) {
    		max = 3;
    	}
    	
		for(int aa = 0 ; aa < max; aa++) {
    		//
    		for (int a = 0; a < 6; a++) {
                Object[] rowData = new Object[32];
                for (int col = 0; col < 32; col++) {
                	//
                    if (col == 0) {
                    	//
                    	int row = a+(aa*6)+term;
                    	rowData[col] = "TDMA" + a;
                    	this.setInit(row, col, "9999", SystemConstMessage.defaultCellColor);
                    } else if (col == 16){
                    	//
                    	if(this.tdmaChannelSumValue >= 10000) {
                    		if(aa < 2 ) {
                    			int row = a+(aa*6)+term;
                        		rowData[col] = "TDMA" + a;
                        		this.setInit(row, col, "9999", SystemConstMessage.defaultCellColor);
                        	}
                    	}else {
                    		if(aa < 12 ) {
                    			int row = a+(aa*6)+term;
                        		rowData[col] = "TDMA" + a;
                        		this.setInit(row, col, "9999", SystemConstMessage.defaultCellColor);
                        	}
                    	}
                    	
                    } else if (col >= 1 && col <= 15){
                    	//
                    	int slotNumber = a + ((col-1)*6)+(aa*180);
                    	int row = a+(aa*6)+term;
                    	
                        rowData[col] = slotNumber;
                        this.setInit(row, col, String.valueOf(slotNumber), SystemConstMessage.defaultCellColor);
                        if(a == 0 && (col == 1 || col == 2 || col == 3 )) {
                        	//
                        	this.setInit(row, col, String.valueOf(slotNumber), SystemConstMessage.BBSC_Color);
                        }
                        if(a != 0 && col == 15) {
                        	//
                        	this.setInit(row, col, String.valueOf(slotNumber), SystemConstMessage.DSCH_Color);
                        }
                        if(a == 0 && col > 3) {
                        	//
                        	if(legType) {
                        		if (col % 2 == 0) {
                            		//짝수
                        			this.setInit(row, col, String.valueOf(slotNumber), SystemConstMessage.RAC_Color);
                                }else {
                                	//홀수
                                	this.setInit(row, col, String.valueOf(slotNumber), SystemConstMessage.ASC_Color);
                                }
                        	}else {
                        		//
                        		this.setInit(row, col, String.valueOf(slotNumber), SystemConstMessage.RAC_Color);
                        	}
                        }
                    } else if (col >= 17){
                    	//
                    	int cut = a + ((col-2)*6)+(aa*180);
                    	int row = a+(aa*6)+term;
                    	
                    	if(this.tdmaChannelSumValue >= 10000) {
                    		if(cut < 450) {
                            	//
                    			rowData[col] = cut;
                            	this.setInit(row, col, String.valueOf(cut), SystemConstMessage.defaultCellColor);
                            	if(a == 0 && (col == 17 || col == 18 || col == 19 )) {
                                	//
                            		this.setInit(row, col, String.valueOf(cut), SystemConstMessage.BBSC_Color);
                                }
                            	if(a != 0 && col == 31) {
                                	//
                            		this.setInit(row, col, String.valueOf(cut), SystemConstMessage.DSCH_Color);
                                }
                            	if(a == 0 && col > 19) {
                                	//
                            		if(legType) {
                                		if (col % 2 == 0) {
                                    		//짝수
                                			this.setInit(row, col, String.valueOf(cut), SystemConstMessage.RAC_Color);
                                        }else {
                                        	//홀수
                                        	this.setInit(row, col, String.valueOf(cut), SystemConstMessage.ASC_Color);
                                        }
                                	}else {
                                		//
                                		this.setInit(row, col, String.valueOf(cut), SystemConstMessage.RAC_Color);
                                	}
                                }
                            }
                    	}else {
                    		//
                    		if(cut < 2250) {
                            	//
                    			rowData[col] = cut;
                            	this.setInit(row, col, String.valueOf(cut), SystemConstMessage.defaultCellColor);
                            	if(a == 0 && (col == 17 || col == 18 || col == 19 )) {
                                	//
                            		this.setInit(row, col, String.valueOf(cut), SystemConstMessage.BBSC_Color);
                                }
                            	if(a != 0 && col == 31) {
                                	//
                            		this.setInit(row, col, String.valueOf(cut), SystemConstMessage.DSCH_Color);
                                }
                            	if(a == 0 && col > 19) {
                                	//
                            		if(legType) {
                                		if (col % 2 == 0) {
                                    		//짝수
                                			this.setInit(row, col, String.valueOf(cut), SystemConstMessage.RAC_Color);
                                        }else {
                                        	//홀수
                                        	this.setInit(row, col, String.valueOf(cut), SystemConstMessage.ASC_Color);
                                        }
                                	}else {
                                		//
                                		this.setInit(row, col, String.valueOf(cut), SystemConstMessage.RAC_Color);
                                	}
                                }
                            }
                    	}
                    	
                    }
                }
                tableModel.addRow(rowData);
            }
    		if(this.tdmaChannelSumValue >= 10000) {
    			if(aa < 2) {
        			//
        			tableModel.addRow(new Object[32]);
        		}
    		}else {
    			if(aa < 12) {
        			//
        			tableModel.addRow(new Object[32]);
        		}
    		}
    		term =  term +1;
    	}
    }
    
    // 배열 생성 메서드
 	private int[] generateArray(int start) {
 		int[] array = new int[14];
 		for (int i = 0; i < 14; i++) {
 			array[i] = start + i * 6;
 		}
 		return array;
 	}
 	
}