package com.ethanaa.shootback.gui.game;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.AmbientLight;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.ethanaa.shootback.component.EntityMesh;
import com.ethanaa.shootback.component.Position;
import com.ethanaa.shootback.manager.GuiManager;
import com.ethanaa.shootback.type.Tag;

public class GameScene extends BorderPane implements CallbackScene {

	private World world;
	
	private GuiManager guiManager;
	private TagManager tagManager;	
	
	public GameScene(World world) {

		super();
		
		this.world = world;	
		this.guiManager = world.getManager(GuiManager.class);
		this.tagManager = world.getManager(TagManager.class);
		
		setTop(createMenuBar());
	}

	private MenuBar createMenuBar() {

		// File
		Menu fileMenu = new Menu("File");
		
		MenuItem quit = new MenuItem("Quit");
		quit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		quit.setOnAction(ae -> {
			Platform.exit();
			System.exit(0);
		});
		
		// Edit
		
		fileMenu.getItems().addAll(quit);
		
		MenuBar menuBar = new MenuBar();		
		
		menuBar.getMenus().addAll(
				fileMenu
				);
		
		return menuBar;				
	}	

	@Override
	public void onInitialized() {		
		
		Entity playerShip = tagManager.getEntity(Tag.PLAYER_SHIP.name());
		Entity enemyShip = tagManager.getEntity(Tag.ENEMY_SHIP.name());					
		
		final MeshView playerShipMeshView = playerShip.getComponent(EntityMesh.class).getMeshView();
		final MeshView enemyShipMeshView = enemyShip.getComponent(EntityMesh.class).getMeshView();
		
		Group group = new Group();
		group.getChildren().addAll(playerShipMeshView, enemyShipMeshView, new AmbientLight(Color.LIGHTGOLDENRODYELLOW));
		
		addDragHandlers(playerShipMeshView, playerShip);
		addDragHandlers(enemyShipMeshView, enemyShip);
		
		SubScene meshScene = create3dScene(group);								
				
		setCenter(meshScene);
		setLeft(createControls(playerShip));
			
		final Position position = playerShip.getComponent(Position.class);
		guiManager.getPrimaryStage().getScene().setOnKeyPressed(e -> {			
			if (e.getCode() == KeyCode.SPACE) {
				position.setTranslateX(0.0d);
				position.setTranslateY(0.0d);
				e.consume();
			}
		});
	}
	
	private void addDragHandlers(MeshView meshView, Entity entity) {		
		
		final Position position = entity.getComponent(Position.class);		
		
		meshView.setOnMouseEntered(e -> {
			meshView.setCursor(Cursor.HAND);
		});
		
		meshView.setOnMousePressed(e -> {			
			position.getDragDelta().x = meshView.getTranslateX() - e.getSceneX();
			position.getDragDelta().y = meshView.getTranslateY() - e.getSceneY();
			meshView.setCursor(Cursor.MOVE);
		});
		
		meshView.setOnMouseDragged(e -> {			
			position.setTranslateX(e.getSceneX() / 50.0d + position.getDragDelta().x / 50.0d);
			position.setTranslateY(e.getSceneY() / 50.0d + position.getDragDelta().y / 50.0d);			
		});
		
		meshView.setOnMouseReleased(e -> {
			meshView.setCursor(Cursor.HAND);
		});		
	}

	private SubScene create3dScene(Group group) {

		double scene3dWidth = guiManager.getPrimaryStage().getWidth() / 1.2d;		
		SubScene scene3d = new SubScene(group, scene3dWidth, scene3dWidth * 9.0d/16, true, SceneAntialiasing.BALANCED);
		
		scene3d.widthProperty().bind(widthProperty().divide(1.2d));
		scene3d.heightProperty().bind(scene3d.widthProperty().multiply(9.0d/16));
		
	    scene3d.setFill(Color.rgb(232, 175, 100));
	    
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.setFieldOfView(60.0d);
		camera.setNearClip(0.1d);
		camera.setFarClip(10_000.0d);
		camera.setTranslateZ(-5.0d);
		
		scene3d.setCamera(camera);		
		
		return scene3d;
	}
	
	private VBox createControls(Entity entity) {
		
		MeshView meshView = entity.getComponent(EntityMesh.class).getMeshView();
		Position position = entity.getComponent(Position.class);
		
		CheckBox wireframe = new CheckBox("Wireframe");
		wireframe.setFocusTraversable(false);
		meshView.drawModeProperty().bind(
				Bindings.when(wireframe.selectedProperty())
					.then(DrawMode.LINE)
					.otherwise(DrawMode.FILL));
		
		CheckBox rotate = new CheckBox("Rotate");
		rotate.setFocusTraversable(false);
		rotate.selectedProperty().set(true);
		rotate.selectedProperty().addListener(observable -> {
			position.setIsRotating(rotate.isSelected());
		});
	
		Slider rotationalVelocity = new Slider(1, 120, position.getRotationDegreesPerTick());
		rotationalVelocity.setFocusTraversable(false);
		rotationalVelocity.showTickLabelsProperty().set(true);
		rotationalVelocity.showTickMarksProperty().set(true);
		rotationalVelocity.setBlockIncrement(30);
		rotationalVelocity.valueProperty().addListener((observable, oldVal, newVal) -> {
			position.setRotationDegreesPerTick(newVal.intValue());
		});
		
		VBox controls = new VBox(10, wireframe, rotate, rotationalVelocity);
		controls.setFocusTraversable(false);
		controls.setPadding(new Insets(10));
		return controls;
	}	
	
}
