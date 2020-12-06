package com.sibk.tasik.Api;

import com.sibk.tasik.Utility.Website;

public class NotifikasiApi {
    public static Website web = new Website();
    public static String POST_FIND_INTERVAL = web.getNewDomain() + "/Notifikasi/finds_interval?hash=" + web.getHash();
    public static String POST_UPDATE = web.getNewDomain() + "/Notifikasi/update?hash=" + web.getHash();
}
