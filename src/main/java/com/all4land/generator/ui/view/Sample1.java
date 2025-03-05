package com.all4land.generator.ui.view;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.ui.service.ResourceService;
import com.all4land.generator.ui.tab.ais.entity.GlobalEntityManager;
import com.all4land.generator.ui.tab.ais.entity.GlobalSlotNumber;
import com.all4land.generator.ui.tab.ais.model.MmsiTableModel;
import com.all4land.generator.ui.tab.ais.model.TcpServerTableModel;
import com.all4land.generator.ui.tab.ais.model.TcpTargetClientTableModel;
import com.all4land.generator.ui.tab.ais.model.UdpServerTableModel;
import com.all4land.generator.ui.tab.ais.model.UdpTargetClientTableModel;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Getter
public class Sample1 extends javax.swing.JFrame {

	private static final long serialVersionUID = -7282770782571791015L;
	private final ApplicationEventPublisher eventPublisher;
	private final Scheduler scheduler; // Scheduler 주입
	private final QuartzCoreService quartzCoreService;

	private final ResourceService resourceService;
	
	private final JCheckBox jCheckBoxAis;
	private final JCheckBox jCheckBoxAsm;
	private final JCheckBox jCheckBoxVde;
	
	private final JTextField jTextFieldSFI;
	
	// Ais 영역
	private final GlobalEntityManager globalEntityManager;
	private final JTable mmsiJTableName;
	private final MmsiTableModel mmsiTableModel2;
	
	private final  JLabel jLabelSlotNumber;
	
	private final JTable currentFrameJTableNameUpper;
	private final JTable currentFrame1JTableNameUpper;
	private final JTable currentFrame2JTableNameUpper;
	private final JTable currentFrame3JTableNameUpper;
	private final JTable currentFrame4JTableNameUpper;
	private final JTable currentFrame5JTableNameUpper;
	private final JTable currentFrame6JTableNameUpper;
	private final JTable currentFrame7JTableNameUpper;
	private final JTable currentFrame8JTableNameUpper;
	private final JTable currentFrame9JTableNameUpper;
	private final JTable currentFrame10JTableNameUpper;
	
	private final JTable currentFrameJTableNameLower;
	private final JTable currentFrame1JTableNameLower;
	private final JTable currentFrame2JTableNameLower;
	private final JTable currentFrame3JTableNameLower;
	private final JTable currentFrame4JTableNameLower;
	private final JTable currentFrame5JTableNameLower;
	private final JTable currentFrame6JTableNameLower;
	private final JTable currentFrame7JTableNameLower;
	private final JTable currentFrame8JTableNameLower;
	private final JTable currentFrame9JTableNameLower;
	private final JTable currentFrame10JTableNameLower;

	private final JTextArea aisTabjTextAreaName;

	private final JProgressBar jProgressBarAisChannelA;
	private final JProgressBar jProgressBarAisChannelB;
	private final JProgressBar jProgressBarAsmChannelA;
	private final JProgressBar jProgressBarAsmChannelB;
	private final JProgressBar jProgressBarVdeUpper;
	private final JProgressBar jProgressBarVdeLower;
	private final JLabel jLabelAisChannelAoccupancyinformation;
	private final JLabel jLabelAisChannelBoccupancyinformation;
	private final JLabel jLabelAsmChannelAoccupancyinformation;
	private final JLabel jLabelAsmChannelBoccupancyinformation;
	private final JLabel jLabelVdeUpperoccupancyinformation;
	private final JLabel jLabelVdeLoweroccupancyinformation;
	
	private final TcpServerTableModel sendTableModel;
	private final TcpTargetClientTableModel tcpTargetClientTableModel;
	
	private final UdpServerTableModel udpServerTableModel;
	private final UdpTargetClientTableModel udpTargetClientTableModel;
	private final JTable sendJTableName;
	private final JTable tcpTargetClientJTableName;
	private final JTable udpServerJTableName;
	private final JTable udpTargetClientJTableName;
	private final JTextArea sendjTextAreaName;
	
	private final GlobalSlotNumber globalSlotNumber;

	Set<Long> generatedMmsiSet = new HashSet<>();
	Set<Color> generatedColors = new HashSet<>();
	
	Set<Long> generatedMmsiSetForAsm = new HashSet<>();
	Set<Color> generatedColorsForAsm = new HashSet<>();

    @Autowired
	public Sample1(ApplicationEventPublisher eventPublisher, Scheduler scheduler, QuartzCoreService quartzCoreService
			, GlobalEntityManager globalEntityManager, @Qualifier("mmsiJTableName") JTable mmsiJTableName
			, @Qualifier("udpServerJTableName") JTable udpServerJTableName
			, @Qualifier("udpTargetClientJTableName") JTable udpTargetClientJTableName
			, @Qualifier("tcpTargetClientJTableName") JTable tcpTargetClientJTableName
			, MmsiTableModel mmsiTableModel2 , TcpServerTableModel sendTableModel
			, TcpTargetClientTableModel tcpTargetClientTableModel
			, UdpServerTableModel udpServerTableModel, UdpTargetClientTableModel udpTargetClientTableModel
			, ResourceService resourceService
			, @Qualifier("sendJTableName") JTable sendJTableName
			, @Qualifier("jCheckBoxAisName") JCheckBox jCheckBoxAis
			, @Qualifier("jCheckBoxAsmName") JCheckBox jCheckBoxAsm
			, @Qualifier("jCheckBoxVdeName") JCheckBox jCheckBoxVde
			
			, @Qualifier("jTextFieldSFI") JTextField jTextFieldSFI
			, @Qualifier("jLabelSlotNumber") JLabel jLabelSlotNumber
			
			, @Qualifier("currentFrameJTableNameUpper") JTable currentFrameJTableNameUpper
			, @Qualifier("currentFrame1JTableNameUpper") JTable currentFrame1JTableNameUpper
			, @Qualifier("currentFrame2JTableNameUpper") JTable currentFrame2JTableNameUpper
			, @Qualifier("currentFrame3JTableNameUpper") JTable currentFrame3JTableNameUpper
			, @Qualifier("currentFrame4JTableNameUpper") JTable currentFrame4JTableNameUpper
			, @Qualifier("currentFrame5JTableNameUpper") JTable currentFrame5JTableNameUpper
			, @Qualifier("currentFrame6JTableNameUpper") JTable currentFrame6JTableNameUpper
			, @Qualifier("currentFrame7JTableNameUpper") JTable currentFrame7JTableNameUpper
			, @Qualifier("currentFrame8JTableNameUpper") JTable currentFrame8JTableNameUpper
			, @Qualifier("currentFrame9JTableNameUpper") JTable currentFrame9JTableNameUpper
			, @Qualifier("currentFrame10JTableNameUpper") JTable currentFrame10JTableNameUpper
			, @Qualifier("currentFrameJTableNameLower") JTable currentFrameJTableNameLower
			, @Qualifier("currentFrame1JTableNameLower") JTable currentFrame1JTableNameLower
			, @Qualifier("currentFrame2JTableNameLower") JTable currentFrame2JTableNameLower
			, @Qualifier("currentFrame3JTableNameLower") JTable currentFrame3JTableNameLower
			, @Qualifier("currentFrame4JTableNameLower") JTable currentFrame4JTableNameLower
			, @Qualifier("currentFrame5JTableNameLower") JTable currentFrame5JTableNameLower
			, @Qualifier("currentFrame6JTableNameLower") JTable currentFrame6JTableNameLower
			, @Qualifier("currentFrame7JTableNameLower") JTable currentFrame7JTableNameLower
			, @Qualifier("currentFrame8JTableNameLower") JTable currentFrame8JTableNameLower
			, @Qualifier("currentFrame9JTableNameLower") JTable currentFrame9JTableNameLower
			, @Qualifier("currentFrame10JTableNameLower") JTable currentFrame10JTableNameLower
			
			, @Qualifier("aisTabjTextAreaName") JTextArea aisTabjTextAreaName
			, @Qualifier("sendjTextAreaName") JTextArea sendjTextAreaName
			
			, @Qualifier("jProgressBarAisChannelA") JProgressBar jProgressBarAisChannelA
			, @Qualifier("jProgressBarAisChannelB") JProgressBar jProgressBarAisChannelB
			, @Qualifier("jProgressBarAsmChannelA") JProgressBar jProgressBarAsmChannelA
			, @Qualifier("jProgressBarAsmChannelB") JProgressBar jProgressBarAsmChannelB
			, @Qualifier("jProgressBarVdeUpper") JProgressBar jProgressBarVdeUpper
			, @Qualifier("jProgressBarVdeLower") JProgressBar jProgressBarVdeLower
			//
			, @Qualifier("jLabelAisChannelAoccupancyinformation") JLabel jLabelAisChannelAoccupancyinformation
			, @Qualifier("jLabelAisChannelBoccupancyinformation") JLabel jLabelAisChannelBoccupancyinformation
			, @Qualifier("jLabelAsmChannelAoccupancyinformation") JLabel jLabelAsmChannelAoccupancyinformation
			, @Qualifier("jLabelAsmChannelBoccupancyinformation") JLabel jLabelAsmChannelBoccupancyinformation
			, @Qualifier("jLabelVdeUpperoccupancyinformation") JLabel jLabelVdeUpperoccupancyinformation
			, @Qualifier("jLabelVdeLoweroccupancyinformation") JLabel jLabelVdeLoweroccupancyinformation
			
			//
			, GlobalSlotNumber globalSlotNumber
			) {
		//
		this.resourceService = resourceService;
		this.eventPublisher = eventPublisher;
		this.scheduler = scheduler;
		this.quartzCoreService = quartzCoreService;
		this.globalEntityManager = globalEntityManager;
		this.mmsiJTableName = mmsiJTableName;
		this.sendJTableName = sendJTableName;
		this.udpServerJTableName = udpServerJTableName;
		this.udpTargetClientJTableName = udpTargetClientJTableName;
		this.tcpTargetClientJTableName = tcpTargetClientJTableName;
		this.mmsiTableModel2 = mmsiTableModel2;
		this.sendTableModel = sendTableModel;
		this.tcpTargetClientTableModel = tcpTargetClientTableModel;
		this.udpServerTableModel = udpServerTableModel;
		this.udpTargetClientTableModel = udpTargetClientTableModel;
		this.jCheckBoxAis = jCheckBoxAis;
		this.jCheckBoxAsm = jCheckBoxAsm;
		this.jCheckBoxVde = jCheckBoxVde;
		this.jTextFieldSFI = jTextFieldSFI;
		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
		this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
		this.currentFrame2JTableNameUpper = currentFrame2JTableNameUpper;
		this.currentFrame3JTableNameUpper = currentFrame3JTableNameUpper;
		this.currentFrame4JTableNameUpper = currentFrame4JTableNameUpper;
		this.currentFrame5JTableNameUpper = currentFrame5JTableNameUpper;
		this.currentFrame6JTableNameUpper = currentFrame6JTableNameUpper;
		this.currentFrame7JTableNameUpper = currentFrame7JTableNameUpper;
		this.currentFrame8JTableNameUpper = currentFrame8JTableNameUpper;
		this.currentFrame9JTableNameUpper = currentFrame9JTableNameUpper;
		this.currentFrame10JTableNameUpper = currentFrame10JTableNameUpper;
		this.currentFrameJTableNameLower = currentFrameJTableNameLower;
		this.currentFrame1JTableNameLower = currentFrame1JTableNameLower;
		this.currentFrame2JTableNameLower = currentFrame2JTableNameLower;
		this.currentFrame3JTableNameLower = currentFrame3JTableNameLower;
		this.currentFrame4JTableNameLower = currentFrame4JTableNameLower;
		this.currentFrame5JTableNameLower = currentFrame5JTableNameLower;
		this.currentFrame6JTableNameLower = currentFrame6JTableNameLower;
		this.currentFrame7JTableNameLower = currentFrame7JTableNameLower;
		this.currentFrame8JTableNameLower = currentFrame8JTableNameLower;
		this.currentFrame9JTableNameLower = currentFrame9JTableNameLower;
		this.currentFrame10JTableNameLower = currentFrame10JTableNameLower;

		this.globalEntityManager.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
		this.globalEntityManager.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
		this.globalEntityManager.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
		this.globalEntityManager.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
		this.globalEntityManager.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
		this.globalEntityManager.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
		this.globalEntityManager.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
		this.globalEntityManager.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
		this.globalEntityManager.setCurrentFrame8JTableNameUpper(this.currentFrame8JTableNameUpper);
		this.globalEntityManager.setCurrentFrame9JTableNameUpper(this.currentFrame9JTableNameUpper);
		this.globalEntityManager.setCurrentFrame10JTableNameUpper(this.currentFrame10JTableNameUpper);
		
		this.globalEntityManager.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
		this.globalEntityManager.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
		this.globalEntityManager.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
		this.globalEntityManager.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
		this.globalEntityManager.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
		this.globalEntityManager.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
		this.globalEntityManager.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
		this.globalEntityManager.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);
		this.globalEntityManager.setCurrentFrame8JTableNameLower(this.currentFrame8JTableNameLower);
		this.globalEntityManager.setCurrentFrame9JTableNameLower(this.currentFrame9JTableNameLower);
		this.globalEntityManager.setCurrentFrame10JTableNameLower(this.currentFrame10JTableNameLower);
		
		this.globalEntityManager.setjTextFieldSFI(jTextFieldSFI);
		
		this.globalEntityManager.setMmsiTableModel(mmsiTableModel2);

		this.aisTabjTextAreaName = aisTabjTextAreaName;
		this.sendjTextAreaName = sendjTextAreaName;

		this.jProgressBarAisChannelA = jProgressBarAisChannelA;
		this.jProgressBarAisChannelB = jProgressBarAisChannelB;
		this.jProgressBarAsmChannelA = jProgressBarAsmChannelA;
		this.jProgressBarAsmChannelB = jProgressBarAsmChannelB;
		this.jProgressBarVdeUpper = jProgressBarVdeUpper;
		this.jProgressBarVdeLower = jProgressBarVdeLower;
		
		this.jLabelAisChannelAoccupancyinformation = jLabelAisChannelAoccupancyinformation;
		this.jLabelAisChannelBoccupancyinformation = jLabelAisChannelBoccupancyinformation;
		this.jLabelAsmChannelAoccupancyinformation = jLabelAsmChannelAoccupancyinformation;
		this.jLabelAsmChannelBoccupancyinformation = jLabelAsmChannelBoccupancyinformation;
		this.jLabelVdeUpperoccupancyinformation = jLabelVdeUpperoccupancyinformation;
		this.jLabelVdeLoweroccupancyinformation = jLabelVdeLoweroccupancyinformation;
		
		this.globalSlotNumber = globalSlotNumber;
		
		this.jLabelSlotNumber = jLabelSlotNumber;
		
		this.resourceService.setMainFrame(this);
		
		initComponents();
		
		
	}

	private void initComponents() {
		//
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        btn_add_180 = new javax.swing.JButton();
        btn_add_10 = new javax.swing.JButton();
        btn_add_6 = new javax.swing.JButton();
        btn_add_2 = new javax.swing.JButton();
        
        btn_send_upper = new javax.swing.JButton();
        btn_send_lower = new javax.swing.JButton();
        
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel19 = new javax.swing.JPanel();
        jSplitPane7 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTable19 = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        jSplitPane8 = new javax.swing.JSplitPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTable20 = new javax.swing.JTable();
        jPanel21 = new javax.swing.JPanel();
        jSplitPane9 = new javax.swing.JSplitPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTable21 = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jSplitPane10 = new javax.swing.JSplitPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jScrollPane24 = new javax.swing.JScrollPane();
        jTable22 = new javax.swing.JTable();
        jPanel23 = new javax.swing.JPanel();
        jSplitPane11 = new javax.swing.JSplitPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jScrollPane25 = new javax.swing.JScrollPane();
        jTable23 = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        jSplitPane12 = new javax.swing.JSplitPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jScrollPane26 = new javax.swing.JScrollPane();
        jTable24 = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        jSplitPane13 = new javax.swing.JSplitPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jScrollPane27 = new javax.swing.JScrollPane();
        jTable25 = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jSplitPane14 = new javax.swing.JSplitPane();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jScrollPane28 = new javax.swing.JScrollPane();
        jTable26 = new javax.swing.JTable();
//        jCheckBox1 = new javax.swing.JCheckBox();
//        jCheckBox2 = new javax.swing.JCheckBox();
//        jCheckBox3 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
//        jTextArea1 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel13 = new javax.swing.JPanel();
//        jButton2 = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jSplitPane5 = new javax.swing.JSplitPane();
        jSplitPane6 = new javax.swing.JSplitPane();
        jPanel14 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jProgressBar3 = new javax.swing.JProgressBar();
        jLabel10 = new javax.swing.JLabel();
        jProgressBar4 = new javax.swing.JProgressBar();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable12 = new javax.swing.JTable();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable13 = new javax.swing.JTable();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTable14 = new javax.swing.JTable();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTable15 = new javax.swing.JTable();
        jPanel31 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTable16 = new javax.swing.JTable();
        jPanel32 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTable17 = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTable18 = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jSplitPane17 = new javax.swing.JSplitPane();
        jPanel34 = new javax.swing.JPanel();
        jSplitPane15 = new javax.swing.JSplitPane();
        jPanel18 = new javax.swing.JPanel();
        jSplitPane16 = new javax.swing.JSplitPane();
        jPanel36 = new javax.swing.JPanel();
//        jLabel7 = new javax.swing.JLabel();
//        jProgressBar6 = new javax.swing.JProgressBar();
        jPanel37 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
//        jLabel13 = new javax.swing.JLabel();
//        jProgressBar7 = new javax.swing.JProgressBar();
        jLabel14 = new javax.swing.JLabel();
//        jLabel15 = new javax.swing.JLabel();
//        jProgressBar8 = new javax.swing.JProgressBar();
        jPanel33 = new javax.swing.JPanel();
//        jLabel3 = new javax.swing.JLabel();
//        jProgressBar2 = new javax.swing.JProgressBar();
        jPanel35 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
//        jLabel2 = new javax.swing.JLabel();
//        jProgressBar1 = new javax.swing.JProgressBar();

        //================================================================================
        jSplitPane18 = new javax.swing.JSplitPane();
        jPanel38 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane29 = new javax.swing.JScrollPane();
//        jTable27 = new javax.swing.JTable();
        jPanel39 = new javax.swing.JPanel();
        jScrollPane30 = new javax.swing.JScrollPane();
//        jTextArea3 = new javax.swing.JTextArea();
        
        //================================================================================
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
//        jLabel18 = new javax.swing.JLabel();
        //
        jPanel108 = new javax.swing.JPanel();
        jPanel109 = new javax.swing.JPanel();
        jPanel110 = new javax.swing.JPanel();
        jSplitPane108 = new javax.swing.JSplitPane();
        jSplitPane109 = new javax.swing.JSplitPane();
        jSplitPane110 = new javax.swing.JSplitPane();
        jScrollPane108 = new javax.swing.JScrollPane();
        jScrollPane109 = new javax.swing.JScrollPane();
        jScrollPane110 = new javax.swing.JScrollPane();
        jScrollPane108_1 = new javax.swing.JScrollPane();
        jScrollPane109_1 = new javax.swing.JScrollPane();
        jScrollPane110_1 = new javax.swing.JScrollPane();
        
        jCheckBoxTsqTestMode = new javax.swing.JCheckBox();
        
     // tab 6 start ================================================
        jSplitPane19 = new javax.swing.JSplitPane();
        jSplitPane20 = new javax.swing.JSplitPane();
        
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        
        jScrollPane31 = new javax.swing.JScrollPane();
        jScrollPane32 = new javax.swing.JScrollPane();
        jScrollPane33 = new javax.swing.JScrollPane();
        
        jTextArea4 = new javax.swing.JTextArea();
        
//        jTable28 = new javax.swing.JTable();
//        jTable29 = new javax.swing.JTable();
        // tab 6 end =================================================
        // tab 7 start ================================================
        jSplitPane23 = new javax.swing.JSplitPane();
        jSplitPane24 = new javax.swing.JSplitPane();
        
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        
        jScrollPane35 = new javax.swing.JScrollPane();
        jScrollPane36 = new javax.swing.JScrollPane();
        jScrollPane37 = new javax.swing.JScrollPane();
        
        jTextArea5 = new javax.swing.JTextArea();
        
//        jTable32 = new javax.swing.JTable();
        // tab 7 end =================================================
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jSplitPane1.setDividerLocation(200);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("MMSI"), "MMSI"));

        btn_add_180.setText("Add 180");
		btn_add_180.addActionListener(e -> {
			//
//			this.globalEntityManager.addMmsiEntity180(this.scheduler, this.quartzCoreService, this.aisTabjTextAreaName);
			for (int i = 0; i < 5; i++) {
				this.globalEntityManager.addMmsiEntity2(this.scheduler, this.quartzCoreService, this.aisTabjTextAreaName);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					log.error("Exception [Err_Location] : {}", e1.getStackTrace()[0]);
				}
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				log.error("Exception [Err_Location] : {}", e1.getStackTrace()[0]);
			}
			
			for (int i = 0; i < 10; i++) {
				this.globalEntityManager.addMmsiEntity6(this.scheduler, this.quartzCoreService, this.aisTabjTextAreaName);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					log.error("Exception [Err_Location] : {}", e1.getStackTrace()[0]);
				}
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				log.error("Exception [Err_Location] : {}", e1.getStackTrace()[0]);
			}
			
			for (int i = 0; i < 100; i++) {
				this.globalEntityManager.addMmsiEntity10(this.scheduler, this.quartzCoreService, this.aisTabjTextAreaName);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					log.error("Exception [Err_Location] : {}", e1.getStackTrace()[0]);
				}
			}
		});
		
		btn_add_10.setText("Add 10");
		btn_add_10.addActionListener(e -> {
			//
			for (int i = 0; i < 100; i++) {
				this.globalEntityManager.addMmsiEntity10(this.scheduler, this.quartzCoreService, this.aisTabjTextAreaName);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					log.error("Exception [Err_Location] : {}", e1.getStackTrace()[0]);
				}
			}
			
		});

		btn_add_6.setText("Add 6");
		btn_add_6.addActionListener(e -> {
			//
			for (int i = 0; i < 10; i++) {
				this.globalEntityManager.addMmsiEntity6(this.scheduler, this.quartzCoreService, this.aisTabjTextAreaName);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					log.error("Exception [Err_Location] : {}", e1.getStackTrace()[0]);
				}
			}
			
		});

		btn_add_2.setText("Add 2");
		btn_add_2.addActionListener(e -> {
			// 버튼 리스너 이벤트에서 thread.sleep 사용 시 JavaSwing Event Dispatcher를 블로킹하는 문제 발생 가능성 있음.
            // UI가 멈추거나 응답하지않는 현상 발생
            // SwingWorker<Void, Void> worker = new SwingWorker<>() {
            //     @Override
            //     protected Void doInBackground() throws Exception {
            //         // addMmsiEntity2 호출로 mmsi 객체 생성
            //         for (int i = 0; i < 5; i++)
            //         {
            //             globalEntityManager.addMmsiEntity2(scheduler, quartzCoreService, aisTabjTextAreaName);
            //         }
            //         return null;
            //     }

            //     @Override
            //     protected void done() {
            //         // addMmsiEntity ui 업데이트
            //     }
            // };
            // worker.execute();
			for (int i = 0; i < 5; i++) {
				this.globalEntityManager.addMmsiEntity2(this.scheduler, this.quartzCoreService, this.aisTabjTextAreaName);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					log.error("Exception [Err_Location] : {}", e1.getStackTrace()[0]);
				}
			}
			
		});
		
		btn_send_upper.setText("send TSQ Upper");
		btn_send_upper.addActionListener(e -> {
			//
		});
		
		btn_send_lower.setText("send TSQ Lower");
		btn_send_lower.addActionListener(e -> {
			//
		});
		

		jScrollPane1.setViewportView(this.mmsiJTableName);

		jLabel17.setText("Slot Number");

//        jLabel18.setText(this.globalSlotNumber.getSlotNumberString());
		
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
//        jPanel3Layout.setHorizontalGroup(
//            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
//            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        jPanel3Layout.setVerticalGroup(
//            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jPanel3Layout.createSequentialGroup()
//                .addComponent(jButton1)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE))
//        );
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(btn_add_180, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addComponent(btn_add_10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addComponent(btn_add_6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addComponent(btn_add_2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addComponent(jLabel17)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabelSlotNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(jLabelSlotNumber))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btn_add_180)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btn_add_10)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btn_add_6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btn_add_2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE))
            );

        jSplitPane1.setLeftComponent(jPanel3);

        jSplitPane2.setDividerLocation(800);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane3.setDividerLocation(215);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("점유율 현황"));

        //=========================================================================================
        jSplitPane17.setDividerLocation(144);
        jSplitPane17.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder("Upper Leg."));

        jSplitPane15.setDividerLocation(64);
        jSplitPane15.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane16.setDividerLocation(700);

        jPanel36.setBorder(javax.swing.BorderFactory.createTitledBorder("AIS"));

        jLabel4.setText("A Ch.");

        this.jLabelAisChannelAoccupancyinformation.setText("0 %");

        this.jProgressBarAisChannelA.setValue(0);

        jLabel6.setText("B Ch.");

        this.jLabelAisChannelBoccupancyinformation.setText("0 %");

        this.jProgressBarAisChannelB.setValue(0);

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(this.jLabelAisChannelAoccupancyinformation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(this.jProgressBarAisChannelA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(this.jLabelAisChannelBoccupancyinformation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(this.jProgressBarAisChannelB, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(this.jLabelAisChannelAoccupancyinformation))
                    .addComponent(this.jProgressBarAisChannelA, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(this.jLabelAisChannelBoccupancyinformation))
                    .addComponent(this.jProgressBarAisChannelB, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jSplitPane16.setLeftComponent(jPanel36);

        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder("ASM"));

        jLabel8.setText("A Ch.");

        this.jLabelAsmChannelAoccupancyinformation.setText("0 %");

        this.jProgressBarAsmChannelA.setValue(0);

        jLabel14.setText("B Ch.");

        this.jLabelAsmChannelBoccupancyinformation.setText("0 %");

        this.jProgressBarAsmChannelB.setValue(0);

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(this.jLabelAsmChannelAoccupancyinformation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(this.jLabelAsmChannelBoccupancyinformation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(this.jProgressBarAsmChannelA, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
                    .addComponent(this.jProgressBarAsmChannelB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(this.jLabelAsmChannelAoccupancyinformation))
                    .addComponent(this.jProgressBarAsmChannelA, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(this.jLabelAsmChannelBoccupancyinformation))
                    .addComponent(this.jProgressBarAsmChannelB, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jSplitPane16.setRightComponent(jPanel37);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane16)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane16)
        );

        jSplitPane15.setTopComponent(jPanel18);

        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder("VDE Upper Leg."));

        this.jLabelVdeUpperoccupancyinformation.setText("0 %");

        this.jProgressBarVdeUpper.setValue(0);

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addComponent(this.jLabelVdeUpperoccupancyinformation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(this.jProgressBarVdeUpper, javax.swing.GroupLayout.DEFAULT_SIZE, 1154, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(this.jLabelVdeUpperoccupancyinformation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(this.jProgressBarVdeUpper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSplitPane15.setRightComponent(jPanel33);

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane15)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane15)
        );

        jSplitPane17.setTopComponent(jPanel34);

        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder("Lower Leg."));

        jLabel1.setText("VDE Lower Leg.");

        this.jLabelVdeLoweroccupancyinformation.setText("0 %");

        this.jProgressBarVdeLower.setValue(0);

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.jLabelVdeLoweroccupancyinformation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(this.jProgressBarVdeLower, javax.swing.GroupLayout.DEFAULT_SIZE, 1062, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(this.jProgressBarVdeLower, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(this.jLabelVdeLoweroccupancyinformation)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jSplitPane17.setRightComponent(jPanel35);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane17)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane17)
        );
        //=========================================================================================
        
        
        jSplitPane3.setLeftComponent(jPanel5);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Slot 정보"));

        jSplitPane7.setDividerLocation(700);

        jScrollPane2.setViewportView(this.currentFrameJTableNameLower);
        
        jSplitPane7.setLeftComponent(jScrollPane2);

        
        jScrollPane21.setViewportView(this.currentFrameJTableNameUpper);

        jSplitPane7.setRightComponent(jScrollPane21);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Current Frame", jPanel19);

        jSplitPane8.setDividerLocation(800);

        
        jScrollPane3.setViewportView(this.currentFrame1JTableNameLower);
        

        jSplitPane8.setLeftComponent(jScrollPane3);

        
        jScrollPane22.setViewportView(this.currentFrame1JTableNameUpper);

        jSplitPane8.setRightComponent(jScrollPane22);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Current +1", jPanel20);

        jSplitPane9.setDividerLocation(800);

        
        jScrollPane4.setViewportView(this.currentFrame2JTableNameLower);
        

        jSplitPane9.setLeftComponent(jScrollPane4);

        
        jScrollPane23.setViewportView(this.currentFrame2JTableNameUpper);

        jSplitPane9.setRightComponent(jScrollPane23);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Current +2", jPanel21);

        jSplitPane10.setDividerLocation(800);

        
        jScrollPane5.setViewportView(this.currentFrame3JTableNameLower);
        
        jSplitPane10.setLeftComponent(jScrollPane5);

        
        jScrollPane24.setViewportView(this.currentFrame3JTableNameUpper);

        jSplitPane10.setRightComponent(jScrollPane24);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Current +3", jPanel22);

        jSplitPane11.setDividerLocation(800);

        
        jScrollPane6.setViewportView(this.currentFrame4JTableNameLower);
        
        jSplitPane11.setLeftComponent(jScrollPane6);

        jScrollPane25.setViewportView(this.currentFrame4JTableNameUpper);

        jSplitPane11.setRightComponent(jScrollPane25);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Current +4", jPanel23);

        jSplitPane12.setDividerLocation(800);

        
        jScrollPane7.setViewportView(this.currentFrame5JTableNameLower);
        
        jSplitPane12.setLeftComponent(jScrollPane7);

        
        jScrollPane26.setViewportView(this.currentFrame5JTableNameUpper);

        jSplitPane12.setRightComponent(jScrollPane26);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Current +5", jPanel24);

        jSplitPane13.setDividerLocation(800);

        
        jScrollPane8.setViewportView(this.currentFrame6JTableNameLower);
        

        jSplitPane13.setLeftComponent(jScrollPane8);

        
        jScrollPane27.setViewportView(this.currentFrame6JTableNameUpper);

        jSplitPane13.setRightComponent(jScrollPane27);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Current +6", jPanel25);

        jSplitPane14.setDividerLocation(800);

        jScrollPane10.setViewportView(this.currentFrame7JTableNameLower);
        
        jSplitPane14.setLeftComponent(jScrollPane10);

        jScrollPane28.setViewportView(this.currentFrame7JTableNameUpper);

        jSplitPane14.setRightComponent(jScrollPane28);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Current +7", jPanel12);
        //==================================================================================
        jSplitPane108.setDividerLocation(800);
        jScrollPane108.setViewportView(this.currentFrame8JTableNameLower);
        jSplitPane108.setLeftComponent(jScrollPane108);
        jScrollPane108_1.setViewportView(this.currentFrame8JTableNameUpper);
        jSplitPane108.setRightComponent(jScrollPane108_1);
        javax.swing.GroupLayout jPanel108Layout = new javax.swing.GroupLayout(jPanel108);
        jPanel108.setLayout(jPanel108Layout);
        jPanel108Layout.setHorizontalGroup(
            jPanel108Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane108, javax.swing.GroupLayout.DEFAULT_SIZE, 1227, Short.MAX_VALUE)
        );
        jPanel108Layout.setVerticalGroup(
            jPanel108Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane108, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
        );
        jTabbedPane3.addTab("Current +8", jPanel108);
        
        jSplitPane109.setDividerLocation(800);
        jScrollPane109.setViewportView(this.currentFrame9JTableNameLower);
        jSplitPane109.setLeftComponent(jScrollPane109);
        jScrollPane109_1.setViewportView(this.currentFrame9JTableNameUpper);
        jSplitPane109.setRightComponent(jScrollPane109_1);
        javax.swing.GroupLayout jPanel109Layout = new javax.swing.GroupLayout(jPanel109);
        jPanel109.setLayout(jPanel109Layout);
        jPanel109Layout.setHorizontalGroup(
            jPanel109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane109, javax.swing.GroupLayout.DEFAULT_SIZE, 1227, Short.MAX_VALUE)
        );
        jPanel109Layout.setVerticalGroup(
            jPanel109Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane109, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
        );
        jTabbedPane3.addTab("Current +9", jPanel109);
        
        jSplitPane110.setDividerLocation(800);
        jScrollPane110.setViewportView(this.currentFrame10JTableNameLower);
        jSplitPane110.setLeftComponent(jScrollPane110);
        jScrollPane110_1.setViewportView(this.currentFrame10JTableNameUpper);
        jSplitPane110.setRightComponent(jScrollPane110_1);
        javax.swing.GroupLayout jPanel110Layout = new javax.swing.GroupLayout(jPanel110);
        jPanel110.setLayout(jPanel110Layout);
        jPanel110Layout.setHorizontalGroup(
            jPanel110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane110, javax.swing.GroupLayout.DEFAULT_SIZE, 1227, Short.MAX_VALUE)
        );
        jPanel110Layout.setVerticalGroup(
            jPanel110Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane110, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
        );
        jTabbedPane3.addTab("Current +10", jPanel110);
        
        
        //==================================================================================

        jCheckBoxAis.setSelected(true);
        jCheckBoxAis.setText("AIS");

        jCheckBoxAsm.setSelected(true);
        jCheckBoxAsm.setText("ASM");

        jCheckBoxVde.setSelected(true);
        jCheckBoxVde.setText("VDE");
        
        jLabel16.setText("SFI");

        jTextFieldSFI.setText("MG0001");
        
        jCheckBoxTsqTestMode.setSelected(false);
        jCheckBoxTsqTestMode.setText("TSQ TEST MODE");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jCheckBoxAis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxAsm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxVde)
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSFI, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxTsqTestMode))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxAis)
                    .addComponent(jCheckBoxAsm)
                    .addComponent(jCheckBoxVde)
                    .addComponent(jLabel16)
                    .addComponent(jTextFieldSFI)
                    .addComponent(jCheckBoxTsqTestMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane3)
                .addGap(0, 0, 0))
        );

        jSplitPane3.setRightComponent(jPanel6);

        jSplitPane2.setLeftComponent(jSplitPane3);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Log"));

//        jTextArea1.setColumns(20);
//        jTextArea1.setRows(5);
        jScrollPane9.setViewportView(aisTabjTextAreaName);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 876, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 132, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel4);

        jSplitPane1.setRightComponent(jSplitPane2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        jTabbedPane1.addTab("AIS", jPanel1);
        
        // AIS TAB Finish =============================================================================
        

        jTabbedPane1.addTab("ASM", jPanel2);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1091, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab3", jPanel7);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1091, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab4", jPanel8);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1091, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab5", jPanel9);

//        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
//        jPanel10.setLayout(jPanel10Layout);
//        jPanel10Layout.setHorizontalGroup(
//            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 1091, Short.MAX_VALUE)
//        );
//        jPanel10Layout.setVerticalGroup(
//            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 611, Short.MAX_VALUE)
//        );
        
        //=================================================================
        jSplitPane19.setDividerLocation(500);
        jSplitPane19.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane20.setDividerLocation(600);

        jPanel40.setBorder(javax.swing.BorderFactory.createTitledBorder("UDP read Port Infos"));

        jButton5.setText("Add Row");
        jButton5.addActionListener(e -> {
			//
			SwingUtilities.invokeLater(() -> {
				//
				int rowCount = this.udpServerJTableName.getRowCount();
				Object[] newRow = { "새로운 행 " + (rowCount + 1), "239.192.0.2:60002", 60002, false };
				this.udpServerTableModel.addRow(newRow);
				
//				this.sendjTextAreaName
//						.append(SystemConstMessage.CRLF + "새로운 장치 행이 추가 되었습니다.");
			});

		});
        
        SwingUtilities.invokeLater(() -> {
			//
			int rowCount = this.udpServerJTableName.getRowCount();
			Object[] newRow = { "새로운 행 " + (rowCount + 1), "239.192.0.2:60002", 60002, false };
			this.udpServerTableModel.addRow(newRow);
		});

        jButton6.setText("Remove Row");
        jButton6.addActionListener(e -> {
			//
			int selectedRow = this.udpServerJTableName.getSelectedRow();
			if (selectedRow != -1) {
				//
				this.udpServerTableModel.removeRow(selectedRow);

//				SwingUtilities.invokeLater(() -> {
//					//
//					this.sendjTextAreaName.append(
//							SystemConstMessage.CRLF + "서버 정보 " + selectedRow + " 행이 삭제 되었습니다.");
//				});

			}
		});

        jScrollPane31.setViewportView(udpServerJTableName);

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane31, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane31, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane20.setLeftComponent(jPanel40);

        jPanel41.setBorder(javax.swing.BorderFactory.createTitledBorder("ssend target client"));

        jButton7.setText("Add Row");
        jButton7.addActionListener(e -> {
			//
        	SwingUtilities.invokeLater(() -> {
				//
        		int parentSelectedRow = this.udpServerJTableName.getSelectedRow();
        		if(parentSelectedRow > -1) {
        			//
        			System.out.println("parent has row");
        			int rowCount = this.udpServerTableModel.getTargetRowCount(parentSelectedRow);
    				Object[] newRow = { "새로운 행 " + (rowCount + 1), "127.0.0.1", 0 };
    				this.udpServerTableModel.addTargetClientRow(parentSelectedRow, newRow);
        		}else {
        			System.out.println("parent not has row");
        		}
				
				
//				this.sendjTextAreaName
//						.append(SystemConstMessage.CRLF + "새로운 장치 행이 추가 되었습니다.");
			});

		});

        jButton8.setText("Remove Row");
        jButton8.addActionListener(e -> {
			//
        	int parentSelectedRow = this.udpServerJTableName.getSelectedRow();
			int targetRow = this.udpTargetClientJTableName.getSelectedRow();
			if (parentSelectedRow != -1 && targetRow != -1) {
				//
				this.udpServerTableModel.removeTargetClientRow(parentSelectedRow, targetRow);

//				SwingUtilities.invokeLater(() -> {
//					//
//					this.sendjTextAreaName.append(
//							SystemConstMessage.CRLF + "서버 정보 " + selectedRow + " 행이 삭제 되었습니다.");
//				});

			}
		});
        jScrollPane32.setViewportView(udpTargetClientJTableName);

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane32, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
                    .addGroup(jPanel41Layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane32, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane20.setRightComponent(jPanel41);

        jSplitPane19.setTopComponent(jSplitPane20);

        jPanel42.setBorder(javax.swing.BorderFactory.createTitledBorder("log"));

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jScrollPane33.setViewportView(jTextArea4);

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane33, javax.swing.GroupLayout.DEFAULT_SIZE, 1422, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addComponent(jScrollPane33, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane19.setRightComponent(jPanel42);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane19)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane19)
        );
        //=================================================================

        jTabbedPane1.addTab("tab6", jPanel10);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1091, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );

        
        //=======================================================================================
//        jSplitPane18.setDividerLocation(500);
//        jSplitPane18.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
//
//        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder("송신 서버 설정"));
//
//        jButton3.setText("Add.");
//        jButton3.addActionListener(e -> {
//			//
//			SwingUtilities.invokeLater(() -> {
//				//
//				int rowCount = this.sendTableModel.getRowCount();
//				Object[] newRow = { "새로운 행 " + (rowCount + 1), 0, false, false, false, false, false };
//				this.sendTableModel.addRow(newRow);
//				
//				this.sendjTextAreaName
//						.append(SystemConstMessage.CRLF + "새로운 장치 행이 추가 되었습니다.");
//			});
//
//		});
//
//        jButton4.setText("Remove.");
//		jButton4.addActionListener(e -> {
//			//
//			int selectedRow = this.sendJTableName.getSelectedRow();
//			if (selectedRow != -1) {
//				//
//				this.sendTableModel.removeRow(selectedRow);
//
//				SwingUtilities.invokeLater(() -> {
//					//
//					this.sendjTextAreaName.append(
//							SystemConstMessage.CRLF + "서버 정보 " + selectedRow + " 행이 삭제 되었습니다.");
//				});
//
//			}
//		});
//
////        jTable27.setModel(new javax.swing.table.DefaultTableModel(
////            new Object [][] {
////                {null, null, null, null, null, null, null, null},
////                {null, null, null, null, null, null, null, null},
////                {null, null, null, null, null, null, null, null},
////                {null, null, null, null, null, null, null, null}
////            },
////            new String [] {
////                "Desc.", "IP", "PORT", "연결/종료", "AIS", "ASM", "VDE Up.", "VDE Lo."
////            }
////        ) {
////            Class[] types = new Class [] {
////                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
////            };
////
////            public Class getColumnClass(int columnIndex) {
////                return types [columnIndex];
////            }
////        });
//        this.sendJTableName.setShowGrid(true);
//        jScrollPane29.setViewportView(this.sendJTableName);
//
//        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
//        jPanel38.setLayout(jPanel38Layout);
//        jPanel38Layout.setHorizontalGroup(
//            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jPanel38Layout.createSequentialGroup()
//                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                .addComponent(jButton4)
//                .addGap(0, 0, Short.MAX_VALUE))
//            .addComponent(jScrollPane29, javax.swing.GroupLayout.DEFAULT_SIZE, 1432, Short.MAX_VALUE)
//        );
//        jPanel38Layout.setVerticalGroup(
//            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jPanel38Layout.createSequentialGroup()
//                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                    .addComponent(jButton3)
//                    .addComponent(jButton4))
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jScrollPane29, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
//                .addGap(0, 0, 0))
//        );
//
//        jSplitPane18.setTopComponent(jPanel38);
//
//        jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder("Log"));
//
////        jTextArea3.setColumns(20);
////        jTextArea3.setRows(5);
//        jScrollPane30.setViewportView(this.sendjTextAreaName);
//
//        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
//        jPanel39.setLayout(jPanel39Layout);
//        jPanel39Layout.setHorizontalGroup(
//            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jScrollPane30, javax.swing.GroupLayout.DEFAULT_SIZE, 1432, Short.MAX_VALUE)
//        );
//        jPanel39Layout.setVerticalGroup(
//            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jScrollPane30, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
//        );
//
//        jSplitPane18.setRightComponent(jPanel39);
//
//        javax.swing.GroupLayout jPanel77Layout = new javax.swing.GroupLayout(jPanel11);
//        jPanel11.setLayout(jPanel77Layout);
//        jPanel77Layout.setHorizontalGroup(
//        		jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jSplitPane18)
//        );
//        jPanel77Layout.setVerticalGroup(
//        		jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jSplitPane18)
//        );
        //========================================================================================
        
        jSplitPane23.setDividerLocation(500);
        jSplitPane23.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane24.setDividerLocation(600);

        jPanel44.setBorder(javax.swing.BorderFactory.createTitledBorder("TCP server Infos"));

        jButton11.setText("Add Row");
        jButton11.addActionListener(e -> {
			//
			SwingUtilities.invokeLater(() -> {
				//
				int rowCount = this.sendJTableName.getRowCount();
				Object[] newRow = { "새로운 행 " + (rowCount + 1), 7777, false };
				this.sendTableModel.addRow(newRow);
				
//				this.sendjTextAreaName
//						.append(SystemConstMessage.CRLF + "새로운 장치 행이 추가 되었습니다.");
			});

		});

        jButton12.setText("Remove Row");
        jButton12.addActionListener(e -> {
			//
			int selectedRow = this.sendJTableName.getSelectedRow();
			if (selectedRow != -1) {
				//
				this.sendTableModel.removeRow(selectedRow);

//				SwingUtilities.invokeLater(() -> {
//					//
//					this.sendjTextAreaName.append(
//							SystemConstMessage.CRLF + "서버 정보 " + selectedRow + " 행이 삭제 되었습니다.");
//				});

			}
		});
//        jTable31.setModel(new javax.swing.table.DefaultTableModel(
//            new Object [][] {
//                {null, null, null},
//                {null, null, null},
//                {null, null, null},
//                {null, null, null}
//            },
//            new String [] {
//                "Desc", "Open Port", "Open"
//            }
//        ) {
//            Class[] types = new Class [] {
//                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
//            };
//
//            public Class getColumnClass(int columnIndex) {
//                return types [columnIndex];
//            }
//        });
        this.sendJTableName.setShowGrid(true);
        jScrollPane35.setViewportView(this.sendJTableName);

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane35, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(jButton12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane35, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane24.setLeftComponent(jPanel44);

        jPanel45.setBorder(javax.swing.BorderFactory.createTitledBorder("ssend target client"));

//        jTable32.setModel(new javax.swing.table.DefaultTableModel(
//            new Object [][] {
//                {null, null, null},
//                {null, null, null},
//                {null, null, null},
//                {null, null, null}
//            },
//            new String [] {
//                "Desc", "Ip", "Port"
//            }
//        ) {
//            Class[] types = new Class [] {
//                java.lang.String.class, java.lang.String.class, java.lang.String.class
//            };
//
//            public Class getColumnClass(int columnIndex) {
//                return types [columnIndex];
//            }
//        });
        this.tcpTargetClientJTableName.setShowGrid(true);
        jScrollPane36.setViewportView(this.tcpTargetClientJTableName);

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane36, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane36, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane24.setRightComponent(jPanel45);

        jSplitPane23.setTopComponent(jSplitPane24);

        jPanel46.setBorder(javax.swing.BorderFactory.createTitledBorder("log"));

        jTextArea5.setColumns(20);
        jTextArea5.setRows(5);
        jScrollPane37.setViewportView(jTextArea5);

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane37, javax.swing.GroupLayout.DEFAULT_SIZE, 1422, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addComponent(jScrollPane37, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane23.setRightComponent(jPanel46);

        javax.swing.GroupLayout jPanel77Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel77Layout);
        jPanel77Layout.setHorizontalGroup(
            jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane23)
        );
        jPanel77Layout.setVerticalGroup(
            jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane23)
        );
        
        jTabbedPane1.addTab("tab7", jPanel11);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
        
    }// </editor-fold>                        

	public TcpServerTableModel getSendTableModel() {
		//
		return this.sendTableModel;
	}
	
	public String getSFIText() {
		//
		return this.jTextFieldSFI.getText();
	}
	
	public GlobalEntityManager getGlobalEntityManager() {
		//
		return this.globalEntityManager;
	}
	
	
	public boolean getTSQTestMode() {
		//
		return this.jCheckBoxTsqTestMode.isSelected();
	}
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(this.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(NewJFrame5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(NewJFrame5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(NewJFrame5.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Sample1().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btn_add_180;
    private javax.swing.JButton btn_add_10;
    private javax.swing.JButton btn_add_6;
    private javax.swing.JButton btn_add_2;
    
    private javax.swing.JButton btn_send_upper;
    private javax.swing.JButton btn_send_lower;
    
//    private javax.swing.JButton jButton2;
//    private javax.swing.JCheckBox jCheckBox1;
//    private javax.swing.JCheckBox jCheckBox2;
//    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
//    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
//    private javax.swing.JLabel jLabel15;
//    private javax.swing.JLabel jLabel2;
//    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
//    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
//    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
//    private javax.swing.JProgressBar jProgressBar1;
//    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JProgressBar jProgressBar3;
    private javax.swing.JProgressBar jProgressBar4;
//    private javax.swing.JProgressBar jProgressBar5;
//    private javax.swing.JProgressBar jProgressBar6;
//    private javax.swing.JProgressBar jProgressBar7;
//    private javax.swing.JProgressBar jProgressBar8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane10;
    private javax.swing.JSplitPane jSplitPane11;
    private javax.swing.JSplitPane jSplitPane12;
    private javax.swing.JSplitPane jSplitPane13;
    private javax.swing.JSplitPane jSplitPane14;
    private javax.swing.JSplitPane jSplitPane15;
    private javax.swing.JSplitPane jSplitPane16;
    private javax.swing.JSplitPane jSplitPane17;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JSplitPane jSplitPane6;
    private javax.swing.JSplitPane jSplitPane7;
    private javax.swing.JSplitPane jSplitPane8;
    private javax.swing.JSplitPane jSplitPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable12;
    private javax.swing.JTable jTable13;
    private javax.swing.JTable jTable14;
    private javax.swing.JTable jTable15;
    private javax.swing.JTable jTable16;
    private javax.swing.JTable jTable17;
    private javax.swing.JTable jTable18;
    private javax.swing.JTable jTable19;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable20;
    private javax.swing.JTable jTable21;
    private javax.swing.JTable jTable22;
    private javax.swing.JTable jTable23;
    private javax.swing.JTable jTable24;
    private javax.swing.JTable jTable25;
    private javax.swing.JTable jTable26;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
//    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    
    //=================================================================
    private javax.swing.JSplitPane jSplitPane18; // = new javax.swing.JSplitPane();
    private javax.swing.JPanel jPanel38; // = new javax.swing.JPanel();
    private javax.swing.JButton jButton3; // = new javax.swing.JButton();
    private javax.swing.JButton jButton4; // = new javax.swing.JButton();
    private javax.swing.JScrollPane jScrollPane29; // = new javax.swing.JScrollPane();
//    private javax.swing.JTable jTable27; // = new javax.swing.JTable();
    private javax.swing.JPanel jPanel39; // = new javax.swing.JPanel();
    private javax.swing.JScrollPane jScrollPane30; // = new javax.swing.JScrollPane();
//    private javax.swing.JTextArea jTextArea3; // = new javax.swing.JTextArea();
    //=================================================================
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
//    private javax.swing.JLabel jLabel18;
    private javax.swing.JPanel jPanel108;
    private javax.swing.JPanel jPanel109;
    private javax.swing.JPanel jPanel110;
    private javax.swing.JSplitPane jSplitPane108;
    private javax.swing.JSplitPane jSplitPane109;
    private javax.swing.JSplitPane jSplitPane110;
    private javax.swing.JScrollPane jScrollPane108;
    private javax.swing.JScrollPane jScrollPane109;
    private javax.swing.JScrollPane jScrollPane110;
    private javax.swing.JScrollPane jScrollPane108_1;
    private javax.swing.JScrollPane jScrollPane109_1;
    private javax.swing.JScrollPane jScrollPane110_1;
    
    private javax.swing.JCheckBox jCheckBoxTsqTestMode;
    // tab 6 start ================================================
    private javax.swing.JSplitPane jSplitPane19;
    private javax.swing.JSplitPane jSplitPane20;
    
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    
    private javax.swing.JScrollPane jScrollPane31;
    private javax.swing.JScrollPane jScrollPane32;
    private javax.swing.JScrollPane jScrollPane33;
    
    private javax.swing.JTextArea jTextArea4;
    
//    private javax.swing.JTable jTable28;
//    private javax.swing.JTable jTable29;
    // tab 6 end =================================================
    
    // tab 7 start ================================================
    private javax.swing.JSplitPane jSplitPane23;
    private javax.swing.JSplitPane jSplitPane24;
    
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    
    private javax.swing.JScrollPane jScrollPane35;
    private javax.swing.JScrollPane jScrollPane36;
    private javax.swing.JScrollPane jScrollPane37;
    
    private javax.swing.JTextArea jTextArea5;
    
//    private javax.swing.JTable jTable32;
    // tab 7 end =================================================
    // End of variables declaration                   
}