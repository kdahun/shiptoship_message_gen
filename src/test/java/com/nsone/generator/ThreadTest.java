package com.nsone.generator;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.nsone.generator.system.constant.SystemConstMessage;

public class ThreadTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Runnable run = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LocalDateTime now = LocalDateTime.now();
		        String formatNow = now.format(SystemConstMessage.formatterForStartIndex);
				
				System.out.println(formatNow);
			}
		};
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(run, 0, 26666666, TimeUnit.NANOSECONDS);
	}

}
