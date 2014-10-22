package uk.ac.lincoln.games.gravbox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GravBox extends ApplicationAdapter {
	World world;
	//Box2DDebugRenderer debugRenderer;//physics debug
	Stage stage;
	//Balls;
	Ball ball_1,ball_2,ball_3,ball_4;
	float x_offset, y_offset;
	
	/**
	 * Ball entity. Manages sprites, drawing and physics body internally.
	 * @author bkirman
	 *
	 */
	public class Ball extends Actor {
		Sprite sprite;
		Body body;
		
		/**
		 * Create a ball in the world
		 * @param world box2d World
		 * @param x position in the _world_ (i.e. not screen)
		 * @param y
		 */
		public Ball(World world, int x, int y) {
			super();
			sprite = new Sprite(new Texture("ball.png"));
			//Ball physics
			BodyDef bodyDef = new BodyDef();
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.set(x,y);
			body = world.createBody(bodyDef);
			CircleShape circle = new CircleShape();
			circle.setPosition(new Vector2(0,0)); 
			circle.setRadius(25f);
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = circle;
			fixtureDef.density = 0.6f; 
			fixtureDef.friction = 0.4f;
			fixtureDef.restitution = 0.7f; //bounciness
			body.createFixture(fixtureDef);
			circle.dispose();
		}
		
		@Override
		public void draw(Batch batch,float parentAlpha) {
			//update physics
			setPosition(body.getPosition().x-25, body.getPosition().y-25);//box2d origin is middle of object, we draw at corner
			setRotation(body.getAngle()*MathUtils.radDeg);
			//draw
			sprite.setRotation(this.getRotation());
			sprite.setX(this.getX());
			sprite.setY(this.getY());
			sprite.draw(batch);
		}
	}
	
	@Override
	public void create () {
		
		stage = new Stage(new FitViewport(360,640));
				
		//Physics
		world = new World(new Vector2(2, -100), true); //grav X, Y initial
		ball_1 = new Ball(world,3*(int)stage.getWidth()/4,3*(int)stage.getHeight()/4);
		ball_2 = new Ball(world,3*(int)stage.getWidth()/4,(int)stage.getHeight()/4);
		ball_3 = new Ball(world,(int)stage.getWidth()/4,3*(int)stage.getHeight()/4);
		ball_4 = new Ball(world,(int)stage.getWidth()/4,(int)stage.getHeight()/4);
		stage.addActor(ball_1);
		stage.addActor(ball_2);
		stage.addActor(ball_3);
		stage.addActor(ball_4);
		
		
		//Make wall/edges
		BodyDef edge_body = new BodyDef();
		edge_body.position.set(0,0);
		Body edge = world.createBody(edge_body);
		EdgeShape edge_shape = new EdgeShape();
		FixtureDef edge_fixture = new FixtureDef();
		edge_fixture.shape = edge_shape;
		
		//bottom
		edge_shape.set(new Vector2(0,0), new Vector2(stage.getWidth(),0));
		edge.createFixture(edge_fixture);
		//left
		edge_shape.set(new Vector2(0,0), new Vector2(0,stage.getHeight()));
		edge.createFixture(edge_fixture);
		//top
		edge_shape.set(new Vector2(0,stage.getHeight()), new Vector2(stage.getWidth(),stage.getHeight()));
		edge.createFixture(edge_fixture);
		//right
		edge_shape.set(new Vector2(stage.getWidth(),stage.getHeight()), new Vector2(stage.getWidth(),0));
		edge.createFixture(edge_fixture);
		
		edge_shape.dispose();
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		
		//use accelerometer to change world physics. Note the X/Y of phone is inverted in box2d. We also use factor of 10 to speed up
		//NB: without accelerometer the gravity will be set to zero and nothing will move. comment out this line to check physics on desktop.
		world.setGravity(new Vector2(-Gdx.input.getAccelerometerX()*10f,-Gdx.input.getAccelerometerY()*10f));
		
		//debugRenderer.render(world, camera.combined);
		world.step(1/60f, 6, 2); //step physics

	}
	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	@Override
	public void dispose () {
		stage.dispose();
	}
}
