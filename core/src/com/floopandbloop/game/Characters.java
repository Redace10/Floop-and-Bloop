package com.floopandbloop.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by MJ on 2017-08-02.
 * Class for the enemy circles
 */

public class Characters {

    private TextureAtlas charAtlas;
    private Animation animation;
    private int charWidth, charHeight;
    private double idealCharHeight;
    private double idealCharWidth;

    private int posX = 0;
    private int posY = 0;
    private int startPosX, endPosX;
    private int maxHeight, maxWidth;
    private int minHeight, minWidth;
    private Polygon polygonShape;
    private JsonReader json;
    private JsonValue jsonFile;
    private float[] polygonVertices;

    private int jumpSpeed;
    private int xSpeed;

    private boolean jumpClicked = false;
    private boolean goUp = false;

    public Characters(String spriteSheet, String sprite, String characterType){
        charAtlas = new TextureAtlas(Gdx.files.internal(spriteSheet));
        animation = new Animation(1/10f, charAtlas.getRegions());

        int animationWidth = charAtlas.findRegion(sprite).getRegionWidth();
        int animationHeight = charAtlas.findRegion(sprite).getRegionHeight();

        idealCharHeight = GameSettings.SCREENHEIGHT / ExtraFeatures.IDEAL_ENEMIES_NUM;
        idealCharWidth = idealCharHeight * animationWidth / animationHeight;
        setCharacterSize();

        json = new JsonReader();

        if (characterType == "player") {
            jumpSpeed = GameSettings.SCREENHEIGHT/ GameSettings.JUMPSPEEDFACTOR;
            xSpeed = GameSettings.SCREENWIDTH/ GameSettings.XSPEEDFACTOR;
            maxHeight = GameSettings.SCREENHEIGHT - charHeight;
            maxWidth = GameSettings.SCREENWIDTH - charWidth;
            minHeight = 0 + GameSettings.JUMP_ERRORMARGIN;
            minWidth = 0;
            startPosX = 0;
            jsonFile = json.parse(Gdx.files.internal("main_shape.json"));
        } else if (characterType == "enemy") {
            xSpeed = GameSettings.SCREENWIDTH/ GameSettings.ENEMY_SPEEDFACTOR;
            jumpSpeed = 0;
            startPosX = GameSettings.SCREENWIDTH;
            endPosX = 0 - charWidth * GameSettings.ENDPOSX_FACTOR;
            posX = startPosX;
            jsonFile = json.parse(Gdx.files.internal("enemy_shape.json"));
        }
        posX = startPosX;
        setPolygonShape(animationWidth, animationHeight);
    }

    public void enemyPositionY(){
        if (posX <= endPosX) {
            ExtraFeatures.repeatAttack = true;
        }
    }

    public int enemyPositionX(){
        if (posX <= endPosX) {
            posX = startPosX;
            ExtraFeatures.getPoint = true;
        } else{
            posX -= xSpeed;
        }
        return posX;
    }

    public void resetEnemyPosition(int pos){
        posY = pos;
        posX = startPosX;
    }

    public int playerJump(){
        if (!jumpClicked) {
            return 0;
        }
        if (posY >= maxHeight) {
            goUp = false;
        }
        if (goUp) {
            posY += jumpSpeed;
        } else {
            if (posY <= minHeight) {
                jumpClicked = false;
                return posY;
            }
            posY -= jumpSpeed;
        }
        if (posY >= maxHeight) {
            posY = maxHeight;
        }
        return posY;
    }

    public int playerXMovement(){
        int tilt = (int) Gdx.input.getAccelerometerY();


        if (tilt > 0) {
            posX += xSpeed;
        } else if (tilt < 0) {
            posX -= xSpeed;
        }
        if (posX <= minWidth) {
            posX = 0;
        } else if (posX >= maxWidth) {
            posX = maxWidth;
        }
        return posX;
    }

    public void resetPlayerPosition() {
        posX = 0;
        posY = 0;
    }

    public void ableToGoUp() {
        if (jumpClicked == false) {
            jumpClicked = true;
            goUp = true;
        }
    }

    public void cannotGoUp() {
        if (jumpClicked == true) {
            jumpClicked = false;
            goUp = false;
        }
    }

    public int getCharWidth(){
        return charWidth;
    }

    public int getCharHeight(){
        return charHeight;
    }

    public void setCharacterSize(){
        charHeight = (int) idealCharHeight;
        charWidth = (int) idealCharWidth;
    }

    public TextureAtlas getCharAtlas(){
        return charAtlas;
    }

    public Animation getAnimation(){
        return animation;
    }

    public int getPosX(){
        return posX;
    }

    public int getPosY(){
        return posY;
    }

    public void setPolygonShape( int w, int h) {
        JsonValue root = jsonFile.getChild("rigidBodies");
        float originX = root.get("origin").getFloat("x");// * texture.getWidth();
        float originY = root.get("origin").getFloat("y");// * texture.getWidth();
		Vector2 origin = new Vector2(originX, originY);
		for (JsonValue poly = root.getChild("polygons"); poly != null; poly = poly.next()) {
            polygonVertices = new float[poly.size * 2];


            int index = 0;
            for (JsonValue vertex = poly.child; vertex != null; vertex = vertex.next()) {
                polygonVertices[index] = vertex.getFloat("x") * (float) idealCharWidth;
                polygonVertices[index + 1] = vertex.getFloat("y") * (float) idealCharWidth;
                index += 2;
            }
            polygonShape = new Polygon(polygonVertices);
            polygonShape.setOrigin(origin.x, origin.y);
        }
    }

    public float[] getPolygonVertices() {
        return polygonVertices;
    }

    public Polygon getPolygonShape() {
        return polygonShape;
    }

}
