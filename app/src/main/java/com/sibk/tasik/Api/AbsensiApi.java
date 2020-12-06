package com.sibk.tasik.Api;

import com.sibk.tasik.Utility.Website;

public class AbsensiApi {
    public static Website web = new Website();
    public static String POST_GENERATE = web.getNewDomain() + "/Absensi/generate_qr?hash=" + web.getHash();
    public static String POST_FIND_HARI = web.getNewDomain() + "/Absensi/find_hari_siswa?hash=" + web.getHash();
    public static String POST_FIND_ABSENSI= web.getNewDomain() + "/Absensi/find_absensi_siswa?hash=" + web.getHash();
    public static String POST_UPDATE_GURU= web.getNewDomain() + "/Absensi/update_by_guru?hash=" + web.getHash();
    public static String POST_PROSES_ABSENSI= web.getNewDomain() + "/Absensi/absensi_siswa?hash=" + web.getHash();
    public static String POST_FIND_PELAJARAN_SISWA = web.getNewDomain() + "/Absensi/findBySiswaNewJadwal?hash=" + web.getHash();
    public static String POST_FIND_PELAJARAN_GURU = web.getNewDomain() + "/Absensi/findByGuruNewJadwal?hash=" + web.getHash();
    public static String POST_FIND_HARI_NEW = web.getNewDomain() + "/Absensi/find_hariNEw?hash=" + web.getHash();

}

