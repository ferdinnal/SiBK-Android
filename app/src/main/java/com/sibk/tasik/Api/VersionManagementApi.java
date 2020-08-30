package com.sibk.tasik.Api;

import com.sibk.tasik.Utility.Website;

public class VersionManagementApi {
    public static  Website web = new Website();
    public static String POST_VERSION_MANAGEMENT = web.getNewDomain() + "/Version_management/is_need_update?hash=" + web.getHash();

}
