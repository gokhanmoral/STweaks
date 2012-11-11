package com.gokhanmoral.stweaks.app;

import java.util.ArrayList;
import java.util.List;

import com.gokhanmoral.stweaks.app.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public final class SyhSpinner extends SyhControl implements OnItemSelectedListener{
	
	SyhSpinner(Activity activityIn) {
		super(activityIn);
	}

	private Spinner spnnr = null;
	private List<String> nameList = new ArrayList<String>();
	private List<String> valueList = new ArrayList<String>();
	
	private int findValueInValueList(String value)
	{
		int index = -1;
		for (int i = 0; i < valueList.size(); i++)
		{
			if (valueList.get(i).equalsIgnoreCase(value))
			{
				index = i;
				break;
			}
		}
		return index;
	}
	
	private void setSpinnerFromHardwareValue()
	{
		//Log.w("spinner", this.name +" setSpinnerFromHardwareValue!");
		if (spnnr != null)
		{
			int index = findValueInValueList(valueFromScript);
			if (-1 == index) index = 0;
			spnnr.setSelection(index);
		}
		valueFromUser = valueFromScript;		
	}
	
	@Override
	public void createInternal() {
		
		//Assumption: valueFromScript is set correctly.
		
		if (this.name.equalsIgnoreCase("FLL Tuning"))
		{
			Log.e("e","e");
		}

		//-- Set spinner programmatically
		//spnnr = new Spinner(context);
		//spnnr.setBackgroundColor(Color.argb(100, 143, 188, 143));
		
		//Create spinner from xml template
		
		/*
		 IMPORTANT NOTE:
		 	In summary to change the text size for a Spinner either:

			Create a custom TextView layout.
			Change the text size with the android:textSize attribute.
			Change the text color with android:textColor in the new style file.
			
			Or:
			
			Create a custom style.
			Use android:TextAppearance.Widget.TextView.SpinnerItem as the parent style.
			Change the text size with the android:textSize attribute.
			
			Or:
			
			Customize the theme
			
			http://androidcookbook.com/Recipe.seam;jsessionid=0443546CEE776318BF6D21552A9D1864?recipeId=4012
		*/
		
		spnnr = (Spinner) LayoutInflater.from(context).inflate(R.layout.template_spinner, controlLayout, false);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.template_spinner_item, nameList); //custom spinner
		//-- ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, nameList);
		//-- CustomArrayAdapter<String> dataAdapter = new CustomArrayAdapter<String>(context, nameList);
		
		//-- dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item); //no radio buttons
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //radio buttons
		//-- dataAdapter.setDropDownViewResource(R.layout.template_spinner_dropdown_item); //custom dropdowns

		spnnr.setAdapter(dataAdapter);
		spnnr.setOnItemSelectedListener(this);
		setSpinnerFromHardwareValue();
		controlLayout.addView(spnnr);
	}
	
	public void addNameAndValue(String name, String value){
		nameList.add(name);
		valueList.add(value);
	}
	
	public void clearNameAndValues(){
		nameList.clear();
		valueList.clear();
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) 
	{
		//Log.w("spinner", this.name +" onItemSelected!");
		valueFromUser = valueList.get(pos);
		//-- ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);//TODO: Change selected text color

		if (isChanged())
    	{
    		this.vci.valueChanged();//TODO: changing text color back to black!!!
    	}
    }
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
	
	static class CustomArrayAdapter<T> extends ArrayAdapter<T>
	{
	    public CustomArrayAdapter(Context ctx, List<T> objects)
	    {
	        super(ctx, android.R.layout.simple_spinner_item, objects);
	    }

	    //other constructors
	    @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent)
	    {
	        View view = super.getView(position, convertView, parent);

	        //we know that simple_spinner_item has android.R.id.text1 TextView:         

	        /* if(isDroidX) {*/
	            TextView text = (TextView)view.findViewById(android.R.id.text1);
	            text.setTextColor(Color.RED);//choose your color :)         
	        /*}*/

	        return view;
	    }
	}

	@Override
	protected void applyScriptValueToUserInterface() {
		setSpinnerFromHardwareValue();	
	}

	
	@Override
	protected String getDefaultValue() {
		return valueList.get(0);
	}

	
}
