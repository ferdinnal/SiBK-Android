package com.sibk.tasik.Api;

import com.sibk.tasik.Utility.Website;

public class BimbinganApi {
    public static Website web = new Website();
    public static String POST_FINDS_JADWAL = web.getNewDomain() + "/Bimbingan/finds?hash=" + web.getHash();
}
