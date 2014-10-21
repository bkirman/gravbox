package uk.ac.lincoln.games.gravbox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class GravBox extends ApplicationAdapter {
	SpriteBatch batch;
	Sprite ball_sprite;
	World world;
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera camera;
	Body ball;
	float x_offset, y_offset;
	
//	stage = new Stage(new FitViewport(360,640));
	@Override
	public void create () {
		batch = new SpriteBatch();
		ball_sprite = new Sprite(new Texture("ball.png"));
		camera = new OrthographicCamera(360,640);
		camera.position.set(0,0,0);
		camera.update();
		world = new World(new Vector2(2, -10), true);
		//debugRenderer = new Box2DDebugRenderer(); //physics debug
		
		//Ball
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set((camera.viewportWidth/2f),(camera.viewportHeight/2f));
		ball = world.createBody(bodyDef);
		CircleShape circle = new CircleShape();
		circle.setPosition(new Vector2(0,0));
		circle.setRadius(25f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.7f; // Make it bounce a little bit
		ball.createFixture(fixtureDef);
		circle.dispose();
		
		//Make wall/edges
		BodyDef edge_body = new BodyDef();
		edge_body.position.set(0,0);
		Body edge = world.createBody(edge_body);
		EdgeShape edge_shape = new EdgeShape();
		FixtureDef edge_fixture = new FixtureDef();
		edge_fixture.shape = edge_shape;
		
		//bottom
		edge_shape.set(new Vector2(0,0), new Vector2(camera.viewportWidth,0));
		edge.createFixture(edge_fixture);
		//left
		edge_shape.set(new Vector2(0,0), new Vector2(0,camera.viewportHeight));
		edge.createFixture(edge_fixture);
		//top
		edge_shape.set(new Vector2(0,camera.viewportHeight), new Vector2(camera.viewportWidth,camera.viewportHeight));
		edge.createFixture(edge_fixture);
		//right
		edge_shape.set(new Vector2(camera.viewportWidth,camera.viewportHeight), new Vector2(camera.viewportWidth,0));
		edge.createFixture(edge_fixture);
		
		edge_shape.dispose();
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ball_sprite.setPosition(ball.getPosition().x-25, ball.getPosition().y-25);
		ball_sprite.setRotation(ball.getAngle()*MathUtils.radDeg);
		batch.begin();
		
		ball_sprite.draw(batch);
		batch.end();
		//debugRenderer.render(world, camera.combined);
		world.step(1/45f, 6, 2);

	}
}
