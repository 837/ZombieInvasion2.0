package ch.redmonkeyass.game.gamestates;

import ch.redmonkeyass.game.entityfactories.EntityBuilder;
import ch.redmonkeyass.game.entityfactories.waves.xmlLoader.XMLWaveLoader;
import ch.redmonkeyass.game.module.modules.*;
import ch.redmonkeyass.game.module.modules.UNUSED.ThetaStarMovementModule;
import ch.redmonkeyass.game.module.modules.debugmodules.DebugRendererModule;
import ch.redmonkeyass.game.module.modules.game.DebugConsoleModule;
import ch.redmonkeyass.game.module.modules.game.DebugRendererGameModule;
import ch.redmonkeyass.game.module.modules.mouse.MouseSelectionModule;
import ch.redmonkeyass.game.module.modules.mouse.MouseTileSelectionModule;
import ch.redmonkeyass.game.module.modules.zombieAI.FollowPlayerAI;
import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entityfactories.EntityType;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;
import ch.redmonkeyass.zombieInvasion.input.InputHandler;
import ch.redmonkeyass.zombieInvasion.module.modules.EntityStatusModule;
import ch.redmonkeyass.zombieInvasion.util.MathUtil;
import ch.redmonkeyass.zombieInvasion.util.shadows.ShadowsShaderManager;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;
import com.badlogic.gdx.math.Vector2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.pbuffer.FBOGraphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.File;
import java.nio.FloatBuffer;

import static ch.redmonkeyass.zombieInvasion.util.shadows.ShadowsShaderManager.toOrtho2D;
import static org.lwjgl.opengl.GL11.*;

public class Game extends BasicGameState {
    private final int ID;
    private final int shadowsRadius = 1024;
    private final int h = shadowsRadius;
    FBOGraphics shadowCastersFBO;
    ShadowsShaderManager shadowsShaderManager;
    FloatBuffer mvpMatrixBuffer = BufferUtils.createFloatBuffer(16);
    XMLWaveLoader xmlWaveLoader = null;
    private double next_game_tick = System.currentTimeMillis();
    private int loops;
    // private double extrapolation;
    private InputHandler inputHandler = null;
    private Logger logger = LogManager.getLogger(Game.class);
    private Image shadowCastersTexture;
    private Image shadowTexture;
    private FBOGraphics shadowFBO;


    public Game(int ID) {
        this.ID = ID;
    }

    //ShaderTester st = new ShaderTester();

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        EntityBuilder.createBuilder(EntityType.MOUSE).createEntity();
        EntityBuilder.createBuilder(EntityType.GAME).createEntity();
        shadowCastersTexture = new Image(Config.WIDTH, Config.HEIGHT);
        shadowCastersFBO = new FBOGraphics(shadowCastersTexture);
        shadowTexture = new Image(Config.WIDTH, Config.HEIGHT);
        shadowFBO = new FBOGraphics(shadowTexture);

        // EntityBuilder.createBuilder(EntityType.ADOLF)
        // .startPosition(WorldHandler.getWorldMap().getWorldMapLoader().getStartRoomPos())
        // .createEntity();
        //
        // EntityBuilder.createBuilder(EntityType.HANS)
        // .startPosition(WorldHandler.getWorldMap().getWorldMapLoader().getStartRoomPos())
        // .createEntity();
        //
        // EntityBuilder.createBuilder(EntityType.GERHART)
        // .startPosition(WorldHandler.getWorldMap().getWorldMapLoader().getStartRoomPos())
        // .createEntity();
        Matrix4f model = new Matrix4f();
        Matrix4f view = new Matrix4f();
        Matrix4f projection = toOrtho2D(null, 0, 0, shadowsRadius, h, 1, -1);
        // MVP = M * V * P;
        Matrix4f mvp = Matrix4f.mul(model, Matrix4f.mul(view, projection, null), null);
        mvp.store(mvpMatrixBuffer);
        mvpMatrixBuffer.flip();//prepare for read

        shadowsShaderManager = new ShadowsShaderManager(mvpMatrixBuffer, shadowsRadius, h, Config.WIDTH, Config.HEIGHT);
        glDisable(GL_DEPTH_TEST);
        glClearColor(0, 0, 0, 1);
        int glError = glGetError();
        if (glError != 0) System.err.println("gl error: " + glError);

        inputHandler = new InputHandler(gc);


        WorldHandler.getCamera().setMapData(WorldHandler.getWorldMap().getMapWidthInMeter(),
                WorldHandler.getWorldMap().getMapHeightInMeter());


        xmlWaveLoader =
                new XMLWaveLoader(new File(Config.RESSOURCE_FOLDER + "/waves/" + Config.WAVES_FILE_NAME));

        xmlWaveLoader.getWaves().get(0).getEntityBuilders().forEach(EntityBuilder::createEntity);
        // .forEach(e -> e.getEntityBuilders().forEach(b -> b.createEntity()));
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        g.translate(-WorldHandler.getCamera().getPosition().x,
                -WorldHandler.getCamera().getPosition().y);


        // WorldMap
        WorldHandler.getWorldMap().RENDER(gc, sbg, g);

        // not need anymoere
//        WorldHandler.getModuleHandler().getModulesOf(SimpleImageRenderModule.class)
//                .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

        WorldHandler.getModuleHandler().getModulesOf(LightEmitter.class)
                .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

        //
        // GL11.glEnable(GL11.GL_BLEND);
        // GL11.glBlendFunc(GL11.GL_DST_ALPHA, GL11.GL_ONE_MINUS_DST_ALPHA);
        //
        // GL11.glBegin(GL11.GL_QUADS);
        // GL11.glColor4f(0, 0, 0, 0);
        // GL11.glVertex2f(WorldHandler.getCamera().getPosition().x,
        // WorldHandler.getCamera().getPosition().y);
        // GL11.glVertex2f(
        // WorldHandler.getCamera().getPosition().x + WorldHandler.getCamera().getViewport_size_X(),
        // WorldHandler.getCamera().getPosition().y);
        // GL11.glVertex2f(
        // WorldHandler.getCamera().getPosition().x + WorldHandler.getCamera().getViewport_size_X(),
        // WorldHandler.getCamera().getPosition().y + WorldHandler.getCamera().getViewport_size_Y());
        // GL11.glVertex2f(WorldHandler.getCamera().getPosition().x,
        // WorldHandler.getCamera().getPosition().y + WorldHandler.getCamera().getViewport_size_Y());
        // GL11.glEnd();
        // GL11.glDisable(GL11.GL_BLEND);
        // g.setDrawMode(Graphics.MODE_NORMAL);


        WorldHandler.getModuleHandler().getModulesOf(MouseSelectionModule.class)
                .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

        WorldHandler.getModuleHandler().getModulesOf(DebugRendererModule.class)
                .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

        WorldHandler.getModuleHandler().getModulesOf(DebugRendererGameModule.class)
                .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

        WorldHandler.getModuleHandler().getModulesOf(DebugConsoleModule.class)
                .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

        // XXX TEST END
        shadowPass(gc, sbg, g);
//        g.drawImage(shadowTexture, 0, 0);
//        g.drawImage(shadowTexture, WorldHandler.getCamera().getPosition().x,
//                WorldHandler.getCamera().getPosition().y);
        g.drawImage(shadowCastersTexture, WorldHandler.getCamera().getPosition().x,
                WorldHandler.getCamera().getPosition().y);

        shadowFBO.clear();

        int glError = glGetError();
        if (glError != 0) System.err.println("gl error: " + glError);
    }

    /**
     * shadow will be stored in the Image associated to shadowFBO
     */
    private void shadowPass(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        shadowCastersFBO.translate(-WorldHandler.getCamera().getPosition().x,
                -WorldHandler.getCamera().getPosition().y);
        shadowCastersFBO.clear();

        //draw all shadow casting textures to a buffer
        WorldHandler.getModuleHandler().getModulesOf(SimpleImageRenderModule.class).ifPresent(
                renderables -> renderables.forEach(r -> {
                    if (r.castShadow()) r.RENDER(gc, sbg, shadowCastersFBO);
                })
        );
//        shadowsShaderManager.renderShadows(shadowCastersTexture, shadowFBO);
    }


    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int d) throws SlickException {
        loops = 0;
        while (System.currentTimeMillis() > next_game_tick && loops < Config.MAX_FRAMESKIP) {

            // GAME UPDATE CODE GOES HERE

            // XXX TEST START

            WorldHandler.getCamera().UPDATE(gc, sbg);

            // EventModule
            WorldHandler.getModuleHandler().getModulesOf(EventListenerModule.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getEventDispatcher().getEvents().parallelStream()
                    .filter(event -> event.getReceiverID().equals("GLOBAL")).forEach(e ->

            {
                switch (e.getEvent()) {
                    case G_PRESSED:
                        EntityBuilder.createBuilder(EntityType.ZOMBIE).numOfEntitiesToSpawn(10)
                                .startPosition(WorldHandler.getWorldMap().getWorldMapLoader().getStartRoomPos())
                                .createEntity();
                        logger.trace("Spawned 10 new Entities");
                        break;
                    case J_PRESSED:
                        for (int i = 0; i < 10; i++) {
                            Node n = WorldHandler.getWorldMap().getAllWalkableNodes().stream()
                                    .skip(MathUtil.randomInt(0,
                                            WorldHandler.getWorldMap().getAllWalkableNodes().size() - 2))
                                    .findAny().get();

                            EntityBuilder.createBuilder(EntityType.ZOMBIE)
                                    .startPosition(new Vector2(n.x * 2, n.y * 2)).createEntity();
                        }
                        logger.trace("Spawned 10 new Zombies at random pos");
                        break;
                    case K_PRESSED:
                        WorldHandler.getEventDispatcher().createEvent(0, EventType.KILL_ENTITY, null,
                                "GAME", "GLOBAL");
                        logger.trace("Removed all Entities");
                        break;
                    case SET_WAVE:
                        if (xmlWaveLoader.getWaves().size() > e.getAdditionalInfo(Integer.class).get()
                                .intValue()) {
                            xmlWaveLoader.getWaves().get(e.getAdditionalInfo(Integer.class).get().intValue())
                                    .getEntityBuilders().forEach(builder -> builder.createEntity());
                            logger
                                    .trace("Set Wave to: " + e.getAdditionalInfo(Integer.class).get().intValue());
                            logger.trace("And created all entities in it");
                        } else {
                            logger.error("Wave not found, try smaller number!");
                        }
                        break;
                }
            });

            WorldHandler.getEntityHandler().UPDATE_ENTITYHANDLER();
            WorldHandler.getModuleHandler().UPDATE_MODULEHANDLER();

            WorldHandler.getModuleHandler().getModulesOf(SelectionModule.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getModuleHandler().getModulesOf(PhysicsModule.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getModuleHandler().getModulesOf(MovementModule.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getModuleHandler().getModulesOf(MoveSelectedEntityToMouseClick.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getModuleHandler().getModulesOf(FollowPlayerAI.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getModuleHandler().getModulesOf(ThetaStarMovementModule.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getModuleHandler().getModulesOf(EntityStatusModule.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getModuleHandler().getModulesOf(MouseSelectionModule.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getModuleHandler().getModulesOf(LightEmitter.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getModuleHandler().getModulesOf(MouseTileSelectionModule.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getModuleHandler().getModulesOf(DebugConsoleModule.class)
                    .ifPresent(modules -> modules.forEach(m -> m.UPDATE(gc, sbg)));

            WorldHandler.getB2World().step(1.0f / Config.TICKS_PER_SECOND, 6, 2);
            WorldHandler.getEventDispatcher().dispatchEvents();

            // XXX TEST END
            next_game_tick += Config.TIME_PER_TICK;
            loops++;

        }

        if (next_game_tick < System.currentTimeMillis())

        {
            next_game_tick = System.currentTimeMillis();
        }

        // extrapolation = 1 - (next_game_tick - System.currentTimeMillis()) / Config.TIME_PER_TICK;

    }

    @Override
    public int getID() {
        return ID;
    }

}
