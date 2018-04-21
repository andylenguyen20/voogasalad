package game_engine.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game_engine.Engine;
import game_engine.Entity;
import game_engine.components.DamageComponent;
import game_engine.components.HealthComponent;
import game_engine.components.collision.CollidableComponent;
import game_engine.components.collision.PassableComponent;
import game_engine.components.collision.edge_collided.BottomCollidedComponent;
import game_engine.components.collision.edge_collided.LeftCollidedComponent;
import game_engine.components.collision.edge_collided.RightCollidedComponent;
import game_engine.components.collision.edge_collided.TopCollidedComponent;
import game_engine.components.collision.hitbox.HitboxHeightComponent;
import game_engine.components.collision.hitbox.HitboxWidthComponent;
import game_engine.components.collision.hitbox.HitboxXOffsetComponent;
import game_engine.components.collision.hitbox.HitboxYOffsetComponent;
import game_engine.components.keyboard.DownKeyboardComponent;
import game_engine.components.keyboard.LeftKeyboardComponent;
import game_engine.components.keyboard.RightKeyboardComponent;
import game_engine.components.keyboard.UpKeyboardComponent;
import game_engine.components.physics.DefaultXVelComponent;
import game_engine.components.physics.DefaultYVelComponent;
import game_engine.components.physics.XAccelComponent;
import game_engine.components.physics.XVelComponent;
import game_engine.components.physics.YAccelComponent;
import game_engine.components.physics.YVelComponent;
import game_engine.components.position.AngleComponent;
import game_engine.components.position.XPosComponent;
import game_engine.components.position.YPosComponent;
import game_engine.components.projectile.ProjectileCollidableComponent;
import game_engine.components.projectile.ProjectileDamageComponent;
import game_engine.components.projectile.ProjectileFilenameComponent;
import game_engine.components.projectile.ProjectileHeightComponent;
import game_engine.components.projectile.ProjectileHitboxHeightComponent;
import game_engine.components.projectile.ProjectileHitboxWidthComponent;
import game_engine.components.projectile.ProjectileHitboxXOffsetComponent;
import game_engine.components.projectile.ProjectileHitboxYOffsetComponent;
import game_engine.components.projectile.ProjectileKeyboardInputComponent;
import game_engine.components.projectile.ProjectileWidthComponent;
import game_engine.components.projectile.ProjectileXVelComponent;
import game_engine.components.projectile.ProjectileYVelComponent;
import game_engine.level.Level;
import game_engine.systems.HealthSystem;
import game_engine.systems.PositionSystem;
import game_engine.systems.ProjectileSystem;
import game_engine.systems.VelocitySystem;
import game_engine.systems.collision.CollisionBroadSystem;
import game_engine.systems.collision.CollisionResponseSystem;
import game_engine.systems.keyboard.DownKeyboardMovementSystem;
import game_engine.systems.keyboard.KeyboardJumpSystem;
import game_engine.systems.keyboard.LeftKeyboardMovementSystem;
import game_engine.systems.keyboard.RightKeyboardMovementSystem;
import game_engine.systems.keyboard.UpKeyboardMovementSystem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CollisionTest extends Application {

    private Entity e1;
    private Entity e2;
    private Entity e3;
    
    Map<Entity, Rectangle> spritesMap = new HashMap<Entity, Rectangle>();

    private Engine e;

    private Rectangle r1;
    private Rectangle r2;
    private Rectangle r3;

    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;    
    private static final String GRAVITY = "0";
	private static final String JUMP_VELOCITY = "500"; //effects of how high you jump

    private static final String TITLE = "asdf";
    private static final int WIDTH = 1600;
    private static final int HEIGHT= 800;
    private static final Paint BACKGROUND = Color.rgb(36, 36, 36);

    private Group root;
    private Scene myScene;

    private CollisionBroadSystem colSys;
    private PositionSystem posSys;
    private VelocitySystem velSys;
    private KeyboardJumpSystem keyboardJumpSys;
    private CollisionResponseSystem colResponseSys;
    private LeftKeyboardMovementSystem leftKeySys;
    private RightKeyboardMovementSystem rightKeySys;
    private UpKeyboardMovementSystem upKeySys;
    private DownKeyboardMovementSystem downKeySys;
    private HealthSystem healthSys;
    private ProjectileSystem projSys;

    @Override
    public void start(Stage stage) throws Exception {
        setup();
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();

        // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    /**
     * @param elapsedTime
     */
    private void step(double elapsedTime) {
    	Level currentLevel = e.getLevel();
    	colSys.act(elapsedTime, currentLevel);
    	colResponseSys.act(elapsedTime, currentLevel);
    	posSys.act(elapsedTime, currentLevel);
		velSys.act(elapsedTime, currentLevel);
		keyboardJumpSys.act(elapsedTime, currentLevel); //update jump
		leftKeySys.act(elapsedTime, currentLevel);
		rightKeySys.act(elapsedTime, currentLevel);
		upKeySys.act(elapsedTime, currentLevel);
		downKeySys.act(elapsedTime, currentLevel);
		healthSys.act(elapsedTime, currentLevel);
		projSys.act(elapsedTime, currentLevel);
		
		System.out.println("Health of smol Rect: " + e1.getComponent(HealthComponent.class).getValue());
		System.out.println("Health of Big Rect: " + e3.getComponent(HealthComponent.class).getValue());
		updateAllEntities();
        //updateRectPos();
        updateRectColor();
        e.clearInputs();
    }

    private void setup(){
        root = new Group();
        myScene = new Scene(root, WIDTH, HEIGHT, BACKGROUND);
        myScene.setOnKeyPressed(b -> e.receiveInput(b));
		myScene.setOnKeyReleased(b -> e.receiveInput(b));

    	buildEntities();
        initRects();
        
        colResponseSys = new CollisionResponseSystem();
        keyboardJumpSys = new KeyboardJumpSystem(e);
        colSys = new CollisionBroadSystem();
        posSys = new PositionSystem();
        velSys = new VelocitySystem();
        leftKeySys = new LeftKeyboardMovementSystem(e);
        rightKeySys = new RightKeyboardMovementSystem(e);
        upKeySys = new UpKeyboardMovementSystem(e);
        downKeySys = new DownKeyboardMovementSystem(e);
        healthSys = new HealthSystem();
        projSys = new ProjectileSystem(e);
        
       
    }
    
    private void updateAllEntities() {
    	for (Entity entity : e.getLevel().getEntities()) {
    		if (!spritesMap.containsKey(entity)) {
    			double x = entity.getComponent(XPosComponent.class).getValue();
    			double y = entity.getComponent(YPosComponent.class).getValue();
    			double width = entity.getComponent(HitboxWidthComponent.class).getValue();
    			double height = entity.getComponent(HitboxHeightComponent.class).getValue();

    			Rectangle bullet = new Rectangle(x, y, width, height);
    			spritesMap.put(entity, bullet);
    			root.getChildren().add(bullet);
    		}
    		else if (!spritesMap.isEmpty()){
	    		Rectangle r = spritesMap.get(entity);
	    		double xPos = entity.getComponent(XPosComponent.class).getValue();
	    		double yPos = entity.getComponent(YPosComponent.class).getValue();
	    		r.setX(xPos);
	    		r.setY(yPos);
    		}
    	}
    }

    private void updateRectPos(){
        double x = e1.getComponent(XPosComponent.class).getValue();
        double y = e1.getComponent(YPosComponent.class).getValue();
        double theta = e1.getComponent(AngleComponent.class).getValue();

        r1.setX(x);
        r1.setY(y);
        r1.setRotate(theta);
    }

    private void updateRectColor(){
        if(e1.getComponent(TopCollidedComponent.class)!=null
        		|| e1.getComponent(BottomCollidedComponent.class)!=null
        		|| e1.getComponent(LeftCollidedComponent.class)!=null
        		|| e1.getComponent(RightCollidedComponent.class)!=null){
            r1.setFill(Color.BLUE);
        }
        else{
            if(r1.getFill()!=Color.GRAY){
                r1.setFill(Color.GRAY);
            }
        }
    }

    private void buildEntities(){
    	e1 = new Entity();
        e2 = new Entity();
        e3 = new Entity();

    	LeftKeyboardComponent keyLeftComp = new LeftKeyboardComponent(KeyCode.LEFT.toString());
    	RightKeyboardComponent keyRightComp = new RightKeyboardComponent(KeyCode.RIGHT.toString());
    	UpKeyboardComponent keyUpComp = new UpKeyboardComponent(KeyCode.UP.toString());
    	DownKeyboardComponent keyDownComp = new DownKeyboardComponent(KeyCode.DOWN.toString());
    	e1.addComponent(keyLeftComp);
    	e1.addComponent(keyRightComp);
    	e1.addComponent(keyUpComp);
    	e1.addComponent(keyDownComp);
    	
    	e1.addComponent(new YAccelComponent(GRAVITY));
    	e1.addComponent(new DefaultYVelComponent(JUMP_VELOCITY));
    	e1.addComponent(new YVelComponent("0"));
    	
    	e1.addComponent(new XVelComponent("0"));
    	e1.addComponent(new DefaultXVelComponent("400"));
    	e1.addComponent(new XAccelComponent("0"));
    	
    	e1.addComponent(new XPosComponent("500"));
    	e1.addComponent(new YPosComponent("350"));
    	e1.addComponent(new AngleComponent("0"));
    	
    	e2.addComponent(new XPosComponent("100"));
    	e2.addComponent(new YPosComponent("100"));
    	e2.addComponent(new AngleComponent("0"));
    	
    	e3.addComponent(new XPosComponent("800"));
    	e3.addComponent(new YPosComponent("300"));
    	e3.addComponent(new AngleComponent("0"));
    	
    	
    	e1.addComponent(new HitboxHeightComponent("100.0"));
    	e1.addComponent(new HitboxWidthComponent("100.0"));
    	e1.addComponent(new HitboxXOffsetComponent("0.0"));
    	e1.addComponent(new HitboxYOffsetComponent("0.0"));
    	
    	e2.addComponent(new HitboxHeightComponent("20.0"));
    	e2.addComponent(new HitboxWidthComponent("20.0"));
    	e2.addComponent(new HitboxXOffsetComponent("0.0"));
    	e2.addComponent(new HitboxYOffsetComponent("0.0"));
    	
    	e3.addComponent(new HitboxHeightComponent("500.0"));
    	e3.addComponent(new HitboxWidthComponent("100.0"));
    	e3.addComponent(new HitboxXOffsetComponent("0.0"));
    	e3.addComponent(new HitboxYOffsetComponent("0.0"));
    	
    	e1.addComponent(new CollidableComponent("true"));
    	//e1.addComponent(new PassableComponent("true"));
    	
    	e2.addComponent(new CollidableComponent("true"));
    	e2.addComponent(new PassableComponent("true"));
    	
    	e3.addComponent(new CollidableComponent("true"));
    	//e3.addComponent(new PassableComponent("true"));
    	
    	//Add Health Component
    	e1.addComponent(new HealthComponent("1000")); //100 health points for e1
    	e3.addComponent(new HealthComponent("2000"));
    	e3.addComponent(new DamageComponent("100")); //e3 does 100 damage
    	
    	//Add Project Components to Entity e1
    	e1.addComponent(new ProjectileWidthComponent("20.0"));
    	e1.addComponent(new ProjectileHeightComponent("20.0"));
    	e1.addComponent(new ProjectileHitboxWidthComponent("20.0"));
    	e1.addComponent(new ProjectileHitboxHeightComponent("20.0"));
    	e1.addComponent(new ProjectileHitboxXOffsetComponent("0.0"));
    	e1.addComponent(new ProjectileHitboxYOffsetComponent("0.0"));
    	e1.addComponent(new ProjectileCollidableComponent("true"));
    	e1.addComponent(new ProjectileDamageComponent("2"));
    	e1.addComponent(new ProjectileKeyboardInputComponent(KeyCode.SPACE.toString()));
    	e1.addComponent(new ProjectileXVelComponent("200"));
    	e1.addComponent(new ProjectileYVelComponent("0"));
    	e1.addComponent(new ProjectileFilenameComponent("Mario.GIF"));
    	
    	e = new Engine();
    	
    	Level asdf = e.createLevel();
    	//asdf.addEntity(e2);
    	asdf.addEntity(e1);
    	asdf.addEntity(e3);
    	
    	List<Level> levels = new ArrayList<Level>();
    	levels.add(asdf);


    }

    private void initRects(){
        double x1 = e1.getComponent(XPosComponent.class).getValue();
        double y1 = e1.getComponent(YPosComponent.class).getValue();
        
        double x2 = e2.getComponent(XPosComponent.class).getValue();
        double y2 = e2.getComponent(YPosComponent.class).getValue();
        
        double x3 = e3.getComponent(XPosComponent.class).getValue();
        double y3 = e3.getComponent(YPosComponent.class).getValue();
        
        double hh1 = e1.getComponent(HitboxHeightComponent.class).getValue();
        double hw1 = e1.getComponent(HitboxWidthComponent.class).getValue();
        
        double hh2 = e2.getComponent(HitboxHeightComponent.class).getValue();
        double hw2 = e2.getComponent(HitboxWidthComponent.class).getValue();
        
        double hh3 = e3.getComponent(HitboxHeightComponent.class).getValue();
        double hw3 = e3.getComponent(HitboxWidthComponent.class).getValue();

        r1 = new Rectangle(x1 - hw1/2, y1 - hh1/2, hw1, hh1);
        //r2 = new Rectangle(x2 - hw2/2, y2 - hh2/2, hw2, hh2);
        r3 = new Rectangle(x3 - hw3/2, y3 - hh3/2, hw3, hh3);
        
        spritesMap.put(e1, r1);
        //spritesMap.put(e2, r2);
        spritesMap.put(e3, r3);

        
        
        root.getChildren().add(r1);
        //root.getChildren().add(r2);
        root.getChildren().add(r3);
    }

    /**
     * Starts the program
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
