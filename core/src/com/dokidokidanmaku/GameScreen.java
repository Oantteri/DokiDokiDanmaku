package com.dokidokidanmaku;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;






public class GameScreen implements Screen {
	final DokiDokiDanmaku game;
   
    public MyTextInputListener InputListener;   
       
	//textures, sounds etc
	Texture bulletTexture;
	Texture bulletTextureL;
	Texture bulletTextureR;
	Texture characterSprite;

	Sound hitSound;
	Music gameMusic;

	OrthographicCamera camera;

	Rectangle character;


	// Variables
	float playTime;
	int livesLeft = 3;
	float tempScore;
	int score;
	String name;
	String highscore;
	boolean shHighScore;
	int intScore;
	int vibrate = 1000;
	
	// Bullets
	int bwidth = 24;  //bullet width
	int bheight = 24; // Bullet height
	long lastBulletTime;
	long lastBulletTimeL;
	long lastBulletTimeR;
	Array<Rectangle> bullets;
	Array<Rectangle> bulletsL;
	Array<Rectangle> bulletsR;
        

	public GameScreen(final DokiDokiDanmaku gam) {
		this.game = gam;
              
               
		// Bullet textures
		bulletTexture = new Texture(Gdx.files.internal("bullet.png"));
		bulletTextureL = new Texture(Gdx.files.internal("bulletL.png"));
		bulletTextureR= new Texture(Gdx.files.internal("bulletR.png"));
		//Character sprite(s)
		characterSprite = new Texture(Gdx.files.internal("character.png"));

		// hitsound(s)
		hitSound = Gdx.audio.newSound(Gdx.files.internal("hitsound.mp3"));
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("gamemusic.mp3"));
		gameMusic.setLooping(true);


		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// Character "hitbox"
		character = new Rectangle();
		character.x = 800 / 2 - 64 / 2;
		character.y = 20;
		character.width = 55;
		character.height = 55;

		//bullets arrays
		bullets = new Array<Rectangle>();
		spawnBullet();
		bulletsL = new Array<Rectangle>();
		spawnBulletLeft();
		bulletsR = new Array<Rectangle>();
		spawnBulletRight();
	}

  
	// Spawners
	private void spawnBullet() {
		Rectangle bullet = new Rectangle();
		bullet.x = MathUtils.random(0, 800 - 64);
		bullet.y = 480;
		bullet.width = bwidth;
		bullet.height = bheight;
		bullets.add(bullet);
		lastBulletTime = TimeUtils.nanoTime();
	}

	private void spawnBulletLeft() {
		Rectangle bulletL = new Rectangle();
		bulletL.y = MathUtils.random(0, 800 - 64);
		bulletL.x =0;
		bulletL.width = bwidth;
		bulletL.height = bheight;
		bulletsL.add(bulletL);
		lastBulletTimeL = TimeUtils.nanoTime();

	}
	private void spawnBulletRight() {
		Rectangle bulletR = new Rectangle();
		bulletR.y = MathUtils.random(0, 800 - 64);
		bulletR.x = 800;
		bulletR.width = bwidth;
		bulletR.height = bheight;
		bulletsR.add(bulletR);
		lastBulletTimeR = TimeUtils.nanoTime();
	}


	// Colors etc
	@Override
	public void render(float delta) {
		playTime+=delta;
		tempScore+=15*delta;
		highscore = checkHighScore();

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		camera.update();

		game.batch.setProjectionMatrix(camera.combined);

		// Draws characters, text, etc

		game.batch.begin();
		game.font.draw(game.batch, "Lives Left: " + livesLeft, 0, 480);
		game.font.draw(game.batch, "Score: " + Float.toString( tempScore), 350, 480);
		game.font.draw(game.batch, "Time: " + Float.toString(playTime), 700, 480);
		game.font.draw(game.batch,  highscore, 0, 300);
		game.batch.draw(characterSprite, character.x, character.y);
		for (Rectangle bullet : bullets) {
			game.batch.draw(bulletTexture, bullet.x, bullet.y);
		}
		for (Rectangle bulletL : bulletsL) {
			game.batch.draw(bulletTextureL, bulletL.x, bulletL.y);
		}
		for (Rectangle bulletR : bulletsR) {
			game.batch.draw(bulletTextureR, bulletR.x, bulletR.y);
		}
		game.batch.end();

		// Grabs the user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			character.x = touchPos.x - 64 / 2;
			character.y = touchPos.y - 64 / 2;
		}

		// Makes sure the character inside the bounds
		if (character.x < 0)
			character.x = 0;
		if (character.x > 800 - 64)
			character.x = 800 - 64;
		if (character.y < 0)
			character.y = 0;
		if (character.y > 480 - 64)
			character.y = 480 - 64;

		// Spawners 
		if (TimeUtils.nanoTime() - lastBulletTime > 200000000)
			spawnBullet();

		if (playTime > 2 && TimeUtils.nanoTime() - lastBulletTimeL > 2000000000)
			spawnBulletLeft();

		if (playTime > 3 && TimeUtils.nanoTime() - lastBulletTimeR > 200000000)
			spawnBulletRight();

		Iterator<Rectangle> iter = bullets.iterator();
		while (iter.hasNext()) {
			Rectangle bullet = iter.next();
			bullet.y -= 200 * Gdx.graphics.getDeltaTime();
			if (bullet.y + 64 < 0)
				iter.remove();
			if (bullet.x + 64 < 0)
				iter.remove();
			if (bullet.overlaps(character)) {
				livesLeft--;
				hitSound.play();
				iter.remove();
				Gdx.input.vibrate(vibrate);

			}
		}

		Iterator<Rectangle> iterL = bulletsL.iterator();
		while (iterL.hasNext()) {
			Rectangle bullet = iterL.next();
			bullet.x += 200 * Gdx.graphics.getDeltaTime();
			if (bullet.y + 64 < 0)
				iterL.remove();
			if (bullet.x + 64 < 0)
				iterL.remove();
			if (bullet.overlaps(character)) {
				livesLeft--;
				hitSound.play();
				iterL.remove();
				Gdx.input.vibrate(vibrate);

			}
		}

		Iterator<Rectangle> iterR = bulletsR.iterator();
		while (iterR.hasNext()) {
			Rectangle bullet = iterR.next();
			bullet.x -= 200 * Gdx.graphics.getDeltaTime();
			if (bullet.y + 64 < 0)
				iterL.remove();
			if (bullet.x + 64 < 0)
				iterR.remove();
			if (bullet.overlaps(character)) {
				livesLeft--;
				hitSound.play();
				iterR.remove();
				Gdx.input.vibrate(vibrate);


			}
		}
		if (livesLeft <= 0){
			CompareScore();
			pause();
			game.setScreen(new MainMenu(game));


		}

	}
	// This should be encrypted if this was a real game, but for this exercise I've left it as plain text
	private String checkHighScore() {
                
		boolean exists = Gdx.files.local("files/highscore.txt").exists();

		FileHandle file= Gdx.files.local("files/highscore.txt");
		if (exists)
		{
			String var = file.readString();
			return var;
		} else {
			file.writeString("HighScore:0", false);

			return highscore;
		}
	}
       //Originally this was supposed to also set the name of the player for highscore, 
       // but I didn't manage to get it work on android
        
         public void CompareScore(){
               // MyTextInputListener listener = new MyTextInputListener();
                if( IntScore() > Integer.parseInt((highscore.split(":")[1])))
		{   
                // Setting names does not work on Android yet   
                 
               // Gdx.input.getTextInput(listener, "New Highscore!", "", "Your name?");  
                 name = "Highscore";
               
                 // Turns the Int & name into a String
		highscore = name + ":" + IntScore();
                FileHandle scoreFile= Gdx.files.local("files/highscore.txt");
                scoreFile.writeString(highscore, false); 
                }
                
	 } 


	public int IntScore(){
		intScore = Math.round(tempScore);
		return intScore;
	}
  

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		gameMusic.setVolume(0.2f);
		gameMusic.play();

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		gameMusic.stop();
		hitSound.stop();
		Gdx.input.cancelVibrate();
		CompareScore();
	}

	@Override
	public void resume() {
          
	}

	@Override
	public void dispose() {
		bulletTexture.dispose();
		characterSprite.dispose();
		hitSound.dispose();
		gameMusic.dispose();
	}


}



