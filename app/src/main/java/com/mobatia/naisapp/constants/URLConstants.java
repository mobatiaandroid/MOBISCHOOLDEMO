/**
 * 
 */
package com.mobatia.naisapp.constants;

/**
 * @author RIJO K JOSE
 * 
 */
public interface URLConstants
{


    //Dev Phase 4
//	public String POST_APITOKENURL="http://mobatia.in/naisV4.1/oauth/access_token";
//	public String URL_HEAD = "http://mobatia.in/naisV4.1/api/";
//
 	public String POST_APITOKENURL="http://beta.mobatia.in:81/naisV4.1/oauth/access_token";
	public String URL_HEAD = "http://beta.mobatia.in:81/naisV4.1/api/";

// 	public String POST_APITOKENURL="http://ec2-3-208-59-158.compute-1.amazonaws.com/nais/oauth/access_token";
//	public String URL_HEAD = "http://ec2-3-208-59-158.compute-1.amazonaws.com/nais/api/";


//	//New Live Phase 4
//	public String POST_APITOKENURL="https://cms.nasdubai.ae/nais/oauth/access_token";
//	public String URL_HEAD = "https://cms.nasdubai.ae/nais/api/";

	//Mobicare NAS below//

	/*public String POST_APITOKENURL="http://mobicare2.mobatia.com/nais/oauth/access_token";
	public String URL_HEAD = "http://mobicare2.mobatia.com/nais/api/";*/


	public String URL_ABOUTUS_LIST = URL_HEAD
			+ "about_us";
	public String URL_DEVICE_REGISTRATION = URL_HEAD
			+ "deviceregistration";
	public String URL_TERMS_OF_SERVICE = URL_HEAD
			+ "terms_of_service";

	public String URL_EARLY_YEARS_LIST = URL_HEAD
			+ "departmentearly";
	public String URL_PRIMARY_LIST = URL_HEAD
			+ "departmentprimary";
	public String URL_SECONDARY_LIST = URL_HEAD
			+ "departmentsecondary";
	public String URL_IB_PROGRAMMME_LIST = URL_HEAD
			+ "department_ib_programms";
	public String URL_TERM_CALENDAR_LIST = URL_HEAD
			+ "term_calender";
	public String URL_CLASS_REPRESENTATIVE_LIST = URL_HEAD
			+ "class_representative";
	public String URL_CHATTER_BOX_LIST = URL_HEAD
			+ "chatter_box";
	public String URL_HOME_BANNER = URL_HEAD
			+ "home_banner_images";
	public String URL_GET_PHOTOS_LIST_DETAIL = URL_HEAD
			+ "photos_V1";
   public String URL_GET_PHOTOS_LIST = URL_HEAD
			+ "albums";

	public String URL_GET_VIDEOS_LIST = URL_HEAD
			+ "videos_V1";
	public String URL_PTA_CONFIRMATION = URL_HEAD
			+ "pta_confirmation";
	public String URL_GET_PTA_REVIEW_LIST = URL_HEAD
			+ "pta_reviewlist";
	public String URL_GET_THUM_NAIL_IMAGE_LIST = URL_HEAD
			+ "thumbnailimages";

	public String URL_STAFFDIRECTORY_LIST=URL_HEAD+"getstaffcategorylist";
	public String URL_EARLY_COMING_UP_LIST=URL_HEAD+"early_coming_up";
	public String URL_PRIMARY_COMING_UP_LIST=URL_HEAD+"primary_coming_up";
	public String URL_SECONDARY_COMING_UP_LIST=URL_HEAD+"secondary_coming_up";
	public String URL_WHOLE_SCHOOL_COMING_UP_LIST=URL_HEAD+"whole_school_coming_up";
	public String URL_IB_PROGRAMME_COMING_UP_LIST=URL_HEAD+"ib_programmes_coming_up";
	public String URL_PERFORMING_ARTS_LIST=URL_HEAD+"performing_arts";
	public String URL_CCAS_LIST=URL_HEAD+"cca";
	public String URL_SPORTS_PDF=URL_HEAD+"sports_pdf";
	public String URL_COMMUNICATION_INFORMATION=URL_HEAD+"communication_info";
	public String URL_NAS_TODAY_LIST=URL_HEAD+"nastoday";
	public String URL_LOGOUT=URL_HEAD+"logout";
	public String URL_NAE_PROGRAMMES_LIST=URL_HEAD+"nae_programme";
	public String URL_GETSTAFFLDEPT_LIST=URL_HEAD+"getstaffdeptlist";
	public String URL_GET_NOTICE_LIST=URL_HEAD+"notice";
	public String URL_GET_NOTICATIONS_LIST=URL_HEAD+"notifications";
	public String URL_GET_ABOUTUS_LIST=URL_HEAD+"about_us";
	public String URL_CLEAR_BADGE=URL_HEAD+"clear_badge";


	public String URL_COMMUNICATION_BANNER = URL_HEAD
			+ "communication_banner_images";

	public String URL_GET_CALENDAR_LIST=URL_HEAD+"calender";
	public String URL_GET_SPORTS_CALENDAR_LIST=URL_HEAD+"sports_calendar";
	public String URL_GET_CONTACTUS=URL_HEAD+"contact_us";
	public String URL_GET_NEWSLETTER_CATEGORY=URL_HEAD+"newsletter_categories";
	public String URL_GET_NEWSLETTERS=URL_HEAD+"newsletter";
	public String URL_PARENT_ESSENTIALS=URL_HEAD+"parents_essentials";
	public String URL_GET_COMMUNICATIONS_EVENTS=URL_HEAD+"events";
	public String URL_GET_SOCIALMEDIA_LIS=URL_HEAD+"social_media";
	public String URL_GET_SPORTSEVENT_LIST=URL_HEAD+"sportseventlist";
	public String URL_GET_SPORTS_EVENTS_LISTDETAILS=URL_HEAD+"sportseventdetails";
	public String URL_GET_STUDENT_LIST=URL_HEAD+"studentlist";
	public String URL_GET_PTA_ALLOTTED_LIST=URL_HEAD+"ptaallotteddates";
	public String URL_GET_PARENT_ASSOCIATION_EVENT_LIST=URL_HEAD+"parent_assoc_events";
	public String URL_GET_PTA_TIME_SLOT_LIST=URL_HEAD+"listingpta";
	public String URL_BOOK_PTA_INSERT_TIME_SLOT=URL_HEAD+"insertpta";
	public String URL_BOOK_PTA_TIME_SLOT=URL_HEAD+"parent_assoc_events_attending_or_not";
	public String URL_BOOK_PTA_TIME_SLOT_NEW=URL_HEAD+"parent_assoc_events_attending_or_not_new";
	public String URL_GET_STAFF_LIST_ACCORDING_TO_STUDENT=URL_HEAD+"stafflist";
	public String URL_SEND_EMAIL_TO_STAFF=URL_HEAD+"sendemail";
	public String URL_SURVEY_SUBMIT=URL_HEAD+"survey_submit";
	public String URL_PARENT_SIGNUP=URL_HEAD+"parent_signup";
	public String URL_LOGIN=URL_HEAD+"login";
	public String URL_CHANGEPSAAWORD=URL_HEAD+"changepassword";
	public String URL_FORGOTPASSWORD=URL_HEAD+"forgotpassword";
	public String URL_UPDATE_EVENTGOINGSTATUS=URL_HEAD+"sports_event_goingstatus";
	public String URL_PARENTS_ASSOCIATION=URL_HEAD+"parents_association";
	public String URL_CCA_DETAILS=URL_HEAD+"cca_details";
	public String URL_CCA_SUBMIT=URL_HEAD+"cca_submit";
	public String URL_CCA_REVIEWS=URL_HEAD+"cca_reviews";
	public String URL_ASSESSMENT_LINK_LIST=URL_HEAD+"assessment";
	public static String URL_GET_LEAVEREQUESTSUBMISSION=URL_HEAD+"requestLeave";
	public static String URL_GET_LEAVEREQUEST_LIST=URL_HEAD+"leaveRequests";
	public static String URL_GET_STATUS_CHANGE_API=URL_HEAD+"status_changeAPI";
	String URL_GET_STUDENT_REPORT=URL_HEAD+"progressreport";
	String URL_GET_PERMISSION_SLIPS=URL_HEAD+"permission_slips";
	String URL_GET_PERMISSION_SLIPS_STATUS_CHANGE=URL_HEAD+"permission_slips_statusChange";
	String URL_GET_SPORTS_TEAMS_LIST=URL_HEAD+"sports_events_groups";
	String URL_GET_SPORTS_TEAMS_CLICK_LIST=URL_HEAD+"sports_events_groups_training_dates";
	String URL_GET_SPORTS_EXTERNAL_PROVIDERS=URL_HEAD+"external_providers";
	public String URL_CCA_DELETE=URL_HEAD+"cca_selection_cancel";
	public String URL_TRIP_LIST=URL_HEAD+"triplist";
	public String URL_TRIP_SUBMISSION=URL_HEAD+"tripsubmition_new";
	public String URL_PAYMENT_BANNER=URL_HEAD+"bannerpayment";
	public String URL_CANTEEN_BANNER=URL_HEAD+"bannercanteen";
	public String URL_PAYMENT_CATEGORY=URL_HEAD+"payment_category_new";
	public String URL_CANTEEN_WALLET = URL_HEAD +"wallet_details";
	public String URL_CANTEEN_WALLET_HISTORY = URL_HEAD +"wallet_history";
//	public String URL_CANTEEN_WALLET_SUBMISSION = URL_HEAD +"wallet_submit";
	public String URL_CANTEEN_WALLET_SUBMISSION = URL_HEAD +"wallet_submit_v2";
	public String URL_FEE_PAYMENT_SUBMISSION = URL_HEAD +"paymentsubmission_new";
	/*Canteen URLS*/
	public String URL_STUDENT_INFO_FOR_CANTEEN = URL_HEAD +"student_info_for_canteen";
	public String URL_CANTEEN_INFO = URL_HEAD +"canteen_informations";
	public String URL_STAFF_LOGIN = URL_HEAD +"staff_login";
	public String URL_STAFF_SIGNUP = URL_HEAD +"staff_signup";
	public String URL_STAFF_FORGOT_PASSWORD = URL_HEAD +"staff_forgot_password";
	public String URL_STAFF_CHANGE_PASSWORD = URL_HEAD +"staff_change_password";
	public String URL_STAFF_INFO_FOR_CANTEEN = URL_HEAD +"staff_info_for_canteen";
	public String URL_CANTEEN_CATEGORY_LIST = URL_HEAD +"item_categories";
	public String URL_CANTEEN_ITEM_LIST = URL_HEAD +"items";
	public String URL_CANTEEN_CART_DETAIL = URL_HEAD +"get_canteen_cart";
	public String URL_CANTEEN_CART_UPDATE = URL_HEAD +"update_canteen_cart";
	public String URL_CANTEEN_REMOVE_CART_ITEM = URL_HEAD +"remove_canteen_cart_item";
	public String URL_CANTEEN_CONFIRMED_ORDER_ITEM = URL_HEAD +"get_canteen_preorders";
	public String URL_CANTEEN_ADD_TO_CART = URL_HEAD +"add_to_canteen_cart";
	public String URL_CANTEEN_CONFIRMED_ORDER_ITEM_CELL_CANCEL = URL_HEAD +"cancel_canteen_preorder_item";
	public String URL_CANTEEN_CONFIRMED_ORDER_DATE_CELL_CANCEL = URL_HEAD +"cancel_canteen_preorder";
	public String URL_CANTEEN_CONFIRMED_ORDER_EDIT = URL_HEAD +"edit_canteen_preorder_item";
	public String URL_CANTEEN_PREORDER_HISTORY = URL_HEAD +"get_canteen_preorders_history";
	public String URL_CANTEEN_PREORDER = URL_HEAD +"canteen_preorder";
	public String URL_PAYMENT_TOKEN = URL_HEAD +"network_payment_gateway_access_token";
	public String URL_PAYMENT_TOKEN_FOR_FEE = URL_HEAD +"network_payment_gateway_access_token_for_fee_payment";
	//public String URL_CREATE_PAYMENT = URL_HEAD +"network_payment_gateway_creating_an_order";
//	public String URL_CREATE_PAYMENT = URL_HEAD +"canteen_wallet_order";
	public String URL_CREATE_PAYMENT = URL_HEAD +"canteen_wallet_order";
	public String URL_CREATE_PAYMENT_FEE = URL_HEAD +"network_payment_gateway_creating_an_order_for_fee_payment";
	public String URL_CREATE_PAYMENT_RETRIVE_FEE = URL_HEAD +"network_payment_gateway_retrieve_order_details_for_fee_payment";
	public String URL_CANTEEN_STAFF_WALLET = URL_HEAD +"staff_wallet_details";
    public String URL_GET_NOTICATIONS_LIST_V1=URL_HEAD+"notifications_V1";
    public String URL_GET_NOTICATIONS_MESSAGE=URL_HEAD+"notification_message";
    public String URL_GET_WALLET_SUBMISSION=URL_HEAD+"wallet_submission";
    public String URL_GET_STAFF_WALLET_SUBMISSION=URL_HEAD+"staff_wallet_submit";
    public String URL_GET_STAFF_WALLET_HISTORY=URL_HEAD+"staff_wallet_history";
    public String URL_GET_UPDATE_APP_USER_VERSION=URL_HEAD+"update_user_app_version";
    public String URL_GET_STAFF_LOGOUT=URL_HEAD+"staff_logout";
    public String URL_GET_STAFF_NOTIFICATION=URL_HEAD+"staff_notifications";
    public String URL_GET_STAFF_NOTIFICATION_MESSAGE=URL_HEAD+"staff_notification_message";
    public String URL_GET_STAFF_NOTIFICATION_STATUS_CHANGE=URL_HEAD+"staff_notification_read_unread_status_change";
    public String URL_GET_STAFF_BADGE_COUNT=URL_HEAD+"staff_badge_counts";
    public String URL_GET_USER_BADGE_COUNT=URL_HEAD+"badge_counts";
    public String URL_GET_USER_SURVEY=URL_HEAD+"surveys";
    public String URL_GET_USER_SURVEY_LIST=URL_HEAD+"survey_list";
    public String URL_GET_USER_SURVEY_DETAIL=URL_HEAD+"survey_detail";
    public String URL_GET_USER_BANNER=URL_HEAD+"home_banner_images_V1";
    public String URL_GET_TIME_EXCEED=URL_HEAD+"time_exceed_status";
    public String URL_GET_BANNER_GUIDANCE=URL_HEAD+"banner_guidance";
    public String URL_GET_GUIDANCE_INFORMATION=URL_HEAD+"guidance_informations";
    public String URL_GET_GUIDANCE_CALENDAR=URL_HEAD+"guidance_calendar";
    public String URL_GET_GUIDANCE_ESSENTIALS=URL_HEAD+"guidance_essentials";
    public String URL_GET_GUIDANCE_ESSENTIALS_DETAILS=URL_HEAD+"guidance_essential_detail";
    public String URL_GET_CLEAR_NOTIFICATION_BADGE=URL_HEAD+"clear_notification_badge";
}
