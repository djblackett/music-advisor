package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Model {

    Controller controller;


    public static void getCode() throws UnsupportedEncodingException, InterruptedException {

        System.out.println("Use this link to request the access code:");
        String path = "/authorize?";
//        String getRequestQueries = "client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=" + encodeValue("http://localhost:8080");
//        String scope = "&scope=" + enc;odeValue("user-read-private user-read-email");
        String getRequestQueries = "client_id=" + CLIENT_ID + "&redirect_uri=" + "http://localhost:8080" + "&response_type=code";
        String scope = "&scope=" + encodeValue("user-read-private user-read-email");
        Thread.sleep(2000);
        System.out.println("https://accounts.spotify.com" + path + getRequestQueries);

        System.out.println("waiting for code...");
        Thread.sleep(2000);
    }

    public void getToken(String code) throws InterruptedException, IOException {
        HttpClient client = HttpClient.newBuilder().build();

        System.out.println("making http request for access_token...");


        HttpRequest postRequest = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(baseURI + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=" + GRANT_TYPE + "&code=" + ACCESS_CODE + "&redirect_uri=" + REDIRECT_URI + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET))
                .build();

        try {
            HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());


            System.out.println("response:");
            String token = "";


            //while (token.equals("") || postResponse.body() == null) {
            //System.out.println("bob");
            Thread.sleep(100);
            //System.out.println("bob2");
            token = postResponse.body();
//            }
            System.out.println(token);
            if (token.contains("access_token")) {
                this.authorized = true;
                JsonObject json = JsonParser.parseString(token).getAsJsonObject();
                ACCESS_TOKEN = json.get("access_token").getAsString();
                System.out.println("---SUCCESS---");
            }
            controller.getInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());

    }
}
