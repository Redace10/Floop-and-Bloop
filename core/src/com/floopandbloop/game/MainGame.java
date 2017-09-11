package com.floopandbloop.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;

public class MainGame extends ApplicationAdapter implements InputProcessor{

	private SpriteBatch batch;
	private float timePassed = 0;

	ExtraFeatures features;
	Characters player;
	Characters enemy1;
	Characters enemy2;
	Characters enemy3;
	Characters enemy4;
	Characters enemy5;

	PolygonSprite poly;
	PolygonSpriteBatch polyBatch;
	Texture textureSolid;
	boolean IS_DRAW_POLY = false;

	AdHandler handler;
	boolean toggle;
	PlayServices playServices;

	public MainGame(AdHandler handler, PlayServices playServices) {
		this.handler = handler;
		this.playServices = playServices;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		features = new ExtraFeatures();
		player = new Characters(GameSettings.PLAYER_SPRITESHEET, GameSettings.PLAYER_SPRITE, "player");
		enemy1 = new Characters(GameSettings.ENEMY_SPRITESHEET, GameSettings.ENEMY_SPRITE, "enemy");
		enemy2 = new Characters(GameSettings.ENEMY_SPRITESHEET, GameSettings.ENEMY_SPRITE, "enemy");
		enemy3 = new Characters(GameSettings.ENEMY_SPRITESHEET, GameSettings.ENEMY_SPRITE, "enemy");
		enemy4 = new Characters(GameSettings.ENEMY_SPRITESHEET, GameSettings.ENEMY_SPRITE, "enemy");
		enemy5 = new Characters(GameSettings.ENEMY_SPRITESHEET, GameSettings.ENEMY_SPRITE, "enemy");
		Gdx.input.setInputProcessor(this);

		if (IS_DRAW_POLY == true) {
			showPolygonShape();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		timePassed += Gdx.graphics.getDeltaTime();

		features.getMusic().play();

		enemy1.enemyPositionY();
		if (ExtraFeatures.repeatAttack == true) {
			features.setScore(1);
			resetEnemyPositions();
		}

		if (ExtraFeatures.isAlive == false) {
			restartGame();
		}

		if (ExtraFeatures.gameOver == false) {
			batch.draw((TextureRegion) enemy1.getAnimation().getKeyFrame(timePassed, true),
					enemy1.enemyPositionX(), enemy1.getPosY(), enemy1.getCharWidth(), enemy1.getCharHeight());
			batch.draw((TextureRegion) enemy2.getAnimation().getKeyFrame(timePassed, true),
					enemy2.enemyPositionX(), enemy2.getPosY(), enemy2.getCharWidth(), enemy2.getCharHeight());
			batch.draw((TextureRegion) enemy3.getAnimation().getKeyFrame(timePassed, true),
					enemy3.enemyPositionX(), enemy3.getPosY(), enemy3.getCharWidth(), enemy3.getCharHeight());
			batch.draw((TextureRegion) enemy4.getAnimation().getKeyFrame(timePassed, true),
					enemy4.enemyPositionX(), enemy4.getPosY(), enemy4.getCharWidth(), enemy4.getCharHeight());
			batch.draw((TextureRegion) enemy5.getAnimation().getKeyFrame(timePassed, true),
					enemy5.enemyPositionX(), enemy5.getPosY(), enemy5.getCharWidth(), enemy5.getCharHeight());
			batch.draw((TextureRegion) player.getAnimation().getKeyFrame(timePassed, true),
					player.playerXMovement(), player.playerJump(), player.getCharWidth(), player.getCharHeight());
		}

		player.getPolygonShape().setPosition(player.getPosX(), player.getPosY());
		enemy1.getPolygonShape().setPosition(enemy1.getPosX(), enemy1.getPosY());
		enemy2.getPolygonShape().setPosition(enemy2.getPosX(), enemy2.getPosY());
		enemy3.getPolygonShape().setPosition(enemy3.getPosX(), enemy3.getPosY());
		enemy4.getPolygonShape().setPosition(enemy4.getPosX(), enemy4.getPosY());
		enemy5.getPolygonShape().setPosition(enemy5.getPosX(), enemy5.getPosY());

		if (Intersector.overlapConvexPolygons(player.getPolygonShape(), enemy1.getPolygonShape()) ||
				Intersector.overlapConvexPolygons(player.getPolygonShape(), enemy2.getPolygonShape()) ||
				Intersector.overlapConvexPolygons(player.getPolygonShape(), enemy3.getPolygonShape()) ||
				Intersector.overlapConvexPolygons(player.getPolygonShape(), enemy4.getPolygonShape()) ||
				Intersector.overlapConvexPolygons(player.getPolygonShape(), enemy5.getPolygonShape())) {
			ExtraFeatures.gameOver = true;
			player.cannotGoUp();
		}

		if (ExtraFeatures.gameOver == false) {
			toggle = false;
			features.getFont().draw(batch, features.getScore(), features.getScoreInGamePosX(), features.getScoreInGamePosY());
		} else {
			int score = (int) playServices.getHighScore();
			features.setHighScore(score);

			if (ExtraFeatures.submitScore == true) {
				playServices.submitScore(features.getHighscoreNumber());
				ExtraFeatures.submitScore = false;
			}

			ExtraFeatures.isLoggedIn = playServices.isSignedIn();
			toggle = true;
			batch.draw(features.getBackgroundTexture(), features.getMain_menu_posX(), features.getMain_menu_posY(),
					features.getMain_menu_width(), features.getMain_menu_height());
			batch.draw((TextureRegion) features.getEnemyAnimation().getKeyFrame(timePassed, true),
					features.getEnemyPosX(), features.getEnemyPosY(),
					features.getEnemyWidth(), features.getEnemyHeight());
			batch.draw((TextureRegion) features.getPlayerAnimation().getKeyFrame(timePassed, true),
					features.getPlayerPosX(), features.getPlayerPosY(),
					features.getPlayerWidth(), features.getPlayerHeight());
			features.getFont().draw(batch, features.getHighscore(), features.getHighScorePosX(), features.getHighScorePosY());
			features.getFont().draw(batch, features.getScore(), features.getScorePosX(), features.getScorePosY());
//			features.getFont().draw(batch, features.getTapInstructions(), features.getTapInstructionPosX(), features.getTapInstructionPosY());
			batch.draw(features.getPlayButton(), features.getPlayButtonPosX(), features.getPlayButtonPosY(),
					features.getButtonWidth(), features.getButtonHeight());
			batch.draw(features.getLoginButton(), features.getLoginButtonPosX(), features.getLoginButtonPosY(),
					features.getButtonWidth(), features.getButtonHeight());
			batch.draw(features.getLeaderboardButton(), features.getLeaderboardButtonPosX(), features.getLeaderboardButtonPosY(),
					features.getButtonWidth(), features.getButtonHeight());
		}
		drawAd();
		batch.end();

		if (IS_DRAW_POLY == true) {
			drawPolygonShape();
		}
	}


	public void drawAd() {
		handler.showAds(toggle);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (ExtraFeatures.gameOver == true) {
			if (features.getLoginCircle().contains(screenX, GameSettings.SCREENHEIGHT - screenY) == true) {
				ExtraFeatures.loginClicked = true;
			} else if (features.getLeaderboardCircle().contains(screenX, GameSettings.SCREENHEIGHT - screenY) == true) {
				ExtraFeatures.leaderboardClicked = true;
			} else if (features.getPlayCircle().contains(screenX, GameSettings.SCREENHEIGHT - screenY) == true) {
				ExtraFeatures.playClicked = true;
			}
		} else {
			player.ableToGoUp();
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (ExtraFeatures.loginClicked == true) {
			if (features.getLoginCircle().contains(screenX, GameSettings.SCREENHEIGHT - screenY) == true) {

				if (playServices.isSignedIn() == false) {
					playServices.signIn();
				} else {
					playServices.signOut();
					features.resetHighScore();
				}
			}
		}
		else if (ExtraFeatures.leaderboardClicked == true) {
			if (features.getLeaderboardCircle().contains(screenX, GameSettings.SCREENHEIGHT - screenY) == true) {
				playServices.showScore();
			}
		}
		else if (ExtraFeatures.playClicked == true) {
			if (features.getPlayCircle().contains(screenX, GameSettings.SCREENHEIGHT - screenY) == true) {
				ExtraFeatures.isAlive = false;
				ExtraFeatures.gameOver = false;
				ExtraFeatures.submitScore = true;
			}
		}
		ExtraFeatures.playClicked = false;
		ExtraFeatures.loginClicked = false;
		ExtraFeatures.leaderboardClicked = false;
		return true;
	}

	public void restartGame() {
		features.resetScore();
		player.resetPlayerPosition();
		resetEnemyPositions();
		ExtraFeatures.isAlive = true;
	}

	public void resetEnemyPositions(){
		int freeSpace = features.generateEnemyPositions(enemy1.getCharHeight());
		Characters[] enemyArray = {enemy1, enemy2, enemy3, enemy4, enemy5};
		int enemyIndex = 0;
		for (int i = 0; i < features.getArrayPositionsLength(); i++) {
			if (i != freeSpace) {
				enemyArray[enemyIndex].resetEnemyPosition(features.getArrayPositions(i));
				enemyIndex++;
			}
		}
		ExtraFeatures.repeatAttack = false;
	}

	public void showPolygonShape(){
		polyBatch = new PolygonSpriteBatch();
		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pix.setColor(0x00FF00AA); // DE is red, AD is green and BE is blue.
		pix.fill();
		textureSolid = new Texture(pix);
		PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
				enemy1.getPolygonVertices(), new short[] {
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14      // Two triangles using vertex indices.
				// Take care of the counter-clockwise direction.
		});
		poly = new PolygonSprite(polyReg);
		polyBatch = new PolygonSpriteBatch();
	}

	public void drawPolygonShape(){
		polyBatch.begin();
		poly.draw(polyBatch);
		polyBatch.end();
	}

//	Functions from Input Processor that are unused
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void dispose(){
		batch.dispose();
		features.getFont().dispose();
		features.getMusic().dispose();
		features.getBackgroundTexture().dispose();
		features.disposeButtons();
		features.getPlayerAtlas().dispose();
		features.getEnemyAtlas().dispose();

		player.getCharAtlas().dispose();
		enemy1.getCharAtlas().dispose();
		enemy2.getCharAtlas().dispose();
		enemy3.getCharAtlas().dispose();
		enemy4.getCharAtlas().dispose();
		enemy5.getCharAtlas().dispose();

		if (IS_DRAW_POLY == true) {
			polyBatch.dispose();
			textureSolid.dispose();
		}
	}
}

