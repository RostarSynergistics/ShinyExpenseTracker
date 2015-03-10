package ca.ualberta.cs.shinyexpensetracker;

import java.util.ArrayList;

import ca.ualberta.cs.shinyexpensetracker.models.IModel;
import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class TagController{
	private TagList list;
	private static TagController tagController;
	
	private transient ArrayList<IView<TagController>> views;
	

	private TagController(){
		list = new TagList();
	}
	
	public static TagController getInstance(){
		if(tagController == null){
			tagController = new TagController();
			return tagController;
		} else {
			return tagController;
		}
		
	}
	public  void addTag(String s) {
		list.addTag(s);
	}
	public  void removeTag(String s) {
		list.removeTag(s);
	}
	
	public TagList getTagList(){
		return list;
	}
}


