package com.lperilla.jmeter.plugin.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Utilities {
	
	private static final String ORG_APACHE_JMETER_RESOURCES_MESSAGES = "com.lperilla.jmeter.plugin.resources.messages";// $NON-NLS-1$

	private static volatile ResourceBundle resources;

	public static final String RES_KEY_PFX = "[res_key=%s]"; // $NON-NLS-1$

	//private static final Logger log = LoggingManager.getLoggerForClass();
	
	/**
	 * Gets the resource string for this key.
	 *
	 * If the resource is not found, a warning is logged
	 *
	 * @param key
	 *            the key in the resource file
	 * @return the resource string if the key is found; otherwise, return
	 *         "[res_key="+key+"]"
	 */
	public static String getResString(String key) {
		return getResStringDefault(key, String.format(RES_KEY_PFX, key), null); // $NON-NLS-1$
	}

	/**
	 * Gets the resource string for this key.
	 *
	 * If the resource is not found, a warning is logged
	 *
	 * @param key
	 *            the key in the resource file
	 * @return the resource string if the key is found; otherwise, return
	 *         "[res_key="+key+"]"
	 */
	public static String getResString(String key, Object... args) {
		return String.format(getResStringDefault(key, String.format(RES_KEY_PFX, key), null), args); 
	}	
	
	/*
	 * Helper method to do the actual work of fetching resources; allows
	 * getResString(S,S) to be deprecated without affecting getResString(S);
	 */
	private static String getResStringDefault(String key, String defaultValue, Locale forcedLocale) {
		if (key == null)
			return null;

		String resKey = key.replace(' ', '_'); // $NON-NLS-1$ // $NON-NLS-2$
		resKey = resKey.toLowerCase(java.util.Locale.ENGLISH);
		String resString = null;
		try {
			ResourceBundle bundle = getResources(forcedLocale);
			if (bundle.containsKey(resKey)) {
				resString = bundle.getString(resKey);
			}
			else {
				//log.warn("ERROR! Resource string not found: [" + resKey + "]");
				System.out.println("ERROR! Resource string not found: [" + resKey + "]");
				resString = defaultValue;
			}
		} catch (MissingResourceException mre) {
			System.out.println(mre.getMessage());
			//log.warn("ERROR! Resource string not found: [" + resKey + "]", mre);
			resString = defaultValue;
		}
		return resString;
	}

	public static ResourceBundle getResources(Locale forcedLocale) {
		if (Utilities.resources == null){
			if (forcedLocale != null){
				Utilities.resources = ResourceBundle.getBundle(ORG_APACHE_JMETER_RESOURCES_MESSAGES, forcedLocale); // $NON-NLS-1$
				
			}else{
				Utilities.resources = ResourceBundle.getBundle(ORG_APACHE_JMETER_RESOURCES_MESSAGES); // $NON-NLS-1$
			}
		}
		return Utilities.resources;
	}

}