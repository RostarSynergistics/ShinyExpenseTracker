package ca.ualberta.cs.shinyexpensetracker;




import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.ualberta.cs.shinyexpensetracker.models.TagList;

public class TagController {
	private static TagList tagList;
	
	static public TagList getTagList()
	{
		if (tagList == null)
		{
			tagList = new TagList();
		}
		return tagList;
	}
	
	// I don't think we need these if it is a singleton.
	/*
	public TagController() {
		tagList = new TagList();
	}
	
	public TagController(TagList list) {
		this.tagList = list;
	}
	*/
	

	
	public static void addTag(String s) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");
		Matcher m = p.matcher(s);
		if (m.matches())
			tagList.addTag(s);
		else
			return;
	}
	public static void removeTag(String s) {
		tagList.removeTag(s);
	}
	
	public static int getTagCount(){
		return tagList.getCount();
	}
}
