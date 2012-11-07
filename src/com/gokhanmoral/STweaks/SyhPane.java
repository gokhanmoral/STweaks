package com.gokhanmoral.STweaks;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SyhPane {
	public String description;
	public String name;
	
	public List<SyhControl> controls = new ArrayList<SyhControl>(); 
	
	public void addPaneToUI(Activity activity, LinearLayout layout)
	{
		Context context = activity;
		
        //TODO: Move this to xml
		TextView paneNameView = new TextView(context);
		paneNameView.setBackgroundColor(Color.DKGRAY);
		paneNameView.setTextColor(Color.LTGRAY);
        paneNameView.setText(this.name.toUpperCase());
        paneNameView.setTypeface(null, Typeface.BOLD);
        layout.addView(paneNameView); 
        
        //TODO: Move this to xml
		if ((this.description != null) && (!this.description.equals("")))
		{
	        TextView paneDescriptionView = new TextView(context);
	        paneDescriptionView.setBackgroundColor(Color.BLACK);
	        paneDescriptionView.setTextColor(Color.LTGRAY);
	        //paneDescriptionView.setTextSize(paneNameView.getTextSize()*0.6f);
	        paneDescriptionView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
	        paneDescriptionView.setText(this.description);
	        paneDescriptionView.setPadding(0, 5, 0, 10);
	        layout.addView(paneDescriptionView);  
		}	
	}
}
