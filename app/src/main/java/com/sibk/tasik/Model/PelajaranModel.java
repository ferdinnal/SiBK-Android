package com.sibk.tasik.Model;

public class PelajaranModel {
    private String kodeMataPelajaran;
    private String namaMataPelajaran;
    private String namaGuru;
    private String tahunAjaran;
    private int idMataPelajaranGuru;
    private int idKelas;
    private int idJadwalPelajaran;

    public String getTahunAjaran() {
        return tahunAjaran;
    }

    public void setTahunAjaran(String tahunAjaran) {
        this.tahunAjaran = tahunAjaran;
    }

    public int getIdJadwalPelajaran() {
        return idJadwalPelajaran;
    }

    public void setIdJadwalPelajaran(int idJadwalPelajaran) {
        this.idJadwalPelajaran = idJadwalPelajaran;
    }


    public int getIdKelas() {
        return idKelas;
    }

    public void setIdKelas(int idKelas) {
        this.idKelas = idKelas;
    }

    public int getIdMataPelajaranGuru() {
        return idMataPelajaranGuru;
    }

    public void setIdMataPelajaranGuru(int idMataPelajaranGuru) {
        this.idMataPelajaranGuru = idMataPelajaranGuru;
    }

    public String getKodeMataPelajaran() {
        return kodeMataPelajaran;
    }

    public void setKodeMataPelajaran(String kodeMataPelajaran) {
        this.kodeMataPelajaran = kodeMataPelajaran;
    }

    public String getNamaMataPelajaran() {
        return namaMataPelajaran;
    }

    public void setNamaMataPelajaran(String namaMataPelajaran) {
        this.namaMataPelajaran = namaMataPelajaran;
    }

    public String getNamaGuru() {
        return namaGuru;
    }

    public void setNamaGuru(String namaGuru) {
        this.namaGuru = namaGuru;
    }

}
