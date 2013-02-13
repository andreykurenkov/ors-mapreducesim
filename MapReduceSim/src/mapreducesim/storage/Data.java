package mapreducesim.storage;

public class Data {
	private int uri;
	private int parentURI;
	private int[] childURI;
	
	private Data child;

	
	
	public void createChild(int childURI) {
		child = new Data(childURI);
	}
	
	public Data(int uriIn) {
		uri = uriIn;
	}
	public Data(int uriIn, int[] childURIs) {
		uri = uriIn;
		this.child = new Data(childURIs[1]);
		//TODO: allow for creating of multiple children.
	}
	public int getURI() {
		return uri;
	}
	public void setURI(int uriIn) {
		uri = uriIn;
	}
	
	public int getParentURI() {
		return parentURI;
	}
	public void setParentURI(int parentUriIn) {
		parentURI = parentUriIn;
	}

	public int getNumChildren() {
		return childURI.length;
	}
	public int getChildURI(int position) {
		if (position > childURI.length) {
			//Msg.error("Position out of childURI array bounds.");
			return -1;
		}
		else
		{
			return childURI[position];
		}		
	}
}
