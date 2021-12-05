package screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import world.Map;
import world.Monster;
import world.Wall;
import world.Beans;

import asciiPanel.AsciiPanel;
import world.Calabashbros;
import world.Creature;
import world.Floor;
import world.Heart;
import world.Magic_prop;
import world.World;

public class WorldScreen implements Screen {

    private Map map;
    private World world;
    private Calabashbros player;
    ArrayList<Creature> creatures;
    private int[][] maze;
    private int[][] map_for_props;
    String[] moveSteps;
    private Wall wall;
    private Floor floor;
    private ExecutorService exec;
    private int state;
    private Color white;
    private Color red;
    private int goal;

    public void build_game_screen() throws IOException {
        state = 2;
        world = new World();
        creatures = new ArrayList<Creature>();
        wall = new Wall(world);

        // build map world
        map = new Map(world);
        map.load_map();
        // map.change();
        maze = map.get_init_map();
        for (int i = 0; i < World.WIDTH; i++) {
            for (int j = 0; j < World.HEIGHT; j++) {
                if (maze[i][j] == 0)
                    world.put(wall, i, j);
            }
        }

        // put props
        map_for_props = map.get_prop_map();
        for (int i = 0; i < World.WIDTH; i++) {
            for (int j = 0; j < World.HEIGHT; j++) {
                if (map_for_props[i][j] == 4)
                    world.put_props(new Beans(world), i, j);
                else if (map_for_props[i][j] == 5)
                    world.put_props(new Magic_prop(world), i, j);
                else if (map_for_props[i][j] == 6)
                    world.put_props(new Heart(world), i, j);
            }
        }

        goal=map.get_total_beans();


        // set player
        player = new Calabashbros(new Color(204, 0, 0), (char) 2, world, map);
        world.put(player, 17, 10);
        creatures.add(player);
        map.add_creature_to_map(3, 17, 10);

        player.setgoal(goal);

        // set monsters
        Monster monster1 = new Monster(new Color(0, 0, 139), (char) 15, world, map);
        Monster monster2 = new Monster(new Color(139, 28, 98), (char) 15, world, map);
        Monster monster3 = new Monster(new Color(139, 125, 123), (char) 15, world, map);
        Monster monster4 = new Monster(new Color(0, 0, 139), (char) 15, world, map);
        Monster monster5 = new Monster(new Color(139, 28, 98), (char) 15, world, map);
        Monster monster6 = new Monster(new Color(139, 125, 123), (char) 15, world, map);
        world.put(monster1, 22, 19);
        world.put(monster2, 7, 10);
        world.put(monster3, 10, 25);
        world.put(monster4, 2, 9);
        world.put(monster5, 13, 4);
        world.put(monster6, 20, 19);
        creatures.add(monster1);
        creatures.add(monster2);
        creatures.add(monster3);
        creatures.add(monster4);
        creatures.add(monster5);
        creatures.add(monster6);
        map.add_creature_to_map(2, 22, 19);
        map.add_creature_to_map(2, 7, 10);
        map.add_creature_to_map(2, 10, 25);
        map.add_creature_to_map(2, 2, 9);
        map.add_creature_to_map(2, 13, 4);
        map.add_creature_to_map(2, 20, 19);

        map.init_creatures(creatures);

        exec = Executors.newCachedThreadPool();
        exec.execute(new Thread(player));
        exec.execute(new Thread(monster1));
        exec.execute(new Thread(monster2));
        exec.execute(new Thread(monster3));
        exec.execute(new Thread(monster4));
        exec.execute(new Thread(monster5));
        exec.execute(new Thread(monster6));
    }

    public void build_start_screen() {
        state = 1;
        world = new World();
    }

    public void show_start(AsciiPanel terminal) {
        terminal.write((char) 80, 6, 15, white);
        terminal.write((char) 82, 7, 15, white);
        terminal.write((char) 69, 8, 15, white);
        terminal.write((char) 83, 9, 15, white);
        terminal.write((char) 83, 10, 15, white);
        terminal.write((char) 0, 11, 15, white);

        terminal.write((char) 34, 12, 15, white);
        terminal.write((char) 65, 13, 15, white);
        terminal.write((char) 34, 14, 15, white);
        terminal.write((char) 0, 15, 15, white);

        terminal.write((char) 84, 16, 15, white);
        terminal.write((char) 79, 17, 15, white);
        terminal.write((char) 0, 18, 15, white);

        terminal.write((char) 83, 19, 15, white);
        terminal.write((char) 84, 20, 15, white);
        terminal.write((char) 65, 21, 15, white);
        terminal.write((char) 82, 22, 15, white);
        terminal.write((char) 84, 23, 15, white);
        terminal.write((char) 0, 24, 15, white);

    }

    public void show_fail(AsciiPanel terminal) {
        terminal.write((char) 89, 9, 15, white);
        terminal.write((char) 79, 10, 15, white);
        terminal.write((char) 85, 11, 15, white);
        terminal.write((char) 0, 12, 15, white);

        terminal.write((char) 70, 13, 15, white);
        terminal.write((char) 65, 14, 15, white);
        terminal.write((char) 73, 15, 15, white);
        terminal.write((char) 76, 16, 15, white);
        terminal.write((char) 69, 17, 15, white);
        terminal.write((char) 68, 18, 15, white);
        terminal.write((char) 0, 19, 15, white);

        terminal.write((char) 33, 20, 15, white);
    }

    public void show_win(AsciiPanel terminal) {
        terminal.write((char) 89, 11, 15, white);
        terminal.write((char) 79, 12, 15, white);
        terminal.write((char) 85, 13, 15, white);
        terminal.write((char) 0, 14, 15, white);

        terminal.write((char) 87, 15, 15, white);
        terminal.write((char) 79, 16, 15, white);
        terminal.write((char) 78, 17, 15, white);
        terminal.write((char) 0, 18, 15, white);

        terminal.write((char) 33, 19, 15, white);

    }

    public void show_game_info(AsciiPanel terminal) {
        terminal.write((char) 76, 0, 30, white);
        terminal.write((char) 73, 1, 30, white);
        terminal.write((char) 70, 2, 30, white);
        terminal.write((char) 69, 3, 30, white);
        terminal.write((char) 58, 4, 30, white);
        terminal.write((char) 0, 5, 30, white);

        for (int i = 0; i < player.getlife(); i++) {
            terminal.write((char) 3, 6 + i, 30, red);
        }

    }

    public void build_fail_screen() {
        state = 4;
        map=null;
        creatures=null;
        player=null;
        
        world = new World();
    }

    public void build_won_screen() {
        state = 3;
        map=null;
        creatures=null;
        player=null;
        world = new World();
    }

    public WorldScreen() throws IOException {
        white = new Color(255, 255, 255);
        red = new Color(255, 0, 0);
        build_start_screen();
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {
                if (state == 2) {
                    terminal.write(world.get_props(x, y).getGlyph(), x, y, world.get_props(x, y).getColor());
                    if (world.get(x, y).get_type() != 1)
                        terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());
                    show_game_info(terminal);

                    if(player.checkifdead()) build_fail_screen();
                    else if(player.checkifwin()) build_won_screen();

                } else if (state == 1) {
                    show_start(terminal);
                } else if (state == 3) {
                    show_win(terminal);
                } else if (state == 4) {
                    show_fail(terminal);
                }
            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) throws IOException {
        if(state==1)
        {
            if(key.getKeyCode()==KeyEvent.VK_A){
                build_game_screen();
            }
        }
        else if (state == 2) {
            switch (key.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.moveAction(1);
                    break;
                case KeyEvent.VK_S:
                    player.moveAction(2);
                    break;
                case KeyEvent.VK_A:
                    player.moveAction(3);
                    break;
                case KeyEvent.VK_D:
                    player.moveAction(4);
                    break;
                default:
                    ;
            }
        }
        return this;
    }

}
