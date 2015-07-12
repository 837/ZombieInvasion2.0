package ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.heuristics;

import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.Heuristic;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.NavigationNode;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.GridCell;

public class EuclideanDistance implements Heuristic {

    @Override
    public float calculate(NavigationNode from, NavigationNode to) {
        GridCell c1 = (GridCell)from, c2 = (GridCell) to;
        
        return calculate(c2.x - c1.x, c2.y - c1.y);
    }
    
    
    public float calculate(float deltaX, float deltaY){
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

}
