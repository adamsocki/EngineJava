package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.objFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJParser;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture, rTexture, gTexture, bTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		
		//StaticShader shader = new StaticShader();
		//Renderer renderer = new Renderer(shader);
			
		//RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
		ModelData data = objFileLoader.loadOBJ("fern");
		

		//RawModel fernModel = OBJParser.loadObjModel("fern", loader);
		RawModel fernModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		
		TexturedModel texturedFernModel = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("fern")));
		
		ModelTexture fernTexture = texturedFernModel.getTexture();
		fernTexture.setHasTransparency(true);
		fernTexture.setShineDamper(10);
		fernTexture.setReflectivity(1);
		fernTexture.setUseFakeLighting(true);
		
		//Entity entity = new Entity(staticModel, new Vector3f(0,-4,-25),0,0,0,1);
		Light light = new Light(new Vector3f(20000,20000,2000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(-0,-1,loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1,-1,loader, texturePack, blendMap);
		
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
		
		RawModel bunnyModel = OBJParser.loadObjModel("bunny", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
				loader.loadTexture("white")));
				
		Player player = new Player(stanfordBunny, new Vector3f(100, 0, -50), 0,0,0,1);
		
		while(!Display.isCloseRequested()){
			
			
			//game logic
			//entity.increaseRotation(0.08f, 0.08f, -0.08f);
			//renderer.prepare();
			camera.move();
			player.move();
			renderer.processEntity(player);
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