package com.ethanaa.shootback;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;
import com.ethanaa.shootback.manager.GuiManager;
import com.ethanaa.shootback.system.EntityFactorySystem;
import com.ethanaa.shootback.system.RenderSystem;
import com.ethanaa.shootback.util.ResourceUtil;

public class Main extends Application {

	public static final String VERSION = "0.0.1";
	private static final String APPLICATION_ICON = "icon.png";
			
	private final Logger logger = LoggerFactory.getLogger(Main.class);
	
	private Stage primaryStage;
	private World world;
	
	private GuiManager guiManager;	
	
	public static void main(String[] args) {
		
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		Screen screen = Screen.getPrimary();
		Rectangle2D screenBounds = screen.getVisualBounds();		
		
		// setup JavaFX stage
		primaryStage = stage;
		primaryStage.setOnCloseRequest(we -> {
			Platform.exit();
			System.exit(0);
		});
		
		double stageWidth = screenBounds.getWidth() / 1.2d;
		double stageHeight = screenBounds.getHeight() / 1.2d;
		
		primaryStage.setWidth(stageWidth);
		primaryStage.setHeight(stageHeight);	
		primaryStage.setX(screenBounds.getWidth() / 2.0d - stageWidth / 2.0d);
		primaryStage.setY(screenBounds.getHeight() / 2.0d - stageHeight / 2.0d);	
		
		primaryStage.setTitle(ResourceUtil.getString("primaryStage.title"));		
		
		primaryStage.getIcons().add(ResourceUtil.getImage(APPLICATION_ICON));
		
		// setup Artemis world
		world = new World();
		world.setManager(new GroupManager());
		world.setManager(new TagManager());
		world.setManager(new PlayerManager());		
		guiManager = world.setManager(new GuiManager(primaryStage));		
		
		EntityFactorySystem efs;
		world.setSystem(efs = new EntityFactorySystem());
		world.setSystem(new RenderSystem());
		
		world.initialize();
		
		primaryStage.show();
		
		logger.debug("World initialized.");
		
		efs.initGame();
		
		gameLoop();
	}

	private void gameLoop() {		
		
		Task<Integer> gameTask = new Task<Integer>() {

			@Override
			protected Integer call() throws Exception {

				boolean gameOver = false;
				
				final double targetDelta = 0.0333;
				final double maxDelta = 0.05;
				long previousTime = System.nanoTime();
				
				while(!gameOver) {
					
					if (isCancelled()) {
						logger.debug("Game loop cancelled. Return 1.");
						return 1;
					}
					
					long currentTime = System.nanoTime();
					double deltaTime = (currentTime - previousTime) / 1_000_000_000.0;
					
					if (deltaTime > maxDelta) {
						deltaTime = maxDelta;
					}					
					
					// update the game state
					update(deltaTime);
					
					// render on the JavaFX app thread
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							render();	
						}

						private void render() {
							guiManager.render();
						}
						
					});
					
					previousTime = currentTime;
					
					double frameTime = (System.nanoTime() - currentTime) / 1_000_000_000.0;										
					
					if (frameTime < targetDelta) {
						try {
							long sleepTime = (long) ((targetDelta - frameTime) * 1000);
							Thread.sleep(sleepTime);							
						} catch (InterruptedException ie) {
							if (isCancelled()) {
								logger.debug("Game loop cancelled while sleeping. Return 2.");
								return 2;
							}
						}
					}
				}
				
				logger.debug("Game Over. Return 0.");
				return 0;
			}

			private void update(double deltaTime) {				
				Platform.runLater(() -> {
					world.setDelta((float) deltaTime);
					world.process();
				});				
			}			
		};
		
		Thread gameThread = new Thread(gameTask);
		gameThread.setDaemon(true);
		gameThread.start();
	}
}
