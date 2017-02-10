package ch.redmonkeyass.game.module.modules;

import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.datahandling.DataType;
import ch.redmonkeyass.zombieInvasion.module.Module;
import ch.redmonkeyass.zombieInvasion.module.UpdatableModule;
import ch.redmonkeyass.zombieInvasion.util.movement.MovementHelper;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;
import com.badlogic.gdx.math.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;
import java.util.Optional;

/**
 * This module allows selected entities to be moved with a right click on a tile.
 * <p>
 * Used path finding algorithm is <b>AStarGridFinder</b>
 * <p>
 * <b>Needs:</b> <br>
 * DataType.IS_SELECTED,<br>
 * DataType.POSITION,<br>
 * EventType.RIGHT_CLICK<br>
 *
 * @author Matthias
 */
public class MoveSelectedEntityToMouseClick extends Module implements UpdatableModule {

	List<Node> pathToEnd = null;

	public MoveSelectedEntityToMouseClick(String entityID) {
		super(entityID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void UPDATE(GameContainer gc, StateBasedGame sbg) {
		WorldHandler.getEntityHandler().getEventsFrom(getEntityID()).ifPresent(events -> {
			events.parallelStream().forEach(event -> {
				switch (event.getEvent()) {
					case RIGHT_CLICK:
						// Asking the Entity if it is selected
						WorldHandler.getEntityHandler()
								.getDataFrom(getEntityID(), DataType.IS_SELECTED, Boolean.class)
								.ifPresent(isSelected -> {

									if (isSelected) {
										// Getting the MouseClick Position from the RIGHT_CLICK event
										event.getAdditionalInfo(Vector2.class).ifPresent(position -> {

											// Calculating the click position and converting it to B2D coordinates
											// Vector2 moveToPos =
											// position.add(WorldHandler.getCamera().getPosition()).scl(Config.PIX2B).cpy();
											WorldHandler.getEntityHandler()
													.getDataFrom("MOUSE", DataType.MOUSE_SELECTED_NODE, Node.class)
													.ifPresent(goalPos -> {

														// Asking the Entity for its position
														WorldHandler.getEntityHandler()
																.getDataFrom(getEntityID(), DataType.POSITION, Vector2.class)
																.ifPresent(entityPos -> {


																	pathToEnd = MovementHelper.ASTAR_CALCULATOR.calculatePath(
																			WorldHandler.getWorldMap().getMapNodePos(entityPos), goalPos);

																});
													});
										});
									}
								});
				}
			});
		});

	}

	@Override
	public Optional<Object> getData(DataType dataType) {
		switch (dataType) {
			case CALCULATED_PATH:
				return Optional.ofNullable(pathToEnd);
		}
		return Optional.empty();
	}

	@Override
	public void prepareModuleForRemoval() {
		pathToEnd = null;
	}
}
