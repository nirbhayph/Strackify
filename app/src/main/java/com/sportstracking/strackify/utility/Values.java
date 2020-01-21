package com.sportstracking.strackify.utility;

/**
 * strackify: values utility class
 * consists of api url, identifiers, countries json url
 * and values for keeping track of favorites with shared preferences
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

public class Values {
    // api urls
    public static final String ALL_SPORTS = "https://www.thesportsdb.com/api/v1/json/1/all_sports.php";
    public static final String TEAMS = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?";
    public static final String PAST_EVENTS = "https://www.thesportsdb.com/api/v1/json/1/eventslast.php?";
    public static final String UPCOMING_EVENTS = "https://www.thesportsdb.com/api/v1/json/1/eventsnext.php?";

    // identifier to use for calling the api
    public static final String SPORT_IDENTIFIER = "s=";
    public static final String COUNTRY_IDENTIFIER = "&c=";
    public static final String TEAM_ID_IDENTIFIER = "id=";

    // custom countries json url
    public static final String COUNTRIES = "https://ganskop.com/proxy/https://people.rit.edu/~np5318/countries_data/countries_data.json";

    // constants for shared preferences
    public static  final String SPORTS_SELECTION = "SPORTS_SELECTION";
    public static  final String COUNTRIES_SELECTION = "COUNTRIES_SELECTION";
    public static  final String TEAMS_SELECTION = "TEAMS_SELECTION" ;
    public static  final String PAST_EVENTS_DISPLAY = "PAST_EVENTS_DISPLAY" ;
    public static  final String UPCOMING_EVENTS_DISPLAY = "UPCOMING_EVENTS_DISPLAY" ;
    public static  final String SETTINGS_DISPLAY = "SETTINGS_DISPLAY" ;
    public static final String ABOUT_TEAM = "ABOUT_TEAM";
    public static final String SIGN_IN = "SIGN_IN";
    public static final String SIGN_IN_EMAIL = "SIGN_IN_EMAIL";
    public static final String SIGN_IN_NAME = "SIGN_IN_NAME";
    public static final String SIGN_IN_PROFILE_IMAGE = "SIGN_IN_PROFILE_IMAGE";
    public static final String INTRO_VISITED = "INTRO_VISITED";
    public static final String SIGN_OUT = "SIGN_OUT";

    // values for keeping track of favorites with shared preferences
    public static  String LATEST_FAV_TEAM;
    public static  String LATEST_FAV_TEAM_NAME;
    public static  String FAV_TEAMS;
    public static  String FAV_CHECKER;
    public static  String DEFAULT;
}
