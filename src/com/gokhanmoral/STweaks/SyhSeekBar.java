package com.gokhanmoral.STweaks;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public final class SyhSeekBar extends SyhControl implements OnSeekBarChangeListener{

	private static String LOG_TAG = Utils.class.getName();


	protected SyhSeekBar(Activity activityIn) {
		super(activityIn);
	}

	private SeekBar seekbar;
	private TextView seekBarValueText;
	private int maxInSteps;
	
	public String unit = "";
	public int min= 0;
	public int max = 0;
	public int step = 1;
	public boolean reversed = false;

	
	//TODO: reverse adjustment needed!
	//TODO: secondary progress needed!
	//TODO: Move to XML
	
	@Override
	public void createInternal() {
		
		//Assumption: valueFromScript is set correctly. 

		Integer val = 0;
		try
		{
			val = Integer.parseInt(valueFromScript);
		}
		catch(Exception e)
		{
			Log.e(LOG_TAG, "SyhSeekBar createInternal: valueFromScript cannot be converted!");
		}
		
		if (val < min)
		{
			val = min;
			valueFromScript = Integer.toString(min);
		}
		else if (val > max)
		{
			val = max;
			valueFromScript = Integer.toString(max);
		}
		valueFromUser = valueFromScript;
		
		maxInSteps = (max - min)/ step;
		
		//--Log.w(LOG_TAG, " max:" + Integer.toString(max) + " step:" + Integer.toString(step) + " maxInSteps:" + Integer.toString(maxInSteps));

		seekbar = new SeekBar(context);
		seekbar.setMax(maxInSteps);
		seekbar.setProgress( (val-min) /step);
		seekbar.setOnSeekBarChangeListener(this); // set listener.
		
		//--seekbar.setSecondaryProgress(max/2);//TODO: fix it
		
		applyScriptValueToUserInterface();
		
		controlLayout.addView(seekbar);
		
        //TODO: Move this to xml
		seekBarValueText = new TextView(context);
        seekBarValueText.setTextColor(Color.WHITE);
        seekBarValueText.setBackgroundColor(Color.BLACK);
        seekBarValueText.setText(valueFromUser + " " + unit);
        seekBarValueText.setGravity(Gravity.CENTER);
		controlLayout.addView(seekBarValueText);        
        
	}
	
	@Override
    public void onProgressChanged(SeekBar seekBar, int progress,
    		boolean fromUser) {
    	
		//-- Log.i(this.getClass().getName(), "min:" + min + " max:" + max + " seekMax:" + seekbar.getMax() + " progress:" + progress);
		int value = min + progress * step;
		valueFromUser = Integer.toString(value);
    	seekBarValueText.setText(valueFromUser + " " + unit);
		//--seekBarValueText.setText(progress + " " + unit);
    }

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
    	if (isChanged())
    	{
    		this.vci.valueChanged();
    	}
	}

	@Override
	protected void applyScriptValueToUserInterface() {
		if (seekbar != null)
		{
			Integer valueHardwareInt = Integer.parseInt(valueFromScript);
			Integer progress = (valueHardwareInt - min) / step;
			seekbar.setProgress(progress);
		}
		valueFromUser = valueFromScript;
	}

	
	@Override
	protected String getDefaultValue() {
		return Integer.toString(min);
	}

	
}
