package com.ethanaa.shootback.component;

import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;

import com.artemis.Component;

public class EntityMesh extends Component {

	private Mesh mesh;
	private MeshView meshView;
	private boolean isVisible = false;
	
	public EntityMesh(MeshView meshView) {
		
		this.meshView = meshView;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}	

	public MeshView getMeshView() {
		return meshView;
	}

	public void setMeshView(MeshView meshView) {
		this.meshView = meshView;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}		
}
