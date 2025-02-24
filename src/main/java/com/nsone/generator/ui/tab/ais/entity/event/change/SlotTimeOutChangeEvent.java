package com.nsone.generator.ui.tab.ais.entity.event.change;

import javax.swing.JTable;

import org.springframework.context.ApplicationEvent;

import com.nsone.generator.ui.tab.ais.entity.MmsiEntity;

public class SlotTimeOutChangeEvent extends ApplicationEvent {

	private static final long serialVersionUID = -604897380289147197L;
	
	private char channel;
	private MmsiEntity mmsiEntity;
	private JTable currentFrameJTableNameUpper;
	private JTable currentFrame1JTableNameUpper;
	private JTable currentFrame2JTableNameUpper;
	private JTable currentFrame3JTableNameUpper;
	private JTable currentFrame4JTableNameUpper;
	private JTable currentFrame5JTableNameUpper;
	private JTable currentFrame6JTableNameUpper;
	private JTable currentFrame7JTableNameUpper;

	public SlotTimeOutChangeEvent(Object source, char channel, MmsiEntity mmsiEntity
			, JTable currentFrameJTableNameUpper, JTable currentFrame1JTableNameUpper,
			JTable currentFrame2JTableNameUpper, JTable currentFrame3JTableNameUpper, JTable currentFrame4JTableNameUpper,
			JTable currentFrame5JTableNameUpper, JTable currentFrame6JTableNameUpper, JTable currentFrame7JTableNameUpper) {
		super(source);
		
		this.channel = channel;
		this.mmsiEntity = mmsiEntity;
		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
		this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
		this.currentFrame2JTableNameUpper = currentFrame2JTableNameUpper;
		this.currentFrame3JTableNameUpper = currentFrame3JTableNameUpper;
		this.currentFrame4JTableNameUpper = currentFrame4JTableNameUpper;
		this.currentFrame5JTableNameUpper = currentFrame5JTableNameUpper;
		this.currentFrame6JTableNameUpper = currentFrame6JTableNameUpper;
		this.currentFrame7JTableNameUpper = currentFrame7JTableNameUpper;
	}

	public MmsiEntity getMmsiEntity() {
		return mmsiEntity;
	}

	public void setMmsiEntity(MmsiEntity mmsiEntity) {
		this.mmsiEntity = mmsiEntity;
	}

	public char getChannel() {
		return channel;
	}

	public void setChannel(char channel) {
		this.channel = channel;
	}

	public JTable getCurrentFrameJTableNameUpper() {
		return currentFrameJTableNameUpper;
	}

	public void setCurrentFrameJTableNameUpper(JTable currentFrameJTableNameUpper) {
		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
	}

	public JTable getCurrentFrame1JTableNameUpper() {
		return currentFrame1JTableNameUpper;
	}

	public void setCurrentFrame1JTableNameUpper(JTable currentFrame1JTableNameUpper) {
		this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
	}

	public JTable getCurrentFrame2JTableNameUpper() {
		return currentFrame2JTableNameUpper;
	}

	public void setCurrentFrame2JTableNameUpper(JTable currentFrame2JTableNameUpper) {
		this.currentFrame2JTableNameUpper = currentFrame2JTableNameUpper;
	}

	public JTable getCurrentFrame3JTableNameUpper() {
		return currentFrame3JTableNameUpper;
	}

	public void setCurrentFrame3JTableNameUpper(JTable currentFrame3JTableNameUpper) {
		this.currentFrame3JTableNameUpper = currentFrame3JTableNameUpper;
	}

	public JTable getCurrentFrame4JTableNameUpper() {
		return currentFrame4JTableNameUpper;
	}

	public void setCurrentFrame4JTableNameUpper(JTable currentFrame4JTableNameUpper) {
		this.currentFrame4JTableNameUpper = currentFrame4JTableNameUpper;
	}

	public JTable getCurrentFrame5JTableNameUpper() {
		return currentFrame5JTableNameUpper;
	}

	public void setCurrentFrame5JTableNameUpper(JTable currentFrame5JTableNameUpper) {
		this.currentFrame5JTableNameUpper = currentFrame5JTableNameUpper;
	}

	public JTable getCurrentFrame6JTableNameUpper() {
		return currentFrame6JTableNameUpper;
	}

	public void setCurrentFrame6JTableNameUpper(JTable currentFrame6JTableNameUpper) {
		this.currentFrame6JTableNameUpper = currentFrame6JTableNameUpper;
	}

	public JTable getCurrentFrame7JTableNameUpper() {
		return currentFrame7JTableNameUpper;
	}

	public void setCurrentFrame7JTableNameUpper(JTable currentFrame7JTableNameUpper) {
		this.currentFrame7JTableNameUpper = currentFrame7JTableNameUpper;
	}

	
}