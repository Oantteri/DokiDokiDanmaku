package com.dokidokidanmaku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MainMenu implements Screen {
	final DokiDokiDanmaku game;
	final static private int WIDTH = 800;
	final static private int HEIGHT = 480;
	public BitmapFont Font = createFont(Arial, 18); //16dp == 12pt
        
	OrthographicCamera camera;
	

        
        
	public MainMenu(final DokiDokiDanmaku passed_game) {
	
	
            game = passed_game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
              
              
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
                    
		game.font.draw(game.batch, "Welcome to DokiDokiDanmaku!!", 100, 150);
		game.font.draw(game.batch, "Tap anywhere to begin!", 100, 120);
                game.font.draw(game.batch, "Your goal is to avoid getting hit for as long as possible!", 100, 100);
		game.batch.end();
		
		// If player activates the game, dispose of this menu.
		if (Gdx.input.justTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
}