package com.all4land.generator.system.schedule.job;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.all4land.generator.entity.GlobalEntityManager;
import com.all4land.generator.entity.MmsiEntity;
import com.all4land.generator.system.constant.SystemConstMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VdeEntityChangeStartDateQuartz implements Job {
	//
	private final GlobalEntityManager globalEntityManager;
	private MmsiEntity mmsiEntity;

	public VdeEntityChangeStartDateQuartz(GlobalEntityManager globalEntityManager) {
		// TODO Auto-generated constructor stub
		this.globalEntityManager = globalEntityManager;
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
		LocalDateTime newLocalDateTime = this.mmsiEntity.getVdeEntity().getStartTime().plusSeconds(60);
		this.mmsiEntity.getVdeEntity().setStartTime(newLocalDateTime, this.mmsiEntity);

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




