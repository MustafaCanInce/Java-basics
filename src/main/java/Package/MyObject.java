package Package;

import java.util.List;

class MyObject {
    private int siparis;
    private List<Urun> urunler;

    // Getter ve Setter
    public int getSiparis() {
        return siparis;
    }

    public void setSiparis(int siparis) {
        this.siparis = siparis;
    }

    public List<Urun> getUrunler() {
        return urunler;
    }

    public void setUrunler(List<Urun> urunler) {
        this.urunler = urunler;
    }

    @Override
    public String toString() {
        return "MyObject{" +
                "siparis=" + siparis +
                ", urunler=" + urunler +
                '}';
    }
}

class Urun {
    private int mal_numarasi;
    private int miktar;
    private double birim_fiyat;

    // Getter ve Setter
    public int getMal_numarasi() {
        return mal_numarasi;
    }

    public void setMal_numarasi(int mal_numarasi) {
        this.mal_numarasi = mal_numarasi;
    }

    public int getMiktar() {
        return miktar;
    }

    public void setMiktar(int miktar) {
        this.miktar = miktar;
    }

    public double getBirim_fiyat() {
        return birim_fiyat;
    }

    public void setBirim_fiyat(double birim_fiyat) {
        this.birim_fiyat = birim_fiyat;
    }

    @Override
    public String toString() {
        return "Urun{" +
                "mal_numarasi=" + mal_numarasi +
                ", miktar=" + miktar +
                ", birim_fiyat=" + birim_fiyat +
                '}';
    }
}
