package com.all4land.generator.system.netty.dto;

import lombok.Data;

/**
 * Virtual Time-Sync MQTT 송신 메시지 DTO
 * 예: [{"virtualTime":"20260129143045.1234"}]
 */
@Data
public class VirtualTimeSyncMessage {
	private String virtualTime;  // 가상 시간 yyyyMMddHHmmss.SSSS
}
