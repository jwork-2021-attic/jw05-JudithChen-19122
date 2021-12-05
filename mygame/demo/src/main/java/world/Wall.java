package world;

import asciiPanel.AsciiPanel;

public class Wall extends Thing {

    public Wall(World world) {
        super(AsciiPanel.cyan, (char) 176, world, 0);
    }

}
