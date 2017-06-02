/*
Does not work yet, maybe someday...
 */
package com.dokidokidanmaku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

	public class MyTextInputListener implements Input.TextInputListener {
          
            GameScreen game;
            MyTextInputListener listener = new MyTextInputListener();	
            public String input = null;
            
               
            @Override
		public void input(String text) {
			input = text;   
                 }
		@Override
		public void canceled () {
		    input = "Anonymous"; 
		}
                public String returnTextToMe(){  
                System.out.println(input);
                return input;
		}
            
               
	}