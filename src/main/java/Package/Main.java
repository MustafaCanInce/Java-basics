package Package;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static JsonObject statusCodes;
    private static String getResponse;
    private static String postResponse;

    public static void main(String[] args) {
        // Load status codes at the start
        statusCodes = loadStatusCodes();

        // JSON verilerini işlemek için Gson nesnesi oluşturuluyor
        Gson gson = new Gson();

        try (FileReader reader = new FileReader("src/main/resources/Data.json")) {
            // JSON dosyasını okuyup Java nesnesine dönüştürmek için TypeToken kullanılıyor
            // JSON dosyasının MyObject tipinde olduğunu belirtiyor
            Type myObjectType = new TypeToken<List<MyObject>>() {}.getType();
            List<MyObject> myObjects = gson.fromJson(reader, myObjectType);

            // Siparişlerdeki malların toplam tutarını hesaplıyor
            double toplamTutar = 0.0;
            for (MyObject siparis : myObjects) {
                for (Urun urun : siparis.getUrunler()) {
                    // Her bir ürünün miktarı ile birim fiyatını çarparak toplam tutarını buluyor
                    toplamTutar += urun.getMiktar() * urun.getBirim_fiyat();
                }
            }
            System.out.println();
            System.out.println("Siparişlerdeki malların toplam tutarı: " + toplamTutar);

            // Siparişlerdeki bütün malların ortalama fiyatını buluyor
            double toplamFiyat = 0.0;
            int toplamMiktar = 0;
            for (MyObject siparis : myObjects) {
                for (Urun urun : siparis.getUrunler()) {
                    // Her bir ürünün birim fiyatını toplam fiyata ekliyor
                    toplamFiyat += urun.getBirim_fiyat();
                    toplamMiktar++;
                }
            }
            // Ortalama fiyatı hesaplıyor
            double ortalamaFiyat = toplamFiyat / toplamMiktar;
            System.out.println();
            System.out.println("Siparişlerdeki bütün malların ortalama fiyatı: " + ortalamaFiyat);

            // Siparişlerdeki bütün malların tek tek mal bazlı ortalama fiyatını buluyor
            // Her bir malın toplam fiyatını ve miktarını hesaplıyor
            Map<Integer, Double> malBazliToplamFiyat = new HashMap<>();
            Map<Integer, Integer> malBazliMiktar = new HashMap<>();
            for (MyObject siparis : myObjects) {
                for (Urun urun : siparis.getUrunler()) {
                    malBazliToplamFiyat.put(urun.getMal_numarasi(),
                            malBazliToplamFiyat.getOrDefault(urun.getMal_numarasi(), 0.0) + urun.getBirim_fiyat());
                    malBazliMiktar.put(urun.getMal_numarasi(),
                            malBazliMiktar.getOrDefault(urun.getMal_numarasi(), 0) + 1);
                }
            }
            // Her bir malın ortalama fiyatını hesaplıyor
            System.out.println();
            System.out.println("Siparişlerdeki bütün malların tek tek mal bazlı ortalama fiyatları:");
            for (Map.Entry<Integer, Double> entry : malBazliToplamFiyat.entrySet()) {
                int malNumarasi = entry.getKey();
                double ortalama = entry.getValue() / malBazliMiktar.get(malNumarasi);
                System.out.println("Mal Numarası: " + malNumarasi + ", Ortalama Fiyat: " + ortalama);
            }

            // Mal bazlı, malların hangi siparişlerde kaç adet olduğunun hesaplıyor
            Map<Integer, Map<Integer, Integer>> malSiparisMiktar = new HashMap<>();
            for (MyObject siparis : myObjects) {
                for (Urun urun : siparis.getUrunler()) {
                    malSiparisMiktar.putIfAbsent(urun.getMal_numarasi(), new HashMap<>());
                    Map<Integer, Integer> siparisMiktar = malSiparisMiktar.get(urun.getMal_numarasi());
                    siparisMiktar.put(siparis.getSiparis(),
                            siparisMiktar.getOrDefault(siparis.getSiparis(), 0) + urun.getMiktar());
                }
            }
            System.out.println();
            System.out.println("Tek tek mal bazlı, malların hangi siparişlerde kaç adet olduğu:");

            for (Map.Entry<Integer, Map<Integer, Integer>> entry : malSiparisMiktar.entrySet()) {
                int malNumarasi = entry.getKey();
                Map<Integer, Integer> siparisMiktar = entry.getValue();
                System.out.print("Mal Numarası: " + malNumarasi + ", Siparişler: ");
                for (Map.Entry<Integer, Integer> siparisEntry : siparisMiktar.entrySet()) {
                    int siparisNumarasi = siparisEntry.getKey();
                    int miktar = siparisEntry.getValue();
                    System.out.print("Sipariş " + siparisNumarasi + ": " + miktar + " adet, ");
                }
                System.out.println();
            }
            System.out.println();
            // GET ve POST isteklerini gönderiyor
            int getResponseCode = ApiClient.sendGetRequest();
            int postResponseCode = ApiClient.sendPostRequest();

            printResponseWithExplanation(getResponse, getResponseCode);
            printResponseWithExplanation(postResponse, postResponseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setGetResponse(String response) {
        getResponse = response;
    }

    public static void setPostResponse(String response) {
        postResponse = response;
    }

    // Yanıt koduna göre açıklamayı yazdıran fonksiyon
    public static void printResponseWithExplanation(String response, int responseCode) {
        if (statusCodes != null && statusCodes.has(String.valueOf(responseCode))) {
            String explanation = statusCodes.getAsJsonPrimitive(String.valueOf(responseCode)).getAsString();
            System.out.println();
            System.out.println("Response Code: " + responseCode);
            System.out.println("Explanation: " + explanation);
        } else {
            System.out.println("Response Code: " + responseCode);
            System.out.println("Explanation: Not Found");
        }
        System.out.println("Response: " + response);
    }


    // Status kodlarını JSON dosyasından yükleyen fonksiyon
    private static JsonObject loadStatusCodes() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("src/main/resources/StatusCodes.json")) {
            return gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonObject();
        }
    }
}
