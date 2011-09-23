package hiks.petitsplaisirs.model;

import java.util.ArrayList;
import java.util.List;

public class Container
{
	public List<Object> objectList;

	public List<Object> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<Object> objectList) {
		this.objectList = objectList;
	}

	public Container(){
		objectList = new ArrayList<Object>();
	}

	public Container(List<Object> objectList)
	{
		this.objectList = objectList;
	}

}