package com.ethanaa.shootback.component;

import com.artemis.Component;

public class Health extends Component {

	private int currentHealth;
	private int totalHealth;
	private boolean isAlive = true;
	
	public Health(int totalHealth) {
		
		this.currentHealth = totalHealth;
		this.totalHealth = totalHealth;
	}
	
	public int getCurrentHealth() {
		
		return this.currentHealth;
	}
	
	public void setCurrentHealth(int currentHealth) {
		
		this.currentHealth = currentHealth;
	}
	
	public int getTotalHealth() {
		
		return this.getTotalHealth();
	}
	
	public void setTotalHealth(int totalHealth) {
		
		this.totalHealth = totalHealth;
	}
	
	public void damage(int ammount) {
		
		this.currentHealth -= ammount;
		this.isAlive = this.currentHealth <= 0;
	}
	
	public float getHealthPercentage() {
		
		return this.currentHealth / this.totalHealth;
	}

	public boolean isAlive() {
		
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		
		this.isAlive = isAlive;
	}	
}
