package com.ethanaa.shootback.manager;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Manager;
import com.ethanaa.shootback.gui.game.CallbackScene;
import com.ethanaa.shootback.gui.game.GameScene;

public class GuiManager extends Manager {

	public static final String STYLESHEET = "com/ethanaa/shootback/css/sb.css";
	
	private Stage primaryStage;
	
	public enum State {
		GAME, NOOP;
	}
	
	private State state;
	private boolean dirty;
	
	private static final Logger logger = LoggerFactory.getLogger(GuiManager.class);	
	
	public GuiManager(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		this.state = State.GAME;
		this.dirty = true;
	}
	
	public void changeState(State newState) {
		
		this.state = newState;
		this.dirty = true;
	}
	
	public void dirty() {
		
		this.dirty = true;
	}

	public void render() {		
		
		if (!dirty) return;
		
		switch(state) {
		case GAME:
			renderGameScene();			
			break;
		case NOOP:
			break;
		default:
			logger.error("Cannot find GuiSystem state " + state + ".");
			throw new RuntimeException("Encountered unknown GuiSystem state.");
		}		
	}		

	private void renderSceneToStage(CallbackScene parent) {
		
		Scene scene = primaryStage.getScene();
		
		if (scene == null) {
			scene = new Scene((Parent) parent);
			scene.getStylesheets().add(STYLESHEET);			
			primaryStage.setScene(scene);
			
		} else {
			scene.setRoot((Parent) parent);
		}
		
		parent.onInitialized();
		
		this.dirty = false;
	}
	
	private void renderGameScene() {		
		
		renderSceneToStage(new GameScene(world));
	}		
	
	public Stage getPrimaryStage() {
		
		return primaryStage;
	}	
}
