package game_engine.components;

import game_engine.Component;

public class LivesComponent extends Component<Integer>{
	public LivesComponent(String arg) {
		super(Integer.parseInt(arg));
	}
}
