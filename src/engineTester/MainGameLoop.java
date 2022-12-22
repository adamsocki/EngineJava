package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJParser;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		//StaticShader shader = new StaticShader();
		//Renderer renderer = new Renderer(shader);
			
		//RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
		RawModel model = OBJParser.loadObjModel("dragon", loader);
		
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("white")));
		ModelTexture texture = staticModel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		//Entity entity = new Entity(staticModel, new Vector3f(0,-4,-25),0,0,0,1);
		Light light = new Light(new Vector3f(0,0,-20), new Vector3f(1,1,1));
		
		
		
		Camera camera = new Camera();
		// create entities
		List<Entity> allDragons = new ArrayList<Entity>();
		Random random = new Random();
		
		for (int i = 0; i < 43; i++)
		{
			float x = random.nextFloat() * 100 - 50;
			float y = random.nextFloat() * 100 - 50;
			float z = random.nextFloat() * -300;
			allDragons.add(new Entity(
					staticModel, new Vector3f(x, y, z), 
					random.nextFloat() * 180f, random.nextFloat() * 180f, 
					0f, 1f));
 		}
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			
			
			//game logic
			//entity.increaseRotation(0.08f, 0.08f, -0.08f);
			//renderer.prepare();
			camera.move();
			
			for (Entity dragon:allDragons)
			{
				renderer.processEntity(dragon);
			}
			//shader.start();
			//shader.loadLight(light);
			//shader.loadViewMatrix(camera);
			//renderer.render(entity,shader);
			//shader.stop();
			renderer.render(light, camera);
			DisplayManager.updateDisplay();		
		}

		//shader.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}