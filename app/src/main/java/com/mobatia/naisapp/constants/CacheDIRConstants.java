/**
 * 
 */
package com.mobatia.naisapp.constants;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.exception.ExceptionHandler;

/**
 * @author Rijo K Jose
 * 
 */
public interface CacheDIRConstants {
	public String APP_DIR = "."
			+ ExceptionHandler.getContext().getString(R.string.app_name) + "/";

	public String HTMLFILE = "file://APP_DIR";

	public String WEB_CONTENT = APP_DIR + "WEBCONTENT/";

	public String WEB_URL = APP_DIR + "WEBURL/";

	public String VIDEOS_DIR = APP_DIR + "VIDEOS";

	public String PHOTOS_LIST_DIR = APP_DIR + "PHOTOS_LIST";

	public String PHOTOS_CAT_LIST_DIR = APP_DIR + "PHOTOS_CAT_LIST";

	public String PHOTOS_GRID_DIR = APP_DIR + "PHOTOS_GRID";

	public String NEWS_LIST_DIR = APP_DIR + "NEWS_LIST";

	public String MENU_LIST_DIR = APP_DIR + "MENU_LIST";

	public String MENU_ITEM_LIST_DIR = APP_DIR + "MENU_ITEM_LIST";

	public String MENU_ITEM_CHECKOUT_DIR = APP_DIR + "MENU_ITEM_CHECKOUT";

	public String LOC_LIST_DIR = APP_DIR + "LOC_LIST";

	public String FOOD_LIST_DIR = APP_DIR + "FOOD_LIST";

	public String FOOD_ITEM_LIST_DIR = APP_DIR + "FOOD_ITEM_LIST";

	public String SOCIAL_LIST_DIR = APP_DIR + "SOCIAL_LIST";

	public String DIRECTORY_CAT_LIST_DIR = APP_DIR + "DIRECTORY_CAT_LIST";

	public String DIRECTORY_DEPT_LIST_DIR = APP_DIR + "DIRECTORY_DEPT_LIST";

	public String DIRECTORY_STAFF_PROFILE_DIR = APP_DIR
			+ "DIRECTORY_STAFF_PROFILE";

	public String GALLERY_DIR = APP_DIR + "GALLERY";

	public String ABOUT_US_DIR = APP_DIR + "ABOUTUS";

	public String FACILITIES_DIR = APP_DIR + "FACILITIES";

	public String CALENDAR_CAT_DIR = APP_DIR + "CALENDAR_CAT";

	public String CALENDAR_DATA_DIR = APP_DIR + "CALENDAR_DATA_DIR";

	public String WISS_UP_DIR = APP_DIR + "WISS_UP";

	public String SETTINGS_CACHE = APP_DIR + "SETTINGS/";

	public String ACADAMIES_DIR = APP_DIR + "ACADAMIES";

}
