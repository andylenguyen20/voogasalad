package game_engine.systems;

import java.util.Arrays;
import java.util.List;

import game_engine.Component;
import game_engine.Engine;
import game_engine.Entity;
import game_engine.GameSystem;
import game_engine.components.Collidable;
import game_engine.components.Physics;
import game_engine.components.Position;

public class Collision extends GameSystem{
	private static final Class<? extends Component> PHYSICS = Physics.class;
	private static final Class<? extends Component> POSITION = Position.class;
	private static final Class<? extends Component> COLLIDABLE = Collidable.class;
	
	public Collision(Engine engine) {
		super(engine);
	}

	@Override
	public void act(double elapsedTime) {
		List<Class<? extends Component>> args = Arrays.asList(PHYSICS, POSITION, COLLIDABLE);
		List<Entity> entities = getEngine().getEntitiesContaining(args);
		for (Entity e : entities) {
			for(Entity collidedWith: entities){
				if(collidedWith == e) continue;
				Physics physics = (Physics) e.getComponent(PHYSICS);
				Position position = (Position) e.getComponent(POSITION);
				Collidable collidable = (Collidable) e.getComponent(COLLIDABLE);
				//TODO: check if collided and then perform action based off of collision
			}
		}
	}
	
}
