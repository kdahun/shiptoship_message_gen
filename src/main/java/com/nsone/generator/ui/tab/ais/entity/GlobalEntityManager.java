package com.nsone.generator.ui.tab.ais.entity;

import java.awt.Point;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import org.quartz.Scheduler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.nsone.generator.ais.ASMMessageUtil;
import com.nsone.generator.ais.AisMessage1Util;
import com.nsone.generator.system.constant.SystemConstMessage;
import com.nsone.generator.system.constant.SystemConstTestData180;
import com.nsone.generator.system.schedule.QuartzCoreService;
import com.nsone.generator.system.util.BeanUtils;
import com.nsone.generator.ui.entity.TargetCellInfoEntity;
import com.nsone.generator.ui.tab.ais.entity.event.change.ColorEntityChangeEvent;
import com.nsone.generator.ui.tab.ais.entity.event.change.ToggleDisplayAis;
import com.nsone.generator.ui.tab.ais.entity.event.change.ToggleDisplayAsm;
import com.nsone.generator.ui.tab.ais.entity.event.change.ToggleDisplayVde;
import com.nsone.generator.ui.tab.ais.model.MmsiTableModel;
import com.nsone.generator.ui.tab.ais.renderer.CustomTableCellRenderer;
import com.nsone.generator.util.RandomGenerator;

import dk.dma.ais.sentence.Vdm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GlobalEntityManager {
	//
	private final String uuid = "00"+String.valueOf(RandomGenerator.generateRandomInt(7));
	private final ASMMessageUtil aSMMessageUtil;
	private final ApplicationEventPublisher eventPublisher;
	Set<Long> generatedMmsiSet = new HashSet<>();
	private JTable currentFrameJTableNameUpper;
	private JTable currentFrame1JTableNameUpper;
	private JTable currentFrame2JTableNameUpper;
	private JTable currentFrame3JTableNameUpper;
	private JTable currentFrame4JTableNameUpper;
	private JTable currentFrame5JTableNameUpper;
	private JTable currentFrame6JTableNameUpper;
	private JTable currentFrame7JTableNameUpper;
	private JTable currentFrame8JTableNameUpper;
	private JTable currentFrame9JTableNameUpper;
	private JTable currentFrame10JTableNameUpper;

	private JTable currentFrameJTableNameLower;
	private JTable currentFrame1JTableNameLower;
	private JTable currentFrame2JTableNameLower;
	private JTable currentFrame3JTableNameLower;
	private JTable currentFrame4JTableNameLower;
	private JTable currentFrame5JTableNameLower;
	private JTable currentFrame6JTableNameLower;
	private JTable currentFrame7JTableNameLower;
	private JTable currentFrame8JTableNameLower;
	private JTable currentFrame9JTableNameLower;
	private JTable currentFrame10JTableNameLower;

	private boolean aisMsgDisplay = true;
	private boolean asmMsgDisplay = true;
	private boolean vdeMsgDisplay = true;

	private List<MmsiEntity> mmsiEntityLists;
	private MmsiTableModel mmsiTableModel2;

	private JTextField jTextFieldSFI;
	
	GlobalEntityManager(ApplicationEventPublisher eventPublisher, ASMMessageUtil aSMMessageUtil) {
		// 생성자에서 리스트를 초기화합니다.
		this.eventPublisher = eventPublisher;
		this.aSMMessageUtil = aSMMessageUtil;
		this.mmsiEntityLists = new ArrayList<>();
	}

	public void setMmsiTableModel(MmsiTableModel mmsiTableModel2) {
		//
		this.mmsiTableModel2 = mmsiTableModel2;
	}

	public List<MmsiEntity> getMmsiEntityLists() {
		//
		return this.mmsiEntityLists;
	}

	public void setMmsiEntityLists(List<MmsiEntity> mmsiEntityLists) {
		//
		this.mmsiEntityLists = mmsiEntityLists;
		this.mmsiTableModel2.setData();
	}

	public void addMmsiEntityLists(List<MmsiEntity> mmsiEntityLists) {
		//
		this.mmsiEntityLists.addAll(mmsiEntityLists);
		this.mmsiTableModel2.setData();
	}

	public void addMmsiEntity180(Scheduler scheduler, QuartzCoreService quartzCoreService,
			JTextArea aisTabjTextAreaName) {
		//
		if (this.mmsiEntityLists == null) {
			//
			this.mmsiEntityLists = new ArrayList<>();
		}
		// 중복되지 않는 MMSI 값을 보장하기 위해 반복문 사용
		long uniqueMmsi;
		do {
			uniqueMmsi = RandomGenerator.generateRandomLong(9);
		} while (!generatedMmsiSet.add(uniqueMmsi));

		BeanUtils.registerBean(uniqueMmsi + "", MmsiEntity.class);
		MmsiEntity mmsi = (MmsiEntity) BeanUtils.getBean(uniqueMmsi + "");
		
		mmsi.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
		mmsi.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
		mmsi.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
		mmsi.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
		mmsi.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
		mmsi.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
		mmsi.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
		mmsi.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
		mmsi.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
		mmsi.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
		mmsi.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
		mmsi.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
		mmsi.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
		mmsi.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
		mmsi.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
		mmsi.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);

		mmsi.setjTextFieldSFI(this.jTextFieldSFI);

		mmsi.setMmsi(uniqueMmsi);
		mmsi.setAisTabjTextAreaName(aisTabjTextAreaName);
		mmsi.setGlobalEntityManager(this);
		
		mmsi.setSpeed(180);
		mmsi.setSlotTimeOut(3);
//		mmsi.setslo
//		this.speed = speed;
//		this.slotTimeOut = slotTimeout;
//		this.slotTimeOut_default = this.slotTimeOut;
		
		
		this.mmsiEntityLists.add(mmsi);
		
		this.mmsiTableModel2.setData();
		mmsi.setChk(true);
	}
	
	public void addMmsiEntity10(Scheduler scheduler, QuartzCoreService quartzCoreService,
			JTextArea aisTabjTextAreaName) {
		//
		if (this.mmsiEntityLists == null) {
			//
			this.mmsiEntityLists = new ArrayList<>();
		}
		// 중복되지 않는 MMSI 값을 보장하기 위해 반복문 사용
		long uniqueMmsi;
		do {
			uniqueMmsi = RandomGenerator.generateRandomLong(9);
		} while (!generatedMmsiSet.add(uniqueMmsi));

		BeanUtils.registerBean(uniqueMmsi + "", MmsiEntity.class);
		MmsiEntity mmsi = (MmsiEntity) BeanUtils.getBean(uniqueMmsi + "");
		
		mmsi.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
		mmsi.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
		mmsi.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
		mmsi.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
		mmsi.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
		mmsi.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
		mmsi.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
		mmsi.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
		mmsi.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
		mmsi.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
		mmsi.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
		mmsi.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
		mmsi.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
		mmsi.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
		mmsi.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
		mmsi.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);

		mmsi.setjTextFieldSFI(this.jTextFieldSFI);

		mmsi.setMmsi(uniqueMmsi);
		mmsi.setAisTabjTextAreaName(aisTabjTextAreaName);
		mmsi.setGlobalEntityManager(this);
		
		mmsi.setSpeed(10);
		mmsi.setSlotTimeOut(7);
		
		this.mmsiEntityLists.add(mmsi);
		
		this.mmsiTableModel2.setData();
		mmsi.setChk(true);
	}
	
	public void addMmsiEntity6(Scheduler scheduler, QuartzCoreService quartzCoreService,
			JTextArea aisTabjTextAreaName) {
		//
		if (this.mmsiEntityLists == null) {
			//
			this.mmsiEntityLists = new ArrayList<>();
		}
		// 중복되지 않는 MMSI 값을 보장하기 위해 반복문 사용
		long uniqueMmsi;
		do {
			uniqueMmsi = RandomGenerator.generateRandomLong(9);
		} while (!generatedMmsiSet.add(uniqueMmsi));

		BeanUtils.registerBean(uniqueMmsi + "", MmsiEntity.class);
		MmsiEntity mmsi = (MmsiEntity) BeanUtils.getBean(uniqueMmsi + "");
		
		mmsi.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
		mmsi.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
		mmsi.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
		mmsi.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
		mmsi.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
		mmsi.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
		mmsi.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
		mmsi.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
		mmsi.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
		mmsi.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
		mmsi.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
		mmsi.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
		mmsi.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
		mmsi.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
		mmsi.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
		mmsi.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);

		mmsi.setjTextFieldSFI(this.jTextFieldSFI);

		mmsi.setMmsi(uniqueMmsi);
		mmsi.setAisTabjTextAreaName(aisTabjTextAreaName);
		mmsi.setGlobalEntityManager(this);
		
		mmsi.setSpeed(6);
		mmsi.setSlotTimeOut(7);
		
		this.mmsiEntityLists.add(mmsi);
		
		this.mmsiTableModel2.setData();
		mmsi.setChk(true);
	}
	
	public void addMmsiEntity2(Scheduler scheduler, QuartzCoreService quartzCoreService,
			JTextArea aisTabjTextAreaName) {
		//
		if (this.mmsiEntityLists == null) {
			//
			this.mmsiEntityLists = new ArrayList<>();
		}
		// 중복되지 않는 MMSI 값을 보장하기 위해 반복문 사용
		long uniqueMmsi;
		do {
			uniqueMmsi = RandomGenerator.generateRandomLong(9);
		} while (!generatedMmsiSet.add(uniqueMmsi));

		BeanUtils.registerBean(uniqueMmsi + "", MmsiEntity.class);
		MmsiEntity mmsi = (MmsiEntity) BeanUtils.getBean(uniqueMmsi + "");
		
		mmsi.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
		mmsi.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
		mmsi.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
		mmsi.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
		mmsi.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
		mmsi.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
		mmsi.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
		mmsi.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
		mmsi.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
		mmsi.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
		mmsi.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
		mmsi.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
		mmsi.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
		mmsi.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
		mmsi.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
		mmsi.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);

		mmsi.setjTextFieldSFI(this.jTextFieldSFI);

		mmsi.setMmsi(uniqueMmsi);
		mmsi.setAisTabjTextAreaName(aisTabjTextAreaName);
		mmsi.setGlobalEntityManager(this);
		
		mmsi.setSpeed(2);
		mmsi.setSlotTimeOut(7);
		
		this.mmsiEntityLists.add(mmsi);
		
		this.mmsiTableModel2.setData();
		mmsi.setChk(true);
	}
	
	public void addMmsiEntity(int cnt, Scheduler scheduler, QuartzCoreService quartzCoreService,
			JTextArea aisTabjTextAreaName) {
		//
		if (this.mmsiEntityLists == null) {
			//
			this.mmsiEntityLists = new ArrayList<>();
		}

		for (int i = 0; i < cnt; i++) {
			//
//			try {
////				System.out.println("sleep start "+ i);
//				Thread.sleep(300); // 1초
////				System.out.println("sleep end");
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			// 중복되지 않는 MMSI 값을 보장하기 위해 반복문 사용
			long uniqueMmsi;
			do {
				uniqueMmsi = RandomGenerator.generateRandomLong(9);
			} while (!generatedMmsiSet.add(uniqueMmsi));

			BeanUtils.registerBean(uniqueMmsi + "", MmsiEntity.class);
			MmsiEntity mmsi = (MmsiEntity) BeanUtils.getBean(uniqueMmsi + "");

			// MmsiEntity mmsi = new MmsiEntity(this.eventPublisher, scheduler,
			// quartzCoreService);

			mmsi.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
			mmsi.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
			mmsi.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
			mmsi.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
			mmsi.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
			mmsi.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
			mmsi.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
			mmsi.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
			mmsi.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
			mmsi.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
			mmsi.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
			mmsi.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
			mmsi.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
			mmsi.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
			mmsi.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
			mmsi.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);

			mmsi.setjTextFieldSFI(this.jTextFieldSFI);

			mmsi.setMmsi(uniqueMmsi);
			mmsi.setAisTabjTextAreaName(aisTabjTextAreaName);
			mmsi.setGlobalEntityManager(this);

			// 2, 6, 10, 180
//			if (i >= 0 && i < 70) {
////				System.out.println("180 ===========================");
//				make180(mmsi, i);
////				mmsi.setChk(true);
//			} else if (i >= 70 && i < 80) {
////				System.out.println("10 ===========================");
//				make10(mmsi, i);
//			} else if (i >= 80 && i < 90) {
////				System.out.println("2 ===========================");
//				make2(mmsi, i);
//			} else if (i >= 90 && i < 95) {
////				System.out.println("6 ===========================");
//				make6(mmsi, i);
//			} 

			if (i >= 0 && i < 10) {
//				System.out.println("180 ===========================");
				make180(mmsi, i);
//				mmsi.setChk(true);
			} else if (i >= 10 && i < 20) {
//				System.out.println("10 ===========================");
				make10(mmsi, i);
			} else if (i >= 20 && i < 30) {
//				System.out.println("2 ===========================");
				make2(mmsi, i);
			} else if (i >= 30 && i < 40) {
//				System.out.println("6 ===========================");
				make6(mmsi, i);
			} 
			
			
//			mmsi.setSpeed(2);

//			newMmsiEntityLists.add(mmsi);
//			log.info("mmsi set {}", mmsi.toString());
			this.mmsiEntityLists.add(mmsi);
//			List<MmsiEntity> oldMmsiLists = this.globalEntityManager.getMmsiEntityLists();
//			if (oldMmsiLists != null && oldMmsiLists.size() > 0) {
//				//
//				SwingUtilities.invokeLater(() -> {
//					this.globalEntityManager.addMmsiEntityLists(newMmsiEntityLists);
//				});
//			} else {
//				SwingUtilities.invokeLater(() -> {
//					this.globalEntityManager.setMmsiEntityLists(newMmsiEntityLists);
//				});
//			}
			this.mmsiTableModel2.setData();
			mmsi.setChk(true);
			
		}
	}

	private void make10(MmsiEntity mmsi, int index) {
		//
		if(index == 10) {
			mmsi.testInit(10, 1, SystemConstMessage.positions_10_0);
		}else if(index == 11) {
			mmsi.testInit(10, 1, SystemConstMessage.positions_10_1);
		}else if(index == 12) {
			mmsi.testInit(10, 2, SystemConstMessage.positions_10_2);
		}else if(index == 13) {
			mmsi.testInit(10, 2, SystemConstMessage.positions_10_3);
		}else if(index == 14) {
			mmsi.testInit(10, 3, SystemConstMessage.positions_10_4);
		}else if(index == 15) {
			mmsi.testInit(10, 3, SystemConstMessage.positions_10_5);
		}
		else if(index == 16) {
			mmsi.testInit(10, 4, SystemConstMessage.positions_10_6);
		}else if(index == 17) {
			mmsi.testInit(10, 4, SystemConstMessage.positions_10_7);
		}else if(index == 18) {
			mmsi.testInit(10, 5, SystemConstMessage.positions_10_8);
		}else if(index == 19) {
			mmsi.testInit(10, 6, SystemConstMessage.positions_10_9);
		}
	}
	
	private void make6(MmsiEntity mmsi, int index) {
		//
		if(index == 30) {
			mmsi.testInit(6, 1, SystemConstMessage.positions_6_0);
		}else if(index == 31) {
			mmsi.testInit(6, 2, SystemConstMessage.positions_6_1);
		}else if(index == 32) {
			mmsi.testInit(6, 3, SystemConstMessage.positions_6_2);
		}else if(index == 33) {
			mmsi.testInit(6, 4, SystemConstMessage.positions_6_3);
		}else if(index == 34) {
			mmsi.testInit(6, 5, SystemConstMessage.positions_6_4);
		}
	}
	
	
	private void make2(MmsiEntity mmsi, int index) {
		//
		if(index == 20) {
			mmsi.testInit(2, 1, SystemConstMessage.positions_2_0);
		}else if(index == 21) {
			mmsi.testInit(2, 2, SystemConstMessage.positions_2_1);
		}else if(index == 22) {
			mmsi.testInit(2, 3, SystemConstMessage.positions_2_2);
		}else if(index == 23) {
			mmsi.testInit(2, 4, SystemConstMessage.positions_2_3);
		}else if(index == 24) {
			mmsi.testInit(2, 5, SystemConstMessage.positions_2_4);
		}else if(index == 25) {
			mmsi.testInit(2, 6, SystemConstMessage.positions_2_5);
		}else if(index == 26) {
			mmsi.testInit(2, 7, SystemConstMessage.positions_2_6);
		}else if(index == 27) {
			mmsi.testInit(2, 7, SystemConstMessage.positions_2_7);
		}else if(index == 28) {
			mmsi.testInit(2, 7, SystemConstMessage.positions_2_8);
		}else if(index == 29) {
			mmsi.testInit(2, 7, SystemConstMessage.positions_2_9);
		}
	}
	private void make180(MmsiEntity mmsi, int index) {
		//
		if(index == 0) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_0);
		}else if(index == 1) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_1);
		}else if(index == 2) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_2);
		}else if(index == 3) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_3);
		}else if(index == 4) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_4);
		}else if(index == 5) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_5);
		}else if(index == 6) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_6);
		}else if(index == 7) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_7);
		}else if(index == 8) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_8);
		}else if(index == 9) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_9);
		}else if(index == 10) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_10);
		}else if(index == 11) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_11);
		}else if(index == 12) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_12);
		}else if(index == 13) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_13);
		}else if(index == 14) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_14);
		}else if(index == 15) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_15);
		}else if(index == 16) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_16);
		}else if(index == 17) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_17);
		}else if(index == 18) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_18);
		}else if(index == 19) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_19);
		}else if(index == 20) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_20);
		}else if(index == 21) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_21);
		}else if(index == 22) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_22);
		}else if(index == 23) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_23);
		}else if(index == 24) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_24);
		}else if(index == 25) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_25);
		}else if(index == 26) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_26);
		}else if(index == 27) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_27);
		}else if(index == 28) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_28);
		}else if(index == 29) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_29);
		}else if(index == 30) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_30);
		}else if(index == 31) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_31);
		}else if(index == 32) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_32);
		}else if(index == 33) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_33);
		}else if(index == 34) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_34);
		}else if(index == 35) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_35);
		}else if(index == 36) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_36);
		}else if(index == 37) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_37);
		}else if(index == 38) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_38);
		}else if(index == 39) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_39);
		}else if(index == 40) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_40);
		}else if(index == 41) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_41);
		}else if(index == 42) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_42);
		}else if(index == 43) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_43);
		}else if(index == 44) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_44);
		}else if(index == 45) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_45);
		}else if(index == 46) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_46);
		}else if(index == 47) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_47);
		}else if(index == 48) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_48);
		}else if(index == 49) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_49);
		}else if(index == 50) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_50);
		}else if(index == 51) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_51);
		}else if(index == 52) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_52);
		}else if(index == 53) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_53);
		}else if(index == 54) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_54);
		}else if(index == 55) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_55);
		}else if(index == 56) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_56);
		}else if(index == 57) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_57);
		}else if(index == 58) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_58);
		}else if(index == 59) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_59);
		}else if(index == 60) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_60);
		}else if(index == 61) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_61);
		}else if(index == 62) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_62);
		}else if(index == 63) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_63);
		}else if(index == 64) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_64);
		}else if(index == 65) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_65);
		}else if(index == 66) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_66);
		}else if(index == 67) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_67);
		}else if(index == 68) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_68);
		}else if(index == 69) {
			mmsi.testInit(180, 3, SystemConstTestData180.positions_180_69);
		}
		
	}
	
	public JTable getCurrentFrameJTableNameUpper() {
		return currentFrameJTableNameUpper;
	}

	public void setCurrentFrameJTableNameUpper(JTable currentFrameJTableNameUpper) {
		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
	}

	public JTable getCurrentFrame1JTableNameUpper() {
		return currentFrame1JTableNameUpper;
	}

	public void setCurrentFrame1JTableNameUpper(JTable currentFrame1JTableNameUpper) {
		this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
	}

	public JTable getCurrentFrame2JTableNameUpper() {
		return currentFrame2JTableNameUpper;
	}

	public void setCurrentFrame2JTableNameUpper(JTable currentFrame2JTableNameUpper) {
		this.currentFrame2JTableNameUpper = currentFrame2JTableNameUpper;
	}

	public JTable getCurrentFrame3JTableNameUpper() {
		return currentFrame3JTableNameUpper;
	}

	public void setCurrentFrame3JTableNameUpper(JTable currentFrame3JTableNameUpper) {
		this.currentFrame3JTableNameUpper = currentFrame3JTableNameUpper;
	}

	public JTable getCurrentFrame4JTableNameUpper() {
		return currentFrame4JTableNameUpper;
	}

	public void setCurrentFrame4JTableNameUpper(JTable currentFrame4JTableNameUpper) {
		this.currentFrame4JTableNameUpper = currentFrame4JTableNameUpper;
	}

	public JTable getCurrentFrame5JTableNameUpper() {
		return currentFrame5JTableNameUpper;
	}

	public void setCurrentFrame5JTableNameUpper(JTable currentFrame5JTableNameUpper) {
		this.currentFrame5JTableNameUpper = currentFrame5JTableNameUpper;
	}

	public JTable getCurrentFrame6JTableNameUpper() {
		return currentFrame6JTableNameUpper;
	}

	public void setCurrentFrame6JTableNameUpper(JTable currentFrame6JTableNameUpper) {
		this.currentFrame6JTableNameUpper = currentFrame6JTableNameUpper;
	}

	public JTable getCurrentFrame7JTableNameUpper() {
		return currentFrame7JTableNameUpper;
	}

	public void setCurrentFrame7JTableNameUpper(JTable currentFrame7JTableNameUpper) {
		this.currentFrame7JTableNameUpper = currentFrame7JTableNameUpper;
	}

	public JTable getCurrentFrameJTableNameLower() {
		return currentFrameJTableNameLower;
	}

	public void setCurrentFrameJTableNameLower(JTable currentFrameJTableNameLower) {
		this.currentFrameJTableNameLower = currentFrameJTableNameLower;
	}

	public JTable getCurrentFrame1JTableNameLower() {
		return currentFrame1JTableNameLower;
	}

	public void setCurrentFrame1JTableNameLower(JTable currentFrame1JTableNameLower) {
		this.currentFrame1JTableNameLower = currentFrame1JTableNameLower;
	}

	public JTable getCurrentFrame2JTableNameLower() {
		return currentFrame2JTableNameLower;
	}

	public void setCurrentFrame2JTableNameLower(JTable currentFrame2JTableNameLower) {
		this.currentFrame2JTableNameLower = currentFrame2JTableNameLower;
	}

	public JTable getCurrentFrame3JTableNameLower() {
		return currentFrame3JTableNameLower;
	}

	public void setCurrentFrame3JTableNameLower(JTable currentFrame3JTableNameLower) {
		this.currentFrame3JTableNameLower = currentFrame3JTableNameLower;
	}

	public JTable getCurrentFrame4JTableNameLower() {
		return currentFrame4JTableNameLower;
	}

	public void setCurrentFrame4JTableNameLower(JTable currentFrame4JTableNameLower) {
		this.currentFrame4JTableNameLower = currentFrame4JTableNameLower;
	}

	public JTable getCurrentFrame5JTableNameLower() {
		return currentFrame5JTableNameLower;
	}

	public void setCurrentFrame5JTableNameLower(JTable currentFrame5JTableNameLower) {
		this.currentFrame5JTableNameLower = currentFrame5JTableNameLower;
	}

	public JTable getCurrentFrame6JTableNameLower() {
		return currentFrame6JTableNameLower;
	}

	public void setCurrentFrame6JTableNameLower(JTable currentFrame6JTableNameLower) {
		this.currentFrame6JTableNameLower = currentFrame6JTableNameLower;
	}

	public JTable getCurrentFrame7JTableNameLower() {
		return currentFrame7JTableNameLower;
	}

	public void setCurrentFrame7JTableNameLower(JTable currentFrame7JTableNameLower) {
		this.currentFrame7JTableNameLower = currentFrame7JTableNameLower;
	}

	public JTable getCurrentFrame8JTableNameUpper() {
		return currentFrame8JTableNameUpper;
	}

	public void setCurrentFrame8JTableNameUpper(JTable currentFrame8JTableNameUpper) {
		this.currentFrame8JTableNameUpper = currentFrame8JTableNameUpper;
	}

	public JTable getCurrentFrame9JTableNameUpper() {
		return currentFrame9JTableNameUpper;
	}

	public void setCurrentFrame9JTableNameUpper(JTable currentFrame9JTableNameUpper) {
		this.currentFrame9JTableNameUpper = currentFrame9JTableNameUpper;
	}

	public JTable getCurrentFrame10JTableNameUpper() {
		return currentFrame10JTableNameUpper;
	}

	public void setCurrentFrame10JTableNameUpper(JTable currentFrame10JTableNameUpper) {
		this.currentFrame10JTableNameUpper = currentFrame10JTableNameUpper;
	}

	public JTable getCurrentFrame8JTableNameLower() {
		return currentFrame8JTableNameLower;
	}

	public void setCurrentFrame8JTableNameLower(JTable currentFrame8JTableNameLower) {
		this.currentFrame8JTableNameLower = currentFrame8JTableNameLower;
	}

	public JTable getCurrentFrame9JTableNameLower() {
		return currentFrame9JTableNameLower;
	}

	public void setCurrentFrame9JTableNameLower(JTable currentFrame9JTableNameLower) {
		this.currentFrame9JTableNameLower = currentFrame9JTableNameLower;
	}

	public JTable getCurrentFrame10JTableNameLower() {
		return currentFrame10JTableNameLower;
	}

	public void setCurrentFrame10JTableNameLower(JTable currentFrame10JTableNameLower) {
		this.currentFrame10JTableNameLower = currentFrame10JTableNameLower;
	}

	public JTextField getjTextFieldSFI() {
		return jTextFieldSFI;
	}

	public void setjTextFieldSFI(JTextField jTextFieldSFI) {
		this.jTextFieldSFI = jTextFieldSFI;
	}

	public boolean isAisMsgDisplay() {
		return aisMsgDisplay;
	}

	public void setAisMsgDisplay(boolean aisMsgDisplay) {
		//
		ToggleDisplayAis event = new ToggleDisplayAis(this, aisMsgDisplay, currentFrameJTableNameUpper,
				currentFrame1JTableNameUpper, currentFrame2JTableNameUpper, currentFrame3JTableNameUpper,
				currentFrame4JTableNameUpper, currentFrame5JTableNameUpper, currentFrame6JTableNameUpper,
				currentFrame7JTableNameUpper, currentFrame8JTableNameUpper, currentFrame9JTableNameUpper,
				currentFrame10JTableNameUpper);
		this.aisMsgDisplay = aisMsgDisplay;
		eventPublisher.publishEvent(event);
	}

	public boolean isAsmMsgDisplay() {
		return asmMsgDisplay;
	}

	public void setAsmMsgDisplay(boolean asmMsgDisplay) {
		//
		ToggleDisplayAsm event = new ToggleDisplayAsm(this, asmMsgDisplay, currentFrameJTableNameUpper,
				currentFrame1JTableNameUpper, currentFrame2JTableNameUpper, currentFrame3JTableNameUpper,
				currentFrame4JTableNameUpper, currentFrame5JTableNameUpper, currentFrame6JTableNameUpper,
				currentFrame7JTableNameUpper, currentFrame8JTableNameUpper, currentFrame9JTableNameUpper,
				currentFrame10JTableNameUpper);
		this.asmMsgDisplay = asmMsgDisplay;
		eventPublisher.publishEvent(event);

	}

	public boolean isVdeMsgDisplay() {
		return vdeMsgDisplay;
	}

	public void setVdeMsgDisplay(boolean vdeMsgDisplay) {
		//
		ToggleDisplayVde event = new ToggleDisplayVde(this, vdeMsgDisplay, currentFrameJTableNameUpper,
				currentFrame1JTableNameUpper, currentFrame2JTableNameUpper, currentFrame3JTableNameUpper,
				currentFrame4JTableNameUpper, currentFrame5JTableNameUpper, currentFrame6JTableNameUpper,
				currentFrame7JTableNameUpper, currentFrame8JTableNameUpper, currentFrame9JTableNameUpper,
				currentFrame10JTableNameUpper, currentFrameJTableNameLower, currentFrame1JTableNameLower,
				currentFrame2JTableNameLower, currentFrame3JTableNameLower, currentFrame4JTableNameLower,
				currentFrame5JTableNameLower, currentFrame6JTableNameLower, currentFrame7JTableNameLower,
				currentFrame8JTableNameLower, currentFrame9JTableNameLower, currentFrame10JTableNameLower);
		this.vdeMsgDisplay = vdeMsgDisplay;
		eventPublisher.publishEvent(event);

	}

	// ====================================================================
	public void setCurrentFrameAchColor(int row, int col, MmsiEntity mmsiEntity, LocalDateTime time) {
		//
		ColorEntityChangeEvent event = new ColorEntityChangeEvent(this, row, col, mmsiEntity, time, 'A',
				this.currentFrameJTableNameUpper, this.currentFrame1JTableNameUpper, this.currentFrame2JTableNameUpper,
				this.currentFrame3JTableNameUpper, this.currentFrame4JTableNameUpper, this.currentFrame5JTableNameUpper,
				this.currentFrame6JTableNameUpper, this.currentFrame7JTableNameUpper);
		eventPublisher.publishEvent(event);
	}

	public void setCurrentFrameBchColor(int row, int col, MmsiEntity mmsiEntity, LocalDateTime time) {
		//
		ColorEntityChangeEvent event = new ColorEntityChangeEvent(this, row, col, mmsiEntity, time, 'B',
				this.currentFrameJTableNameUpper, this.currentFrame1JTableNameUpper, this.currentFrame2JTableNameUpper,
				this.currentFrame3JTableNameUpper, this.currentFrame4JTableNameUpper, this.currentFrame5JTableNameUpper,
				this.currentFrame6JTableNameUpper, this.currentFrame7JTableNameUpper);
		eventPublisher.publishEvent(event);
	}

	private static boolean isValidCell(int row, int col) {
		//
		List<Integer> emptyRow = Arrays.asList(6, 13, 20, 27, 34, 41, 48, 55, 62, 69, 76, 83);

		if (emptyRow.contains(row)) {
			return false;
		}

		if (col == 0 || col == 16) {
			return false;
		}

		if (col >= 1 && col <= 3) {
			return !Arrays.asList(0, 7, 14, 21, 28, 35, 42, 49, 56, 63, 70, 77, 84, 91).contains(row);
		}

		if (col >= 4 && col <= 15) {
			return true;
		}

		if (col >= 17 && col <= 19) {
			if (Arrays.asList(0, 7, 14, 21, 28, 35, 42, 49, 56, 63, 70, 77, 84, 91).contains(row)) {
				return false;
			} else {
				return row < 83;
			}
		}

		if (col >= 20 && col <= 31) {
			return row < 83;
		}

		return false;
	}

	public int findSlotAndMarking(MmsiEntity mmsiEntity) {
		//
		try {
			TableModel model = this.currentFrameJTableNameUpper.getModel();
			CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrameJTableNameUpper
					.getDefaultRenderer(Object.class);

			Point cellInfoPoint = renderer.getCellInfosBySlotNumber(mmsiEntity.getSI()[0]);
			int x0 = (int) cellInfoPoint.getX();
			int x1 = x0 / 7;
			int x2 = x0 % 7;
			int y0 = (int) cellInfoPoint.getY();
			DecimalFormat decimalFormat = new DecimalFormat("#.####");

//			log.info("SI:{}, x0: {}, x1: {}, x2: {}, y0: {}", mmsiEntity.getSI()[0], x0, x1, x2, y0);

			int consecutiveCount = 0;
			List<TargetCellInfoEntity> targetInfoList = new ArrayList<>();

			int rowCount = model.getRowCount();
			int columnCount = model.getColumnCount();
			int rowsPerPage = 7;
			long startTime = System.nanoTime(); // 시작 시간 기록

//			for (int i = 0; i < rowCount; i += rowsPerPage) {
//				for (int col = 0; col < columnCount; col++) {
//					for (int j = 0; j < rowsPerPage; j++) {

			for (int i = (x1 * rowsPerPage); i < rowCount; i += rowsPerPage) {
				for (int col = y0; col < columnCount; col++) {
					for (int j = x2; j < rowsPerPage; j++) {
						//
						// 범위 체크
//						log.info("SI:{}, x0: {}, x1: {}, x2: {}, y0: {}, i: {}, j: {}, col: {}", mmsiEntity.getSI()[0], x0, x1, x2, y0, i, j, col);
						if (i + j < 90 && isValidCell(i + j, col)) {
							//
							int cellSlotNumber = Integer.valueOf(renderer.getCellSlotNumber(i + j, col));
//							log.info("cellSlotNumber: {}", cellSlotNumber);
							if (!renderer.verifyAisAllChannelIsEmpty(i + j, col)
									&& mmsiEntity.getSI()[0] <= cellSlotNumber
									&& mmsiEntity.getSI()[1] >= cellSlotNumber) {
								//
								// 비어있으면
								consecutiveCount++;
								TargetCellInfoEntity targetCellInfoEntity = new TargetCellInfoEntity();
								targetCellInfoEntity.setRow(i + j);
								targetCellInfoEntity.setCol(col);
								targetInfoList.add(targetCellInfoEntity);

								if (consecutiveCount == 4) {
									// 연속으로 비어있는곳 발견
									TargetCellInfoEntity s = RandomGenerator.generateRandomAisTarget(targetInfoList);
									int slotNumber = Integer
											.valueOf(renderer.getCellSlotNumber(s.getRow(), s.getCol()));

									if (mmsiEntity.getSlotTimeOutTime() == null) {
										LocalDateTime modifiedDateTime = mmsiEntity.getStartTime().plusMinutes(1)
												.minus((mmsiEntity.getSpeed() * 1000) - 100, ChronoUnit.MILLIS);
										mmsiEntity.setSlotTimeOutTime(modifiedDateTime);
									}
									// 마킹
									CompletableFuture<Void> future1 = CompletableFuture
											.runAsync(() -> this.setSlotMarking(mmsiEntity, s));

									//
									CompletableFuture<Void> future2 = CompletableFuture
											.runAsync(() -> this.addAISTargetSlotEntity(mmsiEntity, slotNumber, s));
//									TargetSlotEntity newTargetSlotEntity = new TargetSlotEntity();
//									newTargetSlotEntity.setRow(s.getRow());
//									newTargetSlotEntity.setColumn(s.getCol());
//									
//									newTargetSlotEntity.setChannel(mmsiEntity.getTargetChannel());
//									newTargetSlotEntity.setSlotNumber(slotNumber);
									//
//									String ssSSSS = mmsiEntity.getStartTime()
//											.format(SystemConstMessage.formatterForStartIndex);
//									double currentSecond = Double.valueOf(ssSSSS);
//									newTargetSlotEntity.setSsSSSS(currentSecond);
//									mmsiEntity.addTargetSlotEntity(newTargetSlotEntity);

									CompletableFuture<Void> future3 = CompletableFuture
											.runAsync(() -> this.setAISMessage(mmsiEntity, slotNumber));
									CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2,
											future3);
									combinedFuture.join();
//									long endTime = System.nanoTime(); // 종료 시간 기록
//							        long duration = endTime - startTime; // 실행 시간 계산
//							        double seconds = duration / 1_000_000_000.0;
//							        String formattedNumber = decimalFormat.format(seconds);
//							        System.out.println("메서드 실행 시간: "+formattedNumber+" 초");

									return slotNumber;
								}

							} else {
								//
								targetInfoList.clear();
								consecutiveCount = 0;
							}
						}
					}
					x2 = 0;
				}
				y0 = 0;
			}

//			long endTime = System.nanoTime(); // 종료 시간 기록
//	        long duration = endTime - startTime; // 실행 시간 계산
//	        double seconds = duration / 1_000_000_000.0;
//	        String formattedNumber = decimalFormat.format(seconds);
//	        System.out.println("메서드 실행 시간: "+formattedNumber+" 초");
			return -1;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;

	}

	private void setAISMessage(MmsiEntity mmsiEntity, int slotNumber) {
		//
		Vdm vdm = AisMessage1Util.create(mmsiEntity, slotNumber);
		mmsiEntity.setMessage1(vdm, slotNumber);
		mmsiEntity.setAisMessageSequence(mmsiEntity.getAisMessageSequence() + 1);

	}

	private void addAISTargetSlotEntity(MmsiEntity mmsiEntity, int slotNumber, TargetCellInfoEntity s) {
		//
		TargetSlotEntity newTargetSlotEntity = new TargetSlotEntity();
		newTargetSlotEntity.setRow(s.getRow());
		newTargetSlotEntity.setColumn(s.getCol());

		newTargetSlotEntity.setChannel(mmsiEntity.getTargetChannel());
		newTargetSlotEntity.setSlotNumber(slotNumber);

		String ssSSSS = mmsiEntity.getStartTime().format(SystemConstMessage.formatterForStartIndex);
		double currentSecond = Double.valueOf(ssSSSS);
		newTargetSlotEntity.setSsSSSS(currentSecond);
		mmsiEntity.addTargetSlotEntity(newTargetSlotEntity);
//		log.info("aaaaaaaaaaa");
	}

	private void setSlotMarking(MmsiEntity mmsiEntity, TargetCellInfoEntity s) {
		//
		if (mmsiEntity.getTargetChannel()) {
			//
			this.setCurrentFrameAchColor(s.getRow(), s.getCol(), mmsiEntity, mmsiEntity.getStartTime());
		} else {
			this.setCurrentFrameBchColor(s.getRow(), s.getCol(), mmsiEntity, mmsiEntity.getStartTime());
		}
	}

	public void displayAsm(List<TargetCellInfoEntity> targetCellInfoEntitys, MmsiEntity mmsiEntity) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrameJTableNameUpper
				.getDefaultRenderer(Object.class);
		// 마킹
		String strValue = "";

		if (mmsiEntity.getAsmEntity().getChannel() == 'A') {
			//
			if (mmsiEntity.getAsmEntity().getSlotCount() == 1) {
				//
				strValue = "NSONESOFTNSONESOFTNSONESOFT1";
				renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(0).getRow(),
						targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
			} else if (mmsiEntity.getAsmEntity().getSlotCount() == 2) {
				strValue = "NSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFT2";
				renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(0).getRow(),
						targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
				renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(1).getRow(),
						targetCellInfoEntitys.get(1).getCol(), mmsiEntity);
			} else if (mmsiEntity.getAsmEntity().getSlotCount() == 3) {
				strValue = "NSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFT3";
				renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(0).getRow(),
						targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
				renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(1).getRow(),
						targetCellInfoEntitys.get(1).getCol(), mmsiEntity);
				renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(2).getRow(),
						targetCellInfoEntitys.get(2).getCol(), mmsiEntity);
			}

			List<String> message = this.aSMMessageUtil.getMessage(strValue, mmsiEntity);
			mmsiEntity.setAsmMessageList(message, targetCellInfoEntitys.get(0).getSlotNumber());
			mmsiEntity.getAsmEntity().setChannel('B');
			mmsiEntity.setAsmMessageSequence(mmsiEntity.getAsmMessageSequence() + 1);
		} else {
			if (mmsiEntity.getAsmEntity().getSlotCount() == 1) {
				strValue = "NSONESOFTNSONESOFTNSONESOFT1";
				renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(0).getRow(),
						targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
			} else if (mmsiEntity.getAsmEntity().getSlotCount() == 2) {
				strValue = "NSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFT2";
				renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(0).getRow(),
						targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
				renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(1).getRow(),
						targetCellInfoEntitys.get(1).getCol(), mmsiEntity);
			} else if (mmsiEntity.getAsmEntity().getSlotCount() == 3) {
				strValue = "NSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFT3";
				renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(0).getRow(),
						targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
				renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(1).getRow(),
						targetCellInfoEntitys.get(1).getCol(), mmsiEntity);
				renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(2).getRow(),
						targetCellInfoEntitys.get(2).getCol(), mmsiEntity);
			}
			List<String> message = this.aSMMessageUtil.getMessage(strValue, mmsiEntity);
			mmsiEntity.setAsmMessageList(message, targetCellInfoEntitys.get(0).getSlotNumber());
			mmsiEntity.getAsmEntity().setChannel('A');
			mmsiEntity.setAsmMessageSequence(mmsiEntity.getAsmMessageSequence() + 1);
		}
		this.currentFrameJTableNameUpper.repaint();
	}

	public List<TargetCellInfoEntity> findAsmRule1(int startIndex, MmsiEntity mmsiEntity) {
		//
		TableModel model = this.currentFrameJTableNameUpper.getModel();
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrameJTableNameUpper
				.getDefaultRenderer(Object.class);

		int consecutiveCount = 0;

		List<TargetCellInfoEntity> targetInfoList = new ArrayList<>();

		int rowCount = model.getRowCount();
		int columnCount = model.getColumnCount();
		int rowsPerPage = 7;

		for (int i = 0; i < rowCount; i += rowsPerPage) {
			for (int col = 0; col < columnCount; col++) {
				for (int j = 0; j < rowsPerPage; j++) {
					//
					// 범위 체크
					if (i + j < 90 && isValidCell(i + j, col)) {
						//
						int cellText = Integer.valueOf(renderer.getCellSlotNumber(i + j, col));
						if (startIndex <= cellText && cellText <= startIndex + 235
								&& !renderer.verifyAisAllChannelIsEmpty(i + j, col) // ais 가 안쓰고
								&& !renderer.verifyASMChannelAIsEmpty(i + j, col) // asm A 채널이 안쓰고
								&& !renderer.verifyASMChannelBIsEmpty(i + j, col) // asm B 채널이 안쓰고
								&& !renderer.verifyVDEUpperIsEmpty(i + j, col) // VDE upper 가 안쓰고
						) {
							// 비어있으면
							consecutiveCount++;
							TargetCellInfoEntity targetCellInfoEntity = new TargetCellInfoEntity();
							targetCellInfoEntity.setRow(i + j);
							targetCellInfoEntity.setCol(col);
							targetCellInfoEntity.setSlotNumber(String.valueOf(cellText));
							targetInfoList.add(targetCellInfoEntity);

							if (consecutiveCount == 8) {
								// 연속으로 비어있는곳 발견
								// 마킹
								if (mmsiEntity.getMmsi() == 336992171) {
									//
									log.info("GlobalEntity 찾음 = mmsi : {} , {}", mmsiEntity.getMmsi(),
											LocalDateTime.now());
								}
								return targetInfoList;
							}

						} else {
							//
							targetInfoList.clear();
							consecutiveCount = 0;
						}
					}
				}
			}

		}
		// System.out.println("[ASM] MMSI : " + mmsiEntity.getMmsi() + " 못찾았다.");
		if (mmsiEntity.getMmsi() == 336992171) {
			//
			log.info("GlobalEntity 못찾음 = mmsi : {} , {}", mmsiEntity.getMmsi(), LocalDateTime.now());
		}
		return targetInfoList;

	}

	public List<TargetCellInfoEntity> findAsmRule2(int startIndex, MmsiEntity mmsiEntity) {
		//
		TableModel model = this.currentFrameJTableNameUpper.getModel();
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrameJTableNameUpper
				.getDefaultRenderer(Object.class);

		int consecutiveCount = 0;

		List<TargetCellInfoEntity> targetInfoList = new ArrayList<>();

		int rowCount = model.getRowCount();
		int columnCount = model.getColumnCount();
		int rowsPerPage = 7;

		for (int i = 0; i < rowCount; i += rowsPerPage) {
			for (int col = 0; col < columnCount; col++) {
				for (int j = 0; j < rowsPerPage; j++) {
					//
					// 범위 체크
					if (i + j < 90 && isValidCell(i + j, col)) {
						//
						int cellText = Integer.valueOf(renderer.getCellSlotNumber(i + j, col));
						if (startIndex <= cellText && cellText <= startIndex + 235
								&& !renderer.verifyAisAllChannelIsEmpty(i + j, col) // ais 가 안쓰고
								&& !renderer.verifyASMChannelAIsEmpty(i + j, col) // asm A 채널이 안쓰고
								&& !renderer.verifyASMChannelBIsEmpty(i + j, col) // asm B 채널이 안쓰고
						) {
							// 비어있으면
							consecutiveCount++;
							TargetCellInfoEntity targetCellInfoEntity = new TargetCellInfoEntity();
							targetCellInfoEntity.setRow(i + j);
							targetCellInfoEntity.setCol(col);
							targetCellInfoEntity.setSlotNumber(String.valueOf(cellText));
							targetInfoList.add(targetCellInfoEntity);

							if (consecutiveCount == 8) {
								// 연속으로 비어있는곳 발견
								// 마킹
								if (mmsiEntity.getMmsi() == 336992171) {
									//
									log.info("GlobalEntity 찾음 = mmsi : {} , {}", mmsiEntity.getMmsi(),
											LocalDateTime.now());
								}
								return targetInfoList;
							}

						} else {
							//
							targetInfoList.clear();
							consecutiveCount = 0;
						}
					}
				}
			}
		}
		if (mmsiEntity.getMmsi() == 336992171) {
			//
			log.info("GlobalEntity 못찾음 = mmsi : {} , {}", mmsiEntity.getMmsi(), LocalDateTime.now());
		}
		return targetInfoList;

	}

	public List<TargetCellInfoEntity> findAsmRule3(int startIndex, MmsiEntity mmsiEntity) {
		//
		TableModel model = this.currentFrameJTableNameUpper.getModel();
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrameJTableNameUpper
				.getDefaultRenderer(Object.class);

		int consecutiveCount = 0;

		List<TargetCellInfoEntity> targetInfoList = new ArrayList<>();

		int rowCount = model.getRowCount();
		int columnCount = model.getColumnCount();
		int rowsPerPage = 7;

		for (int i = 0; i < rowCount; i += rowsPerPage) {
			for (int col = 0; col < columnCount; col++) {
				for (int j = 0; j < rowsPerPage; j++) {
					//
					// 범위 체크
					if (i + j < 90 && isValidCell(i + j, col)) {
						//
						int cellText = Integer.valueOf(renderer.getCellSlotNumber(i + j, col));
						if (startIndex <= cellText && cellText <= startIndex + 235
								&& !renderer.verifyAisAllChannelIsEmpty(i + j, col)) { // AIS 가 비어있고
							if (mmsiEntity.getAsmEntity().getChannel() == 'A') { // 들어갈 채널이 A 일때
								if (!renderer.verifyASMChannelAIsEmpty(i + j, col)) { // ASM A 채널이 비어있어야됨
									// 비어있으면
									consecutiveCount++;
									TargetCellInfoEntity targetCellInfoEntity = new TargetCellInfoEntity();
									targetCellInfoEntity.setRow(i + j);
									targetCellInfoEntity.setCol(col);
									targetCellInfoEntity.setSlotNumber(String.valueOf(cellText));
									targetInfoList.add(targetCellInfoEntity);

									if (consecutiveCount == 8) {
										// 연속으로 비어있는곳 발견
										// 마킹
										if (mmsiEntity.getMmsi() == 336992171) {
											//
											log.info("GlobalEntity 찾음 = mmsi : {} , {}", mmsiEntity.getMmsi(),
													LocalDateTime.now());
										}
										return targetInfoList;
									}
								} else {
									targetInfoList.clear();
									consecutiveCount = 0;
								}
							} else {// 들어갈 채널이 B 일때
								if (!renderer.verifyASMChannelBIsEmpty(i + j, col)) { // ASM B 채널이 비어있어야됨
									// 비어있으면
									consecutiveCount++;
									TargetCellInfoEntity targetCellInfoEntity = new TargetCellInfoEntity();
									targetCellInfoEntity.setRow(i + j);
									targetCellInfoEntity.setCol(col);
									targetCellInfoEntity.setSlotNumber(String.valueOf(cellText));
									targetInfoList.add(targetCellInfoEntity);

									if (consecutiveCount == 8) {
										// 연속으로 비어있는곳 발견
										// 마킹
										if (mmsiEntity.getMmsi() == 336992171) {
											//
											log.info("GlobalEntity 찾음 = mmsi : {} , {}", mmsiEntity.getMmsi(),
													LocalDateTime.now());
										}
										return targetInfoList;
									}
								} else {
									targetInfoList.clear();
									consecutiveCount = 0;
								}
							}

						} else {
							//
							targetInfoList.clear();
							consecutiveCount = 0;
						}
					}
				}
			}

		}
		// System.out.println("[ASM] MMSI : " + mmsiEntity.getMmsi() + " 못찾았다.");
		if (mmsiEntity.getMmsi() == 336992171) {
			//
			log.info("GlobalEntity 못찾음 = mmsi : {} , {}", mmsiEntity.getMmsi(), LocalDateTime.now());
		}
		return targetInfoList;

	}

	public void findVde(int startIndex, MmsiEntity mmsiEntity) {
		//
		CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrameJTableNameLower
				.getDefaultRenderer(Object.class);

		List<Point> points1 = renderer.findVdeSlotMin(startIndex);
		for (int i = 0; i < points1.size(); i++) {
			//
			if (i == points1.size() - 1) {
				renderer.setCellInfosVdeForLowerLegAck(points1.get(i), mmsiEntity);
			} else {
				renderer.setCellInfosVdeForLowerLeg(points1.get(i), mmsiEntity);
			}

		}

		List<Point> points2 = renderer.findVdeSlotMin(startIndex + 90);
		for (int i = 0; i < points2.size(); i++) {
			//
			if (i == points2.size() - 1) {
				renderer.setCellInfosVdeForLowerLegAck(points2.get(i), mmsiEntity);
			} else {
				renderer.setCellInfosVdeForLowerLeg(points2.get(i), mmsiEntity);
			}

		}

		List<Point> points3 = renderer.findVdeSlotMin(startIndex + 180);
		for (int i = 0; i < points3.size(); i++) {
			//
			if (i == points3.size() - 1) {
				renderer.setCellInfosVdeForLowerLegAck(points3.get(i), mmsiEntity);
			} else {
				renderer.setCellInfosVdeForLowerLeg(points3.get(i), mmsiEntity);
			}

		}

		List<Point> points4 = renderer.findVdeSlotMin(startIndex + 270);
		for (int i = 0; i < points4.size(); i++) {
			//
			if (i == points4.size() - 1) {
				renderer.setCellInfosVdeForLowerLegAck(points4.get(i), mmsiEntity);
			} else {
				renderer.setCellInfosVdeForLowerLeg(points4.get(i), mmsiEntity);
			}

		}

		this.currentFrameJTableNameLower.repaint();

	}

	public String getUuid() {
		return uuid;
	}

	
}
