package com.all4land.generator.ui.controller;


import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import com.all4land.generator.ui.view.Sample1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	//
	private final Sample1 mainFrame;
	
	public MainController (Sample1 mainFrame) {
		//
		this.mainFrame = mainFrame;

	}
	
	public void prepareAndOpenFrame() throws Exception {
		//
		// ClassPathResource를 사용하여 resources 폴더에 있는 이미지를 읽어옴
        ClassPathResource iconResource = new ClassPathResource("all4land_logo.png");
        Image iconImage = loadImageIcon(iconResource).getImage();

        this.mainFrame.setTitle("All4land Message Generator Application");
        // JFrame에 아이콘 설정
        this.mainFrame.setIconImage(iconImage);
        this.mainFrame.setExtendedState(Sample1.MAXIMIZED_BOTH); // 최대화 상태로 설정
		this.mainFrame.setVisible(true);
	}
	
	// 이미지를 로드하는 메소드
    private static ImageIcon loadImageIcon(ClassPathResource resource) {
        try {
            return new ImageIcon(ImageIO.read(resource.getInputStream()));
        } catch (IOException e) {
            log.error("Exception [Err_Location] : {}", e.getStackTrace()[0]);
            return null;
        }
    }
}
