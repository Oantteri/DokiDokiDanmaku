package com.dokidokidanmaku;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class DokiDokiDanmaku extends Game {

   

 
	
	public SpriteBatch batch;
        public BitmapFont font;
        
        
        
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
                font.setColor(1, 0, 0, 1);
                font.getData().setScale(1.25f,1.25f);
		this.setScreen(new MainMenu(this));
	}

	public void render() {
		super.render(); 
	}
	
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

}