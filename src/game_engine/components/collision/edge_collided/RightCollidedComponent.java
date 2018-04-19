package game_engine.components.collision.edge_collided;

import java.util.List;

import game_engine.Entity;
import game_engine.components.collision.CollidedComponent;

/**
 * @author Jeremy Chen
 * Temporary subclass of CollidedComponent, that allows systems to distinguish which side (in this case right) of an AABB an entity has been collided with
 */
public class RightCollidedComponent extends CollidedComponent {
	public RightCollidedComponent(List<Entity> val){
    	super(val);
    }
}
