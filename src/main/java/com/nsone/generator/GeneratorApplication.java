package com.nsone.generator;

import javax.swing.SwingUtilities;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.nsone.generator.ui.controller.MainController;
import com.nsone.generator.ui.util.LookAndFeelUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class GeneratorApplication {

	public static void main(String[] args) {
		//
		LookAndFeelUtils.setWindowsLookAndFeel();
        ConfigurableApplicationContext context = createApplicationContext(args);
        displayMainFrame(context);
	}

	private static ConfigurableApplicationContext createApplicationContext(String[] args) {
        return new SpringApplicationBuilder(GeneratorApplication.class)
                .headless(false)
                .run(args);
    }

    private static void displayMainFrame(ConfigurableApplicationContext context) {
    	//
        SwingUtilities.invokeLater(() -> {
        	//
            MainController mainMenuController = context.getBean(MainController.class);
            try {
				mainMenuController.prepareAndOpenFrame();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
        });
    }
}
