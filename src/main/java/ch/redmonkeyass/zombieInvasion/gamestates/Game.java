package ch.redmonkeyass.zombieInvasion.gamestates;

import java.io.File;
import java.nio.FloatBuffer;

import ch.redmonkeyass.zombieInvasion.module.RenderableModul;
import ch.redmonkeyass.zombieInvasion.util.shadows.ShadowsShaderManager;
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

import com.badlogic.gdx.math.Vector2;

import ch.redmonkeyass.zombieInvasion.Config;
import ch.redmonkeyass.zombieInvasion.WorldHandler;
import ch.redmonkeyass.zombieInvasion.entities.entityfactories.EntityBuilder;
import ch.redmonkeyass.zombieInvasion.entities.entityfactories.EntityType;
import ch.redmonkeyass.zombieInvasion.entities.entityfactories.waves.xmlLoader.XMLWaveLoader;
import ch.redmonkeyass.zombieInvasion.module.modules.EntityStatusModule;
import ch.redmonkeyass.zombieInvasion.module.modules.EventListenerModule;
import ch.redmonkeyass.zombieInvasion.module.modules.LightEmitter;
import ch.redmonkeyass.zombieInvasion.module.modules.MoveSelectedEntityToMouseClick;
import ch.redmonkeyass.zombieInvasion.module.modules.MovementModule;
import ch.redmonkeyass.zombieInvasion.module.modules.PhysicsModule;
import ch.redmonkeyass.zombieInvasion.module.modules.SelectionModule;
import ch.redmonkeyass.zombieInvasion.module.modules.SimpleImageRenderModule;
import ch.redmonkeyass.zombieInvasion.module.modules.UNUSED.ThetaStarMovementModule;
import ch.redmonkeyass.zombieInvasion.module.modules.debugmodules.DebugRendererModule;
import ch.redmonkeyass.zombieInvasion.module.modules.game.DebugConsoleModule;
import ch.redmonkeyass.zombieInvasion.module.modules.game.DebugRendererGameModule;
import ch.redmonkeyass.zombieInvasion.module.modules.mouse.MouseSelectionModule;
import ch.redmonkeyass.zombieInvasion.module.modules.mouse.MouseTileSelectionModule;
import ch.redmonkeyass.zombieInvasion.module.modules.zombieAI.FollowPlayerAI;
import ch.redmonkeyass.zombieInvasion.eventhandling.EventType;
import ch.redmonkeyass.zombieInvasion.input.InputHandler;
import ch.redmonkeyass.zombieInvasion.util.MathUtil;
import ch.redmonkeyass.zombieInvasion.util.ShaderTester;
import ch.redmonkeyass.zombieInvasion.worldmap.Node;

import static ch.redmonkeyass.zombieInvasion.util.shadows.ShadowsShaderManager.toOrtho2D;

public class Game extends BasicGameState {
    private final int ID;
    private double next_game_tick = System.currentTimeMillis();
    private int loops;
    private final int h = 1024;
    private final int w = 1024;
    // private double extrapolation;
    private InputHandler inputHandler = null;
    private Logger logger = LogManager.getLogger(Game.class);
    private Image shadowCasterBuffers;
    FBOGraphics shadowCastingBuffer;
    private Image shadows;
    private FBOGraphics shadowsBuffer;
    ShadowsShaderManager shadowsShaderManager;
    FloatBuffer mvpMatrixBuffer = BufferUtils.createFloatBuffer(16);



    XMLWaveLoader xmlWaveLoader = null;


    public Game(int ID) {
        this.ID = ID;
    }

    //ShaderTester st = new ShaderTester();

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        EntityBuilder.createBuilder(EntityType.MOUSE).createEntity();
        EntityBuilder.createBuilder(EntityType.GAME).createEntity();
        shadowCasterBuffers = new Image(w, h);
        shadowCastingBuffer = new FBOGraphics(shadowCasterBuffers);
        shadows = new Image(w, h);
        shadowsBuffer = new FBOGraphics(shadows);

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
        Matrix4f projection = toOrtho2D(null, 0, 0, w, h, 1, -1);
        // MVP = M * V * P;
        Matrix4f mvp = Matrix4f.mul(model, Matrix4f.mul(view, projection, null), null);
        mvp.store(mvpMatrixBuffer);
        mvpMatrixBuffer.flip();//prepare for read

        shadowsShaderManager = new ShadowsShaderManager(mvpMatrixBuffer,w,h);

        inputHandler = new InputHandler(gc);


        WorldHandler.getCamera().setMapData(WorldHandler.getWorldMap().getMapWidthInMeter(),
                WorldHandler.getWorldMap().getMapHeightInMeter());


        xmlWaveLoader =
                new XMLWaveLoader(new File(Config.RESSOURCE_FOLDER + "/waves/" + Config.WAVES_FILE_NAME));

        xmlWaveLoader.getWaves().get(0).getEntityBuilders().forEach(b -> b.createEntity());
        // .forEach(e -> e.getEntityBuilders().forEach(b -> b.createEntity()));
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        g.translate(-WorldHandler.getCamera().getPosition().x,
                -WorldHandler.getCamera().getPosition().y);


        // WorldMap
        WorldHandler.getWorldMap().RENDER(gc, sbg, g);

        // XXX TEST START
        WorldHandler.getModuleHandler().getModulesOf(SimpleImageRenderModule.class)
                .ifPresent(modules -> modules.forEach(m -> m.RENDER(gc, sbg, g)));

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
        shadowPass(gc,sbg,g);
        g.drawImage(shadows,0,0);
    }

    public void shadowPass(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        shadowCastingBuffer.translate(-WorldHandler.getCamera().getPosition().x,
                -WorldHandler.getCamera().getPosition().y);
        //draw all shadow casting textures to a buffer
        WorldHandler.getModuleHandler().getModulesOf(SimpleImageRenderModule.class).ifPresent(
                renderables -> renderables.forEach(r -> {if(r.castShadow()) r.RENDER(gc,sbg, shadowCastingBuffer);})
        );

        shadowsShaderManager.renderShadows(shadowCasterBuffers,shadowsBuffer);

        g.drawImage(shadows,-WorldHandler.getCamera().getPosition().x,
                -WorldHandler.getCamera().getPosition().y);
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
