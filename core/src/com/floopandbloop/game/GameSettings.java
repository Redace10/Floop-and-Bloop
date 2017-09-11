package com.floopandbloop.game;

import com.badlogic.gdx.Gdx;

public class GameSettings {
    public static final int SCREENHEIGHT = Gdx.graphics.getHeight();
    public static final int SCREENWIDTH = Gdx.graphics.getWidth();

    // enemy
    public static final String ENEMY_SPRITESHEET = "Purple Enemy Sprite Sheet.txt";
    public static final String ENEMY_SPRITE = "enemy sprite 1";
    public static final int ENEMY_SPEEDFACTOR = 90;
    public static final int ENDPOSX_FACTOR = 4;

    // player
    public static final String PLAYER_SPRITESHEET = "main sprite sheet.txt";
    public static final String PLAYER_SPRITE = "main sprite 1";
    public static final int JUMPSPEEDFACTOR = 64;
    public static final int XSPEEDFACTOR = 79;
    public static final int JUMP_ERRORMARGIN = 10;

    // main menu
    public static final float MENU_SIZEFACTOR = 0.66f;
    public static final String PLAYER_MENU_SPRITESHEET = "player menu sprite sheet.txt";
    public static final String PLAYER_SPRITE_MENU = "main character sprite 1";

    public static final float BUTTON_SIZEFACTOR = 7.40f;

}
