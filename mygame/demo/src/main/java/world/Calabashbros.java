package world;

import java.awt.Color;

import javax.lang.model.util.ElementScanner6;

public class Calabashbros extends Creature implements Runnable{

     // everyone owns 3 life at first
    int score; //eat beans to get score
    boolean ifmagic; //check if it is in magic time, when using magic it can kill the monster
     
    final int magictime = 10; //the magic lasts
    int magic_count;

    int goal;

    public Calabashbros(Color color, char glyph, World world,Map tmap) {
        super(color, glyph, world, tmap, 3);
        //TODO Auto-generated constructor stub
        life = 3;
        score= 0;
        magic_count = 10;
        ifmagic = false;
    }

    public void setgoal(int g){
        goal=g;
    }

    public void setmagic(){
        magic_count = 0;
        ifmagic = true;
    }
    
    public void magic_counter(){
        if(magic_count<magictime)
            magic_count++;

        if(magic_count<magictime)
            ifmagic=true;
        else
            ifmagic=false;  
            
        if(ifmagic){
            glyph = 5;
        }
        else{
            glyph = 2;
        }
    }

    @Override
    public void eatBeans(int type){
        if(type==4)
            score++;
        else if(type==5)
            setmagic();
        else if(type==6)
            life++;
    }

    public boolean checkifmagic(){
        return ifmagic;
    }

    @Override
    public boolean attack(Creature creature){
        if(ifmagic) return true;
        else return false;
    }

    public boolean checkifwin(){
        return score >= goal;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(!isdead){
            magic_counter();
            //System.out.println("counter++");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
