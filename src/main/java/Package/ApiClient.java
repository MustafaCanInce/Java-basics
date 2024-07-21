package Package;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {
    // GET ve POST istekleri için kullanılacak URL'ler
    private static final String GET_URL = "https://jsonplaceholder.typicode.com/posts/1";
    private static final String POST_URL = "https://jsonplaceholder.typicode.com/posts";
    // POST isteği ile gönderilecek JSON verisi
    private static final String POST_PARAMS = "{\"userId\":1,\"title\":\"foo\",\"body\":\"bar\"}";

    // GET isteği gönderen fonksiyon
    public static int sendGetRequest() throws Exception {
        // GET isteği yapılacak URL'yi oluşturur
        URL url = new URL(GET_URL);
        // URL bağlantısını açar ve HTTP bağlantısı olarak döndürür
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Bağlantı için HTTP metodunu GET olarak ayarlar
        connection.setRequestMethod("GET");

        // Sunucudan gelen HTTP yanıt kodunu alır
        int responseCode = connection.getResponseCode();
        System.out.println("GET Yanıt Kodu :: " + responseCode);

        // Yanıt kodu HTTP 200 ise işlemi başarılı olarak kabul eder
        if (responseCode == HttpURLConnection.HTTP_OK) { // Başarı
            // Sunucudan gelen yanıtı okuyor
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // Gelen yanıtı satır satır okuyup StringBuilder'a ekler
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // Tam yanıtı döndürür
            Main.setGetResponse(response.toString());
            return responseCode;
        } else {
            // Yanıt kodu 200 değilse bir hata olarak döndürür
            throw new RuntimeException("GET isteği çalışmadı");
        }
    }

    // POST isteği gönderen fonksiyon
    public static int sendPostRequest() throws Exception {
        // POST isteği yapılacak URL'yi oluşturur
        URL url = new URL(POST_URL);
        // URL bağlantısını açar ve HTTP bağlantısı olarak döndürür
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Bağlantı için HTTP metodunu POST olarak ayarlar
        connection.setRequestMethod("POST");
        // POST isteği için Content-Type başlığını ayarlar
        connection.setRequestProperty("Content-Type", "application/json");

        // Sunucuya veri göndermek için bağlantının çıktısını aktif hale getirir
        connection.setDoOutput(true);
        // Bağlantının çıktısını alır
        OutputStream os = connection.getOutputStream();
        // POST parametrelerini çıktısına yazar
        os.write(POST_PARAMS.getBytes());
        // Çıktıyı temizler ve kapatır
        os.flush();
        os.close();

        // Sunucudan gelen HTTP yanıt kodunu alır
        int responseCode = connection.getResponseCode();
        System.out.println("POST Yanıt Kodu :: " + responseCode);

        // Yanıt kodu HTTP 201 ise işlemi başarılı olarak kabul eder
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // Gelen yanıtı satır satır okuyup StringBuilder'a ekler
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // Tam yanıtı döndürür
            Main.setPostResponse(response.toString());
            return responseCode;
        } else {
            // Yanıt kodu 201 değilse bir hata olarak döndürür
            throw new RuntimeException("POST isteği çalışmadı");
        }
    }
}
