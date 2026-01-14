package com.all4land.generator.ui.tab.ais.component;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultCaret;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.ui.service.ResourceService;
import com.all4land.generator.ui.tab.ais.editor.CellEditor;
import com.all4land.generator.ui.tab.ais.editor.MmsiTableCellEditor;
import com.all4land.generator.ui.tab.ais.editor.MmsiTableCheckBoxEditor;
import com.all4land.generator.ui.tab.ais.entity.GlobalEntityManager;
import com.all4land.generator.ui.tab.ais.entity.GlobalSlotNumber;
import com.all4land.generator.ui.tab.ais.model.MmsiTableModel;
import com.all4land.generator.ui.tab.ais.model.TcpServerTableModel;
import com.all4land.generator.ui.tab.ais.model.TcpTargetClientTableModel;
import com.all4land.generator.ui.tab.ais.model.UdpServerTableModel;
import com.all4land.generator.ui.tab.ais.model.UdpTargetClientTableModel;
import com.all4land.generator.ui.tab.ais.renderer.BoldTextHeaderRenderer;
import com.all4land.generator.ui.tab.ais.renderer.CustomTableCellRenderer;
import com.all4land.generator.ui.tab.ais.renderer.MmsiTableCheckboxRenderer;
import com.all4land.generator.ui.util.GroupableColumnModel;
import com.all4land.generator.ui.util.GroupableTableHeader;
import com.all4land.generator.ui.view.popup.MMSIPopUpFrame;

@Lazy
@Configuration
public class MmsiComponent {
	//
	private final ResourceService resourceService;
	private final MmsiTableModel mmsiTableModel;
	
	private final TcpServerTableModel tcpServerTableModel;
	private final TcpTargetClientTableModel tcpTargetClientTableModel;
	
	private final UdpServerTableModel udpServerTableModel;
	private final UdpTargetClientTableModel udpTargetClientTableModel;
	
	private GlobalEntityManager globalEntityManager;
	private GlobalSlotNumber globalSlotNumber;

	MmsiComponent(MmsiTableModel mmsiTableModel
			, TcpServerTableModel tcpServerTableModel, TcpTargetClientTableModel tcpTargetClientTableModel, ResourceService resourceService
			, UdpServerTableModel udpServerTableModel, UdpTargetClientTableModel udpTargetClientTableModel
			) {
		//
		this.mmsiTableModel = mmsiTableModel;
		this.tcpServerTableModel = tcpServerTableModel;
		this.tcpTargetClientTableModel = tcpTargetClientTableModel;
		this.resourceService = resourceService;
		this.udpServerTableModel = udpServerTableModel;
		this.udpTargetClientTableModel= udpTargetClientTableModel;
	}

	public GlobalEntityManager getGlobalEntityManager() {
		return globalEntityManager;
	}

	@Autowired
	public void setGlobalEntityManager(@Lazy @Qualifier("uiGlobalEntityManager") GlobalEntityManager globalEntityManager) {
		this.globalEntityManager = globalEntityManager;
	}
	
	@Autowired
	public void setGlobalSlotNumber(@Lazy GlobalSlotNumber globalSlotNumber) {
		this.globalSlotNumber = globalSlotNumber;
	}

	@Bean(name = "jLabelSlotNumber")
	JLabel jLabelSlotNumber() {
		//
		JLabel jLabel = new JLabel();
		return jLabel;
	}
	
    @Bean(name = "jCheckBoxAisName")
    JCheckBox jCheckBoxAis() {
		//
		JCheckBox jCheckBox = new JCheckBox("AIS");
		jCheckBox.setSelected(true);
		jCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	//
                	globalEntityManager.setAisMsgDisplay(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                	//
                	globalEntityManager.setAisMsgDisplay(false);
                }
            }
        });
		return jCheckBox;
	}

    @Bean(name = "jCheckBoxAsmName")
    JCheckBox jCheckBoxAsm() {
		//
		JCheckBox jCheckBox = new JCheckBox("ASM");
		jCheckBox.setSelected(true);
		jCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	//
                	globalEntityManager.setAsmMsgDisplay(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                	//
                	globalEntityManager.setAsmMsgDisplay(false);
                }
            }
        });
		return jCheckBox;
	}

    @Bean(name = "jCheckBoxVdeName")
    JCheckBox jCheckBoxVde() {
		//
		JCheckBox jCheckBox = new JCheckBox("VDE");
		jCheckBox.setSelected(true);
		jCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	//
                	globalEntityManager.setVdeMsgDisplay(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                	//
                	globalEntityManager.setVdeMsgDisplay(false);
                }
            }
        });
		return jCheckBox;
	}

    @Bean(name = "jTextFieldSFI")
    JTextField jTextFieldSFI() {
		//
    	JTextField jTextField = new JTextField("MG0001");
		
		return jTextField;
	}
    
    //=============================================================================
    @Bean(name = "jProgressBarAisChannelA")
    JProgressBar jProgressBarAisChannelA() {
		//
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(SystemConstMessage.AIS_Color);
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		// Set the background color
		progressBar.setBackground(Color.BLACK);
		return progressBar;
	}

    @Bean(name = "jProgressBarAisChannelB")
    JProgressBar jProgressBarAisChannelB() {
		//
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(SystemConstMessage.AIS_Color);
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		// Set the background color
		progressBar.setBackground(Color.BLACK);
		return progressBar;
	}

    @Bean(name = "jProgressBarAsmChannelA")
    JProgressBar jProgressBarAsmChannelA() {
		//
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(SystemConstMessage.ASM_Color);
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		// Set the background color
		progressBar.setBackground(Color.BLACK);
		return progressBar;
	}

    @Bean(name = "jProgressBarAsmChannelB")
    JProgressBar jProgressBarAsmChannelB() {
		//
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(SystemConstMessage.ASM_Color);
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		// Set the background color
		progressBar.setBackground(Color.BLACK);
		return progressBar;
	}

    @Bean(name = "jProgressBarVdeUpper")
    JProgressBar jProgressBarVdeUpper() {
		//
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(SystemConstMessage.VDE_Color);
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		// Set the background color
		progressBar.setBackground(Color.BLACK);
		return progressBar;
	}

    @Bean(name = "jProgressBarVdeLower")
    JProgressBar jProgressBarVdeLower() {
		//
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(SystemConstMessage.VDE_Color);
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		// Set the background color
		progressBar.setBackground(Color.BLACK);
		return progressBar;
	}

    @Bean(name = "jLabelAisChannelAoccupancyinformation")
    JLabel jLabelAisChannelAoccupancyinformation() {
		//
		JLabel jLabel = new JLabel();
		jLabel.setText("0 %");
		return jLabel;
	}

    @Bean(name = "jLabelAisChannelBoccupancyinformation")
    JLabel jLabelAisChannelBoccupancyinformation() {
		//
		JLabel jLabel = new JLabel();
		jLabel.setText("0 %");
		return jLabel;
	}

    @Bean(name = "jLabelAsmChannelAoccupancyinformation")
    JLabel jLabelAsmChannelAoccupancyinformation() {
		//
		JLabel jLabel = new JLabel();
		jLabel.setText("0 %");
		return jLabel;
	}

    @Bean(name = "jLabelAsmChannelBoccupancyinformation")
    JLabel jLabelAsmChannelBoccupancyinformation() {
		//
		JLabel jLabel = new JLabel();
		jLabel.setText("0 %");
		return jLabel;
	}

    @Bean(name = "jLabelVdeUpperoccupancyinformation")
    JLabel jLabelVdeUpperoccupancyinformation() {
		//
		JLabel jLabel = new JLabel();
		jLabel.setText("0 %");
		return jLabel;
	}

    @Bean(name = "jLabelVdeLoweroccupancyinformation")
    JLabel jLabelVdeLoweroccupancyinformation() {
		//
		JLabel jLabel = new JLabel();
		jLabel.setText("0 %");
		return jLabel;
	}

    //=============================================================================

    @Bean(name = "aisTabjTextAreaName")
    JTextArea aisTabjTextArea() {
		//
		JTextArea jTextArea = new javax.swing.JTextArea();
		SwingUtilities.invokeLater(() -> {
			//
			jTextArea.setEditable(false);
			DefaultCaret caret = (DefaultCaret) jTextArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

			Timer timer = new Timer(3000, e -> {
				// 타이머가 실행될 때마다 caret을 업데이트
				caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
				// JTextArea의 텍스트 길이를 가져와서 caret을 텍스트의 끝으로 이동
				int textLength = jTextArea.getText().length();
				caret.setDot(textLength);
			});
			timer.start();

			jTextArea.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					checkAndClear();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					checkAndClear();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					// TODO Auto-generated method stub

				}

				private void checkAndClear() {
					// 라인 수가 1000개를 넘어가면 JTextArea를 클리어
					if (jTextArea.getLineCount() > 1000) {
						//
						SwingUtilities.invokeLater(() -> {
							jTextArea.setText("");
						});
					}
				}
			});
		});

		return jTextArea;
	}
    
    @Bean(name = "sendjTextAreaName")
    JTextArea sendjTextArea() {
		//
		JTextArea jTextArea = new javax.swing.JTextArea();
		SwingUtilities.invokeLater(() -> {
			//
			jTextArea.setEditable(false);
			DefaultCaret caret = (DefaultCaret) jTextArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

			Timer timer = new Timer(3000, e -> {
				// 타이머가 실행될 때마다 caret을 업데이트
				caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
				// JTextArea의 텍스트 길이를 가져와서 caret을 텍스트의 끝으로 이동
				int textLength = jTextArea.getText().length();
				caret.setDot(textLength);
			});
			timer.start();

			jTextArea.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					checkAndClear();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					checkAndClear();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					// TODO Auto-generated method stub

				}

				private void checkAndClear() {
					// 라인 수가 1000개를 넘어가면 JTextArea를 클리어
					if (jTextArea.getLineCount() > 1000) {
						//
						SwingUtilities.invokeLater(() -> {
							jTextArea.setText("");
						});
					}
				}
			});
		});

		return jTextArea;
	}

	@Bean(name = "mmsiJTableName")
	JTable mmsiJTable() {
		//
		this.mmsiTableModel.setGlobalEntityManager(this.globalEntityManager);
		JTable jTable = new JTable(this.mmsiTableModel) {
			//
			private static final long serialVersionUID = -1555561625151478076L;

			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				//
				if (column == 0) {
					return new MmsiTableCheckBoxEditor(); // 체크박스
				} else if (column == 1) {
					return new MmsiTableCellEditor(new JTextField(), false); // mmsi
				} else if (column == 2) {
//					return new MmsiColorButtonEditor();
					return new MmsiTableCheckBoxEditor(); // 체크박스
				} else if (column == 3) {
//					return new MmsiColorButtonEditor();
					return new MmsiTableCheckBoxEditor(); // 체크박스
				}
				return super.getCellEditor(row, column);
			}
		};

		// 테이블 셀 렌더러 설정
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		// 토글 버튼 셀 렌더러 설정
		jTable.getColumnModel().getColumn(0).setCellRenderer(new MmsiTableCheckboxRenderer());
		jTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		jTable.getColumnModel().getColumn(2).setCellRenderer(new MmsiTableCheckboxRenderer());
		jTable.getColumnModel().getColumn(3).setCellRenderer(new MmsiTableCheckboxRenderer());
		
		// JTableHeader의 배경색 설정
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		TableColumnModel columnModel = jTable.getColumnModel();
		
		columnModel.getColumn(0).setMinWidth(20); // 최소 너비 설정
		columnModel.getColumn(0).setMaxWidth(20); // 최대 너비 설정
		columnModel.getColumn(0).setWidth(20); // 초기 너비 설정
		
		columnModel.getColumn(2).setMinWidth(35); // 최소 너비 설정
		columnModel.getColumn(2).setMaxWidth(35); // 최대 너비 설정
		columnModel.getColumn(2).setWidth(35); // 초기 너비 설정
		
		columnModel.getColumn(3).setMinWidth(35); // 최소 너비 설정
		columnModel.getColumn(3).setMaxWidth(35); // 최대 너비 설정
		columnModel.getColumn(3).setWidth(35); // 초기 너비 설정
		
		jTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // 더블 클릭 확인
					int row = jTable.rowAtPoint(e.getPoint()); // 클릭된 포인트로부터 행 인덱스 가져오기
					int column = jTable.columnAtPoint(e.getPoint()); // 클릭된 포인트로부터 열 인덱스 가져오기
//					Object value = jTable.getValueAt(row, column); // 더블 클릭된 셀의 값 가져오기
					
					// 팝업 창 생성
					MMSIPopUpFrame popUpFrame = new MMSIPopUpFrame(mmsiTableModel.getMmsiEntity(row));
					popUpFrame.setSize(600, 450);

					// 클릭된 셀의 주변에 팝업 창 표시
					Point cellLocation = jTable.getCellRect(row, column, true).getLocation();
					popUpFrame.setLocation(cellLocation.x + jTable.getLocationOnScreen().x + 100,
							cellLocation.y + jTable.getLocationOnScreen().y);

					// 팝업 창 내용 설정
//		                popUpFrame.getJLabel2().setText(value.toString());

					// 팝업 창 표시
					popUpFrame.setVisible(true);

					// 팝업 창 생성
		                JDialog dialog = new JDialog();
		                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		                dialog.setTitle("팝업");
		                dialog.setSize(200, 100);
		                
		                // 클릭된 셀의 주변에 팝업 창 표시
		                Point cellLocation2 = jTable.getCellRect(row, column, true).getLocation();
		                dialog.setLocation(cellLocation2.x + jTable.getLocationOnScreen().x + 100, cellLocation2.y + jTable.getLocationOnScreen().y);
		                
		                // 팝업 창 내용 설정
		                JLabel label = new JLabel("MMSI count: " + mmsiTableModel.getRowCount());
		                label.setHorizontalAlignment(SwingConstants.CENTER);
		                dialog.add(label);
		                
		                // 팝업 창 표시
		                dialog.setVisible(true);
				}
			}
		});
		
		jTable.setShowGrid(true);

		return jTable;
	}

	@Bean(name = "udpServerJTableName")
	JTable udpServerJTableName() {
		//
		this.udpServerTableModel.setGlobalSlotNumber(this.globalSlotNumber);
		JTable jTable = new JTable(this.udpServerTableModel) {
			//
			private static final long serialVersionUID = 6792193789489233103L;
			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				//
				if (column == 0) {
					return new CellEditor(new JTextField(), true); // Desc
				} else if (column == 1) {
					return new CellEditor(new JTextField(), true); // MultiCast
				} else if (column == 2) {
					return new CellEditor(new JTextField(), true); // PORT
				} else if (column == 3) {
					return new CellEditor(new JCheckBox(), true); // 연결/종료
				} 
				return super.getCellEditor(row, column);
			}
		};

		jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                // Only process events when the selection is finalized
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = jTable.getSelectedRow();
                    if (selectedRow != -1) {
//                        System.out.println("Selected row: " + selectedRow);
//                        System.out.println("Selected value: " + jTable.getValueAt(selectedRow, 0) + " " + jTable.getValueAt(selectedRow, 1));
                    	udpServerTableModel.rowSelectedEvent(selectedRow);
                    }
                }
            }
        });
		
		// JTableHeader의 배경색 설정
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		// 토글 버튼 셀 렌더러 설정
		jTable.getColumnModel().getColumn(3).setCellRenderer(new MmsiTableCheckboxRenderer());
		jTable.setShowGrid(true);

		return jTable;
	}
	
	@Bean(name = "udpTargetClientJTableName")
	JTable udpTargetClientJTableName() {
		//
		
		JTable jTable = new JTable(this.udpTargetClientTableModel) {
			//
			private static final long serialVersionUID = 6792193789489233103L;
			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				//
				if (column == 0) {
					return new CellEditor(new JTextField(), true); // Desc
				} else if (column == 1) {
					return new CellEditor(new JTextField(), true); // IP
				} else if (column == 2) {
					return new CellEditor(new JTextField(), true); // PORT
				} else if (column == 3) {
					return new CellEditor(new JCheckBox(), true); // AIS
				} else if (column == 4) {
					return new CellEditor(new JCheckBox(), true); // ASM
				} else if (column == 5) {
					return new CellEditor(new JCheckBox(), true); // TSQ
				} 
				return super.getCellEditor(row, column);
			}
		};

		// JTableHeader의 배경색 설정
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		// 토글 버튼 셀 렌더러 설정
		jTable.getColumnModel().getColumn(3).setCellRenderer(new MmsiTableCheckboxRenderer());
		jTable.getColumnModel().getColumn(4).setCellRenderer(new MmsiTableCheckboxRenderer());
		jTable.getColumnModel().getColumn(5).setCellRenderer(new MmsiTableCheckboxRenderer());
		
		jTable.setShowGrid(true);

		return jTable;
	}
	
	@Bean(name = "sendJTableName")
	JTable sendJTable() {
		//
//		this.tcpServerTableModel.setSendjTextAreaName(this.sendjTextArea());
		this.tcpServerTableModel.setGlobalSlotNumber(this.globalSlotNumber);
		JTable jTable = new JTable(this.tcpServerTableModel) {
			//
			private static final long serialVersionUID = 6792193789489233103L;
			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				//
				if (column == 0) {
					return new CellEditor(new JTextField(), true); // Desc
				} else if (column == 1) {
					return new CellEditor(new JTextField(), true); // PORT
				} else if (column == 2) {
					return new CellEditor(new JCheckBox(), true); // 연결/종료
				} 
//				else if (column == 3) {
//					return new CellEditor(new JCheckBox(), true); // 체크박스 AIS
//				} else if (column == 4) {
//					return new CellEditor(new JCheckBox(), true); // 체크박스 ASM
//				} else if (column == 5) {
//					return new CellEditor(new JCheckBox(), true); // 체크박스 VDE UP
//				} else if (column == 6) {
//					return new CellEditor(new JCheckBox(), true); // 체크박스 VDE LO
//				}
				return super.getCellEditor(row, column);
			}
		};

		jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                // Only process events when the selection is finalized
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = jTable.getSelectedRow();
                    if (selectedRow != -1) {
//                        System.out.println("Selected row: " + selectedRow);
//                        System.out.println("Selected value: " + jTable.getValueAt(selectedRow, 0) + " " + jTable.getValueAt(selectedRow, 1));
                    	tcpServerTableModel.rowSelectedEvent(selectedRow);
                    }
                }
            }
        });
		
		// JTableHeader의 배경색 설정
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		// 토글 버튼 셀 렌더러 설정
		jTable.getColumnModel().getColumn(2).setCellRenderer(new MmsiTableCheckboxRenderer());
//		jTable.getColumnModel().getColumn(3).setCellRenderer(new MmsiTableCheckboxRenderer());
//		jTable.getColumnModel().getColumn(4).setCellRenderer(new MmsiTableCheckboxRenderer());
//		jTable.getColumnModel().getColumn(5).setCellRenderer(new MmsiTableCheckboxRenderer());
//		jTable.getColumnModel().getColumn(6).setCellRenderer(new MmsiTableCheckboxRenderer());
		
		
		jTable.setShowGrid(true);

		return jTable;
	}
	
	@Bean(name = "tcpTargetClientJTableName")
	JTable tcpTargetClientJTableName() {
		//
		
		JTable jTable = new JTable(this.tcpTargetClientTableModel) {
			//
			private static final long serialVersionUID = 6792193789489233103L;
			@Override
			public TableCellEditor getCellEditor(int row, int column) {
				//
				if (column == 0) {
					return new CellEditor(new JTextField(), true); // Desc
				} else if (column == 1) {
					return new CellEditor(new JTextField(), true); // IP
				} else if (column == 2) {
					return new CellEditor(new JTextField(), true); // PORT
				} else if (column == 3) {
					return new CellEditor(new JCheckBox(), true); // AIS
				} else if (column == 4) {
					return new CellEditor(new JCheckBox(), true); // ASM
				} else if (column == 5) {
					return new CellEditor(new JCheckBox(), true); // TSQ
				} 
				return super.getCellEditor(row, column);
			}
		};

		// JTableHeader의 배경색 설정
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		// 토글 버튼 셀 렌더러 설정
		jTable.getColumnModel().getColumn(3).setCellRenderer(new MmsiTableCheckboxRenderer());
		jTable.getColumnModel().getColumn(4).setCellRenderer(new MmsiTableCheckboxRenderer());
		jTable.getColumnModel().getColumn(5).setCellRenderer(new MmsiTableCheckboxRenderer());
		
		jTable.setShowGrid(true);

		return jTable;
	}
	
	@Bean(name = "currentFrameJTableNameUpper")
	JTable currentFrameJTableUpper() {
		//
		JTable jTable = new JTable() {
			//
			private static final long serialVersionUID = 6534225932102582317L;

			@Override
			public boolean editCellAt(int row, int column, EventObject e) {
				return false; // Disable cell editing
			}
		};
		jTable.setAutoCreateColumnsFromModel(false);
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_0));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);

		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		MouseListener mouseListener = new MouseAdapter() {
		    private long lastClickTime = 0;
		    private final long doubleClickDelay = 300; // 더블 클릭 간격 (밀리초)

			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
		        long clickTime = System.currentTimeMillis();
		        if (clickTime - lastClickTime <= doubleClickDelay) {
		            // 더블 클릭 이벤트 처리
		            int row = jTable.getSelectedRow();
		            int column = jTable.getSelectedColumn();
		            
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) jTable.getDefaultRenderer(Object.class);
					String slotNumber = renderer.getCellSlotNumber(row, column);
		            // 이제 여기서 더블 클릭된 셀에 대한 작업을 수행할 수 있습니다.
		            System.out.println("currentFrameJTableNameUpper : (" + row + ", " + column + ")");
		            resourceService.resourceStart(row, column, slotNumber);
		        }
		        lastClickTime = clickTime;
			}
		};
		jTable.addMouseListener(mouseListener);
		
		// 툴팁 딜레이 조절
		ToolTipManager.sharedInstance().setInitialDelay(1); // 초기 지연 시간 1000 밀리초 (1초)
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE); // 숨김 지연 시간을 최대값으로 설정

		this.tcpServerTableModel.setCurrentFrameJTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrameJTableNameUpper(jTable);
		return jTable;
	}
	
	@Bean(name = "currentFrameJTableNameLower")
	JTable currentFrameJTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable() {
			//
			private static final long serialVersionUID = 6534225932102582317L;

			@Override
			public boolean editCellAt(int row, int column, EventObject e) {
				return false; // Disable cell editing
			}
		};
		jTable.setAutoCreateColumnsFromModel(false);
		
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_0));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);

		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		MouseListener mouseListener = new MouseAdapter() {
		    private long lastClickTime = 0;
		    private final long doubleClickDelay = 300; // 더블 클릭 간격 (밀리초)

			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
		        long clickTime = System.currentTimeMillis();
		        if (clickTime - lastClickTime <= doubleClickDelay) {
		            // 더블 클릭 이벤트 처리
		            int row = jTable.getSelectedRow();
		            int column = jTable.getSelectedColumn();
		            
					CustomTableCellRenderer renderer = (CustomTableCellRenderer) jTable.getDefaultRenderer(Object.class);
					String slotNumber = renderer.getCellSlotNumber(row, column);
		            // 이제 여기서 더블 클릭된 셀에 대한 작업을 수행할 수 있습니다.
		            System.out.println("currentFrameJTableNameLower : (" + row + ", " + column + ")");
		            resourceService.resourceStart(row, column, slotNumber);
		        }
		        lastClickTime = clickTime;
			}
		};
		jTable.addMouseListener(mouseListener);

		// 툴팁 딜레이 조절
		ToolTipManager.sharedInstance().setInitialDelay(1); // 초기 지연 시간 1000 밀리초 (1초)
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE); // 숨김 지연 시간을 최대값으로 설정

		this.tcpServerTableModel.setCurrentFrameJTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrameJTableNameLower(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame1JTableNameUpper")
	JTable currentFrame1JTableUpper() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_1));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame1JTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrame1JTableNameUpper(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame2JTableNameUpper")
	JTable currentFrame2JTableUpper() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_2));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame2JTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrame2JTableNameUpper(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame3JTableNameUpper")
	JTable currentFrame3JTableUpper() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_3));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame3JTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrame3JTableNameUpper(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame4JTableNameUpper")
	JTable currentFrame4JTableUpper() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_4));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame4JTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrame4JTableNameUpper(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame5JTableNameUpper")
	JTable currentFrame5JTableUpper() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_5));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame5JTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrame5JTableNameUpper(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame6JTableNameUpper")
	JTable currentFrame6JTableUpper() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_6));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame6JTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrame6JTableNameUpper(jTable);
		return jTable;
	}
	
	@Bean(name = "currentFrame7JTableNameUpper")
	JTable currentFrame7JTableUpper() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_7));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame7JTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrame7JTableNameUpper(jTable);
		return jTable;
	}
	
	@Bean(name = "currentFrame8JTableNameUpper")
	JTable currentFrame8JTableUpper() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_8));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame8JTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrame8JTableNameUpper(jTable);
		return jTable;
	}
	
	@Bean(name = "currentFrame9JTableNameUpper")
	JTable currentFrame9JTableUpper() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_9));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame9JTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrame9JTableNameUpper(jTable);
		return jTable;
	}
	
	@Bean(name = "currentFrame10JTableNameUpper")
	JTable currentFrame10JTableUpper() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, true, SystemConstMessage.PREFIX_CONTINUES_COUNT_10));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Upper Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame10JTableNameUpper(jTable);
		this.udpServerTableModel.setCurrentFrame10JTableNameUpper(jTable);
		return jTable;
	}
	
	@Bean(name = "currentFrame1JTableNameLower")
	JTable currentFrame1JTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_1));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame1JTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrame1JTableNameLower(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame2JTableNameLower")
	JTable currentFrame2JTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_2));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");

		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame2JTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrame2JTableNameLower(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame3JTableNameLower")
	JTable currentFrame3JTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_3));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame3JTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrame3JTableNameLower(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame4JTableNameLower")
	JTable currentFrame4JTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_4));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame4JTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrame4JTableNameLower(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame5JTableNameLower")
	JTable currentFrame5JTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_5));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");

		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame5JTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrame5JTableNameLower(jTable);
		return jTable;
	}

	@Bean(name = "currentFrame6JTableNameLower")
	JTable currentFrame6JTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_6));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");

		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		// JTableHeader의 배경색 설정
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame6JTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrame6JTableNameLower(jTable);
		return jTable;
	}
	
	@Bean(name = "currentFrame7JTableNameLower")
	JTable currentFrame7JTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_7));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		// JTableHeader의 배경색 설정
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame7JTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrame7JTableNameLower(jTable);
		return jTable;
	}
	
	@Bean(name = "currentFrame8JTableNameLower")
	JTable currentFrame8JTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_8));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		// JTableHeader의 배경색 설정
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame8JTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrame8JTableNameLower(jTable);
		return jTable;
	}
	
	@Bean(name = "currentFrame9JTableNameLower")
	JTable currentFrame9JTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_9));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		// JTableHeader의 배경색 설정
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame9JTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrame9JTableNameLower(jTable);
		return jTable;
	}
	
	@Bean(name = "currentFrame10JTableNameLower")
	JTable currentFrame10JTableLower() {
		//
		DefaultTableModel tableModel = new DefaultTableModel(0, 32);
		JTable jTable = new JTable();
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(tableModel, false, SystemConstMessage.PREFIX_CONTINUES_COUNT_10));

		jTable.setModel(tableModel);

		// 헤더 만들기
		this.makeHeader(jTable, "Lower Leg.");
		// 컬럼 사이즈 조절
		this.setColumnSize(jTable);
		// 행 사이즈 조절
		this.setRowSize(jTable);
		// JTableHeader의 배경색 설정
		JTableHeader tableHeader = jTable.getTableHeader();
		tableHeader.setResizingAllowed(false);
		tableHeader.setDefaultRenderer(new BoldTextHeaderRenderer());

		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setAutoscrolls(false);
		jTable.setCellSelectionEnabled(false);
		jTable.setMaximumSize(new java.awt.Dimension(1100, 400));
		jTable.setMinimumSize(new java.awt.Dimension(1000, 400));
		jTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		jTable.getTableHeader().setReorderingAllowed(false);
		// JTable의 에디터 비활성화
		jTable.setEnabled(false);
		// focus
		jTable.setFocusable(false);
		jTable.getTableHeader().setFocusable(false);

		// 클릭 및 더블클릭 리스너 생성
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 클릭 및 더블클릭 이벤트 처리
				// 이 부분을 비워두면 해당 이벤트를 무시하게 됩니다.
			}
		};

		// JTable에 리스너 추가
		jTable.addMouseListener(mouseListener);
		this.tcpServerTableModel.setCurrentFrame10JTableNameLower(jTable);
		this.udpServerTableModel.setCurrentFrame10JTableNameLower(jTable);
		return jTable;
	}

	private void makeHeader(JTable jTable, String value) {
		//
		GroupableColumnModel model = new GroupableColumnModel();
		model.addColumn(createColumn("", 0));
		model.addColumn(createColumn("", 1));
		model.addColumn(createColumn("", 2));
		model.addColumn(createColumn("", 3));
		model.addColumn(createColumn("", 4));
		model.addColumn(createColumn("", 5));
		model.addColumn(createColumn("", 6));
		model.addColumn(createColumn("", 7));
		model.addColumn(createColumn("", 8));
		model.addColumn(createColumn("", 9));
		model.addColumn(createColumn("", 10));
		model.addColumn(createColumn("", 11));
		model.addColumn(createColumn("", 12));
		model.addColumn(createColumn("", 13));
		model.addColumn(createColumn("", 14));
		model.addColumn(createColumn("", 15));
		model.addColumn(createColumn("", 16));
		model.addColumn(createColumn("", 17));
		model.addColumn(createColumn("", 18));
		model.addColumn(createColumn("", 19));
		model.addColumn(createColumn("", 20));
		model.addColumn(createColumn("", 21));
		model.addColumn(createColumn("", 22));
		model.addColumn(createColumn("", 23));
		model.addColumn(createColumn("", 24));
		model.addColumn(createColumn("", 25));
		model.addColumn(createColumn("", 26));
		model.addColumn(createColumn("", 27));
		model.addColumn(createColumn("", 28));
		model.addColumn(createColumn("", 29));
		model.addColumn(createColumn("", 30));
		model.addColumn(createColumn("", 31));
		GroupableColumnModel.IColumnGroup groupA = model.addGroup(value);
		groupA.addColumn(model.getColumn(model.getColumnIndex(0)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(1)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(2)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(3)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(4)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(5)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(6)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(7)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(8)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(9)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(10)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(11)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(12)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(13)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(14)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(15)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(16)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(17)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(18)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(19)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(20)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(21)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(22)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(23)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(24)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(25)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(26)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(27)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(28)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(29)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(30)));
		groupA.addColumn(model.getColumn(model.getColumnIndex(31)));
		
		jTable.setColumnModel(model);
		jTable.setTableHeader(new GroupableTableHeader(jTable.getColumnModel()));

	}

	private void setColumnSize(JTable jTable) {
		//
		TableColumnModel columnModel = jTable.getColumnModel();
	    
	    // 첫 번째 열의 너비 설정
	    TableColumn firstColumn = columnModel.getColumn(0);
	    firstColumn.setMinWidth(35);
	    firstColumn.setMaxWidth(35);
	    firstColumn.setWidth(35);

	    // 나머지 열의 너비 설정
	    for (int i = 1; i < columnModel.getColumnCount(); i++) {
	        TableColumn column = columnModel.getColumn(i);
	        column.setMinWidth(i == 16 ? 35 : 23);
	        column.setMaxWidth(i == 16 ? 35 : 23);
	        column.setWidth(i == 16 ? 35 : 23);
	    }
//		TableColumnModel columnModel = jTable.getColumnModel();
//		columnModel.getColumn(0).setMinWidth(35); // 최소 너비 설정
//		columnModel.getColumn(0).setMaxWidth(35); // 최대 너비 설정
//		columnModel.getColumn(0).setWidth(35); // 초기 너비 설정
//
//		int columnCnt = columnModel.getColumnCount();
//
//		for (int i = 1; i < columnCnt; i++) {
//			//
//			if(i == 16) {
//				columnModel.getColumn(i).setMinWidth(35); // 최소 너비 설정
//				columnModel.getColumn(i).setMaxWidth(35); // 최대 너비 설정
//				columnModel.getColumn(i).setWidth(35); // 초기 너비 설정
//			}else {
//				columnModel.getColumn(i).setMinWidth(23); // 최소 너비 설정
//				columnModel.getColumn(i).setMaxWidth(23); // 최대 너비 설정
//				columnModel.getColumn(i).setWidth(23); // 초기 너비 설정
//			}
//		}
	}
	
	private void setRowSize(JTable jTable) {
		//
		// 행 크기를 설정할 행 번호 리스트
	    int[] rowNumbers = {6, 13, 20, 27, 34, 41, 48, 55, 62, 69, 76, 83};
	    // 설정할 행의 높이
	    int rowHeight = 10;
	    // 각 행 번호에 대해 설정
	    for (int rowNumber : rowNumbers) {
	        jTable.setRowHeight(rowNumber, rowHeight);
	    }
//		jTable.setRowHeight(6, 10);
//		jTable.setRowHeight(13, 10);
//		jTable.setRowHeight(20, 10);
//		jTable.setRowHeight(27, 10);
//		jTable.setRowHeight(34, 10);
//		jTable.setRowHeight(41, 10);
//		jTable.setRowHeight(48, 10);
//		jTable.setRowHeight(55, 10);
//		jTable.setRowHeight(62, 10);
//		jTable.setRowHeight(69, 10);
//		jTable.setRowHeight(76, 10);
//		jTable.setRowHeight(83, 10);
	}
	
	protected TableColumn createColumn(String title, int modelIndex) {
		TableColumn column = new TableColumn();
		column.setHeaderValue(title);
		column.setIdentifier(modelIndex);
		column.setModelIndex(modelIndex);
		return column;
	}
}
