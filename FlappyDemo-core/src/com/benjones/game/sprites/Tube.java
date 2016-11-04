package com.benjones.game.sprites;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Tube {
	private static final int FLUCTUATION = 130;
	private static final int TUBE_GAP = 100;
	private static final int LOWEST_OPENING = 120;
	public static final int TUBE_WIDTH = 52;
	
	private Texture topTube;
	private Texture bottomTube;
	private Vector2 posTopTube;
	private Vector2 posBottomTube;
	private Rectangle boundsTop;
	private Rectangle boundsBottom;
	private Rectangle boundsBetween;
	private Random rand;
	
	public Tube (float x) {
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		rand = new Random();
		
		posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
		posBottomTube = new Vector2(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
		
		boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());
		boundsBottom = new Rectangle(posBottomTube.x, posBottomTube.y, bottomTube.getWidth(), bottomTube.getHeight());
		boundsBetween = new Rectangle(posBottomTube.x + bottomTube.getWidth(), posTopTube.y - TUBE_GAP, 1, TUBE_GAP);
		
	}
	
	public void reposition(float x) {
		posTopTube.set(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
		posBottomTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());
		
		boundsTop.setPosition(posTopTube.x, posTopTube.y);
		boundsBottom.setPosition(posBottomTube.x, posBottomTube.y);
		boundsBetween.setPosition(posBottomTube.x + bottomTube.getWidth(), posTopTube.y - TUBE_GAP);
	}
	
	public boolean collides(Rectangle player) {
		return player.overlaps(boundsTop) || player.overlaps(boundsBottom);
	}
	
	public boolean passesThrough(Rectangle player) {
		return player.overlaps(boundsBetween);
	}
	
	public void dispose() {
		topTube.dispose();
		bottomTube.dispose();
	}

	public Texture getTopTube() {
		return topTube;
	}

	public void setTopTube(Texture topTube) {
		this.topTube = topTube;
	}

	public Texture getBottomTube() {
		return bottomTube;
	}

	public void setBottomTube(Texture bottomTube) {
		this.bottomTube = bottomTube;
	}

	public Vector2 getPosTopTube() {
		return posTopTube;
	}

	public void setPosTopTube(Vector2 posTopTube) {
		this.posTopTube = posTopTube;
	}

	public Vector2 getPosBottomTube() {
		return posBottomTube;
	}

	public void setPosBottomTube(Vector2 posBottomTube) {
		this.posBottomTube = posBottomTube;
	}
	
}
