package com.mobatia.naisapp.fragments.calendar.adapter;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NASCalendarConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.calendar.CalendarFragment;
import com.mobatia.naisapp.fragments.calendar.model.CalendarModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Rijo K Jose on 15/3/17.
 */
public class CalendarFragmentListSportsAdapter extends BaseAdapter implements NASCalendarConstants,URLConstants,JSONConstants, StatusConstants,
        IntentPassValueConstants, NaisClassNameConstants {
    Context mContext;
    ArrayList<CalendarModel> calendarModels;
    //ArrayList<CalendarModel> eventModels=new ArrayList<>();
    ArrayList<CalendarModel> eventModels;
    //    int[] colours = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};
    int[] colours = AppController.getInstance().getResources().getIntArray(R.array.calendar_row_colors);
    int colorValue;
    LayoutInflater minfalter;
    ViewHolder viewHolder;
    private int mnthId;
    EventsAdapter calendarFragmentListAdapter;
    boolean isRead=false;

    public CalendarFragmentListSportsAdapter(Context mContext, ArrayList<CalendarModel> calendarModels, ArrayList<CalendarModel> eventModels) {
        this.mContext = mContext;
        this.calendarModels = calendarModels;
        this.eventModels = eventModels;
    }

    public CalendarFragmentListSportsAdapter(Context mContext, ArrayList<CalendarModel> calendarModels) {
        this.mContext = mContext;
        this.calendarModels = calendarModels;
    }

    @Override
    public int getCount() {
        return calendarModels.size();
    }

    @Override
    public Object getItem(int position) {
        return calendarModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            minfalter = LayoutInflater.from(mContext);
//              minfalter = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = minfalter.inflate(R.layout.fragment_calendar_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.eventsListView = (ListView) convertView.findViewById(R.id.eventsListView);
            viewHolder.headerTextView = (TextView) convertView.findViewById(R.id.dateNTime);
            viewHolder.header = (LinearLayout) convertView.findViewById(R.id.header);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.headerTextView.setText(calendarModels.get(position).getDateNTime());
        colorValue = colours[position % colours.length];
        viewHolder.header.setBackgroundColor(colorValue);
        setListView(colorValue, position);
        setListViewHeightBasedOnChildren(viewHolder.eventsListView);
        return convertView;
    }

    class ViewHolder {
        ListView eventsListView;
        TextView headerTextView;
        LinearLayout header;
    }

    private void setListView(int colors, final int mPosition) {
        //for(int i=0;i<2;i++){
        // loadSubItems();
        // }
        isRead=false;
         calendarFragmentListAdapter = new EventsAdapter(mContext, calendarModels.get(mPosition).getEventModels(), colors, mPosition,isRead);
        viewHolder.eventsListView.setAdapter(calendarFragmentListAdapter);
        viewHolder.eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //showCalendarEvent( calendarModels.get(mPosition).getEventModels().get(position).getEvent(), calendarModels.get(position).getDateNTime(), eventTypeStr) ;
                if(calendarModels.get(mPosition).getEventModels().get(position).getStatus().equalsIgnoreCase("0"))
                {
                    calendarModels.get(mPosition).getEventModels().get(position).setStatus("1");
                    notifyDataSetChanged();
                    PreferenceManager.setUniversity_edit_badge(mContext,"0");
                    PreferenceManager.setUniversity_badge(mContext,"0");
                    HomeListAppCompatActivity.mListAdapter.notifyDataSetChanged();
                    //callStatusChangeApi(URL_GET_STATUS_CHANGE_API,calendarModels.get(mPosition).getEventModels().get(position).getId(),position,mPosition,calendarModels.get(mPosition).getEventModels().get(position).getStatus());

                }
                else if(calendarModels.get(mPosition).getEventModels().get(position).getStatus().equalsIgnoreCase("2"))
                {
                    calendarModels.get(mPosition).getEventModels().get(position).setStatus("1");
                    notifyDataSetChanged();
                    PreferenceManager.setUniversity_edit_badge(mContext,"0");
                    PreferenceManager.setUniversity_badge(mContext,"0");
                    HomeListAppCompatActivity.mListAdapter.notifyDataSetChanged();
                   // callStatusChangeApi(URL_GET_STATUS_CHANGE_API,calendarModels.get(mPosition).getEventModels().get(position).getId(),position,mPosition,calendarModels.get(mPosition).getEventModels().get(position).getStatus());
                }
                if ( calendarModels.get(mPosition).getEventModels().get(position).getIsAllDay().equalsIgnoreCase("1"))
                {
                    showCalendarEvent( calendarModels.get(mPosition).getEventModels().get(position).getEvent(), calendarModels.get(mPosition).getDateNTime(), "All Day Event", calendarModels.get(mPosition).getEventModels(),position,calendarModels.get(mPosition).getEventModels().get(position).getVpml()) ;

                }
                else if (!(calendarModels.get(mPosition).getEventModels().get(position).getStartTime().equalsIgnoreCase("")) && !(calendarModels.get(mPosition).getEventModels().get(position).getEndTime().equalsIgnoreCase(""))){
                    showCalendarEvent( calendarModels.get(mPosition).getEventModels().get(position).getEvent(), calendarModels.get(mPosition).getDateNTime(), calendarModels.get(mPosition).getEventModels().get(position).getStartTime() + " - " + calendarModels.get(mPosition).getEventModels().get(position).getEndTime(),calendarModels.get(mPosition).getEventModels(),position,calendarModels.get(mPosition).getEventModels().get(position).getVpml()) ;

                }
                else if (!(calendarModels.get(mPosition).getEventModels().get(position).getStartTime().equalsIgnoreCase("")))
                {
                    showCalendarEvent( calendarModels.get(mPosition).getEventModels().get(position).getEvent(), calendarModels.get(mPosition).getDateNTime(), calendarModels.get(mPosition).getEventModels().get(position).getStartTime(),calendarModels.get(mPosition).getEventModels(),position,calendarModels.get(mPosition).getEventModels().get(position).getVpml()) ;

                }else if (!(calendarModels.get(mPosition).getEventModels().get(position).getEndTime().equalsIgnoreCase("")))
                {
                    showCalendarEvent( calendarModels.get(mPosition).getEventModels().get(position).getEvent(), calendarModels.get(mPosition).getDateNTime(), calendarModels.get(mPosition).getEventModels().get(position).getEndTime(),calendarModels.get(mPosition).getEventModels(),position,calendarModels.get(mPosition).getEventModels().get(position).getVpml()) ;

                }
            }
        });
        //calendarFragmentListAdapter.notifyDataSetChanged();

    }
    private void callStatusChangeApi(String URL, final String id, final int eventPosition, final int eventMainPosition, final String status) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token","users_id","id","type"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),id,"sports_calendar"};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is status change" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                              if(status.equalsIgnoreCase("0")||status.equalsIgnoreCase("2"))
                              {
                                  calendarModels.get(eventMainPosition).getEventModels().get(eventPosition).setStatus("1");
                                  calendarFragmentListAdapter.notifyDataSetChanged();

                              }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                       // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                            response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                            response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {

                            }
                        });

                    } else {

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {


        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
    }

    private void showCalendarEvent(String eventNameStr, String eventDateStr, String eventTypeStr, final ArrayList<CalendarModel> mCalendarEventModels, final int eventPosition,String vlink) {
        final Dialog dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_calendar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        // set the custom dialog components - edit text, button
        TextView eventDate = (TextView) dialog.findViewById(R.id.eventDate);
        TextView eventName = (TextView) dialog.findViewById(R.id.eventName);
        TextView eventType = (TextView) dialog.findViewById(R.id.eventType);
        TextView linkBtn = (Button) dialog.findViewById(R.id.linkBtn);
        eventName.setText(eventNameStr);
        eventDate.setText(eventDateStr);
        eventType.setText("("+eventTypeStr+")");
        Button deleteCalendar = (Button) dialog
                .findViewById(R.id.deleteCalendar);
        Button addToCalendar = (Button) dialog
                .findViewById(R.id.addToCalendar);
        Button dismiss = (Button) dialog.findViewById(R.id.dismiss);
        if (vlink.equalsIgnoreCase(""))
        {
            linkBtn.setVisibility(View.GONE);
        }
        else
        {
            linkBtn.setVisibility(View.VISIBLE);
        }

        linkBtn.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {
                                           Uri uri = Uri.parse(mCalendarEventModels.get(eventPosition).getVpml());
                                           Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                           mContext.startActivity(intent);
                                           HomeListAppCompatActivity.dialogCal.dismiss();
                                       }
                                   }

        );
        // if button is clicked, close the custom dialog
        deleteCalendar.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {


                                                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                                      if (AppController.eventIdList.size() == 0) {
                                                          if (!mCalendarEventModels.get(eventPosition).getId()
                                                                  .equalsIgnoreCase("")) {
                                                              Uri deleteUri = ContentUris.withAppendedId(
                                                                      CalendarContract.Events.CONTENT_URI, Long
                                                                              .parseLong(mCalendarEventModels.get(eventPosition)
                                                                                      .getId()));
                                                              mContext.getContentResolver().delete(deleteUri, null,
                                                                      null);
                                                              mCalendarEventModels.get(eventPosition).setId("");
                                                              Toast.makeText(mContext,
                                                                      mContext.getResources().getString(
                                                                              R.string.del_cal_success), Toast.LENGTH_SHORT).show();
                            /*PreferenceManager.setCalendarEventNames(
                                    mContext,
                                    PreferenceManager.getCalendarEventNames(
                                            mContext).replace(
                                            (calendarModels.get(position).getEvent()
                                                    + calendarModels.get(position).getEvent() + ","), ""));*/
                                                              PreferenceManager.setCalendarEventNames(mContext, "");

                                                          } else {
                                                              Toast.makeText(mContext,
                                                                      mContext.getResources().getString(
                                                                              R.string.del_cal_success), Toast.LENGTH_SHORT).show();
                                                          }
                                                      } else {
                                                          Uri deleteUri = null;
                                                          deleteUri = ContentUris.withAppendedId(
                                                                  CalendarContract.Events.CONTENT_URI, Long
                                                                          .parseLong(mCalendarEventModels.get(eventPosition)
                                                                                  .getId()));
                                                          //                        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(String.valueOf(AppController.eventIdList.get(position))));
                                                          int rows = mContext.getContentResolver().delete(deleteUri, null, null);
                                                          Log.d("deletion1 ", rows + " events deleted");

                                                          Toast.makeText(mContext,
                                                                  mContext.getResources().getString(
                                                                          R.string.del_cal_success), Toast.LENGTH_SHORT).show();
                                                            /*PreferenceManager.setCalendarEventNames(
                                mContext,
                                PreferenceManager.getCalendarEventNames(
                                        mContext).replace(
                                        (calendarModels.get(position).getEvent()
                                                + calendarModels.get(position).getEvent() + ","), ""));*/
                                                          PreferenceManager.setCalendarEventNames(mContext, "");
//                        AppController.eventIdList.remove(String.valueOf(AppController.eventIdList.get(position)));

                                                      }
                                                      dialog.dismiss();

                                                  }
                                              }
                                          }

        );


        // if button is clicked, close the custom dialog
        addToCalendar.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 String reqSentence = AppUtils.htmlparsing(String.valueOf(
                                                         Html.fromHtml(mCalendarEventModels.get(eventPosition)
                                                                 .getEvent())).replaceAll("\\s+", " "));
                                                 String[] splited = reqSentence.split("\\s+");
                                                 String[] dateString;
                                                 int year = -1;
                                                 int month = -1;
                                                 int day = -1;
                                                 String[] timeString;
                                                 int hour = -1;
                                                 int min = -1;
                                                 String[] timeString1;
                                                 int hour1 = -1;
                                                 int min1 = -1;
                                                 String allDay = "0";
                                                 year = Integer.parseInt(mCalendarEventModels.get(eventPosition).getYearDate());
                                                 month = getMonthDetails(mContext, mCalendarEventModels.get(eventPosition).getMonthDate());
                                                 day = Integer.parseInt(mCalendarEventModels.get(eventPosition).getDayDate());
                                                 if(mCalendarEventModels.get(eventPosition).getFromTime().equalsIgnoreCase(""))
                                                 {
                                                      hour = -1;
                                                      min = -1;
                                                 }
                                                 else {
                                                     timeString = mCalendarEventModels.get(eventPosition).getFromTime().split(":");
                                                     hour = Integer.parseInt(timeString[0]);
                                                     min = Integer.parseInt(timeString[1]);
                                                 }
                                                 allDay = mCalendarEventModels.get(eventPosition).getIsAllDay();

                                                 if(mCalendarEventModels.get(eventPosition).getToTime().equalsIgnoreCase(""))
                                                 {
                                                     hour1 = -1;
                                                     min1 = -1;
                                                 }
                                                 else {
                                                     timeString1 = mCalendarEventModels.get(eventPosition).getToTime().split(":");
                                                     hour = Integer.parseInt(timeString1[0]);
                                                     min = Integer.parseInt(timeString1[1]);
                                                 }

                                             /*    timeString1 = mCalendarEventModels.get(eventPosition).getToTime().split(":");
                                                 hour1 = Integer.parseInt(timeString1[0]);
                                                 min1 = Integer.parseInt(timeString1[1]);*/
                                                 boolean addToCalendar = true;
                                                 String[] prefData = PreferenceManager
                                                         .getCalendarEventNames(mContext).split(",");
                                                 for (int i = 0; i < prefData.length; i++) {
                                                     if (prefData[i].equalsIgnoreCase(mCalendarEventModels.get(eventPosition).getEvent() + mCalendarEventModels.get(eventPosition).getEvent())) {
                                                         addToCalendar = false;
                                                         break;
                                                     }
                                                 }
                                                 System.out.println("addToCalendar---" + addToCalendar);
                                                 if (addToCalendar) {
                                                     if (year != -1 && month != -1 && day != -1 && hour != -1
                                                             && min != -1) {
                                                         if (hour1==-1 && min1==-1)
                                                         {
                                                             addReminder(year, month, day, hour, min, year, month,
                                                                     (day), hour, min,
                                                                     mCalendarEventModels.get(eventPosition).getEvent(),
                                                                     mCalendarEventModels.get(eventPosition).getEvent(), 0, eventPosition, allDay, mCalendarEventModels);

                                                         }
                                                         else {
                                                             addReminder(year, month, day, hour, min, year, month,
                                                                     (day), hour1, min1,
                                                                     mCalendarEventModels.get(eventPosition).getEvent(),
                                                                     mCalendarEventModels.get(eventPosition).getEvent(), 0, eventPosition, allDay, mCalendarEventModels);
                                                         }
                                                         } else {
                                                         Toast.makeText(mContext,
                                                                 mContext.getResources().getString(
                                                                         R.string.no_evnt_details), Toast.LENGTH_SHORT).show();
                                                     }
                                                 } else {
                                                     Toast.makeText(mContext,
                                                             mContext.getResources().getString(
                                                                     R.string.add_cal_success), Toast.LENGTH_SHORT).show();
                                                 }
                                                 notifyDataSetChanged();
                                                 dialog.dismiss();

                                             }
                                         }

        );

        dismiss.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {
                                           notifyDataSetChanged();
                                           dialog.dismiss();
                                       }
                                   }

        );
        dialog.show();
    }
    /*******************************************************
     * Method name : addReminder() Description : add reminder to calendar
     * without popup Parameters : statrYear, startMonth, startDay, startHour,
     * startMinute, endYear, endMonth, endDay, endHour, endMinutes, name Return
     * type : void Date : 21-Jan-2015 Author : Rijo K Jose
     *****************************************************/

    public void addReminder(int startYear, int startMonth, int startDay,
                            int startHour, int startMinute, int endYear, int endMonth,
                            int endDay, int endHour, int endMinutes, String name,
                            String description, int count, int position, String allDay,ArrayList<CalendarModel>mCalendarEventModelAdd) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startYear, startMonth, startDay, startHour, startMinute);
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(endYear, endMonth, endDay, endHour, endMinutes);
        long endMillis = endTime.getTimeInMillis();

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();
        if (Build.VERSION.SDK_INT >  Build.VERSION_CODES.M) {
            // Marshmallow+
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 3);//1

        }else if (Build.VERSION.SDK_INT ==  Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT ==  Build.VERSION_CODES.LOLLIPOP_MR1) {
            // lollipop
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 3);//1

        }else{
            //below Marshmallow
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);//1

        }

//        eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);//1
        eventValues.put(CalendarContract.Events.TITLE, name);
        eventValues.put(CalendarContract.Events.DESCRIPTION, description);
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.SHORT);
        eventValues.put(CalendarContract.Events.DTSTART, startMillis);
        eventValues.put(CalendarContract.Events.DTEND, endMillis);
        if (allDay.equals("1")) {
            eventValues.put(CalendarContract.Events.ALL_DAY, true);
        } else {
            eventValues.put(CalendarContract.Events.ALL_DAY, false);
        }
        eventValues.put("eventStatus", 1);
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1);
        Uri eventUri = mContext.getContentResolver().insert(
                Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());
        Log.d("TAG", "1----" + eventID);
        mCalendarEventModelAdd.get(position).setId(String.valueOf(eventID));
        Log.d("TAG", "2----");

        PreferenceManager.setCalendarEventNames(mContext,
                PreferenceManager.getCalendarEventNames(mContext) + name
                        + description + ",");
        if (count == 0) {
            Toast.makeText(mContext, mContext.getResources()
                    .getString(R.string.add_cal_success), Toast.LENGTH_SHORT).show();
        }
        /***************** Event: Reminder(with alert) Adding reminder to event *******************/
        String reminderUriString = "content://com.android.calendar/reminders";
        ContentValues reminderValues = new ContentValues();
        reminderValues.put(EVENT_ID, eventID);
        reminderValues.put(MINUTES, 1440);
        reminderValues.put(METHOD, 1);
        Uri reminderUri = mContext.getContentResolver().insert(
                Uri.parse(reminderUriString), reminderValues);
        long eventIDlong = Long.parseLong(reminderUri.getLastPathSegment());
        AppController.eventIdList.add(String.valueOf(eventID));

    }
    public int getMonthDetails(Context mContext, String descStringTime) {
        // january
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.january))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.jan_short));
//            }
            mnthId = 0;
        } // february
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.february))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.feb_short));
//            }
            mnthId = 1;
        } // march
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.march))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.mar_short));
//            }
            mnthId = 2;
        } // april
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.april))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.apr_short));
//            }
            mnthId = 3;
        }// may
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.may))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.may_short));
//            }
            mnthId = 4;
        } // june
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.june))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.jun_short));
//            }
            mnthId = 5;
        } // july
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.july))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.jul_short));
//            }
            mnthId = 6;
        } // august
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.august))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.aug_short));
//            }
            mnthId = 7;
        } // september
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.september))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.sep_short));
//            }
            mnthId = 8;
        } // october
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.october))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.oct_short));
//            }
            mnthId = 9;
        } // november
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.november))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.nov_short));
//            }
            mnthId = 10;
        } // december
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.december))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.dec_short));
//            }
            mnthId = 11;
        }
        return mnthId;
    }

}
