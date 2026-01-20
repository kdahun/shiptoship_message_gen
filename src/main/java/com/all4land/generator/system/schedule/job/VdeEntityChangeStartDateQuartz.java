package com.all4land.generator.system.schedule.job;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.system.component.VirtualTimeManager;
import com.all4land.generator.system.constant.SystemConstMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VdeEntityChangeStartDateQuartz implements Job {
	//
	private final GlobalEntityManager globalEntityManager;
	private final VirtualTimeManager virtualTimeManager;
	private MmsiEntity mmsiEntity;

	public VdeEntityChangeStartDateQuartz(GlobalEntityManager globalEntityManager
			, VirtualTimeManager virtualTimeManager) {
		// TODO Auto-generated constructor stub
		this.globalEntityManager = globalEntityManager;
		this.virtualTimeManager = virtualTimeManager;
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		this.mmsiEntity = (MmsiEntity) jobDataMap.get("mmsiEntity");

		log.info("VDE quartz.....");
		this.globalEntityManager.findVde(getStartIndex(), mmsiEntity);
		this.addFuture();
	}
	
	private void addFuture() {
		//
		// 가상 시간 기준으로 다음 시간 계산 (60초 후)
		LocalDateTime currentVirtualTime = virtualTimeManager.getCurrentVirtualTime();
		LocalDateTime newLocalDateTime = currentVirtualTime.plusSeconds(60);
		this.mmsiEntity.getVdeEntity().setStartTime(newLocalDateTime, this.mmsiEntity);
		
		System.out.println("[DEBUG] 다음 VDE 메시지 가상 시간: " + newLocalDateTime + 
				" (현재 가상 시간: " + currentVirtualTime + ", +60초)");
	}
	
	
	
	private int getStartIndex() {
		//
		String ssSSSS = this.mmsiEntity.getVdeEntity().getStartTime().format(SystemConstMessage.formatterForStartIndex);

		double divisor = 0.0266666666666667;
		double currentSecond = Double.valueOf(ssSSSS);

		int tmp = (int) (currentSecond / divisor);

		if (tmp <= 5) {
			tmp = 0;
		} else {
			tmp = tmp - 5;
		}

		return tmp;
	}
	
}




