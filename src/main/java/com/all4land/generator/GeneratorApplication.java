package com.all4land.generator;

import javax.swing.SwingUtilities;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.all4land.generator.ui.controller.MainController;
import com.all4land.generator.ui.util.LookAndFeelUtils;

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
                log.error(e.getMessage());
            }
        });
    }
}
