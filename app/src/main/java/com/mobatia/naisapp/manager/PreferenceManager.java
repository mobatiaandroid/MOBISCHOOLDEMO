/**
 *
 */
package com.mobatia.naisapp.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.constants.NaisTabConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * @author Rijo K Jose
 *
 */
public class PreferenceManager implements NaisTabConstants {
    public static String SHARED_PREF_NAS = "NAS";

    public static void setIsFirstLaunch(Context context, boolean result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_first_launch", result);
        editor.commit();
    }

    /*******************************************************
     * Method name : getIsFirstLaunch() Description : get if app is launching
     * for first time Parameters : context Return type : boolean Date :
     * 11-Dec-2014 Author : Rijo K Jose
     *****************************************************/
    public static boolean getIsFirstLaunch(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getBoolean("is_first_launch", true);
    }
    public static void saveArrayList(ArrayList<String> list, String key,Context context){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public static ArrayList<String> getArrayList(String key,Context context){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static void setCalendarEventNames(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cal_event", result);
        editor.commit();
    }

    /*******************************************************
     * Method name : getCalendarEventNames() Description : get calendar event
     * names Parameters : context Return type : String Date : 23-Jan-2015 Author
     * : Rijo K Jose
     *****************************************************/
    public static String getCalendarEventNames(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("cal_event", "");
    }
    public static void setSportsCalendarEventNames(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cal_sports_event", result);
        editor.commit();
    }

    /*******************************************************
     * Method name : getCalendarEventNames() Description : get calendar event
     * names Parameters : context Return type : String Date : 23-Jan-2015 Author
     * : Rijo K Jose
     *****************************************************/
    public static String getSportsCalendarEventNames(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("cal_sports_event", "");
    }
    public static void setTeamsCalendarEventNames(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cal_teams_event", result);
        editor.commit();
    }

    /*******************************************************
     * Method name : getCalendarEventNames() Description : get calendar event
     * names Parameters : context Return type : String Date : 23-Jan-2015 Author
     * : Rijo K Jose
     *****************************************************/
    public static String getTeamsCalendarEventNames(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("cal_teams_event", "");
    }
    //save access token
    public static void setAccessToken(Context mContext, String accesstoken) {
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREF_NAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("access_token", accesstoken);
        editor.commit();
    }

    //get accesstoken
    public static String getAccessToken(Context mContext) {
        String tokenValue = "";
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        tokenValue = prefs.getString("access_token", "0");
        return tokenValue;
    }

    public static String getUserName(Context context) {
        String userName = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        userName = prefs.getString("username", "");
        return userName;
    }

    /*******************************************************
     * Method name : setUserName Description : get user name after login
     * Parameters : context, username Return type : void Date : Oct 28, 2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setUserName(Context context, String userName) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", userName);
        editor.commit();
    }

    public static int getButtonOneBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_one_color_v3",
                context.getResources().getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonOneBg() Description : set home button one bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonOneBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_one_color_v3", color);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonOneTextImage() Description : get home button one
     * text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonOneTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_one_pos_v3", "1");//21

    }

    /*******************************************************
     * Method name : setButtonOneTextImage() Description : set home button one
     * text and image Parameters : context, position Return type : void Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonOneTextImage(Context context, String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_one_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonOneTabId() Description : get home button one tab
     * id Parameters : context Return type : String Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static String getButtonOneTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_one_tab_v3", TAB_CALENDAR_REG);
    }

    /*******************************************************
     * Method name : getButtonOneTabId() Description : set home button one tab
     * id Parameters : context, TAB_ID Return type : void Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonOneTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_one_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonTwoBg() Description : get home button two bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonTwoBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_two_color_v3",
                context.getResources().getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonTwoBg() Description : set home button two bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonTwoBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_two_color_v3", color);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonTwoTextImage() Description : get home button two
     * text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonTwoTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_two_pos_v3", "10");

    }

    /*******************************************************
     * Method name : setButtonTwoTextImage() Description : set home button two
     * text and image Parameters : context, position Return type : void Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonTwoTextImage(Context context, String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_two_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonTwoTabId() Description : get home button two tab
     * id Parameters : context Return type : String Date : 25-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static String getButtonTwoTabId(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_two_tab_v3", "10");
    }

    /*******************************************************
     * Method name : setButtonTwoTabId() Description : set home button two tab
     * id Parameters : context, TAB_ID Return type : void Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonTwoTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_two_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonThreeBg() Description : get home button three bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonThreeBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_three_color_v3",
                context.getResources().getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonThreeBg() Description : set home button three bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonThreeBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_three_color_v3", color);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonThreeTextImage() Description : get home button
     * three text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonThreeTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_three_pos_v3", "21");//19

    }

    /*******************************************************
     * Method name : setButtonThreeTextImage() Description : set home button
     * three text and image Parameters : context, position Return type : void
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonThreeTextImage(Context context, String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_three_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonThreeTabId() Description : get home button three
     * tab id Parameters : context Return type : String Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static String getButtonThreeTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_three_tab_v3", "21");
    }

    /*******************************************************
     * Method name : setButtonThreeTabId() Description : set home button three
     * tab id Parameters : context, TAB_ID Return type : void Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonThreeTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_three_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFourBg() Description : get home button four bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonFourBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_four_color_v3",
                context.getResources().getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonFourBg() Description : set home button four bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonFourBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_four_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFourTextImage() Description : get home button four
     * text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonFourTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_four_pos_v3", "14");

    }

    /*******************************************************
     * Method name : setButtonFourTextImage() Description : set home button four
     * text and image Parameters : context, position Return type : void Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFourTextImage(Context context, String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_four_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonFourTabId() Description : get home button four tab
     * id Parameters : context Return type : String Date : 25-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static String getButtonFourTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_four_tab_v3", "14");
    }

    /*******************************************************
     * Method name : setButtonFourTabId() Description : set home button four tab
     * id Parameters : context, TAB_ID Return type : void Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFourTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_four_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFiveBg() Description : get home button five bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonFiveBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_five_color_v3",
                context.getResources().getColor(R.color.rel_five));

    }

    /*******************************************************
     * Method name : setButtonFiveBg() Description : set home button five bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonFiveBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_five_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFiveTextImage() Description : get home button five
     * text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonFiveTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_five_pos_v3", "8");

    }

    /*******************************************************
     * Method name : setButtonFiveTextImage() Description : set home button five
     * text and image Parameters : context, position Return type : void Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFiveTextImage(Context context, String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_five_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonFiveTabId() Description : get home button five tab
     * id Parameters : context Return type : String Date : 25-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static String getButtonFiveTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_five_tab_v3", "8");
    }

    /*******************************************************
     * Method name : setButtonFiveTabId() Description : set home button five tab
     * id Parameters : context, TAB_ID Return type : void Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFiveTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_five_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSixBg() Description : get home button six bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonSixBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_six_color_v3",
                context.getResources().getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonSixBg() Description : set home button six bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonSixBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_six_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSixImage() Description : get home button six text
     * and image Parameters : context Return type : String Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSixTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_six_pos_v3", "9");

    }

    /*******************************************************
     * Method name : setButtonSixTextImage() Description : set home button six
     * text and image Parameters : context, position Return type : void Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSixTextImage(Context context, String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_six_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonSixTabId() Description : get home button six tab
     * id Parameters : context Return type : String Date : 25-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static String getButtonSixTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_six_tab_v3", "9");
    }

    /*******************************************************
     * Method name : setButtonSixTabId() Description : set home button six tab
     * id Parameters : context, TAB_ID Return type : void Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSixTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_six_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSevenBg() Description : get home button seven bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonSevenBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_seven_color_v3",
                context.getResources().getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonSevenBg() Description : set home button seven bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonSevenBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_seven_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSevenTextImage() Description : get home button
     * seven text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSevenTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_seven_pos_v3", "3");

    }

    /*******************************************************
     * Method name : setButtonSevenTextImage() Description : set home button
     * seven text and image Parameters : context, position Return type : void
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSevenTextImage(Context context, String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_seven_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonSevenTabId() Description : get home button seven
     * tab id Parameters : context Return type : String Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSevenTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_seven_tab_v3", TAB_COMMUNICATIONS_REG);
    }

    /*******************************************************
     * Method name : setButtonSevenTabId() Description : set home button seven
     * tab id Parameters : context, TAB_ID Return type : void Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSevenTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_seven_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonEightBg() Description : get home button eight bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonEightBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_eight_color_v3",
                context.getResources().getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonEightBg() Description : set home button eight bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonEightBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_eight_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonEightTextImage() Description : get home button
     * eight text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonEightTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_eight_pos_v3", "22");//19

    }

    /*******************************************************
     * Method name : setButtonEightTextImage() Description : set home button
     * eight text and image Parameters : context, position Return type : void
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonEightTextImage(Context context, String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_eight_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonEightTabId() Description : get home button eight
     * tab id Parameters : context Return type : String Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static String getButtonEightTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_eight_tab_v3", "22");
    }

    /*******************************************************
     * Method name : setButtonEightTabId() Description : set home button eight
     * tab id Parameters : context, TAB_ID Return type : void Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonEightTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_eight_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonNineBg() Description : get home button nine bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonNineBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_nine_color_v3",
                context.getResources().getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonNineBg() Description : set home button nine bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonNineBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_nine_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonNineTextImage() Description : get home button nine
     * text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonNineTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_nine_pos_v3", "13");

    }

    /*******************************************************
     * Method name : setButtonNineTextImage() Description : set home button nine
     * text and image Parameters : context, position Return type : void Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonNineTextImage(Context context, String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_nine_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonNineTabId() Description : get home button nine tab
     * id Parameters : context Return type : String Date : 25-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static String getButtonNineTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_nine_tab_v3", "13");
    }

    /*******************************************************
     * Method name : setButtonNineTabId() Description : set home button nine tab
     * id Parameters : context, TAB_ID Return type : void Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonNineTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_nine_tab_v3", TAB_ID);
        editor.commit();
    }

    public static boolean getIfHomeItemClickEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getBoolean("home_item_click", true);

    }

    /*******************************************************
     * Method name : setIfHomeItemClickEnabled() Description : set if home list
     * item click is enabled Parameters : context, result Return type : void
     * Date : 11-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setIfHomeItemClickEnabled(Context context, boolean result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("home_item_click", result);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonOneGuestBg() Description : get home button one bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonOneGuestBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_one_guest_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonOneGuestBg() Description : set home button one bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonOneGuestBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_one_guest_color_v3", color);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonOneGuestTextImage() Description : get home button
     * one text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonOneGuestTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_one_guest_pos_v3", "6");

    }

    /*******************************************************
     * Method name : setButtonOneGuestTextImage() Description : set home button
     * one text and image Parameters : context, position Return type : void Date
     * : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonOneGuestTextImage(Context context,
                                                  String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_one_guest_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonOneGuestTabId() Description : get home button one
     * guest tab id Parameters : context Return type : String Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static String getButtonOneGuestTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_one_guest_tab_v3", TAB_IB_PROGRAMME_GUEST);
    }

    /*******************************************************
     * Method name : setButtonOneGuestTabId() Description : set home button one
     * guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonOneGuestTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_one_guest_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonTwoGuestBg() Description : get home button two bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonTwoGuestBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_two_guest_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonTwoGuestBg() Description : set home button two bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonTwoGuestBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_two_guest_color_v3", color);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonTwoGuestTextImage() Description : get home button
     * two text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonTwoGuestTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_two_guest_pos_v3", "5");

    }

    /*******************************************************
     * Method name : setButtonTwoGuestTextImage() Description : set home button
     * two text and image Parameters : context, position Return type : void Date
     * : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonTwoGuestTextImage(Context context,
                                                  String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_two_guest_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonTwoGuestTabId() Description : get home button two
     * guest tab id Parameters : context Return type : String Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static String getButtonTwoGuestTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_two_guest_tab_v3", "5");
    }

    /*******************************************************
     * Method name : setButtonTwoGuestTabId() Description : set home button two
     * guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonTwoGuestTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_two_guest_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonThreeGuestBg() Description : get home button three
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonThreeGuestBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_three_guest_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonThreeGuestBg() Description : set home button three
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonThreeGuestBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_three_guest_color_v3", color);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonThreeGuestTextImage() Description : get home
     * button three text and image Parameters : context Return type : String
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonThreeGuestTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_three_guest_pos_v3", "9");

    }

    /*******************************************************
     * Method name : setButtonThreeGuestTextImage() Description : set home
     * button three text and image Parameters : context, position Return type :
     * void Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonThreeGuestTextImage(Context context,
                                                    String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_three_guest_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonThreeGuestTabId() Description : get home button
     * three guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonThreeGuestTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_three_guest_tab_v3", TAB_ABOUT_US_GUEST);
    }

    /*******************************************************
     * Method name : setButtonThreeGuestTabId() Description : set home button
     * three guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonThreeGuestTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_three_guest_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFourGuestBg() Description : get home button four
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonFourGuestBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_four_guest_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonFourGuestBg() Description : set home button four
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFourGuestBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_four_guest_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFourGuestTextImage() Description : get home button
     * four text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonFourGuestTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_four_guest_pos_v3", "8");

    }

    /*******************************************************
     * Method name : setButtonFourGuestTextImage() Description : set home button
     * four text and image Parameters : context, position Return type : void
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFourGuestTextImage(Context context,
                                                   String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_four_guest_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonFourGuestTabId() Description : get home button
     * four guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonFourGuestTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_four_guest_tab_v3", TAB_NAE_PROGRAMMES_GUEST);
    }

    /*******************************************************
     * Method name : setButtonFourGuestTabId() Description : set home button
     * four guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFourGuestTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_four_guest_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFiveGuestBg() Description : get home button five
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonFiveGuestBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_five_guest_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonFiveGuestBg() Description : set home button five
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFiveGuestBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_five_guest_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFiveGuestTextImage() Description : get home button
     * five text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonFiveGuestTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_five_guest_pos_v3", "3");

    }

    /*******************************************************
     * Method name : setButtonFiveGuestTextImage() Description : set home button
     * five text and image Parameters : context, position Return type : void
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFiveGuestTextImage(Context context,
                                                   String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_five_guest_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonFiveGuestTabId() Description : get home button
     * five guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonFiveGuestTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_five_guest_tab_v3", "3");
    }

    /*******************************************************
     * Method name : setButtonFiveGuestTabId() Description : set home button
     * five guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFiveGuestTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_five_guest_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSixGuestBg() Description : get home button six bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonSixGuestBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_six_guest_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonSixGuestBg() Description : set home button six bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonSixGuestBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_six_guest_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSixGuestTextImage() Description : get home button
     * six text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSixGuestTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_six_guest_pos_v3", "4");

    }

    /*******************************************************
     * Method name : setButtonSixGuestTextImage() Description : set home button
     * six text and image Parameters : context, position Return type : void Date
     * : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSixGuestTextImage(Context context,
                                                  String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_six_guest_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonSixGuestTabId() Description : get home button six
     * guest tab id Parameters : context Return type : String Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSixGuestTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_six_guest_tab_v3", "4");
    }

    /*******************************************************
     * Method name : setButtonSixGuestTabId() Description : set home button six
     * guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSixGuestTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_six_guest_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSevenGuestBg() Description : get home button seven
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonSevenGuestBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_seven_guest_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonSevenGuestBg() Description : set home button seven
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSevenGuestBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_seven_guest_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSevenGuestTextImage() Description : get home
     * button seven text and image Parameters : context Return type : String
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSevenGuestTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_seven_guest_pos_v3", "1");

    }

    /*******************************************************
     * Method name : setButtonSevenGuestTextImage() Description : set home
     * button seven text and image Parameters : context, position Return type :
     * void Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSevenGuestTextImage(Context context,
                                                    String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_seven_guest_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonSevenGuestTabId() Description : get home button
     * seven guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSevenGuestTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_seven_guest_tab_v3", TAB_COMMUNICATIONS_GUEST);
    }

    /*******************************************************
     * Method name : setButtonSixGuestTabId() Description : set home button
     * seven guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSevenGuestTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_seven_guest_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonEightGuestBg() Description : get home button eight
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonEightGuestBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_eight_guest_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonEightGuestBg() Description : set home button eight
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonEightGuestBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_eight_guest_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonEightGuestTextImage() Description : get home
     * button eight text and image Parameters : context Return type : String
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonEightGuestTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_eight_guest_pos_v3", "10");//TAB_ABOUT_US

    }

    /*******************************************************
     * Method name : setButtonEightGuestTextImage() Description : set home
     * button eight text and image Parameters : context, position Return type :
     * void Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonEightGuestTextImage(Context context,
                                                    String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_eight_guest_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonEightGuestTabId() Description : get home button
     * eight guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonEightGuestTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_eight_guest_tab_v3", "10");
    }

    /*******************************************************
     * Method name : setButtonEightGuestTabId() Description : set home button
     * eight guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonEightGuestTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_eight_guest_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonNineGuestBg() Description : get home button nine
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonNineGuestBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_nine_guest_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonNineGuestBg() Description : set home button nine
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonNineGuestBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_nine_guest_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonNineGuestTextImage() Description : get home button
     * nine text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonNineGuestTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_nine_guest_pos_v3", "7");

    }

    /*******************************************************
     * Method name : setButtonNineGuestTextImage() Description : set home button
     * nine text and image Parameters : context, position Return type : void
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonNineGuestTextImage(Context context,
                                                   String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_nine_guest_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonNineGuestTabId() Description : get home button
     * nine guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonNineGuestTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_nine_guest_tab_v3", TAB_PERFORMING_ARTS_GUEST);
    }

    /*******************************************************
     * Method name : setButtonNineGuestTabId() Description : set home button
     * nine guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonNineGuestTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_nine_guest_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getCacheClearStatusForShop() Description : get cache clear
     * status for shop Parameters : context Return type : boolean Date :
     * 24-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static boolean getCacheClearStatusForShop(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getBoolean("shop_clear_cache", true);

    }

    public static void setFireBaseId(Context context, String id) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("firebase id", id);
        editor.commit();
    }

    public static String getFireBaseId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("firebase id", "0");

    }

    public static String getUserId(Context context) {
        String userid = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        userid = prefs.getString("userid", "");
        return userid;
    }

    /*******************************************************
     * Method name : setUserName Description : get user name after login
     * Parameters : context, username Return type : void Date : Oct 28, 2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setStaffId(Context context, String staffId) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("staffid", staffId);
        editor.commit();
    }
    public static String getStaffId(Context context) {
        String staffId = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        staffId = prefs.getString("staffid", "");
        return staffId;
    }

    /*******************************************************
     * Method name : setUserName Description : get user name after login
     * Parameters : context, username Return type : void Date : Oct 28, 2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setUserId(Context context, String userid) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userid", userid);
        editor.commit();
    }

    public static String getUserType(Context context) {
        String userType = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        userType = prefs.getString("user_type", "");
        return userType;
    }

    /*******************************************************
     * Method name : setUserName Description : get user name after login
     * Parameters : context, username Return type : void Date : Oct 28, 2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setUserType(Context context, String userType) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_type", userType);
        editor.commit();
    }

    public static String getUserEmail(Context context) {
        String userid = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        userid = prefs.getString("user_email", "");
        return userid;
    }
    public static void setLeaveStudentId(Context context, String mLeaveStudentId) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LeaveStudentId", mLeaveStudentId);
        editor.commit();
    }

    public static String getLeaveStudentId(Context context) {
        String mLeaveStudentId = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        mLeaveStudentId = prefs.getString("LeaveStudentId", "");
        return mLeaveStudentId;
    }
    public static void setLeaveStudentName(Context context, String mLeaveStudentName) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LeaveStudentName", mLeaveStudentName);
        editor.commit();
    }

    public static String getLeaveStudentName(Context context) {
        String mLeaveStudentName = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        mLeaveStudentName = prefs.getString("LeaveStudentName", "");
        return mLeaveStudentName;
    }
    /*******************************************************
     * Method name : setUserName Description : get user name after login
     * Parameters : context, username Return type : void Date : Oct 28, 2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setUserEmail(Context context, String userid) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_email", userid);
        editor.commit();
    }

    public static void setIsFirstTimeInPA(Context context, boolean result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_first_pa", result);
        editor.commit();
    }

    /*******************************************************
     * Method name : getIsFirstLaunch() Description : get if app is launching
     * for first time Parameters : context Return type : boolean Date :
     * 11-Dec-2014 Author : Rijo K Jose
     *****************************************************/
    public static boolean getIsFirstTimeInPA(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getBoolean("is_first_pa", true);
    }

    public static void setIsFirstTimeInPE(Context context, boolean result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_first_pe", result);
        editor.commit();
    }

    /*******************************************************
     * Method name : getIsFirstLaunch() Description : get if app is launching
     * for first time Parameters : context Return type : boolean Date :
     * 11-Dec-2014 Author : Rijo K Jose
     *****************************************************/
    public static boolean getIsFirstTimeInPE(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getBoolean("is_first_pe", true);
    }

    public static void setBackParent(Context context, boolean result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("BackParent", result);
        editor.commit();
    }

    /*******************************************************
     * Method name : getIsFirstLaunch() Description : get if app is launching
     * for first time Parameters : context Return type : boolean Date :
     * 11-Dec-2014 Author : Rijo K Jose
     *****************************************************/
    public static boolean getIBackParent(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getBoolean("BackParent", true);
    }

    public static void setIsFirstTimeInCalendar(Context context, boolean result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_first_calendar", result);
        editor.commit();
    }

    /*******************************************************
     * Method name : getIsFirstLaunch() Description : get if app is launching
     * for first time Parameters : context Return type : boolean Date :
     * 11-Dec-2014 Author : Rijo K Jose
     *****************************************************/
    public static boolean getIsFirstTimeInCalendar(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getBoolean("is_first_calendar", true);
    }

    public static void setStudIdForCCA(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("StudIdForCCA", result);
        editor.commit();
    }

    //getStudIdForCCA
    public static String getStudIdForCCA(Context context) {
        String StudIdForCCA = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        StudIdForCCA = prefs.getString("StudIdForCCA", "");
        return StudIdForCCA;
    }


    public static void setStudNameForCCA(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("StudNameForCCA", result);
        editor.commit();
    }

    public static String getStudNameForCCA(Context context) {
        String StudNameForCCA = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        StudNameForCCA = prefs.getString("StudNameForCCA", "");
        return StudNameForCCA;
    }

    public static void setStudClassForCCA(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("StudClassForCCA", result);
        editor.commit();
    }

    public static String getStudClassForCCA(Context context) {
        String StudClassForCCA = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        StudClassForCCA = prefs.getString("StudClassForCCA", "");
        return StudClassForCCA;
    }

    public static void setCCATitle(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CCATitle", result);
        editor.commit();
    }

    public static String getCCATitle(Context context) {
        String CCATitle = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        CCATitle = prefs.getString("CCATitle", "");
        return CCATitle;
    }


    public static void setCCAItemId(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CCAItemId", result);
        editor.commit();
    }

    public static String getCCAItemId(Context context) {
        String CCAItemId = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        CCAItemId = prefs.getString("CCAItemId", "");
        return CCAItemId;
    }


    public static void setCCAStudentIdPosition(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CCAStudentIdPosition", result);
        editor.commit();
    }

    public static String getCCAStudentIdPosition(Context context) {
        String CCAStudentIdPosition = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        CCAStudentIdPosition = prefs.getString("CCAStudentIdPosition", "");
        return CCAStudentIdPosition;
    }
    public static void setGoToSettings(Context context, String mGoToSetting) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("GoToSetting", mGoToSetting);
        editor.commit();
    }

    public static String getGoToSettings(Context context) {
        String mGoToSetting = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        mGoToSetting = prefs.getString("GoToSetting", "0");
        return mGoToSetting;
    }


    public static void setVersionFromApi(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("version_api", result);
        editor.commit();
    }

    public static String getVersionFromApi(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("version_api", "");
    }
        /********************badge***************/

        public static void setCalendarBadge(Context context, String calendar_badge) {
            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("calendar_badge", calendar_badge);
            editor.commit();
        }
        public static String getCalendarBadge(Context context) {
            String calendar_badge = "";
            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                    Context.MODE_PRIVATE);
            calendar_badge = prefs.getString("calendar_badge", "0");
            return calendar_badge;
        }
    public static void setCalendarEditedBadge(Context context, String calendar_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("calendar_edited_badge", calendar_edited_badge);
        editor.commit();
    }
    public static String getCalendarEditedBadge(Context context) {
        String calendar_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        calendar_edited_badge = prefs.getString("calendar_edited_badge", "0");
        return calendar_edited_badge;
    }
    public static void setNotificationBadge(Context context, String notification_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("notification_badge", notification_badge);
        editor.commit();
    }
    public static String getNotificationBadge(Context context) {
        String notification_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        notification_badge = prefs.getString("notification_badge", "0");
        return notification_badge;
    }
    public static void setNotificationEditedBadge(Context context, String notification_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("notification_edited_badge", notification_edited_badge);
        editor.commit();
    }
    public static String getNotificationEditedBadge(Context context) {
        String notification_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        notification_edited_badge = prefs.getString("notification_edited_badge", "0");
        return notification_edited_badge;
    }
    public static void setNoticeBadge(Context context, String whole_school_coming_up_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("whole_school_coming_up_badge", whole_school_coming_up_badge);
        editor.commit();
    }
    public static String getNoticeBadge(Context context) {
        String whole_school_coming_up_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        whole_school_coming_up_badge = prefs.getString("whole_school_coming_up_badge", "0");
        return whole_school_coming_up_badge;
    }
    public static void setNoticeEditedBadge(Context context, String whole_school_coming_up_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("whole_school_coming_up_edited_badge", whole_school_coming_up_edited_badge);
        editor.commit();
    }
    public static String getNoticeEditedBadge(Context context) {
        String whole_school_coming_up_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        whole_school_coming_up_edited_badge = prefs.getString("whole_school_coming_up_edited_badge", "0");
        return whole_school_coming_up_edited_badge;
    }public static void setPaymentitem_badge(Context context, String paymentitem_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("paymentitem_badge", paymentitem_badge);
        editor.commit();
    }
    public static String getPaymentitem_badge(Context context) {
        String paymentitem_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        paymentitem_badge = prefs.getString("paymentitem_badge", "0");
        return paymentitem_badge;
    }
    public static void setPaymentitem_edit_badge(Context context, String paymentitem_edit_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("paymentitem_edit_badge", paymentitem_edit_badge);
        editor.commit();
    }
    public static String getPaymentitem_edit_badge(Context context) {
        String paymentitem_edit_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        paymentitem_edit_badge = prefs.getString("paymentitem_edit_badge", "0");
        return paymentitem_edit_badge;
    }
    public static void setSportsFixtureBadge(Context context, String sports_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sports_fixture_badge", sports_badge);
        editor.commit();
    }
    public static String getSportsFixtureBadge(Context context) {
        String sports_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        sports_badge = prefs.getString("sports_fixture_badge", "0");
        return sports_badge;
    }
    public static void setSportsEditedFixtureBadge(Context context, String sports_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sports_edited_fixture_badge", sports_edited_badge);
        editor.commit();
    }
    public static String getSportsEditedFixtureBadge(Context context) {
        String sports_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        sports_edited_badge = prefs.getString("sports_edited_fixture_badge", "0");
        return sports_edited_badge;
    }
    public static void setSportsCalendarBadge(Context context, String sports_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sports_calendar_badge", sports_badge);
        editor.commit();
    }
    public static String getSportsCalendarBadge(Context context) {
        String sports_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        sports_badge = prefs.getString("sports_calendar_badge", "0");
        return sports_badge;
    }
    public static void setSportsEditedCalendarBadge(Context context, String sports_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sports_edited_calendar_badge", sports_edited_badge);
        editor.commit();
    }
    public static String getSportsEditedCalendarBadge(Context context) {
        String sports_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        sports_edited_badge = prefs.getString("sports_edited_calendar_badge", "0");
        return sports_edited_badge;
    }
    public static void setSportsInformationBadge(Context context, String sports_information_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sports_information_badge", sports_information_badge);
        editor.commit();
    }
    public static String getSportsInformationBadge(Context context) {
        String sports_information_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        sports_information_badge = prefs.getString("sports_information_badge", "0");
        return sports_information_badge;
    }
    public static void setSportsEditedInformationBadge(Context context, String sports_edited_information_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sports_edited_information_badge", sports_edited_information_badge);
        editor.commit();
    }
    public static String getSportsEditedInformationBadge(Context context) {
        String sports_edited_information_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        sports_edited_information_badge = prefs.getString("sports_edited_information_badge", "0");
        return sports_edited_information_badge;
    }
    public static void setSportsBadge(Context context, String sports_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sports_badge", sports_badge);
        editor.commit();
    }
    public static String getSportsBadge(Context context) {
        String sports_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        sports_badge = prefs.getString("sports_badge", "0");
        return sports_badge;
    }
    public static void setSportsEditedBadge(Context context, String sports_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sports_edited_badge", sports_edited_badge);
        editor.commit();
    }
    public static String getSportsEditedBadge(Context context) {
        String sports_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        sports_edited_badge = prefs.getString("sports_edited_badge", "0");
        return sports_edited_badge;
    }
    public static void setReportsBadge(Context context, String reports_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("reports_badge", reports_badge);
        editor.commit();
    }
    public static String getReportsBadge(Context context) {
        String reports_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        reports_badge = prefs.getString("reports_badge", "0");
        return reports_badge;
    }
    public static void setReportsEditedBadge(Context context, String reports_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("reports_edited_badge", reports_edited_badge);
        editor.commit();
    }
    public static String getReportsEditedBadge(Context context) {
        String reports_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        reports_edited_badge = prefs.getString("reports_edited_badge", "0");
        return reports_edited_badge;
    }
    public static void setCcaBadge(Context context, String cca_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cca_badge", cca_badge);
        editor.commit();
    }
    public static String getCcaBadge(Context context) {
        String cca_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        cca_badge = prefs.getString("cca_badge", "0");
        return cca_badge;
    }
    public static void setCcaEditedBadge(Context context, String cca_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cca_edited_badge", cca_edited_badge);
        editor.commit();
    }
    public static String getCcaEditedBadge(Context context) {
        String cca_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        cca_edited_badge = prefs.getString("cca_edited_badge", "0");
        return cca_edited_badge;
    }
    public static void setCcaOptionBadge(Context context, String cca_option_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cca_option_badge", cca_option_badge);
        editor.commit();
    }
    public static String getCcaOptionBadge(Context context) {
        String cca_option_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        cca_option_badge = prefs.getString("cca_option_badge", "0");
        return cca_option_badge;
    }
    public static void setCcaOptionEditedBadge(Context context, String cca_option_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cca_option_edited_badge", cca_option_edited_badge);
        editor.commit();
    }
    public static String getCcaOptionEditedBadge(Context context) {
        String cca_option_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        cca_option_edited_badge = prefs.getString("cca_option_edited_badge", "0");
        return cca_option_edited_badge;
    }
    public static void setCommunicationWholeSchoolEditedBadge(Context context, String communication_whole_school_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("whole_school_coming_up_edited_badge", communication_whole_school_edited_badge);
        editor.commit();
    }
    public static String getCommunicationWholeSchoolEditedBadge(Context context) {
        String communication_whole_school_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        communication_whole_school_edited_badge = prefs.getString("whole_school_coming_up_edited_badge", "0");
        return communication_whole_school_edited_badge;
    }
    public static void setCommunicationWholeSchooldBadge(Context context, String communication_whole_school_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("whole_school_coming_up_badge", communication_whole_school_badge);
        editor.commit();
    }
    public static String getCommunicationWholeSchoolBadge(Context context) {
        String communication_whole_school_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        communication_whole_school_badge = prefs.getString("whole_school_coming_up_badge", "0");
        return communication_whole_school_badge;
    }

    // Payment

    // Merchant id Canteen Payment done by aparna
    public static String getMerchantId(Context context) {
        String merchant_id = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        merchant_id = prefs.getString("merchant_id", "");
        return merchant_id;
    }

    public static void setMerchantId(Context context, String merchant_id) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("merchant_id", merchant_id);
        editor.commit();
    }
    // Authorization id Canteen Payment done by aparna
    public static String getAuthId(Context context) {
        String auth_id = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        auth_id = prefs.getString("auth_id", "");
        return auth_id;
    }

    public static void setAuthId(Context context, String auth_id) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("auth_id", auth_id);
        editor.commit();
    }
    // session_url  Canteen Payment done by aparna
    public static String getSessionUrl(Context context) {
        String session_url = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        session_url = prefs.getString("session_url", "");
        return session_url;
    }

    public static void setSessionUrl(Context context, String session_url) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("session_url", session_url);
        editor.commit();
    }
    // payment_url  Canteen Payment done by aparna
    public static String getPaymentUrl(Context context) {
        String payment_url = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        payment_url = prefs.getString("payment_url", "");
        return payment_url;
    }

    public static void setPaymentUrl(Context context, String payment_url) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("payment_url", payment_url);
        editor.commit();
    }
    // payment_url  Canteen Payment done by aparna
    public static String getMerchantName(Context context) {
        String merchant_name = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        merchant_name = prefs.getString("merchant_name", "");
        return merchant_name;
    }

    public static void setMerchantName(Context context, String merchant_name) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("merchant_name", merchant_name);
        editor.commit();
    }
    public static String getMerchantPassword(Context context) {
        String merchant_password = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        merchant_password = prefs.getString("merchant_password", "");
        return merchant_password;
    }

    public static void setMerchantPassword(Context context, String merchant_password) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("merchant_password", merchant_password);
        editor.commit();
    }
    //Canteen

    public static void setCanteenStudentId(Context context, String mCanteenStudentId) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CanteenStudentId", mCanteenStudentId);
        editor.commit();
    }

    public static String getCanteenStudentId(Context context) {
        String mCanteenStudentId = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        mCanteenStudentId = prefs.getString("CanteenStudentId", "");
        return mCanteenStudentId;
    }
    public static void setCanteenStudentName(Context context, String mCanteenStudentName) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CanteenStudentName", mCanteenStudentName);
        editor.commit();
    }

    public static String getCanteenStudentName(Context context) {
        String mCanteenStudentName = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        mCanteenStudentName = prefs.getString("CanteenStudentName", "");
        return mCanteenStudentName;
    }
    public static void setCanteenStudentImage(Context context, String mCanteenStudentImage) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CanteenStudentImage", mCanteenStudentImage);
        editor.commit();
    }

    public static String getCanteenStudentImage(Context context) {
        String mCanteenStudentImage = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        mCanteenStudentImage = prefs.getString("CanteenStudentImage", "");
        return mCanteenStudentImage;
    }

    public static void setTrnNo(Context context, String mTrnNo) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TrnNo", mTrnNo);
        editor.commit();
    }

    public static String getTrnNo(Context context) {
        String mTrnNo = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        mTrnNo = prefs.getString("TrnNo", "");
        return mTrnNo;
    }



    public static int getButtonOneStaffBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_one_staff_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonOneGuestBg() Description : set home button one bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonOneStaffBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_one_staff_color_v3", color);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonOneGuestTextImage() Description : get home button
     * one text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonOneStaffTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_one_staff_pos_v3", "1");

    }

    /*******************************************************
     * Method name : setButtonOneGuestTextImage() Description : set home button
     * one text and image Parameters : context, position Return type : void Date
     * : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonOneStaffTextImage(Context context,
                                                  String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_one_staff_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonOneGuestTabId() Description : get home button one
     * guest tab id Parameters : context Return type : String Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static String getButtonOneStaffTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_one_staff_tab_v3", TAB_NOTIFICATIONS_STAFF);
    }

    /*******************************************************
     * Method name : setButtonOneGuestTabId() Description : set home button one
     * guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonOneStaffTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_one_staff_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonTwoGuestBg() Description : get home button two bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonTwoStaffBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_two_staff_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonTwoGuestBg() Description : set home button two bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonTwoStaffBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_two_staff_color_v3", color);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonTwoGuestTextImage() Description : get home button
     * two text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonTwoStaffTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_two_staff_pos_v3", "3");

    }

    /*******************************************************
     * Method name : setButtonTwoGuestTextImage() Description : set home button
     * two text and image Parameters : context, position Return type : void Date
     * : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonTwoStaffTextImage(Context context,
                                                  String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_two_staff_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonTwoGuestTabId() Description : get home button two
     * guest tab id Parameters : context Return type : String Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static String getButtonTwoStaffTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_two_staff_tab_v3", "3");
    }

    /*******************************************************
     * Method name : setButtonTwoGuestTabId() Description : set home button two
     * guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonTwoStaffTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_two_staff_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonThreeGuestBg() Description : get home button three
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonThreeStaffBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_three_staff_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonThreeGuestBg() Description : set home button three
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonThreeStaffBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_three_staff_color_v3", color);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonThreeGuestTextImage() Description : get home
     * button three text and image Parameters : context Return type : String
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonThreeStaffTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_three_staff_pos_v3", "11");

    }

    /*******************************************************
     * Method name : setButtonThreeGuestTextImage() Description : set home
     * button three text and image Parameters : context, position Return type :
     * void Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonThreeStaffTextImage(Context context,
                                                    String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_three_staff_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonThreeGuestTabId() Description : get home button
     * three guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonThreeStaffTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_three_staff_tab_v3", "11");
    }

    /*******************************************************
     * Method name : setButtonThreeGuestTabId() Description : set home button
     * three guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonThreeStaffTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_three_staff_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFourGuestBg() Description : get home button four
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonFourStaffBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_four_staff_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonFourGuestBg() Description : set home button four
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFourStaffBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_four_staff_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFourGuestTextImage() Description : get home button
     * four text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonFourStaffTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_four_staff_pos_v3", "9");

    }

    /*******************************************************
     * Method name : setButtonFourGuestTextImage() Description : set home button
     * four text and image Parameters : context, position Return type : void
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFourStaffTextImage(Context context,
                                                   String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_four_staff_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonFourGuestTabId() Description : get home button
     * four guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonFourStaffTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_four_staff_tab_v3", "9");
    }

    /*******************************************************
     * Method name : setButtonFourGuestTabId() Description : set home button
     * four guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFourStaffTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_four_staff_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFiveGuestBg() Description : get home button five
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonFiveStaffBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_five_staff_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonFiveGuestBg() Description : set home button five
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFiveStaffBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_five_staff_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonFiveGuestTextImage() Description : get home button
     * five text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonFiveStaffTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_five_staff_pos_v3", "4");

    }

    /*******************************************************
     * Method name : setButtonFiveGuestTextImage() Description : set home button
     * five text and image Parameters : context, position Return type : void
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFiveStaffTextImage(Context context,
                                                   String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_five_staff_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonFiveGuestTabId() Description : get home button
     * five guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonFiveStaffTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_five_staff_tab_v3", "4");
    }

    /*******************************************************
     * Method name : setButtonFiveGuestTabId() Description : set home button
     * five guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonFiveStaffTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_five_staff_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSixGuestBg() Description : get home button six bg
     * Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonSixStaffBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_six_staff_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonSixGuestBg() Description : set home button six bg
     * Parameters : context, color Return type : void Date : 07-Nov-2014 Author
     * : Rijo K Jose
     *****************************************************/
    public static void setButtonSixStaffBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_six_staff_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSixGuestTextImage() Description : get home button
     * six text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSixStaffTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_six_staff_pos_v3", "5");

    }

    /*******************************************************
     * Method name : setButtonSixGuestTextImage() Description : set home button
     * six text and image Parameters : context, position Return type : void Date
     * : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSixStaffTextImage(Context context,
                                                  String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_six_staff_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonSixGuestTabId() Description : get home button six
     * guest tab id Parameters : context Return type : String Date : 25-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSixStaffTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_six_staff_tab_v3", "5");
    }

    /*******************************************************
     * Method name : setButtonSixGuestTabId() Description : set home button six
     * guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSixStaffTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_six_staff_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSevenGuestBg() Description : get home button seven
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonSevenStaffBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_seven_staff_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonSevenGuestBg() Description : set home button seven
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSevenStaffBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_seven_staff_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonSevenGuestTextImage() Description : get home
     * button seven text and image Parameters : context Return type : String
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSevenStaffTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_seven_staff_pos_v3", "2");

    }

    /*******************************************************
     * Method name : setButtonSevenGuestTextImage() Description : set home
     * button seven text and image Parameters : context, position Return type :
     * void Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSevenStaffTextImage(Context context,
                                                    String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_seven_staff_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonSevenGuestTabId() Description : get home button
     * seven guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonSevenStaffTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_seven_staff_tab_v3", "2");
    }

    /*******************************************************
     * Method name : setButtonSixGuestTabId() Description : set home button
     * seven guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonSevenStaffTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_seven_staff_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonEightGuestBg() Description : get home button eight
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonEightStaffBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_eight_staff_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonEightGuestBg() Description : set home button eight
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonEightStaffBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_eight_staff_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonEightGuestTextImage() Description : get home
     * button eight text and image Parameters : context Return type : String
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonEightStaffTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_eight_staff_pos_v3", "12");//TAB_ABOUT_US

    }

    /*******************************************************
     * Method name : setButtonEightGuestTextImage() Description : set home
     * button eight text and image Parameters : context, position Return type :
     * void Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonEightStaffTextImage(Context context,
                                                    String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_eight_staff_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonEightGuestTabId() Description : get home button
     * eight guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonEightStaffTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_eight_staff_tab_v3", "12");
    }

    /*******************************************************
     * Method name : setButtonEightGuestTabId() Description : set home button
     * eight guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonEightStaffTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_eight_staff_tab_v3", TAB_ID);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonNineGuestBg() Description : get home button nine
     * bg Parameters : context Return type : int Date : 07-Nov-2014 Author :
     * Rijo K Jose
     *****************************************************/
    public static int getButtonNineStaffBg(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getInt("btn_nine_staff_color_v3", context.getResources()
                .getColor(R.color.transparent));

    }

    /*******************************************************
     * Method name : setButtonNineGuestBg() Description : set home button nine
     * bg Parameters : context, color Return type : void Date : 07-Nov-2014
     * Author : Rijo K Jose
     *****************************************************/
    public static void setButtonNineStaffBg(Context context, int color) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("btn_nine_staff_color_v3", color);
        editor.commit();
    }

    /*******************************************************
     * Method name : getButtonNineGuestTextImage() Description : get home button
     * nine text and image Parameters : context Return type : String Date :
     * 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonNineStaffTextImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_nine_staff_pos_v3", "8");

    }

    /*******************************************************
     * Method name : setButtonNineGuestTextImage() Description : set home button
     * nine text and image Parameters : context, position Return type : void
     * Date : 07-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonNineStaffTextImage(Context context,
                                                   String position) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_nine_staff_pos_v3", position);
        editor.commit();

    }

    /*******************************************************
     * Method name : getButtonNineGuestTabId() Description : get home button
     * nine guest tab id Parameters : context Return type : String Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static String getButtonNineStaffTabId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("btn_nine_staff_tab_v3", "8");
    }

    /*******************************************************
     * Method name : setButtonNineGuestTabId() Description : set home button
     * nine guest tab id Parameters : context, TAB_ID Return type : void Date :
     * 25-Nov-2014 Author : Rijo K Jose
     *****************************************************/
    public static void setButtonNineStaffTabId(Context context, String TAB_ID) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("btn_nine_staff_tab_v3", TAB_ID);
        editor.commit();
    }
    public static void setStaffNotificationBadge(Context context, String staff_notification_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("staff_notification_badge", staff_notification_badge);
        editor.commit();
    }
    public static String getStaffNotificationBadge(Context context) {
        String staff_notification_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        staff_notification_badge = prefs.getString("staff_notification_badge", "0");
        return staff_notification_badge;
    }
    public static void setStaffNotificationEditedBadge(Context context, String staff_notification_edited_badge) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("staff_notification_edited_badge", staff_notification_edited_badge);
        editor.commit();
    }
    public static String getStaffNotificationEditedBadge(Context context) {
        String staff_notification_edited_badge = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        staff_notification_edited_badge = prefs.getString("staff_notification_edited_badge", "0");
        return staff_notification_edited_badge;
    }
    public static void setStaffOnly(Context context, String staff_only) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("staff_only", staff_only);
        editor.commit();
    }
    public static String getStaffOnly(Context context) {
        String staff_only = "";
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        staff_only = prefs.getString("staff_only", "0");
        return staff_only;
    }
}

