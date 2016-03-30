package database;

public class Node {
	
	public String name;
	public String type;
	public int id;
	public String Net;
	
	public Node(String n,String t,int i,String net ){
		name=n;
		type=t;
		id=i;
		Net=net;
		
	}
	
	public boolean equal(Node n){
		if( this.name.equals(n.name) && this.type.equals(n.type) ) return true;
		else return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNet() {
		return Net;
	}

	public void setNet(String net) {
		Net = net;
	}
	

}
