package ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid;

import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.NavigationNode;

/**
 * A node within a {@link NavigationGridGraphNode}. It contains an [x,y] coordinate.
 * @author Xavier Guzman
 */
public interface NavigationGridGraphNode extends NavigationNode{
    int getX();
    int getY();
    void setX(int x);
    void setY(int y);
}
