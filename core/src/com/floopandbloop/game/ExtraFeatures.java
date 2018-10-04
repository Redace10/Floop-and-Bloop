package com.floopandbloop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

public class ExtraFeatures {
    private BitmapFont font;
    private String message = "GG";
    private Music music;

    private int score = 0;
    private int highScore = 0;
    private Preferences prefs;
    private Texture backgroundTexture;
    private Sprite backgroundSprite;

    private final int FONT_SIZEFACTOR = 1661;
    public static boolean getPoint = false;
    public static boolean repeatAttack = false;
    public static boolean gameOver = true;
    public static boolean isAlive = false;
    private int spaceToSurvive;
    private int isSpaceUp = 0;   // down is 0 and up is 1
    public static final int IDEAL_ENEMIES_NUM = 7;

    private float highScorePosX, highScorePosY;
    private float scorePosX, scorePosY;
    private float scoreMessagePosX;
    private float scoreMessagePosY;
    private GlyphLayout glyphLayout;

    private int arrayPositions[] = new int[6];
    private Random random;

    // main menu
    private float main_menu_posX;
    private float main_menu_posY;
    private float main_menu_width;
    private float main_menu_height;

    private TextureAtlas playerAtlas;
    private Animation playerAnimation;
    private int playerHeight, playerWidth;
    private int playerPosX, playerPosY;

    private TextureAtlas enemyAtlas;
    private Animation enemyAnimation;
    private int enemyHeight, enemyWidth;
    private int enemyPosX, enemyPosY;

    private float tapInstructionPosX, tapInstructionPosY;

    // google play services
    private Texture loginButton;
    private Texture logoutButton;
    private Texture playButton;
    private Texture leaderboardButton;
    private Texture policyButton;
    private Texture loginButtonAfter;
    private Texture logoutButtonAfter;
    private Texture playButtonAfter;
    private Texture leaderboardButtonAfter;
    private Texture policyButtonAfter;

    private int buttonWidth, buttonHeight;
    private int loginButtonPosX, loginButtonPosY;
    private int playButtonPosX, playButtonPosY;
    private int leaderboardButtonPosX, leaderboardButtonPosY;
    private int policyButtonPosX, policyButtonPosY;

    private Circle playCircle;
    private Circle loginCircle;
    private Circle leaderboardCircle;
    private Circle policyCircle;

    public static boolean playClicked = false;
    public static boolean loginClicked = false;
    public static boolean leaderboardClicked = false;
    public static boolean policyClicked = false;
    public static boolean isLoggedIn = false;
    public static boolean submitScore = true;

    public ExtraFeatures() {
        font = new BitmapFont(Gdx.files.internal("comicAndy.fnt"));
        font.setColor(Color.GREEN);
        float fontSize = (float) GameSettings.SCREENHEIGHT / FONT_SIZEFACTOR;
        font.getData().setScale(fontSize);

        // main menu
        setMainMenu();

        glyphLayout = new GlyphLayout();
        random = new Random();
        music = Gdx.audio.newMusic(Gdx.files.internal("spy marching song.mp3"));
//        music = Gdx.audio.newMusic(Gdx.files.internal("Jermas Pirate Marching Band.mp3"));

        prefs = Gdx.app.getPreferences("Game_Preferences");
    }

    public void setMainMenu() {
        backgroundTexture = new Texture("background.png");
        int backgroundTextureWidth = backgroundTexture.getWidth();
        int backgroundTextureHeight = backgroundTexture.getHeight();

        main_menu_height = (float) GameSettings.SCREENHEIGHT * GameSettings.MENU_SIZEFACTOR;
        main_menu_width = main_menu_height * backgroundTextureWidth / backgroundTextureHeight;
        main_menu_posX = GameSettings.SCREENWIDTH/2 - main_menu_width / 2;
        main_menu_posY = GameSettings.SCREENHEIGHT/2 - main_menu_height / 2;

        loginButton = new Texture("login button.png");
        logoutButton = new Texture("logout button.png");
        playButton = new Texture("play button.png");
        leaderboardButton = new Texture("leaderboard button.png");
        policyButton = new Texture("policy button.png");

        loginButtonAfter = new Texture("login button after.png");
        logoutButtonAfter = new Texture("logout button after.png");
        playButtonAfter = new Texture("play button after.png");
        leaderboardButtonAfter = new Texture("leaderboard button after.png");
        policyButtonAfter = new Texture("policy button after.png");

        buttonWidth = (int) (GameSettings.SCREENHEIGHT / GameSettings.BUTTON_SIZEFACTOR);
        buttonHeight = (int) (GameSettings.SCREENHEIGHT / GameSettings.BUTTON_SIZEFACTOR);

        playerAtlas = new TextureAtlas(Gdx.files.internal(GameSettings.PLAYER_MENU_SPRITESHEET));
        playerAnimation = new Animation(1/10f, playerAtlas.getRegions());
        int tempPlayerWidth = playerAtlas.findRegion(GameSettings.PLAYER_SPRITE_MENU).getRegionWidth();
        int tempPlayerHeight = playerAtlas.findRegion(GameSettings.PLAYER_SPRITE_MENU).getRegionHeight();
        playerHeight = GameSettings.SCREENHEIGHT / ExtraFeatures.IDEAL_ENEMIES_NUM * 2;
        playerWidth = playerHeight * tempPlayerWidth / tempPlayerHeight;
        playerPosX = (int) main_menu_posX + (int) main_menu_posX / 4;
        playerPosY = (int) main_menu_posY + (int) main_menu_height / 2 - GameSettings.SCREENHEIGHT/8;

        playButtonPosX = playerPosX;
        loginButtonPosX = playButtonPosX + buttonWidth/2 + 100;
        leaderboardButtonPosX = loginButtonPosX + buttonWidth/2 + 100;
        policyButtonPosX = leaderboardButtonPosX + buttonWidth/2 + 100;
        playButtonPosY = playerPosY - buttonHeight - buttonHeight/16;
        loginButtonPosY = playButtonPosY;
        leaderboardButtonPosY = playButtonPosY;
        policyButtonPosY = playButtonPosY;
        playButtonPosY = playerPosY - buttonHeight - buttonHeight/16;
        loginButtonPosY = playButtonPosY;
        leaderboardButtonPosY = playButtonPosY;

        playCircle = new Circle(playButtonPosX + buttonWidth/2, playButtonPosY + buttonHeight/2, buttonHeight/2);
        loginCircle = new Circle(loginButtonPosX + buttonWidth/2, loginButtonPosY + buttonHeight/2, buttonHeight/2);
        leaderboardCircle = new Circle(leaderboardButtonPosX + buttonWidth/2, leaderboardButtonPosY + buttonHeight/2, buttonHeight/2);
        policyCircle = new Circle(policyButtonPosX + buttonWidth/2, policyButtonPosY + buttonHeight/2, buttonHeight/2);

        enemyAtlas = new TextureAtlas(Gdx.files.internal(GameSettings.ENEMY_SPRITESHEET));
        enemyAnimation = new Animation(1/10f, enemyAtlas.getRegions());
        int tempEnemyWidth = enemyAtlas.findRegion(GameSettings.ENEMY_SPRITE).getRegionWidth();
        int tempEnemyHeight = enemyAtlas.findRegion(GameSettings.ENEMY_SPRITE).getRegionHeight();
        enemyHeight = GameSettings.SCREENHEIGHT / ExtraFeatures.IDEAL_ENEMIES_NUM * 2;
        enemyWidth = enemyHeight * tempEnemyWidth / tempEnemyHeight;
        enemyPosX = playerPosX + playerWidth - (int) (enemyWidth/2.1);
        enemyPosY = playerPosY + playerHeight/2;
    }

    public String getScore() {
        message = "Score: " + score;
        return message;
    }

    public String getHighscore() {
        message = "High Score: " + highScore;
        return message;
    }

    public int getHighscoreNumber() {
        return highScore;
    }

    public String getTapInstructions() {
        message = "Tap Anywhere to Play";
        return message;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public void setHighScore(int tempScore) {
        if (score >= highScore) {
            highScore = score;
        }
        if (tempScore >= highScore) {
            highScore = tempScore;
        }
    }

    public void resetHighScore() {
        highScore = 0;
    }

    public int generateEnemyPositions(int enemyHeight) {
        isSpaceUp = random.nextInt(2);
        spaceToSurvive = random.nextInt(arrayPositions.length);
        int pos;
        if (isSpaceUp == 0) {
            pos = GameSettings.SCREENHEIGHT - enemyHeight;
        } else {
            pos = 0;
        }
        for (int i = 0; i < arrayPositions.length; i++) {
            if (i == spaceToSurvive) {
                if (isSpaceUp == 0) {
                    isSpaceUp = 1;
                    pos = 0;
                } else {
                    isSpaceUp = 0;
                    pos = GameSettings.SCREENHEIGHT - enemyHeight;
                }
                continue;
            }
            arrayPositions[i] = pos;

            if (isSpaceUp == 0) {
                pos -= enemyHeight;
            } else {
                pos += enemyHeight;
            }
        }
        return spaceToSurvive;
    }

    public int getArrayPositions(int pos) {
        return arrayPositions[pos];
    }

    public int getArrayPositionsLength() {
        return arrayPositions.length;
    }

    public void resetScore() {
        score = 0;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Music getMusic() {
        return music;
    }

    public float getHighScorePosX() {
        glyphLayout.setText(font, message);
        highScorePosX = getScorePosX() + glyphLayout.width;
        return highScorePosX;
    }

    public float getHighScorePosY() {
        glyphLayout.setText(font, message);
        highScorePosY = getScorePosY();
        return highScorePosY;
    }

    public float getScorePosX() {
        glyphLayout.setText(font, message);
        scorePosX = main_menu_posX;
        return scorePosX;
    }

    public float getScorePosY() {
        glyphLayout.setText(font, message);
        scorePosY = getMain_menu_posY() + getMain_menu_height() + glyphLayout.height*2;
        return scorePosY;
    }

    public float getScoreInGamePosX() {
        glyphLayout.setText(font, message);
        scoreMessagePosX = glyphLayout.height / 4;
        return scoreMessagePosX;
    }

    public float getScoreInGamePosY() {
        glyphLayout.setText(font, message);
        scoreMessagePosY = GameSettings.SCREENHEIGHT - glyphLayout.height / 4;
        return scoreMessagePosY;
    }

    public float getTapInstructionPosX() {
        glyphLayout.setText(font, message);
        tapInstructionPosX = GameSettings.SCREENWIDTH / 2 - glyphLayout.width / 2;
        return tapInstructionPosX;
    }

    public float getTapInstructionPosY() {
        glyphLayout.setText(font, message);
        tapInstructionPosY = main_menu_posY - glyphLayout.height/2;
        return tapInstructionPosY;
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public void disposeButtons() {
        loginButton.dispose();
        logoutButton.dispose();
        playButton.dispose();
        leaderboardButton.dispose();

        loginButtonAfter.dispose();
        logoutButtonAfter.dispose();
        playButtonAfter.dispose();
        leaderboardButtonAfter.dispose();
    }

    public Texture getLoginButton() {
        if (isLoggedIn == true) {
            if (loginClicked == true) {
                return logoutButtonAfter;
            } else {
                return logoutButton;
            }
        } else {
            if (loginClicked == true) {
                return loginButtonAfter;
            } else {
                return loginButton;
            }
        }
    }

    public Texture getPlayButton() {
        if (playClicked == true) {
            return playButtonAfter;
        } else {
            return playButton;
        }
    }

    public Texture getLeaderboardButton() {
        if (leaderboardClicked == true) {
            return leaderboardButtonAfter;
        } else {
            return leaderboardButton;
        }
    }

    public Texture getPolicyButton() {
        if (policyClicked == true) {
            return policyButtonAfter;
        } else {
            return policyButton;
        }
    }

    public float getMain_menu_posX() {
        return main_menu_posX;
    }

    public float getMain_menu_posY() {
        return main_menu_posY;
    }

    public float getMain_menu_width() {
        return main_menu_width;
    }

    public float getMain_menu_height() {
        return main_menu_height;
    }

    public TextureAtlas getPlayerAtlas() {
        return playerAtlas;
    }

    public TextureAtlas getEnemyAtlas() {
        return enemyAtlas;
    }

    public Animation getPlayerAnimation(){
        return playerAnimation;
    }

    public Animation getEnemyAnimation(){
        return enemyAnimation;
    }

    public int getPlayerHeight() {
        return playerHeight;
    }

    public int getPlayerWidth() {
        return playerWidth;
    }

    public int getEnemyHeight() {
        return playerHeight;
    }

    public int getEnemyWidth() {
        return enemyWidth;
    }

    public int getPlayerPosX() {
        return playerPosX;
    }

    public int getPlayerPosY(){
        return playerPosY;
    }

    public int getEnemyPosX(){
        return enemyPosX;
    }

    public int getEnemyPosY(){
        return enemyPosY;
    }

    public int getButtonWidth(){
        return buttonWidth;
    }

    public int getButtonHeight(){
        return buttonHeight;
    }

    public int getLoginButtonPosX(){
        return loginButtonPosX;
    }

    public int getLoginButtonPosY(){
        return loginButtonPosY;
    }

    public int getPlayButtonPosX(){
        return playButtonPosX;
    }

    public int getPlayButtonPosY(){
        return playButtonPosY;
    }

    public int getLeaderboardButtonPosX(){
        return leaderboardButtonPosX;
    }

    public int getLeaderboardButtonPosY(){
        return leaderboardButtonPosY;
    }

    public int getPolicyButtonPosX(){
        return policyButtonPosX;
    }

    public int getPolicyButtonPosY(){
        return policyButtonPosY;
    }

    public Circle getPlayCircle() {
        return playCircle;
    }

    public Circle getLoginCircle() {
        return loginCircle;
    }

    public Circle getLeaderboardCircle() {
        return leaderboardCircle;
    }

    public Circle getPolicyCircle() {
        return policyCircle;
    }
}