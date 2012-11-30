package com.gokhanmoral.stweaks.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SyhPane {
	public String description;
	public String name;
	
	public List<SyhControl> controls = new ArrayList<SyhControl>(); 
	
	public void addPaneToUI(Activity activity, LinearLayout layout)
	{
		Context context = activity;
		
		TextView paneNameView = new TextView(context);
		paneNameView = (TextView)LayoutInflater.from(context).inflate(R.layout.template_panelname, layout, false);
		paneNameView.setText(this.name.toUpperCase());
		layout.addView(paneNameView); 
        
		if ((this.description != null) && (!this.description.equals("")))
		{
	        TextView paneDescriptionView = new TextView(context);
	        paneDescriptionView = (TextView)LayoutInflater.from(context).inflate(R.layout.template_paneldesc, layout, false);
	        paneDescriptionView.setText(this.description);
	        paneDescriptionView.setPadding(0, 5, 0, 10);
	        layout.addView(paneDescriptionView);  
		}	
	}
}
