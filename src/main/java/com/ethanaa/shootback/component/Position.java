package com.ethanaa.shootback.component;

import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

import com.artemis.Component;

public class Position extends Component {

	private Point3D position;	
	private Rotate rotation;
	private Boolean isRotating;
	private int rotationDegreesPerTick = 1;
	private double translateX = 0.0d;
	private double translateY = 0.0d;
	private Delta dragDelta = new Delta(0.0d, 0.0d);
	
	public class Delta {
		public double x, y;		
		public Delta(double x, double y) { this.x = x; this.y = y;}
	}
	
	public Position(Point3D position, Rotate rotation) {
		
		this.setPosition(position);
		this.setRotation(rotation);
		this.setIsRotating(true);
		
		this.setTranslateX(position.getX());
		this.setTranslateY(position.getY());		
	}
	
	public Position(Point3D position) {
		
		this.setPosition(position);
		this.setIsRotating(false);
		
		this.setTranslateX(position.getX());
		this.setTranslateY(position.getY());
	}

	public Point3D getPosition() {
		return position;
	}

	public void setPosition(Point3D position) {
		this.position = position;
	}

	public Rotate getRotation() {
		return rotation;
	}

	public void setRotation(Rotate rotation) {
		this.rotation = rotation;
	}

	public Boolean getIsRotating() {
		return isRotating;
	}

	public void setIsRotating(Boolean isRotating) {
		this.isRotating = isRotating;
	}

	public int getRotationDegreesPerTick() {
		return rotationDegreesPerTick;
	}

	public void setRotationDegreesPerTick(int rotationDegreesPerTick) {
		this.rotationDegreesPerTick = rotationDegreesPerTick;
	}

	public double getTranslateX() {
		return translateX;
	}

	public void setTranslateX(double sceneX) {
		this.translateX = sceneX;
	}

	public double getTranslateY() {
		return translateY;
	}

	public void setTranslateY(double sceneY) {
		this.translateY = sceneY;
	}

	public Delta getDragDelta() {
		return dragDelta;
	}	
}
