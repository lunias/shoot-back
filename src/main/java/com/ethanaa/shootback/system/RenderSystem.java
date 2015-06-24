package com.ethanaa.shootback.system;

import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.ethanaa.shootback.component.EntityMesh;
import com.ethanaa.shootback.component.Position;
import com.ethanaa.shootback.manager.GuiManager;

@Wire
public class RenderSystem extends EntityProcessingSystem {

	protected ComponentMapper<Position> positionMapper;
	protected ComponentMapper<EntityMesh> entityMeshMapper;
	
	protected GuiManager guiManager;	
	
	@SuppressWarnings("unchecked")
	public RenderSystem() {
		super(Aspect.getAspectForAll(Position.class, EntityMesh.class));
	}

	@Override
	protected void process(Entity e) {				
		
		Position position = positionMapper.get(e);
		EntityMesh entityMesh = entityMeshMapper.get(e);

		MeshView meshView = entityMesh.getMeshView();
		
		meshView.setTranslateX(position.getTranslateX());
		meshView.setTranslateY(position.getTranslateY());
		
		if (position.getIsRotating()) {
			
			Rotate currentRotation = position.getRotation();
			double currentRotationAngle = currentRotation.getAngle();				
			
			double newRotationAngle = currentRotationAngle + position.getRotationDegreesPerTick();				
			if (newRotationAngle >= 360d) newRotationAngle = 0d;
			
			currentRotation.setAngle(newRotationAngle);					
			
			meshView.getTransforms().clear();		
			meshView.getTransforms().addAll(currentRotation);	
		}		
	}

}
