package com.sibk.tasik.Api;

import com.sibk.tasik.Utility.Website;

public class JadwalPelajaranApi {
    public static Website web = new Website();
    public static String POST_FINDS_ALL = web.getNewDomain() + "/Jadwal_pelajaran/jadwal_pelajaran_all_siswa?hash=" + web.getHash();
    public static String POST_FINDS_GURU = web.getNewDomain() + "/Jadwal_pelajaran/jadwal_pelajaran_all_guru?hash=" + web.getHash();

}
