package ch.redmonkeyass.zombieInvasion.worldmap;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.worldmap.WorldMap.FieldType;
import ch.redmonkeyass.zombieInvasion.worldmap.pathfinding.grid.GridCell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Node extends GridCell {
    private final FieldType type;
    private final int tileSize;
    private Body b2dBody;
    private int regionGroup = 0;

    public Node(int x, int y, Body b2dBody, FieldType type, int tileSize) {
        super(x, y, type.isWalkable());
        this.b2dBody = b2dBody;
        this.type = type;
        this.tileSize = tileSize;
    }

    public Body getBody() {
        return b2dBody;
    }

    public FieldType getType() {
        return type;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getRegionGroup() {
        return regionGroup;
    }

    public void setRegionGroup(int regionGroup) {
        this.regionGroup = regionGroup;
    }

    public boolean isSame(Node other) {
        return (other.x == this.x && other.y == this.y);
    }

    public void prepareForRemoval() {
        if (b2dBody != null) {
            WorldHandler.getB2World().destroyBody(b2dBody);
            b2dBody = null;
        }
    }

    public Vector2 getCornerInMeter(CornerType type) {
        float offSet = (tileSize / (tileSize / Config.B2PIX));
        switch (type) {
            case BOTTOM_LEFT:
                return new Vector2(x, y + (offSet * Config.PIX2B));
            case BOTTOM_RIGHT:
                return new Vector2(x + (offSet * Config.PIX2B), y + (offSet * Config.PIX2B));
            case TOP_LEFT:
                return new Vector2(x, y);
            case TOP_RIGHT:
                return new Vector2(x + (offSet * Config.PIX2B), y);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Node:[" + x + "][" + y + "]{Type=" + type.name() + "; TileSize=" + getTileSize() + ";}";
    }


    enum CornerType {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}
