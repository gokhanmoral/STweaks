package com.gokhanmoral.stweaks.app;

import com.gokhanmoral.stweaks.app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SyhExtrasTab extends SyhTab implements OnClickListener {
     public SyhExtrasTab(Context context, Activity activity) {
		super(context, activity);
		this.name = "Extras";
	}

	@Override
	public View getCustomView(ViewGroup parent)
	{
 		 View v = LayoutInflater.from(mContext).inflate(R.layout.syh_extrastab, parent, false);
     	 
    	 final TextView tv = (TextView) v.findViewById(R.id.textViewAppVersion);
 		 try 
 		 {
 	    	final String appVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
			tv.setText("App Version: " + appVersion);
 		 } 
 		 catch (NameNotFoundException e) 
 		 {
			tv.setText("App Version: Not found!");
 		 }
 		 
 		 
 		 final Button button2 = (Button) v.findViewById(R.id.FlashKernel);
 		 button2.setOnClickListener(this);
     	 final Button button4 = (Button) v.findViewById(R.id.ResetSettings);
 		 button4.setOnClickListener(this);
  		 
    	 final TextView tv2 = (TextView) v.findViewById(R.id.textViewKernelVersion);
    	 tv2.setText("Kernel version: " + System.getProperty("os.version"));

    	 String s = "";
    	 s += "\n Kernel Version: " + System.getProperty("os.version");
    	 s += "\n ROM Version: " + android.os.Build.VERSION.INCREMENTAL;
       	 s += "\n ROM API Level: " + android.os.Build.VERSION.SDK_INT;
       	 s += "\n ROM Codename: " + android.os.Build.VERSION.CODENAME;
       	 s += "\n ROM Release Version: " + android.os.Build.VERSION.RELEASE;
       	 s += "\n Hardware Serial: " + android.os.Build.SERIAL;
      	 s += "\n Radio Version: " + android.os.Build.getRadioVersion();
      	 //s += "\n Device: " + android.os.Build.DEVICE;
    	 //s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";   
    	 tv2.setText(s);
    	 		 
    	 
 		 return v;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
        case R.id.FlashKernel:
           	Toast toast1 = Toast.makeText(mContext, R.string.coming_soon, Toast.LENGTH_LONG);
        	toast1.show();  
            break;
        case R.id.ResetSettings:
        	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        	builder.setMessage("All settings will be reset. You will have to relaunch the application.")
        	       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	               // Handle Ok
        	           		Utils.executeRootCommandInThread("/res/uci.sh delete default");
        	           		System.exit(0);
        	           }
        	       })
        	       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	               // Handle Cancel
        	           }
        	       })
        	       .setTitle("Warning")
        	       .setIcon(R.drawable.ic_launcher)
        	       .create();
        	builder.show();

        	break;
		}		
	}
}
