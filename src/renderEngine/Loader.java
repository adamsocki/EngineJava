package renderEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Loader 
{
	// need to handle some memory management
	// when we close our game, delete all the vao and vbo created in our memory
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions, int[] indices)
	{
		//1. create empty vao and get id of it
		int vaoID = createVAO();
		//1.1 bind  the indices buffer to it
		bindIndicesBuffer(indices);
		//2. store positional data into one of the attribute lists of the vao
		storeDataInAttributeList(0, positions);
		//3. now that we are finished with it, we unbind it
		unbindVAO();
		//4 return the data of the vao/model
		return new RawModel(vaoID, indices.length);
	}
	
	public void cleanUp()
	{	// called when we close our game
		for (int i = 0; i < vaos.size(); i++)
		{
			GL30.glDeleteVertexArrays(vaos.get(i));
		}
		for (int i = 0 ; i < vbos.size(); i++)
		{
			GL15.glDeleteBuffers(vbos.get(i));
		}
	}
	private int createVAO()
	{
		//1. creates an empty vao to return the id of that vao;
		int vaoID = GL30.glGenVertexArrays();
		//1.1 whenever we create a vao, going to add it to our list
		vaos.add(vaoID);
		//2. activate the vao by binding it
		GL30.glBindVertexArray(vaoID);
		return vaoID;
		
	}
	
	private void storeDataInAttributeList(int attributeNumber, float[] data)
	{
		//1. store the data into the attribute lists via a VBO
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		//2. bind it first to do something with it first is required
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		//3. store data into it (as a float buffer)
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		// now that it's in a float buffer we can store it to the VBO
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		//5. put that vbo into the vao
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0,0);
		//6. now unbind it
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO()
	{	// 0 unbinds the array
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices)
	{
		//1. create an empty vbo
		int vboID = GL15.glGenBuffers();
		//2. add to list to get deleted
		vbos.add(vboID);
		//3. bind so we can use it
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		//4. store our indices into this vbo | converts our indicies array into an int buffer
		IntBuffer buffer = storeDataInIntBuffer(indices);
		//5. store that int buffer into the vbo
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{	// convert from array to a buffer
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		// prepare the buffer to be read from (instead of written to)
		buffer.flip();
		return buffer;
	}
	
	
}
