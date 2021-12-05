package world;

import java.awt.Color;

public class Monster extends Creature implements Runnable {

    public Monster(Color color, char glyph, World world,Map tmap) {
        super(color, glyph, world, tmap, 2);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(!isdead){
            int dir = ((int)(Math.random()*100)) % 4 + 1;
            moveAction(dir);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean attack(Creature creature){
        if(creature.checkifmagic()) return false ;
        else return true;
    }
    
}
