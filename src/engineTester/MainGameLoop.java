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
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		//StaticShader shader = new StaticShader();
		//Renderer renderer = new Renderer(shader);
			
		//RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
		RawModel fernModel = OBJParser.loadObjModel("fern", loader);
		
		TexturedModel texturedFernModel = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("fern")));
		
		ModelTexture fernTexture = texturedFernModel.getTexture();
		fernTexture.setHasTransparency(true);
		fernTexture.setShineDamper(10);
		fernTexture.setReflectivity(1);
		fernTexture.setUseFakeLighting(true);
		
		//Entity entity = new Entity(staticModel, new Vector3f(0,-4,-25),0,0,0,1);
		Light light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(-0,-1,loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(-1,-1,loader, new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();
		Random random = new Random();

		// create entities
		//List<Entity> allDragons = new ArrayList<Entity>();
		List<Entity> allFerns = new ArrayList<Entity>();
		for (int i = 0; i < 80; i++)
		{
			float x = random.nextFloat() * 100 - 50;
			//float y = random.nextFloat() * 100 - 50;
			float y = 0;
			float z = random.nextFloat() * -300;
			allFerns.add(new Entity(
					texturedFernModel, new Vector3f(x, y, z), 
					0f, 0f, 0f, 0.4f));
 		}
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			
			
			//game logic
			//entity.increaseRotation(0.08f, 0.08f, -0.08f);
			//renderer.prepare();
			camera.move();
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			for (Entity ferns:allFerns)
			{
				renderer.processEntity(ferns);
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