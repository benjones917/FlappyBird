package com.benjones.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.benjones.game.FlappyDemo;
import com.benjones.game.sprites.Bird;
import com.benjones.game.sprites.Tube;

public class PlayState extends State {
	private static final int TUBE_SPACING = 125;
	private static final int TUBE_COUNT = 4;
	private static final int FLOOR_Y_OFFSET = -50;
	public static Integer gameScore = 0;
	private CharSequence textScore = "";
	private BitmapFont font;
	
	private Bird bird;
	private Texture background;
	private Texture floor;
	
	private Vector2 floorPos1;
	private Vector2 floorPos2;
	
	private Array<Tube> tubes;
	private Sound hit;
	private Sound pass;
	private boolean passed;

	protected PlayState(GameStateManager gsm) {
		super(gsm);
		bird = new Bird(50, 300);
		gameScore = 0;
		cam.setToOrtho(false, (FlappyDemo.WIDTH/2), (FlappyDemo.HEIGHT/2));
		background = new Texture("bg.png");
		floor = new Texture("ground.png");
		floorPos1 = new Vector2(cam.position.x - (cam.viewportWidth/2), FLOOR_Y_OFFSET);
		floorPos2 = new Vector2((cam.position.x - (cam.viewportWidth/2) + floor.getWidth()), FLOOR_Y_OFFSET);
		hit = Gdx.audio.newSound(Gdx.files.internal("hit.ogg"));
		pass = Gdx.audio.newSound(Gdx.files.internal("pass.ogg"));
		font = new BitmapFont();
		font.getData().setScale(1.5f);
		font.setColor(new Color().BLACK);
		
		tubes = new Array<Tube>();
		
		for(int i=1; i<=TUBE_COUNT; i++) {
			tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
		}
	}

	@Override
	protected void handleInput() {
		if (Gdx.input.justTouched()) {
			bird.jump();
		}
	}

	@Override
	public void update(float dt) {
		handleInput();
		updateGround();
		textScore = gameScore.toString();
		bird.update(dt);
		cam.position.x = bird.getPosition().x + 80;
		
		
		for (Tube tube : tubes) {
			if((cam.position.x - (cam.viewportWidth/2)) > (tube.getPosTopTube().x + tube.getTopTube().getWidth())) {
				tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
				passed = false;
			}
			if(tube.collides(bird.getBounds())) {
				hit.play(1f);
				gsm.set(new MenuState(gsm));
				break;
			}
			if(tube.passesThrough(bird.getBounds()) && passed == false) {
				pass.play(1f);
				gameScore++;
				System.out.println(gameScore);
				passed = true;
			}
		}
		
		if(bird.getPosition().y <= (floor.getHeight() + FLOOR_Y_OFFSET)) {
			hit.play(1f);
			gsm.set(new MenuState(gsm));
		}
		
		cam.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		sb.draw(background, (cam.position.x - (cam.viewportWidth / 2)), 0);
		sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
		for (Tube tube : tubes) {
			sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
			sb.draw(tube.getBottomTube(), tube.getPosBottomTube().x, tube.getPosBottomTube().y);
		}
		sb.draw(floor, floorPos1.x, floorPos1.y);
		sb.draw(floor, floorPos2.x, floorPos2.y);
		font.draw(sb, textScore, (bird.getPosition().x - 20), 30);
		sb.end();
	}
	
	private void updateGround() {
		if(cam.position.x - (cam.viewportWidth/2) > (floorPos1.x + floor.getWidth())) {
			floorPos1.add(floor.getWidth()*2, 0);
		}
		if(cam.position.x - (cam.viewportWidth/2) > (floorPos2.x + floor.getWidth())) {
			floorPos2.add(floor.getWidth()*2, 0);
		}
	}

	@Override
	public void dispose() {
		background.dispose();
		bird.dispose();
		for (Tube tube : tubes) {
			tube.dispose();
		}
		floor.dispose();
		System.out.println("Play State Disposed");
	}
	
	

}
