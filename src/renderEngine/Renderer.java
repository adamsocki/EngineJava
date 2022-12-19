package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer 
{
	public void prepare() 
	{		
		GL11.glClearColor(1.0f, 0.4f, 0.4f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	public void render(RawModel model)
	{
		//1. bind the vao that we want to use
		GL30.glBindVertexArray(model.getVaoID());
		//2. activate the attribute list that is stored
		GL20.glEnableVertexAttribArray(0);
		//3. render it
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		//4. disable it
		GL20.glDisableVertexAttribArray(0);
		//5. unbind vao
		GL30.glBindVertexArray(0);
	}
}
