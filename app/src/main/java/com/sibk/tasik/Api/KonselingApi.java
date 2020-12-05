package com.sibk.tasik.Api;

import com.sibk.tasik.Utility.Website;

public class KonselingApi {
    public static Website web = new Website();
    public static String POST_FINDS_JADWAL = web.getNewDomain() + "/Konseling/finds?hash=" + web.getHash();
    public static String POST_FINDS_JADWAL2 = web.getNewDomain() + "/Konseling/finds2?hash=" + web.getHash();
    public static String POST_PENGAJUAN = web.getNewDomain() + "/Konseling/tambah?hash=" + web.getHash();

}
