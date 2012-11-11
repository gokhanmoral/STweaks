package com.gokhanmoral.stweaks.app;

import com.gokhanmoral.stweaks.app.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class SyhCheckBox extends SyhControl implements OnClickListener{

	protected SyhCheckBox(Activity activityIn) {
		super(activityIn);
	}

	private CheckBox checkBox;
	public String label;
	
	@Override
	public void createInternal() {		
		
		//Assumption: valueFromScript is set correctly. 

/*
 		//OK: Move this to xml
		checkBox = new CheckBox(context);
		checkBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		checkBox.setTextColor(Color.WHITE);
		checkBox.setGravity(Gravity.CENTER);
		checkBox.setText(label);
		checkBox.setChecked(false);  
		checkBox.setOnClickListener(this);
		controlLayout.addView(checkBox);
*/
		//create CheckBox from xml template
		//View temp = LayoutInflater.from(context).inflate(R.layout.template_checkbox, controlLayout, false);
		//checkBox = (CheckBox) temp.findViewById(R.id.SyhCheckBox);
		checkBox = (CheckBox)LayoutInflater.from(context).inflate(R.layout.template_checkbox, controlLayout, false);
		checkBox.setText(label);
		checkBox.setOnClickListener(this);
		
		//--checkBox.setChecked(convertFromScriptFormatToControlFormat(valueFromScript));  
		applyScriptValueToUserInterface();
		
		controlLayout.addView(checkBox);
	}

	@Override
	public void onClick(View v) {
		//-- This not true >>>  this.valueInput = Boolean.toString(checkBox.isChecked());
		this.valueFromUser = convertFromControlFormatToScriptFormat(checkBox.isChecked());
		this.vci.valueChanged();
	}

	@Override
	protected void applyScriptValueToUserInterface() {
		//-- This not true >>> boolean hardware = Boolean.parseBoolean(this.valueHardware);

		if (checkBox != null)
		{
			boolean hardware = convertFromScriptFormatToControlFormat(valueFromScript);
			checkBox.setChecked(hardware);
		}		
		valueFromUser = valueFromScript;
	}

	protected Boolean convertFromScriptFormatToControlFormat(String input) {
		boolean hardware = input.equals("on");
		return hardware;
	}

	protected String convertFromControlFormatToScriptFormat(Boolean input) {
		String scriptVal = (input) ? ("on"): ("off");
		return scriptVal;
	}

	
	@Override
	protected String getDefaultValue() {
		return "off";
	}


}
