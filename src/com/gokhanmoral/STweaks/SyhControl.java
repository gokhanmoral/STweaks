package com.gokhanmoral.STweaks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class SyhControl {
	public String description = "";
	public String name = "";
	public String action = "";
	public String valueFromScript = "0"; 	//loaded from the kernel script (integer, float, "on"/"off"...)
	public String valueFromUser = "0";    	//user input to be applied to the kernel script (integer, float, "on"/"off"...)
	public View view;
	protected Boolean canGetValueFromScript = true;
	protected String syh_command = "/res/uci.sh "; 

	protected SyhValueChangedInterface vci; //interface to inform main activity about changed values
	protected Activity activity;
	protected Context context;
	protected LinearLayout controlLayout;
	private TextView nameTextView;
	private TextView descriptionTextView;
	
	protected SyhControl(Activity activityIn)
	{
		activity = activityIn;
		context = activityIn;
		vci = (SyhValueChangedInterface) activityIn;
	}
		
	public boolean isChanged()
	{
		boolean changed = (valueFromUser.equals(valueFromScript) == false);
		return changed;
	}
	
	// apply user selected value to the kernel script
	public String setValueViaScript() 
	{ 
		String command = syh_command + action + " " + valueFromUser;
		String response = Utils.executeRootCommandInThread3(command);
		if(response == null) response = "";
		valueFromScript = valueFromUser;
		return response;		
	}
	
	// get the value from kernel script - user interface NOT CHANGED! 
	public boolean getValueViaScript() 
	{
		boolean isOk = false;
		
		if (this.canGetValueFromScript)
		{
			String command = syh_command + action;
			String response = Utils.executeRootCommandInThread3(command);
			if(response != null)
			{
				if (!response.isEmpty())
				{
					valueFromScript = response.replaceAll("[\n\r]", "");;
					isOk = true;
				}
			}
			
			if (!isOk)
			{
				valueFromScript = this.getDefaultValue();
				if (valueFromScript == null)
				{
					valueFromScript = "";
				}
			}
			
			Log.i("getValueViaScript " + this.getClass().getName() + "[" + this.name + "]:", "Value from script:" + valueFromScript);
		}
				
		return isOk;		
	}
	
	public void create()
	{		
		//Assumptions: 
		//1. valueFromScript is set correctly before creation.
		
/*		
 * TODO: Later concern!
		If we use fragments which can be put to stack then we have problems.
		Because of two conditions we are here:
		1.) Control is created for the first time
		2.) Fragment is paused and resuming...
		Question: Which value should be displayed in the user interface:
		          valueFromScript or valueFromUser?
*/
		
		valueFromUser = valueFromScript; //prevent value changed event!!!
		
		controlLayout = new LinearLayout(context);
		controlLayout.setOrientation(LinearLayout.VERTICAL);
		controlLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		controlLayout.setPadding(30, 5, 30, 5);
		
        //TODO: Move this to xml
		//Control name
		nameTextView = new TextView(context);
		//-- nameTextView.setBackgroundColor(Color.BLACK);
		nameTextView.setTextColor(Color.LTGRAY);
		nameTextView.setText(name.toUpperCase());
		nameTextView.setTypeface(null, Typeface.BOLD);
		controlLayout.addView(nameTextView);
		
		//TODO: Move this to xml
		//Control description
		descriptionTextView = new TextView(context);
		descriptionTextView.setPadding(15, 5, 0, 0);
		//-- descriptionTextView.setBackgroundColor(Color.BLACK);
		//descriptionTextView.setTextSize(nameTextView.getTextSize()*0.5f);
		descriptionTextView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
		descriptionTextView.setTextColor(Color.LTGRAY);
		descriptionTextView.setText(description);
		controlLayout.addView(descriptionTextView);
		
		createInternal();
		
		TextView paneSeparatorBlank = new TextView(context);
		paneSeparatorBlank.setHeight(5);
        //--paneSeparatorBlank.setBackgroundColor(Color.BLACK);
        paneSeparatorBlank.setText("");
        controlLayout.addView(paneSeparatorBlank); 
		
		TextView paneSeparatorLine = new TextView(context);
		paneSeparatorLine.setHeight(2);
        paneSeparatorLine.setBackgroundColor(Color.LTGRAY);
        paneSeparatorLine.setText("");
        controlLayout.addView(paneSeparatorLine);  
        
		TextView paneSeparatorBlankAfterLine = new TextView(context);
		paneSeparatorBlankAfterLine.setHeight(10);
        //--paneSeparatorBlank.setBackgroundColor(Color.BLACK);
        paneSeparatorBlankAfterLine.setText("");
        controlLayout.addView(paneSeparatorBlankAfterLine);  

		view = controlLayout;
	}
	
	abstract protected void createInternal(); 	//sets the view
	
	abstract protected void applyScriptValueToUserInterface();	//clear user input, set it back to the script value
	
	abstract protected String getDefaultValue();
		
}
