package com.anipr.mosambe;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;

public class AppControler extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("appcontroler", "running");
		Parse.initialize(this, "54exU9DzVeENPnZNbZqpGU4zahjMSjtX4n1eBrdj", "9C2q7jmHzi4OsuNVH2uGWypGdWFiyo55MfHpqAAO"); 
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();
		TypefaceUtils.overrideFont(getApplicationContext(), "SERIF",
				"fonts/RobotoCondensed-Regular.ttf");
		
	}
}
 