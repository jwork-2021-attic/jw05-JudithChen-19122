package world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map {

    private int[][] init_map;// map from file map_design.txt
    private int[][] map;
    private int[][] prop_map; // map for beans and magic prop
    private int num_beans;

    private ArrayList<Creature> creatures;

    World world;

    public Map(World tworld) {
        world = tworld;
    }

    public void load_map() throws IOException {
        BufferedReader bf = new BufferedReader(
                new FileReader((new File("")).getAbsolutePath() + "/src/main/java/resources/map_design.txt"));
        String textLine = new String();
        String str = new String();
        while ((textLine = bf.readLine()) != null) {
            str += textLine + ",";
        }
        String[] numbers = str.split(",");
        init_map = new int[30][30];// 矩阵数组
        String[] stmp = null;
        for (int i = 0; i < 30; i++) {
            stmp = numbers[i].split(" ");
            for (int j = 0; j < 30; j++) {
                init_map[i][j] = Integer.parseInt(stmp[j]);
            }
        }
        bf.close();

        map = new int[30][30];
        prop_map = new int[30][30];
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                map[i][j] = init_map[i][j];
                prop_map[i][j] = init_map[i][j];
                if (init_map[i][j] == 0) {
                    prop_map[i][j] = 1;
                }
                if (init_map[i][j] == 1) { // the floor set beans first
                    prop_map[i][j] = 4; // 4 stands for beans
                }
            }
        }
        // remove some beans
        for (int i = 0; i < 30; i++)
            prop_map[i][10] = 1;

        int temp_for_bean = 0;
        num_beans = 0;

        prop_map[7][3] = 5; // 5 stands for magic prop
        prop_map[20][25] = 6; //6 stands for heart

        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                temp_for_bean = ((int) (Math.random() * 100)) % 10;
                if (temp_for_bean == 1)
                    prop_map[i][j] = 1;
                if (prop_map[i][j] == 4)
                    num_beans++;
            }
        }
    }

    public int get_total_beans() {
        return num_beans;
    }

    public int[][] get_init_map() {
        return init_map;
    }

    public int[][] get_prop_map() {
        return prop_map;
    }

    public void init_creatures(ArrayList<Creature> init_creatures) {
        creatures = init_creatures;
    }

    public void add_creature_to_map(int type, int x, int y) {
        map[x][y] = type;
    }

    public int[][] get_map() {
        return map;
    }

    private void swap(int x, int y, int nx, int ny) {
        int tt = map[x][y];
        map[x][y] = map[nx][ny];
        map[nx][ny] = tt;
    }

    // creature moving action includes the calabsh eating beans
    public synchronized void moveAction(int x, int y, int nx, int ny) {
        if (map[x][y] == 2 && nx == 17 && ny == 10) {
            return;// the monster can't get to (17,10) since it is the location for resurrection of
                   // calabash
        } else if (map[nx][ny] == 1) {
            world.swap(x, y, nx, ny);
            swap(x, y, nx, ny);
            if (map[nx][ny] == 3) {
                Creature c = null;
                for (int i = 0; i < creatures.size(); i++) {
                    int ax = creatures.get(i).getX();
                    int ay = creatures.get(i).getY();
                    if (ax == nx && ay == ny)
                        c = creatures.get(i);
                }
                if (c == null)
                    return;
                if (prop_map[nx][ny] == 4 || prop_map[nx][ny] == 5 || prop_map[nx][ny] == 6) {
                    c.eatBeans(prop_map[nx][ny]);
                    prop_map[nx][ny] = 1;
                    world.put_props(new Floor(world), nx, ny);
                }
            }
        } else if (map[nx][ny] == 2 && map[x][y] == 3) { // calabash to monster
            Creature m = null;
            Creature c = null;
            for (int i = 0; i < creatures.size(); i++) {
                int ax = creatures.get(i).getX();
                int ay = creatures.get(i).getY();
                if (ax == nx && ay == ny)
                    m = creatures.get(i);
                else if (ax == x && ay == y)
                    c = creatures.get(i);
            }
            if (c != null && m != null) {
                if (c.attack(m)) {
                    map[nx][ny] = 1;
                    m.beenkill();
                    creatures.remove(m); // the monster been killed
                    world.put(new Floor(world), nx, ny);
                    world.swap(x, y, nx, ny);
                    swap(x, y, nx, ny);
                    if (prop_map[nx][ny] == 4 || prop_map[nx][ny] == 5 || prop_map[nx][ny] == 6) {
                        c.eatBeans(prop_map[nx][ny]);
                        prop_map[nx][ny] = 1;
                        world.put_props(new Floor(world), nx, ny);
                    }
                } else { // the calabash been killed
                    c.beenkill();
                    // creatures.remove(c); //the calabash been killed
                    world.put(c, 17, 10);
                    map[17][10] = 3;
                    world.put(new Floor(world), x, y);
                    map[x][y] = 1;
                }
            }
        } else if (map[nx][ny] == 3 && map[x][y] == 2) { // monster to calabash
            Creature m = null;
            Creature c = null;
            for (int i = 0; i < creatures.size(); i++) {
                int ax = creatures.get(i).getX();
                int ay = creatures.get(i).getY();
                if (ax == nx && ay == ny)
                    c = creatures.get(i);
                else if (ax == x && ay == y)
                    m = creatures.get(i);
            }
            if (c != null && m != null) {
                if (m.attack(c)) {
                    map[nx][ny] = 1;
                    c.beenkill();
                    // creatures.remove(c); //the calabash been killed
                    world.put(c, 17, 10);
                    map[17][10] = 3;
                    world.put(new Floor(world), nx, ny);
                    world.put(c, 17, 10);
                    world.swap(x, y, nx, ny);
                    swap(x, y, nx, ny);
                } else {
                    // the monster been killed
                    m.beenkill();
                    map[x][y] = 1;
                    creatures.remove(m);
                    world.put(new Floor(world), x, y);
                }
            }
        }
    }
}
