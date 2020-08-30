package com.sibk.tasik.Api;

import com.sibk.tasik.Utility.Website;

public class UserApi {
    public static Website web = new Website();
    public static String POST_FIND_USER = web.getNewDomain() + "/user/find?hash=" + web.getHash();
    public static String POST_LOGIN_USER = web.getNewDomain() + "/user/login?hash=" + web.getHash();
    public static String POST_UPDATE_EMAIL= web.getNewDomain() + "/user/update_email?hash=" + web.getHash();
    public static String POST_UPDATE_PASSWORD= web.getNewDomain() + "/user/update_password?hash=" + web.getHash();

}
