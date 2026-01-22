package com.all4land.generator.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Scheduler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.all4land.generator.ais.ASMMessageUtil;
import com.all4land.generator.ais.AisMessage1Util;
import com.all4land.generator.system.constant.SystemConstMessage;
import com.all4land.generator.system.constant.SystemConstTestData180;
import com.all4land.generator.system.schedule.QuartzCoreService;
import com.all4land.generator.system.util.BeanUtils;
import com.all4land.generator.entity.TargetCellInfoEntity;
import com.all4land.generator.entity.SlotStateManager;
import com.all4land.generator.util.RandomGenerator;

import dk.dma.ais.sentence.Vdm;

@Component
public class GlobalEntityManager {
	//
	private final String uuid = "00"+String.valueOf(RandomGenerator.generateRandomInt(7));
	private final ASMMessageUtil aSMMessageUtil;
	private final ApplicationEventPublisher eventPublisher;
	private final SlotStateManager slotStateManager;
	Set<Long> generatedMmsiSet = new HashSet<>();
	// UI 제거로 인해 주석 처리
	// private JTable currentFrameJTableNameUpper;
	// private JTable currentFrame1JTableNameUpper;
	// private JTable currentFrame2JTableNameUpper;
	// private JTable currentFrame3JTableNameUpper;
	// private JTable currentFrame4JTableNameUpper;
	// private JTable currentFrame5JTableNameUpper;
	// private JTable currentFrame6JTableNameUpper;
	// private JTable currentFrame7JTableNameUpper;
	// private JTable currentFrame8JTableNameUpper;
	// private JTable currentFrame9JTableNameUpper;
	// private JTable currentFrame10JTableNameUpper;
	//
	// private JTable currentFrameJTableNameLower;
	// private JTable currentFrame1JTableNameLower;
	// private JTable currentFrame2JTableNameLower;
	// private JTable currentFrame3JTableNameLower;
	// private JTable currentFrame4JTableNameLower;
	// private JTable currentFrame5JTableNameLower;
	// private JTable currentFrame6JTableNameLower;
	// private JTable currentFrame7JTableNameLower;
	// private JTable currentFrame8JTableNameLower;
	// private JTable currentFrame9JTableNameLower;
	// private JTable currentFrame10JTableNameLower;

	private boolean aisMsgDisplay = true;
	private boolean asmMsgDisplay = true;
	private boolean vdeMsgDisplay = true;

	private List<MmsiEntity> mmsiEntityLists;
	// UI 제거로 인해 주석 처리
	// private MmsiTableModel mmsiTableModel2;

	// UI 제거로 인해 주석 처리
	// private JTextField jTextFieldSFI;
	private String sfiValue; // UI 제거 후 SFI 값을 문자열로 저장
	
	GlobalEntityManager(ApplicationEventPublisher eventPublisher, ASMMessageUtil aSMMessageUtil, SlotStateManager slotStateManager) {
		// 생성자에서 리스트를 초기화합니다.
		this.eventPublisher = eventPublisher;
		this.aSMMessageUtil = aSMMessageUtil;
		this.slotStateManager = slotStateManager;
		this.mmsiEntityLists = new ArrayList<>();
	}

	// UI 제거로 인해 주석 처리
	// public void setMmsiTableModel(MmsiTableModel mmsiTableModel2) {
	//	//
	//	this.mmsiTableModel2 = mmsiTableModel2;
	// }

	public List<MmsiEntity> getMmsiEntityLists() {
		//
		return this.mmsiEntityLists;
	}

	public void setMmsiEntityLists(List<MmsiEntity> mmsiEntityLists) {
		//
		this.mmsiEntityLists = mmsiEntityLists;
		// UI 제거로 인해 주석 처리
		// this.mmsiTableModel2.setData();
	}

	public void addMmsiEntityLists(List<MmsiEntity> mmsiEntityLists) {
		//
		this.mmsiEntityLists.addAll(mmsiEntityLists);
		// UI 제거로 인해 주석 처리
		// this.mmsiTableModel2.setData();
	}
	
	/**
	 * MMSI로 선박을 찾아서 반환합니다.
	 */
	public MmsiEntity findMmsiEntity(long mmsi) {
		if (this.mmsiEntityLists == null) {
			return null;
		}
		for (MmsiEntity entity : this.mmsiEntityLists) {
			if (entity.getMmsi() == mmsi) {
				return entity;
			}
		}
		return null;
	}
	
	/**
	 * MMSI별로 메시지 생성 상태를 제어합니다.
	 * @param mmsi MMSI 번호
	 * @param state "0"=OFF(중단), "1"=ON(시작/재개)
	 * @return 제어 성공 여부
	 */
	public boolean controlMmsiState(long mmsi, String state) {
		return controlMmsiState(mmsi, state, null);
	}

	/**
	 * MMSI별로 메시지 생성 상태를 제어합니다 (destMMSI 포함).
	 * @param mmsi MMSI 번호
	 * @param state "0"=OFF(중단), "1"=ON(시작/재개)
	 * @param destMMSIList destMMSI 리스트 (null이면 기존 동작)
	 * @return 제어 성공 여부
	 */
	public boolean controlMmsiState(long mmsi, String state, List<Long> destMMSIList) {
		MmsiEntity mmsiEntity = findMmsiEntity(mmsi);
		if (mmsiEntity == null) {
			System.out.println("[DEBUG] ⚠️ MMSI를 찾을 수 없음: " + mmsi);
			return false;
		}
		
		// destMMSI 리스트가 제공된 경우 destMMSI 기능 사용
		boolean useDestMMSI = (destMMSIList != null && !destMMSIList.isEmpty());
		System.out.println("[DEBUG] controlMmsiState - MMSI: " + mmsi + ", state: " + state + 
				", useDestMMSI: " + useDestMMSI + ", destMMSIList: " + destMMSIList);
		
		if ("1".equals(state)) {
			// ON: 메시지 생성 시작/재개
			if (useDestMMSI) {
				System.out.println("[DEBUG] destMMSI 사용 모드 - MMSI: " + mmsi);
				// destMMSI 리스트에 추가
				for (Long destMMSI : destMMSIList) {
					if (destMMSI != null) {
						mmsiEntity.addDestMMSI(destMMSI);
					}
				}
				// destMMSI 리스트가 비어있지 않으면 메시지 생성 시작
				boolean hasDestMMSI = mmsiEntity.hasDestMMSI();
				System.out.println("[DEBUG] addDestMMSI 후 hasDestMMSI 체크 - MMSI: " + mmsi + 
						", hasDestMMSI: " + hasDestMMSI + ", 리스트 크기: " + mmsiEntity.getDestMMSIList().size());
				if (hasDestMMSI) {
					if (!mmsiEntity.isChk()) {
						System.out.println("[DEBUG] AIS 활성화 시작 (destMMSI 사용) - MMSI: " + mmsi);
						mmsiEntity.setChk(true);
						System.out.println("[DEBUG] ✅ AIS 활성화 완료 - MMSI: " + mmsi + 
								", destMMSI 리스트 크기: " + mmsiEntity.getDestMMSIList().size());
					} else {
						System.out.println("[DEBUG] ⚠️ AIS가 이미 활성화되어 있음 (destMMSI 사용) - MMSI: " + mmsi);
					}
				} else {
					System.out.println("[DEBUG] ⚠️ destMMSI 리스트가 비어있어 메시지 생성하지 않음 - MMSI: " + mmsi);
				}
			} else {
				// 기존 동작: destMMSI 없이 state만으로 제어
				if (!mmsiEntity.isChk()) {
					System.out.println("[DEBUG] AIS 활성화 시작 - MMSI: " + mmsi);
					mmsiEntity.setChk(true);
					System.out.println("[DEBUG] ✅ AIS 활성화 완료 - MMSI: " + mmsi);
				} else {
					System.out.println("[DEBUG] ⚠️ AIS가 이미 활성화되어 있음 - MMSI: " + mmsi);
				}
			}
			return true;
		} else if ("0".equals(state)) {
			// OFF: 메시지 생성 중단
			if (useDestMMSI) {
				// destMMSI 리스트에서 제거
				for (Long destMMSI : destMMSIList) {
					if (destMMSI != null) {
						mmsiEntity.removeDestMMSI(destMMSI);
					}
				}
				// destMMSI 리스트가 비어있으면 메시지 생성 중단
				if (!mmsiEntity.hasDestMMSI()) {
					if (mmsiEntity.isChk()) {
						System.out.println("[DEBUG] AIS 비활성화 시작 (destMMSI 리스트 비어있음) - MMSI: " + mmsi);
						mmsiEntity.setChk(false);
						System.out.println("[DEBUG] ✅ AIS 비활성화 완료 - MMSI: " + mmsi);
					} else {
						System.out.println("[DEBUG] ⚠️ AIS가 이미 비활성화되어 있음 - MMSI: " + mmsi);
					}
				} else {
					System.out.println("[DEBUG] ℹ️ destMMSI 리스트에 항목이 남아있어 메시지 생성 계속 - MMSI: " + mmsi + 
							", 리스트 크기: " + mmsiEntity.getDestMMSIList().size());
				}
			} else {
				// 기존 동작: destMMSI 없이 state만으로 제어
				if (mmsiEntity.isChk()) {
					System.out.println("[DEBUG] AIS 비활성화 시작 - MMSI: " + mmsi);
					mmsiEntity.setChk(false);
					System.out.println("[DEBUG] ✅ AIS 비활성화 완료 - MMSI: " + mmsi);
				} else {
					System.out.println("[DEBUG] ⚠️ AIS가 이미 비활성화되어 있음 - MMSI: " + mmsi);
				}
			}
			return true;
		} else {
			System.out.println("[DEBUG] ❌ 유효하지 않은 state 값: " + state + " (MMSI: " + mmsi + ")");
			return false;
		}
	}
	
	/**
	 * ASM 메시지 생성 상태를 제어합니다.
	 * @param mmsi 선박 MMSI
	 * @param state "0"=OFF, "1"=ON
	 * @param size 슬롯 점유 개수 (1~3)
	 * @param asmPeriod "0"=단문 메시지, "1"=계속 보내는 메시지
	 * @param quartzCoreService QuartzCoreService (스케줄 삭제용)
	 * @return 성공 여부
	 */
	public boolean controlAsmState(long mmsi, String state, String size, String asmPeriod, QuartzCoreService quartzCoreService) {
		return controlAsmState(mmsi, state, size, asmPeriod, quartzCoreService, null);
	}

	/**
	 * ASM 메시지 생성 상태를 제어합니다 (destMMSI 포함).
	 * @param mmsi 선박 MMSI
	 * @param state "0"=OFF, "1"=ON
	 * @param size 슬롯 점유 개수 (1~3)
	 * @param asmPeriod "0"=단문 메시지, "1"=계속 보내는 메시지
	 * @param quartzCoreService QuartzCoreService (스케줄 삭제용)
	 * @param destMMSIList destMMSI 리스트 (null이면 기존 동작)
	 * @return 성공 여부
	 */
	public boolean controlAsmState(long mmsi, String state, String size, String asmPeriod, QuartzCoreService quartzCoreService, List<Long> destMMSIList) {
		MmsiEntity mmsiEntity = findMmsiEntity(mmsi);
		if (mmsiEntity == null) {
			System.out.println("[DEBUG] ❌ MMSI를 찾을 수 없음: " + mmsi);
			return false;
		}

		// destMMSI 리스트가 제공된 경우 destMMSI 기능 사용
		boolean useDestMMSI = (destMMSIList != null && !destMMSIList.isEmpty());
		System.out.println("[DEBUG] controlAsmState - MMSI: " + mmsi + ", state: " + state + 
				", useDestMMSI: " + useDestMMSI + ", destMMSIList: " + destMMSIList);

		boolean newState = "1".equals(state);
		
		// 슬롯 개수 설정 (1~3)
		if (size != null && !size.isEmpty()) {
			try {
				int slotCount = Integer.parseInt(size);
				if (slotCount >= 1 && slotCount <= 3) {
					mmsiEntity.getAsmEntity().setSlotCount(slotCount);
					System.out.println("[DEBUG] ✅ MMSI: " + mmsi + " ASM 슬롯 개수 설정: " + slotCount);
				} else {
					System.out.println("[DEBUG] ⚠️ MMSI: " + mmsi + " 유효하지 않은 슬롯 개수: " + slotCount + " (1~3 범위여야 함)");
				}
			} catch (NumberFormatException e) {
				System.out.println("[DEBUG] ⚠️ MMSI: " + mmsi + " 슬롯 개수 파싱 실패: " + size);
			}
		}
		
		// asmPeriod 값 결정 (기본값 "1")
		String asmPeriodValue = (asmPeriod != null && !asmPeriod.isEmpty()) ? asmPeriod : "1";
		
		// ASM 상태 변경
		if ("1".equals(state)) {
			// ON: 메시지 생성 시작/재개
			if (useDestMMSI) {
				System.out.println("[DEBUG] ASM destMMSI 사용 모드 - MMSI: " + mmsi);
				// destMMSI 리스트에 추가 (asmPeriod와 함께, mmsi는 추가하지 않음)
				for (Long destMMSI : destMMSIList) {
					if (destMMSI != null) {
						mmsiEntity.getAsmEntity().addDestMMSI(destMMSI, asmPeriodValue);
					}
				}
				// destMMSI 리스트가 비어있지 않으면 메시지 생성 시작
				boolean hasDestMMSI = mmsiEntity.getAsmEntity().hasDestMMSI();
				System.out.println("[DEBUG] addDestMMSI 후 hasDestMMSI 체크 - MMSI: " + mmsi + 
						", hasDestMMSI: " + hasDestMMSI + ", 리스트 크기: " + mmsiEntity.getAsmEntity().getDestMMSIList().size());
				if (hasDestMMSI) {
					if (!mmsiEntity.isAsm()) {
						System.out.println("[DEBUG] ASM 활성화 시작 (destMMSI 사용) - MMSI: " + mmsi);
						mmsiEntity.setAsm(true);
						System.out.println("[DEBUG] ✅ ASM 활성화 완료 - MMSI: " + mmsi + 
								", destMMSI 리스트 크기: " + mmsiEntity.getAsmEntity().getDestMMSIList().size());
					} else {
						System.out.println("[DEBUG] ⚠️ ASM이 이미 활성화되어 있음 (destMMSI 사용) - MMSI: " + mmsi);
					}
				} else {
					System.out.println("[DEBUG] ⚠️ destMMSI 리스트가 비어있어 ASM 메시지 생성하지 않음 - MMSI: " + mmsi);
				}
			} else {
				// 기존 동작: destMMSI 없이 state만으로 제어
				if (!mmsiEntity.isAsm()) {
					System.out.println("[DEBUG] ASM 활성화 시작 - MMSI: " + mmsi);
					mmsiEntity.setAsm(true);
					System.out.println("[DEBUG] ✅ ASM 활성화 완료 - MMSI: " + mmsi);
				} else {
					System.out.println("[DEBUG] ⚠️ ASM이 이미 활성화되어 있음 - MMSI: " + mmsi);
				}
			}
		} else if ("0".equals(state)) {
			// OFF: 메시지 생성 중단
			if (useDestMMSI) {
				// destMMSI 리스트에서 제거 (destMMSI 배열의 각 값만 제거, mmsi는 제거하지 않음)
				for (Long destMMSI : destMMSIList) {
					if (destMMSI != null) {
						mmsiEntity.getAsmEntity().removeDestMMSI(destMMSI);
					}
				}
				// destMMSI 리스트가 비어있으면 메시지 생성 중단
				if (!mmsiEntity.getAsmEntity().hasDestMMSI()) {
					if (mmsiEntity.isAsm()) {
						System.out.println("[DEBUG] ASM 비활성화 시작 (destMMSI 리스트 비어있음) - MMSI: " + mmsi);
						// setAsm(false)가 내부에서 스케줄러 job을 제거하므로 별도 호출 불필요
						mmsiEntity.setAsm(false);
						System.out.println("[DEBUG] ✅ ASM 메시지 생성 중지 완료 - MMSI: " + mmsi);
					} else {
						System.out.println("[DEBUG] ⚠️ ASM이 이미 비활성화되어 있음 - MMSI: " + mmsi);
					}
				} else {
					System.out.println("[DEBUG] ℹ️ destMMSI 리스트에 항목이 남아있어 ASM 메시지 생성 계속 - MMSI: " + mmsi + 
							", 리스트 크기: " + mmsiEntity.getAsmEntity().getDestMMSIList().size());
				}
			} else {
				// 기존 동작: destMMSI 없이 state만으로 제어
				if (mmsiEntity.isAsm()) {
					System.out.println("[DEBUG] ASM 비활성화 시작 - MMSI: " + mmsi);
					mmsiEntity.setAsm(false);
					if (quartzCoreService != null) {
						try {
							quartzCoreService.removeAsmStartTimeTrigger(mmsiEntity);
							System.out.println("[DEBUG] ✅ MMSI: " + mmsi + " ASM 메시지 생성 중지 및 스케줄 삭제 완료");
						} catch (Exception e) {
							System.out.println("[DEBUG] ⚠️ MMSI: " + mmsi + " ASM 스케줄 삭제 중 오류: " + e.getMessage());
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("[DEBUG] ℹ️ MMSI: " + mmsi + " ASM 메시지 생성 상태가 이미 OFF입니다.");
				}
			}
		} else {
			System.out.println("[DEBUG] ❌ 유효하지 않은 state 값: " + state + " (MMSI: " + mmsi + ")");
			return false;
		}
		
		// asmPeriod 저장 (전체 ASM에 대한 기본값으로 사용)
		mmsiEntity.getAsmEntity().setAsmPeriod(asmPeriodValue);
		System.out.println("[DEBUG] ✅ MMSI: " + mmsi + " ASM Period 저장: " + asmPeriodValue + 
				" (" + ("0".equals(asmPeriodValue) ? "단발" : "계속") + ")");
		
		return true;
	}

	public void addMmsiEntity180(Scheduler scheduler, QuartzCoreService quartzCoreService) {
		// UI 제거로 인해 JTextArea 파라미터 제거
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
		
		// UI 제거로 인해 주석 처리
		// mmsi.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
		// mmsi.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
		// mmsi.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
		// mmsi.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
		// mmsi.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
		// mmsi.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
		// mmsi.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
		// mmsi.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
		// mmsi.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
		// mmsi.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
		// mmsi.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
		// mmsi.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
		// mmsi.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
		// mmsi.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
		// mmsi.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
		// mmsi.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);

		mmsi.setSfiValue(this.sfiValue);

		mmsi.setMmsi(uniqueMmsi);
		// UI 제거로 인해 주석 처리
		// mmsi.setAisTabjTextAreaName(aisTabjTextAreaName);
		mmsi.setGlobalEntityManager(this);
		
		mmsi.setSpeed(180);
		mmsi.setSlotTimeOut(3);
//		mmsi.setslo
//		this.speed = speed;
//		this.slotTimeOut = slotTimeout;
//		this.slotTimeOut_default = this.slotTimeOut;
		
		
		this.mmsiEntityLists.add(mmsi);
		
		// UI 제거로 인해 주석 처리
		// this.mmsiTableModel2.setData();
		mmsi.setChk(true);
	}
	
	public void addMmsiEntity10(Scheduler scheduler, QuartzCoreService quartzCoreService) {
		// UI 제거로 인해 JTextArea 파라미터 제거
		//
		System.out.println("[DEBUG] GlobalEntityManager.addMmsiEntity10() 시작");
		if (this.mmsiEntityLists == null) {
			//
			this.mmsiEntityLists = new ArrayList<>();
		}
		// 중복되지 않는 MMSI 값을 보장하기 위해 반복문 사용
		long uniqueMmsi;
		do {
			uniqueMmsi = RandomGenerator.generateRandomLong(9);
		} while (!generatedMmsiSet.add(uniqueMmsi));

		System.out.println("[DEBUG] MMSI 생성: " + uniqueMmsi);
		BeanUtils.registerBean(uniqueMmsi + "", MmsiEntity.class);
		MmsiEntity mmsi = (MmsiEntity) BeanUtils.getBean(uniqueMmsi + "");
		System.out.println("[DEBUG] MmsiEntity Bean 생성 완료 - MMSI: " + mmsi.getMmsi());
		
		// UI 제거로 인해 주석 처리
		// mmsi.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
		// mmsi.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
		// mmsi.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
		// mmsi.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
		// mmsi.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
		// mmsi.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
		// mmsi.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
		// mmsi.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
		// mmsi.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
		// mmsi.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
		// mmsi.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
		// mmsi.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
		// mmsi.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
		// mmsi.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
		// mmsi.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
		// mmsi.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);

		mmsi.setSfiValue(this.sfiValue);

		mmsi.setMmsi(uniqueMmsi);
		// UI 제거로 인해 주석 처리
		// mmsi.setAisTabjTextAreaName(aisTabjTextAreaName);
		mmsi.setGlobalEntityManager(this);
		
		mmsi.setSpeed(10);
		mmsi.setSlotTimeOut(7);
		
		this.mmsiEntityLists.add(mmsi);
		
		// UI 제거로 인해 주석 처리
		// this.mmsiTableModel2.setData();
		System.out.println("[DEBUG] setChk(true) 호출 전 - MMSI: " + mmsi.getMmsi() + ", chk: " + mmsi.isChk());
		mmsi.setChk(true);
		System.out.println("[DEBUG] setChk(true) 호출 후 - MMSI: " + mmsi.getMmsi() + ", chk: " + mmsi.isChk());
		System.out.println("[DEBUG] GlobalEntityManager.addMmsiEntity10() 완료");
	}
	
	/**
	 * JSON 데이터로부터 선박을 생성합니다.
	 * @param scheduler Quartz Scheduler
	 * @param quartzCoreService QuartzCoreService
	 * @param mmsiStr MMSI 문자열
	 * @param lat 위도
	 * @param lon 경도
	 * @param aisPeriod AIS 메시지 전송 주기 (초 단위)
	 * @param regionId 지역 ID (선택사항)
	 */
	public void createMmsiFromJson(Scheduler scheduler, QuartzCoreService quartzCoreService,
			String mmsiStr, double lat, double lon, int aisPeriod, String regionId) {
		System.out.println("[DEBUG] ========== GlobalEntityManager.createMmsiFromJson() 시작 ==========");
		System.out.println("[DEBUG] MMSI: " + mmsiStr + ", Lat: " + lat + ", Lon: " + lon + 
				", AIS Period: " + aisPeriod + ", Region: " + regionId);
		
		if (this.mmsiEntityLists == null) {
			this.mmsiEntityLists = new ArrayList<>();
		}
		
		// MMSI 문자열을 Long으로 변환
		long mmsi;
		try {
			mmsi = Long.parseLong(mmsiStr);
		} catch (NumberFormatException e) {
			System.out.println("[DEBUG] ❌ 유효하지 않은 MMSI: " + mmsiStr);
			throw new IllegalArgumentException("Invalid MMSI format: " + mmsiStr, e);
		}
		
		// MMSI 중복 확인
		if (!generatedMmsiSet.add(mmsi)) {
			System.out.println("[DEBUG] ⚠️ 중복된 MMSI - 선박 생성 건너뜀: " + mmsi);
			return; // 예외를 throw하지 않고 경고만 출력하고 종료
		}
		
		// MmsiEntity Bean 등록 및 생성
		BeanUtils.registerBean(mmsi + "", MmsiEntity.class);
		MmsiEntity mmsiEntity = (MmsiEntity) BeanUtils.getBean(mmsi + "");
		System.out.println("[DEBUG] MmsiEntity Bean 생성 완료 - MMSI: " + mmsiEntity.getMmsi());
		
		// 기본 설정
		mmsiEntity.setSfiValue(this.sfiValue);
		mmsiEntity.setMmsi(mmsi);
		mmsiEntity.setGlobalEntityManager(this);
		
		// AIS Period를 speed로 설정 (aisPeriod가 180이면 speed=180, 그 외에는 aisPeriod 값 사용)
		int speed = aisPeriod;
		mmsiEntity.setSpeed(speed);
		
		// SlotTimeOut 설정 (speed가 180이면 3, 그 외에는 7)
		int slotTimeOut = (speed == 180) ? 3 : 7;
		mmsiEntity.setSlotTimeOut(slotTimeOut);
		
		// 위도/경도 설정을 위한 positions Map 생성
		// 단일 위치를 사용하여 초기 위치 설정
		Map<Integer, double[]> positions = new HashMap<>();
		double[] position = new double[3];
		position[0] = lat;  // 위도
		position[1] = lon;   // 경도
		position[2] = 0;     // COG (초기값)
		positions.put(0, position);
		
		// testInit 메서드를 사용하여 위치 정보 설정
		mmsiEntity.testInit(speed, slotTimeOut, positions);
		System.out.println("[DEBUG] 위치 정보 설정 완료 - Lat: " + lat + ", Lon: " + lon);
		
		// 리스트에 추가
		this.mmsiEntityLists.add(mmsiEntity);
		
		// 선박 생성과 메시지 생성 시작을 분리
		// AIS는 제어 메시지(STG-02)로 별도 활성화해야 함
		System.out.println("[DEBUG] 선박 생성 완료 - MMSI: " + mmsi + " (메시지 생성은 제어 메시지로 활성화 필요)");
		
		// ASM 활성화 (선택사항)
		//mmsiEntity.setAsm(true);
		System.out.println("[DEBUG] ASM 활성화 완료 - MMSI: " + mmsi);
		
		System.out.println("[DEBUG] ========== GlobalEntityManager.createMmsiFromJson() 완료 - MMSI: " + mmsi + " ==========");
	}
	
	public void addMmsiEntity6(Scheduler scheduler, QuartzCoreService quartzCoreService) {
		// UI 제거로 인해 JTextArea 파라미터 제거
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
		
		// UI 제거로 인해 주석 처리
		// mmsi.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
		// mmsi.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
		// mmsi.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
		// mmsi.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
		// mmsi.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
		// mmsi.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
		// mmsi.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
		// mmsi.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
		// mmsi.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
		// mmsi.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
		// mmsi.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
		// mmsi.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
		// mmsi.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
		// mmsi.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
		// mmsi.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
		// mmsi.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);

		mmsi.setSfiValue(this.sfiValue);

		mmsi.setMmsi(uniqueMmsi);
		// UI 제거로 인해 주석 처리
		// mmsi.setAisTabjTextAreaName(aisTabjTextAreaName);
		mmsi.setGlobalEntityManager(this);
		
		mmsi.setSpeed(6);
		mmsi.setSlotTimeOut(7);
		
		this.mmsiEntityLists.add(mmsi);
		
		// UI 제거로 인해 주석 처리
		// this.mmsiTableModel2.setData();
		mmsi.setChk(true);
	}
	
	/**
	 * [MMSI_AIS_FLOW]-2
	 * MmsiEntity 객체 생성 Bean등록, 환경 설정 후 SlotTimeOut 이벤트 발행 및 생성완료 신호-setChk(true)호출
	 * To [MMSI_AIS_FLOW]-2-1 MmsiEntity.setSlotTimeOut , [MMSI_AIS_FLOW]-2-2 MmsiEntity.setChk
	 */
	public void addMmsiEntity2(Scheduler scheduler, QuartzCoreService quartzCoreService) {
		// UI 제거로 인해 JTextArea 파라미터 제거
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
		
		// UI 제거로 인해 주석 처리
		// mmsi.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
		// mmsi.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
		// mmsi.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
		// mmsi.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
		// mmsi.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
		// mmsi.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
		// mmsi.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
		// mmsi.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
		// mmsi.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
		// mmsi.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
		// mmsi.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
		// mmsi.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
		// mmsi.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
		// mmsi.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
		// mmsi.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
		// mmsi.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);

		mmsi.setSfiValue(this.sfiValue);

		mmsi.setMmsi(uniqueMmsi);
		// UI 제거로 인해 주석 처리
		// mmsi.setAisTabjTextAreaName(aisTabjTextAreaName);
		mmsi.setGlobalEntityManager(this);
		
		mmsi.setSpeed(2);
		mmsi.setSlotTimeOut(7);
		
		this.mmsiEntityLists.add(mmsi);
		
		// mmsi 리스트 갱신 및 table UI 업데이트
		// UI 제거로 인해 주석 처리
		// this.mmsiTableModel2.setData();
		mmsi.setChk(true);
	}
	
	public void addMmsiEntity(int cnt, Scheduler scheduler, QuartzCoreService quartzCoreService) {
		// UI 제거로 인해 JTextArea 파라미터 제거
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
			
			// UI 제거로 인해 주석 처리
			// mmsi.setCurrentFrameJTableNameUpper(this.currentFrameJTableNameUpper);
			// mmsi.setCurrentFrame1JTableNameUpper(this.currentFrame1JTableNameUpper);
			// mmsi.setCurrentFrame2JTableNameUpper(this.currentFrame2JTableNameUpper);
			// mmsi.setCurrentFrame3JTableNameUpper(this.currentFrame3JTableNameUpper);
			// mmsi.setCurrentFrame4JTableNameUpper(this.currentFrame4JTableNameUpper);
			// mmsi.setCurrentFrame5JTableNameUpper(this.currentFrame5JTableNameUpper);
			// mmsi.setCurrentFrame6JTableNameUpper(this.currentFrame6JTableNameUpper);
			// mmsi.setCurrentFrame7JTableNameUpper(this.currentFrame7JTableNameUpper);
			// mmsi.setCurrentFrameJTableNameLower(this.currentFrameJTableNameLower);
			// mmsi.setCurrentFrame1JTableNameLower(this.currentFrame1JTableNameLower);
			// mmsi.setCurrentFrame2JTableNameLower(this.currentFrame2JTableNameLower);
			// mmsi.setCurrentFrame3JTableNameLower(this.currentFrame3JTableNameLower);
			// mmsi.setCurrentFrame4JTableNameLower(this.currentFrame4JTableNameLower);
			// mmsi.setCurrentFrame5JTableNameLower(this.currentFrame5JTableNameLower);
			// mmsi.setCurrentFrame6JTableNameLower(this.currentFrame6JTableNameLower);
			// mmsi.setCurrentFrame7JTableNameLower(this.currentFrame7JTableNameLower);

			mmsi.setSfiValue(this.sfiValue);

			mmsi.setMmsi(uniqueMmsi);
			// UI 제거로 인해 주석 처리
			// mmsi.setAisTabjTextAreaName(aisTabjTextAreaName);
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
			// UI 제거로 인해 주석 처리
			// this.mmsiTableModel2.setData();
			mmsi.setChk(true);
			
		}
	}

	private void make10(MmsiEntity mmsi, int index) {
            //
            switch (index) {
                case 10 -> mmsi.testInit(10, 1, SystemConstMessage.positions_10_0);
                case 11 -> mmsi.testInit(10, 1, SystemConstMessage.positions_10_1);
                case 12 -> mmsi.testInit(10, 2, SystemConstMessage.positions_10_2);
                case 13 -> mmsi.testInit(10, 2, SystemConstMessage.positions_10_3);
                case 14 -> mmsi.testInit(10, 3, SystemConstMessage.positions_10_4);
                case 15 -> mmsi.testInit(10, 3, SystemConstMessage.positions_10_5);
                case 16 -> mmsi.testInit(10, 4, SystemConstMessage.positions_10_6);
                case 17 -> mmsi.testInit(10, 4, SystemConstMessage.positions_10_7);
                case 18 -> mmsi.testInit(10, 5, SystemConstMessage.positions_10_8);
                case 19 -> mmsi.testInit(10, 6, SystemConstMessage.positions_10_9);
                default -> {
                }
            }
	}
	
	private void make6(MmsiEntity mmsi, int index) {
            //
            switch (index) {
                case 30 -> mmsi.testInit(6, 1, SystemConstMessage.positions_6_0);
                case 31 -> mmsi.testInit(6, 2, SystemConstMessage.positions_6_1);
                case 32 -> mmsi.testInit(6, 3, SystemConstMessage.positions_6_2);
                case 33 -> mmsi.testInit(6, 4, SystemConstMessage.positions_6_3);
                case 34 -> mmsi.testInit(6, 5, SystemConstMessage.positions_6_4);
                default -> {
                }
            }
	}
	
	
	private void make2(MmsiEntity mmsi, int index) {
            //
            switch (index) {
                case 20 -> mmsi.testInit(2, 1, SystemConstMessage.positions_2_0);
                case 21 -> mmsi.testInit(2, 2, SystemConstMessage.positions_2_1);
                case 22 -> mmsi.testInit(2, 3, SystemConstMessage.positions_2_2);
                case 23 -> mmsi.testInit(2, 4, SystemConstMessage.positions_2_3);
                case 24 -> mmsi.testInit(2, 5, SystemConstMessage.positions_2_4);
                case 25 -> mmsi.testInit(2, 6, SystemConstMessage.positions_2_5);
                case 26 -> mmsi.testInit(2, 7, SystemConstMessage.positions_2_6);
                case 27 -> mmsi.testInit(2, 7, SystemConstMessage.positions_2_7);
                case 28 -> mmsi.testInit(2, 7, SystemConstMessage.positions_2_8);
                case 29 -> mmsi.testInit(2, 7, SystemConstMessage.positions_2_9);
                default -> {
                }
            }
	}
	private void make180(MmsiEntity mmsi, int index) {
            //
            switch (index) {
                case 0 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_0);
                case 1 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_1);
                case 2 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_2);
                case 3 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_3);
                case 4 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_4);
                case 5 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_5);
                case 6 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_6);
                case 7 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_7);
                case 8 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_8);
                case 9 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_9);
                case 10 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_10);
                case 11 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_11);
                case 12 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_12);
                case 13 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_13);
                case 14 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_14);
                case 15 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_15);
                case 16 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_16);
                case 17 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_17);
                case 18 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_18);
                case 19 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_19);
                case 20 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_20);
                case 21 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_21);
                case 22 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_22);
                case 23 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_23);
                case 24 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_24);
                case 25 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_25);
                case 26 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_26);
                case 27 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_27);
                case 28 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_28);
                case 29 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_29);
                case 30 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_30);
                case 31 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_31);
                case 32 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_32);
                case 33 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_33);
                case 34 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_34);
                case 35 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_35);
                case 36 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_36);
                case 37 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_37);
                case 38 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_38);
                case 39 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_39);
                case 40 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_40);
                case 41 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_41);
                case 42 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_42);
                case 43 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_43);
                case 44 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_44);
                case 45 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_45);
                case 46 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_46);
                case 47 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_47);
                case 48 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_48);
                case 49 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_49);
                case 50 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_50);
                case 51 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_51);
                case 52 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_52);
                case 53 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_53);
                case 54 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_54);
                case 55 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_55);
                case 56 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_56);
                case 57 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_57);
                case 58 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_58);
                case 59 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_59);
                case 60 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_60);
                case 61 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_61);
                case 62 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_62);
                case 63 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_63);
                case 64 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_64);
                case 65 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_65);
                case 66 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_66);
                case 67 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_67);
                case 68 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_68);
                case 69 -> mmsi.testInit(180, 3, SystemConstTestData180.positions_180_69);
                default -> {
                }
            }
		
	}
	
	// UI 제거로 인해 주석 처리
	// public JTable getCurrentFrameJTableNameUpper() {
	//	return currentFrameJTableNameUpper;
	// }
	//
	// public void setCurrentFrameJTableNameUpper(JTable currentFrameJTableNameUpper) {
	//	this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
	// }

	// UI 제거로 인해 주석 처리 - 모든 JTable getter/setter
	// public JTable getCurrentFrame1JTableNameUpper() {
	//	return currentFrame1JTableNameUpper;
	// }
	//
	// public void setCurrentFrame1JTableNameUpper(JTable currentFrame1JTableNameUpper) {
	//	this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
	// }
	//
	// ... (모든 JTable getter/setter 주석 처리)

	// UI 제거로 인해 주석 처리
	// public JTextField getjTextFieldSFI() {
	//	return jTextFieldSFI;
	// }
	//
	// public void setjTextFieldSFI(JTextField jTextFieldSFI) {
	//	this.jTextFieldSFI = jTextFieldSFI;
	// }
	
	public String getSfiValue() {
		return sfiValue;
	}

	public void setSfiValue(String sfiValue) {
		this.sfiValue = sfiValue;
	}
	
	// UI 없이 프레임 정보를 관리하기 위한 getter/setter 메서드 (기능 유지를 위한 대체 메커니즘)

	public boolean isAisMsgDisplay() {
		return aisMsgDisplay;
	}

	public void setAisMsgDisplay(boolean aisMsgDisplay) {
		//
		// UI 제거로 인해 주석 처리
		// ToggleDisplayAis event = new ToggleDisplayAis(this, aisMsgDisplay, currentFrameJTableNameUpper,
		//		currentFrame1JTableNameUpper, currentFrame2JTableNameUpper, currentFrame3JTableNameUpper,
		//		currentFrame4JTableNameUpper, currentFrame5JTableNameUpper, currentFrame6JTableNameUpper,
		//		currentFrame7JTableNameUpper, currentFrame8JTableNameUpper, currentFrame9JTableNameUpper,
		//		currentFrame10JTableNameUpper);
		this.aisMsgDisplay = aisMsgDisplay;
		// eventPublisher.publishEvent(event);
	}

	public boolean isAsmMsgDisplay() {
		return asmMsgDisplay;
	}

	public void setAsmMsgDisplay(boolean asmMsgDisplay) {
		//
		// UI 제거로 인해 주석 처리
		// ToggleDisplayAsm event = new ToggleDisplayAsm(this, asmMsgDisplay, currentFrameJTableNameUpper,
		//		currentFrame1JTableNameUpper, currentFrame2JTableNameUpper, currentFrame3JTableNameUpper,
		//		currentFrame4JTableNameUpper, currentFrame5JTableNameUpper, currentFrame6JTableNameUpper,
		//		currentFrame7JTableNameUpper, currentFrame8JTableNameUpper, currentFrame9JTableNameUpper,
		//		currentFrame10JTableNameUpper);
		this.asmMsgDisplay = asmMsgDisplay;
		// eventPublisher.publishEvent(event);

	}

	public boolean isVdeMsgDisplay() {
		return vdeMsgDisplay;
	}

	public void setVdeMsgDisplay(boolean vdeMsgDisplay) {
		//
		// UI 제거로 인해 주석 처리
		// ToggleDisplayVde event = new ToggleDisplayVde(this, vdeMsgDisplay, currentFrameJTableNameUpper,
		//		currentFrame1JTableNameUpper, currentFrame2JTableNameUpper, currentFrame3JTableNameUpper,
		//		currentFrame4JTableNameUpper, currentFrame5JTableNameUpper, currentFrame6JTableNameUpper,
		//		currentFrame7JTableNameUpper, currentFrame8JTableNameUpper, currentFrame9JTableNameUpper,
		//		currentFrame10JTableNameUpper, currentFrameJTableNameLower, currentFrame1JTableNameLower,
		//		currentFrame2JTableNameLower, currentFrame3JTableNameLower, currentFrame4JTableNameLower,
		//		currentFrame5JTableNameLower, currentFrame6JTableNameLower, currentFrame7JTableNameLower,
		//		currentFrame8JTableNameLower, currentFrame9JTableNameLower, currentFrame10JTableNameLower);
		this.vdeMsgDisplay = vdeMsgDisplay;
		// eventPublisher.publishEvent(event);

	}

	// ====================================================================
	// UI 제거로 인해 주석 처리
	public void setCurrentFrameAchColor(int row, int col, MmsiEntity mmsiEntity, LocalDateTime time) {
		//
		// ColorEntityChangeEvent event = new ColorEntityChangeEvent(this, row, col, mmsiEntity, time, 'A',
		//		this.currentFrameJTableNameUpper, this.currentFrame1JTableNameUpper, this.currentFrame2JTableNameUpper,
		//		this.currentFrame3JTableNameUpper, this.currentFrame4JTableNameUpper, this.currentFrame5JTableNameUpper,
		//		this.currentFrame6JTableNameUpper, this.currentFrame7JTableNameUpper);
		// eventPublisher.publishEvent(event);
	}

	public void setCurrentFrameBchColor(int row, int col, MmsiEntity mmsiEntity, LocalDateTime time) {
		//
		// ColorEntityChangeEvent event = new ColorEntityChangeEvent(this, row, col, mmsiEntity, time, 'B',
		//		this.currentFrameJTableNameUpper, this.currentFrame1JTableNameUpper, this.currentFrame2JTableNameUpper,
		//		this.currentFrame3JTableNameUpper, this.currentFrame4JTableNameUpper, this.currentFrame5JTableNameUpper,
		//		this.currentFrame6JTableNameUpper, this.currentFrame7JTableNameUpper);
		// eventPublisher.publishEvent(event);
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

	/**
	 * 슬롯 검색 및 점유(슬롯 마킹), AIS 메시지 생성-송신 함수
	 * UI 없이 SI(Selection Interval) 범위 내에서 슬롯을 찾아 메시지를 생성합니다.
	 * @param mmsiEntity
	 * @return 찾은 슬롯 번호, 실패 시 -1
	 */
	public int findSlotAndMarking(MmsiEntity mmsiEntity) {
		//
		System.out.println("[DEBUG] GlobalEntityManager.findSlotAndMarking() 호출됨 - MMSI: " + mmsiEntity.getMmsi());
		try {
			int[] si = mmsiEntity.getSI();
			if (si == null || si.length < 2) {
				System.out.println("[DEBUG] ❌ SI가 설정되지 않음 - MMSI: " + mmsiEntity.getMmsi());
				return -1;
			}
			
			int siMin = si[0];
			int siMax = si[1];
			System.out.println("[DEBUG] SI 범위 - MMSI: " + mmsiEntity.getMmsi() + ", SI: [" + siMin + ", " + siMax + "]");
			
			// SI 범위 내에서 유효한 슬롯 찾기 (0~2249 범위 체크)
			if (siMin < 0 || siMax > 2249 || siMin > siMax) {
				System.out.println("[DEBUG] ❌ 유효하지 않은 SI 범위 - MMSI: " + mmsiEntity.getMmsi() + ", SI: [" + siMin + ", " + siMax + "]");
				return -1;
			}
			
			// SlotStateManager를 사용하여 빈 슬롯 찾기
			int selectedSlot = slotStateManager.findAvailableSlot(siMin, siMax);
			if (selectedSlot == -1) {
				System.out.println("[DEBUG] ❌ SI 범위 내에서 빈 슬롯을 찾을 수 없음 - MMSI: " + mmsiEntity.getMmsi());
				return -1;
			}
			
			System.out.println("[DEBUG] 슬롯 선택 - MMSI: " + mmsiEntity.getMmsi() + ", SlotNumber: " + selectedSlot);
			
			// 슬롯의 TimeOutTime이 설정되어 있지 않은 경우에만 설정
			if (mmsiEntity.getSlotTimeOutTime() == null) {
				LocalDateTime modifiedDateTime = mmsiEntity.getStartTime().plusMinutes(1)
						.minus((mmsiEntity.getSpeed() * 1000) - 100, ChronoUnit.MILLIS);
				mmsiEntity.setSlotTimeOutTime(modifiedDateTime);
				System.out.println("[DEBUG] SlotTimeOutTime 설정 - MMSI: " + mmsiEntity.getMmsi() + ", Time: " + modifiedDateTime);
			}
			
			// 슬롯 좌표 계산
			int row = selectedSlot / 32;
			int col = selectedSlot % 32;
			boolean channel = mmsiEntity.getTargetChannel(); // true: A channel, false: B channel
			
			// SlotStateManager에 슬롯 초기화 및 점유
			slotStateManager.initializeSlot(selectedSlot, row, col, channel);
			boolean occupied = slotStateManager.occupySlot(
				selectedSlot, 
				mmsiEntity.getMmsi(), 
				mmsiEntity.getStartTime(), 
				mmsiEntity.getSlotTimeOutTime(), 
				"AIS"
			);
			
			if (!occupied) {
				System.out.println("[DEBUG] ❌ 슬롯 점유 실패 (이미 점유됨) - MMSI: " + mmsiEntity.getMmsi() + ", SlotNumber: " + selectedSlot);
				return -1;
			}
			
			// TargetCellInfoEntity 생성
			TargetCellInfoEntity targetCellInfo = new TargetCellInfoEntity();
			targetCellInfo.setRow(row);
			targetCellInfo.setCol(col);
			targetCellInfo.setSlotNumber(String.valueOf(selectedSlot));
			
			// AIS 타겟 슬롯 Entity 추가
			this.addAISTargetSlotEntity(mmsiEntity, selectedSlot, targetCellInfo);
			
			// AIS 메시지 생성
			this.setAISMessage(mmsiEntity, selectedSlot);
			
			System.out.println("[DEBUG] ✅ 슬롯 찾기 및 메시지 생성 완료 - MMSI: " + mmsiEntity.getMmsi() + ", SlotNumber: " + selectedSlot);
			return selectedSlot;
			
		} catch (Exception e) {
			System.out.println("[DEBUG] ❌ findSlotAndMarking() 실행 중 오류 발생 - MMSI: " + mmsiEntity.getMmsi());
			e.printStackTrace();
			return -1;
		}
	}

	private void setAISMessage(MmsiEntity mmsiEntity, int slotNumber) {
		//
		System.out.println("[DEBUG] ✅ setAISMessage() 호출 - MMSI: " + mmsiEntity.getMmsi() + 
				", SlotNumber: " + slotNumber);
		Vdm vdm = AisMessage1Util.create(mmsiEntity, slotNumber);
		System.out.println("[DEBUG] AIS 메시지 생성 완료 - MMSI: " + mmsiEntity.getMmsi() + 
				", SlotNumber: " + slotNumber);
		mmsiEntity.setMessage1(vdm, slotNumber);
		mmsiEntity.setAisMessageSequence(mmsiEntity.getAisMessageSequence() + 1);
		System.out.println("[DEBUG] ✅ setAISMessage() 완료 - MMSI: " + mmsiEntity.getMmsi() + 
				", Sequence: " + mmsiEntity.getAisMessageSequence());

	}

	private void addAISTargetSlotEntity(MmsiEntity mmsiEntity, int slotNumber, TargetCellInfoEntity s) {
		//
		TargetSlotEntity newTargetSlotEntity = new TargetSlotEntity();
		newTargetSlotEntity.setRow(s.getRow());
		newTargetSlotEntity.setColumn(s.getCol());

		newTargetSlotEntity.setChannel(mmsiEntity.getTargetChannel());
		newTargetSlotEntity.setSlotNumber(slotNumber);

		String ssSSSS = mmsiEntity.getStartTime().format(SystemConstMessage.formatterForStartIndex);
		double currentSecond = Double.parseDouble(ssSSSS);
		newTargetSlotEntity.setSsSSSS(currentSecond);
		mmsiEntity.addTargetSlotEntity(newTargetSlotEntity);
	}

	// /**
	//  * 테이블 셀 마킹
	//  * @param mmsiEntity MmsiEntity
	//  * @param s 타겟 셀 정보
	//  */
	// private void setSlotMarking(MmsiEntity mmsiEntity, TargetCellInfoEntity s) {
	// 	//
	// 	if (mmsiEntity.getTargetChannel()) {
	// 		//
	// 		this.setCurrentFrameAchColor(s.getRow(), s.getCol(), mmsiEntity, mmsiEntity.getStartTime());
	// 	} else {
	// 		this.setCurrentFrameBchColor(s.getRow(), s.getCol(), mmsiEntity, mmsiEntity.getStartTime());
	// 	}
	// }

	public void displayAsm(List<TargetCellInfoEntity> targetCellInfoEntitys, MmsiEntity mmsiEntity) {
		//
		// UI 제거로 인해 주석 처리
		// CustomTableCellRenderer renderer = (CustomTableCellRenderer) this.currentFrameJTableNameUpper
		//		.getDefaultRenderer(Object.class);
		// 마킹
		String strValue = "";

		System.out.println("=================================");
		System.out.println("[ASM Slot Count] : " + mmsiEntity.getAsmEntity().getSlotCount());
		System.out.println("[ASM    Channel] : " + mmsiEntity.getAsmEntity().getChannel());
		System.out.println("=================================");
		if (mmsiEntity.getAsmEntity().getChannel() == 'A') {
			//
			switch (mmsiEntity.getAsmEntity().getSlotCount()) {
				case 1 -> {
					strValue = "NSONESOFTNSONESOFTNSONESOFT1";
					// UI 제거로 인해 주석 처리
					// renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(0).getRow(),
					//		targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
                        }
				case 2 -> {
					strValue = "NSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFT2";
					// UI 제거로 인해 주석 처리
					// renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(0).getRow(),
					//		targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
					// renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(1).getRow(),
					//		targetCellInfoEntitys.get(1).getCol(), mmsiEntity);
                        }
				case 3 -> {
					strValue = "NSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFT3";
					// UI 제거로 인해 주석 처리
					// renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(0).getRow(),
					//		targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
					// renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(1).getRow(),
					//		targetCellInfoEntitys.get(1).getCol(), mmsiEntity);
					// renderer.setCellInfosAsmForA(targetCellInfoEntitys.get(2).getRow(),
					//		targetCellInfoEntitys.get(2).getCol(), mmsiEntity);
                        }
				default -> {}
			}

			List<String> message = this.aSMMessageUtil.getMessage(strValue, mmsiEntity);
			mmsiEntity.setAsmMessageList(message, targetCellInfoEntitys.get(0).getSlotNumber());
			mmsiEntity.getAsmEntity().setChannel('B');
			mmsiEntity.setAsmMessageSequence(mmsiEntity.getAsmMessageSequence() + 1);
		} else {
			switch (mmsiEntity.getAsmEntity().getSlotCount()) {
				case 1 -> {
					strValue = "NSONESOFTNSONESOFTNSONESOFT1";
					// UI 제거로 인해 주석 처리
					// renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(0).getRow(),
					//		targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
                        }
				case 2 -> {
					strValue = "NSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFT2";
					// UI 제거로 인해 주석 처리
					// renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(0).getRow(),
					//		targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
					// renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(1).getRow(),
					//		targetCellInfoEntitys.get(1).getCol(), mmsiEntity);
                        }
				case 3 -> {
					strValue = "NSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFTNSONESOFT3";
					// UI 제거로 인해 주석 처리
					// renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(0).getRow(),
					//		targetCellInfoEntitys.get(0).getCol(), mmsiEntity);
					// renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(1).getRow(),
					//		targetCellInfoEntitys.get(1).getCol(), mmsiEntity);
					// renderer.setCellInfosAsmForB(targetCellInfoEntitys.get(2).getRow(),
					//		targetCellInfoEntitys.get(2).getCol(), mmsiEntity);
                        }
				default -> {
                        }
			}
			List<String> message = this.aSMMessageUtil.getMessage(strValue, mmsiEntity);
			mmsiEntity.setAsmMessageList(message, targetCellInfoEntitys.get(0).getSlotNumber());
			mmsiEntity.getAsmEntity().setChannel('A');
			mmsiEntity.setAsmMessageSequence(mmsiEntity.getAsmMessageSequence() + 1);
		}
		// UI 제거로 인해 주석 처리
		// this.currentFrameJTableNameUpper.repaint();
	}

	/**
	 * ASM Rule1: 연속된 8개 이상의 빈 슬롯 찾기
	 * AIS, ASM A/B 채널, VDE 모두 비어있는 슬롯 찾기
	 */
	public List<TargetCellInfoEntity> findAsmRule1(int startIndex, MmsiEntity mmsiEntity) {
		//
		System.out.println("[DEBUG] findAsmRule1 시작 - MMSI: " + mmsiEntity.getMmsi() + ", startIndex: " + startIndex);
		List<TargetCellInfoEntity> targetInfoList = new ArrayList<>();
		
		int consecutiveCount = 0;
		int maxSlot = Math.min(startIndex + 235, 2249); // 최대 슬롯 번호 제한
		
		// startIndex부터 연속된 빈 슬롯 찾기
		for (int slot = startIndex; slot <= maxSlot; slot++) {
			SlotStateManager.SlotInfo slotInfo = slotStateManager.getSlotInfo(slot);
			
			// 슬롯이 비어있거나 점유되지 않은 경우
			if (slotInfo == null || !slotInfo.isOccupied()) {
				consecutiveCount++;
				TargetCellInfoEntity targetCell = new TargetCellInfoEntity();
				
				// 슬롯 번호를 기반으로 row, col 계산 (간단한 매핑)
				int row = slot / 32;
				int col = slot % 32;
				targetCell.setRow(row);
				targetCell.setCol(col);
				targetCell.setSlotNumber(String.valueOf(slot));
				targetInfoList.add(targetCell);
				
				// 연속으로 8개 이상 찾으면 반환
				if (consecutiveCount >= 8) {
					System.out.println("[DEBUG] findAsmRule1 성공 - MMSI: " + mmsiEntity.getMmsi() + 
							", 연속 슬롯: " + consecutiveCount);
					return targetInfoList;
				}
			} else {
				// 연속이 끊기면 초기화
				targetInfoList.clear();
				consecutiveCount = 0;
			}
		}
		
		System.out.println("[DEBUG] findAsmRule1 실패 - MMSI: " + mmsiEntity.getMmsi() + 
				", 찾은 연속 슬롯: " + consecutiveCount);
		return targetInfoList;
	}

	/**
	 * ASM Rule2: 연속된 8개 이상의 빈 슬롯 찾기 (AIS만 체크)
	 * AIS가 비어있고, ASM A/B 채널이 비어있는 슬롯 찾기
	 */
	public List<TargetCellInfoEntity> findAsmRule2(int startIndex, MmsiEntity mmsiEntity) {
		//
		System.out.println("[DEBUG] findAsmRule2 시작 - MMSI: " + mmsiEntity.getMmsi() + ", startIndex: " + startIndex);
		List<TargetCellInfoEntity> targetInfoList = new ArrayList<>();
		
		int consecutiveCount = 0;
		int maxSlot = Math.min(startIndex + 235, 2249);
		
		for (int slot = startIndex; slot <= maxSlot; slot++) {
			SlotStateManager.SlotInfo slotInfo = slotStateManager.getSlotInfo(slot);
			
			// 슬롯이 비어있거나 AIS 메시지 타입이 아닌 경우
			if (slotInfo == null || !slotInfo.isOccupied() || 
					!"AIS".equals(slotInfo.getMessageType())) {
				consecutiveCount++;
				TargetCellInfoEntity targetCell = new TargetCellInfoEntity();
				int row = slot / 32;
				int col = slot % 32;
				targetCell.setRow(row);
				targetCell.setCol(col);
				targetCell.setSlotNumber(String.valueOf(slot));
				targetInfoList.add(targetCell);
				
				if (consecutiveCount >= 8) {
					System.out.println("[DEBUG] findAsmRule2 성공 - MMSI: " + mmsiEntity.getMmsi() + 
							", 연속 슬롯: " + consecutiveCount);
					return targetInfoList;
				}
			} else {
				targetInfoList.clear();
				consecutiveCount = 0;
			}
		}
		
		System.out.println("[DEBUG] findAsmRule2 실패 - MMSI: " + mmsiEntity.getMmsi() + 
				", 찾은 연속 슬롯: " + consecutiveCount);
		return targetInfoList;
	}

	/**
	 * ASM Rule3: 연속된 8개 이상의 빈 슬롯 찾기 (채널별 체크)
	 * AIS가 비어있고, ASM 채널(A 또는 B)이 비어있는 슬롯 찾기
	 */
	public List<TargetCellInfoEntity> findAsmRule3(int startIndex, MmsiEntity mmsiEntity) {
		//
		System.out.println("[DEBUG] findAsmRule3 시작 - MMSI: " + mmsiEntity.getMmsi() + 
				", startIndex: " + startIndex + ", Channel: " + mmsiEntity.getAsmEntity().getChannel());
		List<TargetCellInfoEntity> targetInfoList = new ArrayList<>();
		
		int consecutiveCount = 0;
		int maxSlot = Math.min(startIndex + 235, 2249);
		char targetChannel = mmsiEntity.getAsmEntity().getChannel();
		
		for (int slot = startIndex; slot <= maxSlot; slot++) {
			SlotStateManager.SlotInfo slotInfo = slotStateManager.getSlotInfo(slot);
			
			// 슬롯이 비어있거나, AIS가 아니고, ASM이 아니거나 다른 채널인 경우
			boolean isAvailable = false;
			if (slotInfo == null || !slotInfo.isOccupied()) {
				isAvailable = true;
			} else {
				String msgType = slotInfo.getMessageType();
				boolean isChannel = slotInfo.isChannel();
				
				// AIS가 아니고, ASM이 아니거나 같은 채널이 아닌 경우
				if (!"AIS".equals(msgType) && 
					(!"ASM".equals(msgType) || (targetChannel == 'A' && !isChannel) || (targetChannel == 'B' && isChannel))) {
					isAvailable = true;
				}
			}
			
			if (isAvailable) {
				consecutiveCount++;
				TargetCellInfoEntity targetCell = new TargetCellInfoEntity();
				int row = slot / 32;
				int col = slot % 32;
				targetCell.setRow(row);
				targetCell.setCol(col);
				targetCell.setSlotNumber(String.valueOf(slot));
				targetInfoList.add(targetCell);
				
				if (consecutiveCount >= 8) {
					System.out.println("[DEBUG] findAsmRule3 성공 - MMSI: " + mmsiEntity.getMmsi() + 
							", 연속 슬롯: " + consecutiveCount);
					return targetInfoList;
				}
			} else {
				targetInfoList.clear();
				consecutiveCount = 0;
			}
		}
		
		System.out.println("[DEBUG] findAsmRule3 실패 - MMSI: " + mmsiEntity.getMmsi() + 
				", 찾은 연속 슬롯: " + consecutiveCount);
		return targetInfoList;
	}

	public void findVde(int startIndex, MmsiEntity mmsiEntity) {
		//
		// UI 제거로 인해 주석 처리 - 이 메서드는 UI 테이블에 의존하므로 비활성화
		// TODO: UI 없이 VDE 슬롯 검색 로직을 구현해야 함
		return;
	}

	public String getUuid() {
		return uuid;
	}

	
}
