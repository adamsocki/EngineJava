package models;

public class RawModel 
{
	private int vaoID;
	private int vertexCount;
	
	// constructor
	public RawModel(int vaoID, int vertexCount)
	{
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	// getter for VaoID
	public int getVaoID() {
		return vaoID;
	}
	
	// getter for number of vertices
	public int getVertexCount() {
		return vertexCount;
	}
}
