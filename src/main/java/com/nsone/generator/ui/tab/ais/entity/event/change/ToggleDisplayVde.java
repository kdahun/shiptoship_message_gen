package com.nsone.generator.ui.tab.ais.entity.event.change;

import javax.swing.JTable;

import org.springframework.context.ApplicationEvent;

public class ToggleDisplayVde extends ApplicationEvent {
	//
	private static final long serialVersionUID = 8978271322618217609L;

	private boolean bool;
	private JTable currentFrameJTableNameUpper;
	private JTable currentFrame1JTableNameUpper;
	private JTable currentFrame2JTableNameUpper;
	private JTable currentFrame3JTableNameUpper;
	private JTable currentFrame4JTableNameUpper;
	private JTable currentFrame5JTableNameUpper;
	private JTable currentFrame6JTableNameUpper;
	private JTable currentFrame7JTableNameUpper;
	private JTable currentFrame8JTableNameUpper;
	private JTable currentFrame9JTableNameUpper;
	private JTable currentFrame10JTableNameUpper;
	private JTable currentFrameJTableNameLower;
	private JTable currentFrame1JTableNameLower;
	private JTable currentFrame2JTableNameLower;
	private JTable currentFrame3JTableNameLower;
	private JTable currentFrame4JTableNameLower;
	private JTable currentFrame5JTableNameLower;
	private JTable currentFrame6JTableNameLower;
	private JTable currentFrame7JTableNameLower;
	private JTable currentFrame8JTableNameLower;
	private JTable currentFrame9JTableNameLower;
	private JTable currentFrame10JTableNameLower;

	public ToggleDisplayVde(Object source, boolean bool, JTable currentFrameJTableNameUpper,
			JTable currentFrame1JTableNameUpper, JTable currentFrame2JTableNameUpper,
			JTable currentFrame3JTableNameUpper, JTable currentFrame4JTableNameUpper,
			JTable currentFrame5JTableNameUpper, JTable currentFrame6JTableNameUpper,
			JTable currentFrame7JTableNameUpper, JTable currentFrame8JTableNameUpper,
			JTable currentFrame9JTableNameUpper, JTable currentFrame10JTableNameUpper,
			
			JTable currentFrameJTableNameLower,
			JTable currentFrame1JTableNameLower, JTable currentFrame2JTableNameLower,
			JTable currentFrame3JTableNameLower, JTable currentFrame4JTableNameLower,
			JTable currentFrame5JTableNameLower, JTable currentFrame6JTableNameLower,
			JTable currentFrame7JTableNameLower, JTable currentFrame8JTableNameLower,
			JTable currentFrame9JTableNameLower, JTable currentFrame10JTableNameLower) {
		super(source);
		// TODO Auto-generated constructor stub
		this.bool = bool;
		this.currentFrameJTableNameUpper = currentFrameJTableNameUpper;
		this.currentFrame1JTableNameUpper = currentFrame1JTableNameUpper;
		this.currentFrame2JTableNameUpper = currentFrame2JTableNameUpper;
		this.currentFrame3JTableNameUpper = currentFrame3JTableNameUpper;
		this.currentFrame4JTableNameUpper = currentFrame4JTableNameUpper;
		this.currentFrame5JTableNameUpper = currentFrame5JTableNameUpper;
		this.currentFrame6JTableNameUpper = currentFrame6JTableNameUpper;
		this.currentFrame7JTableNameUpper = currentFrame7JTableNameUpper;
		this.currentFrame8JTableNameUpper = currentFrame8JTableNameUpper;
		this.currentFrame9JTableNameUpper = currentFrame9JTableNameUpper;
		this.currentFrame10JTableNameUpper = currentFrame10JTableNameUpper;

		this.currentFrameJTableNameLower = currentFrameJTableNameLower;
		this.currentFrame1JTableNameLower = currentFrame1JTableNameLower;
		this.currentFrame2JTableNameLower = currentFrame2JTableNameLower;
		this.currentFrame3JTableNameLower = currentFrame3JTableNameLower;
		this.currentFrame4JTableNameLower = currentFrame4JTableNameLower;
		this.currentFrame5JTableNameLower = currentFrame5JTableNameLower;
		this.currentFrame6JTableNameLower = currentFrame6JTableNameLower;
		this.currentFrame7JTableNameLower = currentFrame7JTableNameLower;
		this.currentFrame8JTableNameLower = currentFrame8JTableNameLower;
		this.currentFrame9JTableNameLower = currentFrame9JTableNameLower;
		this.currentFrame10JTableNameLower = currentFrame10JTableNameLower;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
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

	public JTable getCurrentFrameJTableNameLower() {
		return currentFrameJTableNameLower;
	}

	public void setCurrentFrameJTableNameLower(JTable currentFrameJTableNameLower) {
		this.currentFrameJTableNameLower = currentFrameJTableNameLower;
	}

	public JTable getCurrentFrame1JTableNameLower() {
		return currentFrame1JTableNameLower;
	}

	public void setCurrentFrame1JTableNameLower(JTable currentFrame1JTableNameLower) {
		this.currentFrame1JTableNameLower = currentFrame1JTableNameLower;
	}

	public JTable getCurrentFrame2JTableNameLower() {
		return currentFrame2JTableNameLower;
	}

	public void setCurrentFrame2JTableNameLower(JTable currentFrame2JTableNameLower) {
		this.currentFrame2JTableNameLower = currentFrame2JTableNameLower;
	}

	public JTable getCurrentFrame3JTableNameLower() {
		return currentFrame3JTableNameLower;
	}

	public void setCurrentFrame3JTableNameLower(JTable currentFrame3JTableNameLower) {
		this.currentFrame3JTableNameLower = currentFrame3JTableNameLower;
	}

	public JTable getCurrentFrame4JTableNameLower() {
		return currentFrame4JTableNameLower;
	}

	public void setCurrentFrame4JTableNameLower(JTable currentFrame4JTableNameLower) {
		this.currentFrame4JTableNameLower = currentFrame4JTableNameLower;
	}

	public JTable getCurrentFrame5JTableNameLower() {
		return currentFrame5JTableNameLower;
	}

	public void setCurrentFrame5JTableNameLower(JTable currentFrame5JTableNameLower) {
		this.currentFrame5JTableNameLower = currentFrame5JTableNameLower;
	}

	public JTable getCurrentFrame6JTableNameLower() {
		return currentFrame6JTableNameLower;
	}

	public void setCurrentFrame6JTableNameLower(JTable currentFrame6JTableNameLower) {
		this.currentFrame6JTableNameLower = currentFrame6JTableNameLower;
	}

	public JTable getCurrentFrame7JTableNameLower() {
		return currentFrame7JTableNameLower;
	}

	public void setCurrentFrame7JTableNameLower(JTable currentFrame7JTableNameLower) {
		this.currentFrame7JTableNameLower = currentFrame7JTableNameLower;
	}

	public JTable getCurrentFrame8JTableNameUpper() {
		return currentFrame8JTableNameUpper;
	}

	public void setCurrentFrame8JTableNameUpper(JTable currentFrame8JTableNameUpper) {
		this.currentFrame8JTableNameUpper = currentFrame8JTableNameUpper;
	}

	public JTable getCurrentFrame9JTableNameUpper() {
		return currentFrame9JTableNameUpper;
	}

	public void setCurrentFrame9JTableNameUpper(JTable currentFrame9JTableNameUpper) {
		this.currentFrame9JTableNameUpper = currentFrame9JTableNameUpper;
	}

	public JTable getCurrentFrame10JTableNameUpper() {
		return currentFrame10JTableNameUpper;
	}

	public void setCurrentFrame10JTableNameUpper(JTable currentFrame10JTableNameUpper) {
		this.currentFrame10JTableNameUpper = currentFrame10JTableNameUpper;
	}

	public JTable getCurrentFrame8JTableNameLower() {
		return currentFrame8JTableNameLower;
	}

	public void setCurrentFrame8JTableNameLower(JTable currentFrame8JTableNameLower) {
		this.currentFrame8JTableNameLower = currentFrame8JTableNameLower;
	}

	public JTable getCurrentFrame9JTableNameLower() {
		return currentFrame9JTableNameLower;
	}

	public void setCurrentFrame9JTableNameLower(JTable currentFrame9JTableNameLower) {
		this.currentFrame9JTableNameLower = currentFrame9JTableNameLower;
	}

	public JTable getCurrentFrame10JTableNameLower() {
		return currentFrame10JTableNameLower;
	}

	public void setCurrentFrame10JTableNameLower(JTable currentFrame10JTableNameLower) {
		this.currentFrame10JTableNameLower = currentFrame10JTableNameLower;
	}

}
