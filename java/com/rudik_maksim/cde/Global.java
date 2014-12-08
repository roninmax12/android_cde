package com.rudik_maksim.cde;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Максим on 30.04.14.
 */
public class Global {
    public static class Application{
        public static Connection connection;
        public static Context context = null;
        public static String SHARED_PREFS_NAME;// = "cde_prefs";
        public static String FILE_PROTOCOL;// = "file_protocol.txt";
        public static SharedPreferences preferences = null;
        public static SharedPreferences.Editor SPEditor;
        public static boolean authorized = false;
        public static int currentFragmentId = Configuration.NAV_POINTS;
        public static Fragment fragment = null; // need for navDrawer
        public static boolean isFragmentScheduleActionExists = false;

        public static boolean isOnline(){
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            if (nInfo != null && nInfo.isConnected()) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static class Configuration{
        //public static int FragmentNumber = 0; // PointsActivity with NavigationDrawer
        //Indexes of NavigationDrawer items
        public static final int NAV_POINTS = 0;
        public static final int NAV_PROTOCOL = 1;
        public static final int NAV_RATING = 2;
        public static final int NAV_RECORD_CDE = 3;
        public static final int NAV_SCHEDULE = 4;
        public static final int NAV_SESSION_SCHEDULE = 5;
        public static final int NAV_ATTESTATION_SCHEDULE = 6;
        public static final int NAV_SETTINGS = 7;
        public static final int NAV_EXIT = 8;
        //Settings
        public static boolean show_data_on_cur_sem = false;
        public static boolean push_enabled = false;
        public static boolean expandListView = false;
        public static boolean clear_login_password_edittext = false;
        //PointsActivity
        public static String currentYearChosen = null;
        //FragmentPoints
        public static boolean isProtocolBackgroundLoaded = false;
        //FragmentSchedule
        public static boolean isFirstOpen = true; // need for automatically choose tab/week
        public static boolean isScheduleTeacherFragment = false;
        //FragmentScheduleAttestation
        public static boolean isFirstAttestationOpen = true;
    }

    public static class CDEData{
        public static String login = null;
        public static String password = null;
        // PointsPage
        public static String GROUP = null;
        public static String CUR_GROUP = null;
        public static String SELECTED_YEAR = null;
        public static ArrayList<String> YEARS = new ArrayList<String>();
        public static ArrayList<String> SUBJECTS1 = new ArrayList<String>();
        public static ArrayList<String> POINTS1 = new ArrayList<String>();
        public static ArrayList<String> CONTROL1 = new ArrayList<String>();
        public static ArrayList<String> SUBJECTS2 = new ArrayList<String>();
        public static ArrayList<String> POINTS2 = new ArrayList<String>();
        public static ArrayList<String> CONTROL2 = new ArrayList<String>();
        public static ArrayList<String> URLS = new ArrayList<String>();

        // ProtocolChangesPage
        public static ArrayList<String> P_SUBJECTS = new ArrayList<String>();
        public static ArrayList<String> P_DESCRIPTION = new ArrayList<String>();
        public static ArrayList<String> P_USERPOINTS = new ArrayList<String>();

        public static String first_P_subject = "---";     // use for background AsyncTaskGetProtocolFirstRow for NewPointsNotificationService
        public static String first_P_description = "---";
        public static String first_P_point = "---";

        // RatingPage
        public static ArrayList<String> R_FACULTY = new ArrayList<String>();
        public static ArrayList<String> R_COURSE = new ArrayList<String>();
        public static ArrayList<String> R_POSITION = new ArrayList<String>();

        //SessionSchedulePage
        public static ArrayList<String> SS_TEACHERS = new ArrayList<String>();
        public static ArrayList<String> SS_SUBJECTS = new ArrayList<String>();
        //public static ArrayList<String> SS_EXAMS = new ArrayList<String>();
        //public static ArrayList<String> SS_CONS = new ArrayList<String>();

        public static ArrayList<String> SS_EXAM_TIME = new ArrayList<String>();
        public static ArrayList<String> SS_EXAM_DATE = new ArrayList<String>();
        public static ArrayList<String> SS_EXAM_PLACE = new ArrayList<String>();
        public static ArrayList<String> SS_CONS_TIME = new ArrayList<String>();
        public static ArrayList<String> SS_CONS_DATE = new ArrayList<String>();
        public static ArrayList<String> SS_CONS_PLACE = new ArrayList<String>();

        //SchedulePage
        public static ArrayList<ArrayList<String>> S_DATA_EVEN = new ArrayList<ArrayList<String>>();
        public static ArrayList<ArrayList<String>> S_DATA_OVEN = new ArrayList<ArrayList<String>>();

        public static ArrayList<ArrayList<String>> S_DATA = new ArrayList<ArrayList<String>>();

        public static String ParityOfWeek = "";
        public static int WeekNumber = 0;

        //ScheduleTeacherPage
        public static ArrayList<ArrayList<String>> ST_DATA = new ArrayList<ArrayList<String>>();

        //ScheduleAttestationPage
            // String - Неделя
            // ArrayList<ArrayList> - Названия предметов
            // ArrayList - названия тестов
        public static HashMap<String, HashMap<String, ArrayList<String>>> SA_DATA = new HashMap<String, HashMap<String, ArrayList<String>>>();
        public static boolean attestationNotFound = false;

        //SubjectDetailsInfoPage
        public static ArrayList<String> SD_NUMBER = new ArrayList<String>();
        public static ArrayList<String> SD_TITLE = new ArrayList<String>();
        public static ArrayList<String> SD_RATE = new ArrayList<String>();
        public static ArrayList<String> SD_DATE = new ArrayList<String>();
        public static String CURRENT_RATE = "";

        public static void clearPointsData(){
            POINTS1.clear();
            POINTS2.clear();
            SUBJECTS1.clear();
            SUBJECTS2.clear();
            CONTROL1.clear();
            CONTROL2.clear();
            YEARS.clear();
            URLS.clear();
        }

        public static void clearSubjectDetailsData(){
            SD_NUMBER.clear();
            SD_TITLE.clear();
            SD_RATE.clear();
            SD_DATE.clear();
        }

        public static void clearAllData(){
            POINTS1.clear();
            POINTS2.clear();
            SUBJECTS1.clear();
            SUBJECTS2.clear();
            CONTROL1.clear();
            CONTROL2.clear();
            YEARS.clear();
            URLS.clear();

            P_DESCRIPTION.clear();
            P_USERPOINTS.clear();
            P_SUBJECTS.clear();

            R_POSITION.clear();
            R_COURSE.clear();
            R_FACULTY.clear();

            SS_SUBJECTS.clear();
            SS_TEACHERS.clear();
            SS_EXAM_PLACE.clear();
            SS_EXAM_DATE.clear();
            SS_EXAM_TIME.clear();
            SS_CONS_PLACE.clear();
            SS_CONS_DATE.clear();
            SS_CONS_TIME.clear();
            //SS_EXAMS.clear();
            //SS_CONS.clear();

            SD_NUMBER.clear();
            SD_TITLE.clear();
            SD_RATE.clear();
            SD_DATE.clear();

            S_DATA_EVEN.clear();
            S_DATA_OVEN.clear();
            S_DATA.clear();

            ST_DATA.clear();

            SA_DATA.clear();

            DataLoaded.Points = false;
            DataLoaded.Protocol = false;
            DataLoaded.Rating = false;
            DataLoaded.Schedule = false;
            DataLoaded.ScheduleSession = false;

            CURRENT_RATE = "";
            CUR_GROUP = "";
            GROUP = null;
            SELECTED_YEAR = null;
            login = null;
            password = null;

            ParityOfWeek = "";
            WeekNumber = 0;

            first_P_subject = "---";
            first_P_description = "---";
            first_P_point = "---";

            ParityOfWeek = "";
        }
    }

    public static class DataLoaded{
        public static boolean Points = false;
        public static boolean Protocol = false;
        public static boolean Rating = false;
        public static boolean Schedule = false;
        public static boolean ScheduleSession = false;
        public static boolean ScheduleTeacher = false;
        public static boolean ScheduleAttestation = false;
    }

    public static class Loading{
        public static boolean Points = false;
        public static boolean Protocol = false;
        public static boolean Rating = false;
        public static boolean Schedule = false;
        public static boolean ScheduleSession = false;
        public static boolean ScheduleTeacher = false;
        public static boolean ScheduleAttestation = false;
    }

    public static class Debug{
        public static String LOG_TAG = "cdelogs";
    }

    public static class Fragments{
        public static DialogFragment fragment_year;
    }
}
