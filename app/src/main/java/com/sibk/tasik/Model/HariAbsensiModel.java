package com.sibk.tasik.Model;

public class HariAbsensiModel {
    private int hari;

    public String getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai;
    }

    private String jamMulai;
    private String jamBeres;
    private String namaKelas;
    private String namaMataPelajaran;
    private String kodeMataPelajaran;
    private int historyQrCodeId;

    public int getHistoryQrCodeId() {
        return historyQrCodeId;
    }

    public void setHistoryQrCodeId(int historyQrCodeId) {
        this.historyQrCodeId = historyQrCodeId;
    }

    public int getIdJadwalPelajaran() {
        return idJadwalPelajaran;
    }

    public void setIdJadwalPelajaran(int idJadwalPelajaran) {
        this.idJadwalPelajaran = idJadwalPelajaran;
    }

    private int idJadwalPelajaran;

    public int getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    private int idKelas;

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    private String dateCreated;

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setNamaKelas(String namaKelas) {
        this.namaKelas = namaKelas;
    }

    public String getNamaMataPelajaran() {
        return namaMataPelajaran;
    }

    public void setNamaMataPelajaran(String namaMataPelajaran) {
        this.namaMataPelajaran = namaMataPelajaran;
    }

    public String getKodeMataPelajaran() {
        return kodeMataPelajaran;
    }

    public void setKodeMataPelajaran(String kodeMataPelajaran) {
        this.kodeMataPelajaran = kodeMataPelajaran;
    }


    public int getHari() {
        return hari;
    }

    public void setHari(int hari) {
        this.hari = hari;
    }

    public String getJamBeres() {
        return jamBeres;
    }

    public void setJamBeres(String jamBeres) {
        this.jamBeres = jamBeres;
    }

}
