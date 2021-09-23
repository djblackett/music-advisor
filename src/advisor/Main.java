package advisor;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String CLIENT_ID = "80be6d61403e43a9af639a464ee12462";
    public static String REDIRECT_URI = "http://localhost:8080";

    public static String CLIENT_SECRET = "1f32240541fa422c89cc85cf47dc4007";
    public static String ACCESS_TOKEN = "";
    public static String ACCESS_CODE = "";
    private static String apiPath;

    Map<String, String> categoriesMap = new HashMap<>();
    private static String baseURI;
    private static final String GRANT_TYPE = "authorization_code";
    HttpServer server;
    Gson gson = new Gson();

    private boolean authorized = false;


    public static void main(String[] args) throws IOException, InterruptedException {

        Main main = new Main();
        Gson gson = new Gson();


        baseURI = "https://accounts.spotify.com";
        apiPath = "https://api.spotify.com";

        if (args.length >= 4) {
            if (args[0].equals("-access")) {
                baseURI = args[1];
            }

            if (args[2].equals("-resource")) {
                apiPath = args[3];
            }
        }

        System.out.println(baseURI);
        System.out.println(apiPath);

        main.initServer();
        Thread.sleep(2000);
        main.getInput();

    }

    private void initServer() throws IOException {
        if (server == null) {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
        }

        server.createContext("/",
                new HttpHandler() {
                    public void handle(HttpExchange exchange) throws IOException {

                        String query = null;
                        //while (query == null || query.length() == 0) {
                            query = exchange.getRequestURI().getQuery();
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                       // }
                       // server.stop(10);


                            String message = "";

//                        System.out.println(query);
//                        System.out.println(query == null);

                            if (query == null || query.contains("error")) {
                                message = "Authorization code not found. Try again.";
                                exchange.sendResponseHeaders(200, message.length());
                                exchange.getResponseBody().write(message.getBytes());
                                exchange.getResponseBody().close();
                            } else if (query == null || query.contains("code")) {
                                message = "Got the code. Return back to your program.";
                                ACCESS_CODE = query.substring(5);
                                exchange.sendResponseHeaders(200, message.length());
                                exchange.getResponseBody().write(message.getBytes());
                                exchange.getResponseBody().close();
                                //System.out.println(ACCESS_CODE);
                                try {
                                    System.out.println("code received");

                                    getToken(ACCESS_CODE);
                                    authorized = true;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                               // message = "oops. no idea what the hell happened";
                                return;
                            }



                        }
                    });
        server.setExecutor(null);
        server.start();
    }

    private void getInput() throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));



        while (true) {
            String input = reader.readLine();
            String category = "";
            if (input.trim().contains(" ")) {
                String[] split = input.trim().split(" ");

                if (split.length > 1) {
                    category = input.substring(input.indexOf(" ")+1);
                }
                input = split[0];
            }

            if (!authorized && !input.equals("auth") && !input.equals("exit")) {
                System.out.println("Please, provide access for application.");
                continue;
            }
            switch (input) {
                case "auth":
                    getCode();
                    break;
                case "new":
                    HttpRequest httpRequest = HttpRequest.newBuilder()
                            .header("Authorization", "Bearer " + ACCESS_TOKEN)
                            .uri(URI.create(apiPath + "/v1/browse/new-releases"))
                            .GET()
                            .build();
                    HttpClient client = HttpClient.newBuilder().build();
                    HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                    JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                    JsonElement albums =  json.get("albums");

                    JsonArray items = albums.getAsJsonObject().get("items").getAsJsonArray();

                    for (JsonElement j : items) {
                        JsonArray artistsArr = j.getAsJsonObject().get("artists").getAsJsonArray();

                        List<String> artists = new ArrayList<>();
                        List<String> links = new ArrayList<>();
                        String link = j.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();

                        for (JsonElement k : artistsArr) {
                            String name = k.getAsJsonObject().get("name").getAsString();
                            artists.add(name);

                            //String url = k.getAsJsonObject();
                            //links.add(url);
                        }
                        String title = j.getAsJsonObject().get("name").getAsString();


                        System.out.println(title);
                        String artistList = artists.stream().reduce("", (text, name) -> text + ", " + name);
                        artistList = artistList.substring(2);
                        System.out.println("[" + artistList + "]");
                        System.out.println(link);
                        System.out.println();
                    }

                    break;
                case "featured":
                     httpRequest = HttpRequest.newBuilder()
                            .header("Authorization", "Bearer " + ACCESS_TOKEN)
                            .uri(URI.create(apiPath + "/v1/browse/featured-playlists"))
                            .GET()
                            .build();
                     client = HttpClient.newBuilder().build();
                     response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                    json = JsonParser.parseString(response.body()).getAsJsonObject();
                    System.out.println(json);
                    JsonElement playlists = json.get("playlists");
                    System.out.println(playlists);
                    JsonArray jsonArray = playlists.getAsJsonObject().get("items").getAsJsonArray();

                    for (JsonElement j : jsonArray) {
                        String name = j.getAsJsonObject().get("name").getAsString();
                        String url = j.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();

                        System.out.println(name);
                        System.out.println(url);
                        System.out.println();
                    }


                    break;
                case "categories":

                    httpRequest = HttpRequest.newBuilder()
                            .header("Authorization", "Bearer " + ACCESS_TOKEN)
                            .uri(URI.create(apiPath + "/v1/browse/categories"))
                            .GET()
                            .build();
                    client = HttpClient.newBuilder().build();
                    response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                    json = JsonParser.parseString(response.body()).getAsJsonObject();
                    //System.out.println(json);
                    JsonElement categories =  json.get("categories");

                    JsonArray  arr = categories.getAsJsonObject().get("items").getAsJsonArray();

                    for (JsonElement j : arr) {
                        System.out.println(j.getAsJsonObject().get("name").getAsString());
                    }
                    break;

                case "playlists":

                    HttpRequest getCategories = HttpRequest.newBuilder()
                            .header("Authorization", "Bearer " + ACCESS_TOKEN)
                            .uri(URI.create(apiPath + "/v1/browse/categories"))
                            .GET()
                            .build();
                    client = HttpClient.newBuilder().build();
                    response = client.send(getCategories, HttpResponse.BodyHandlers.ofString());

                    JsonObject categoriesJson = JsonParser.parseString(response.body()).getAsJsonObject();

                    JsonElement categoriesElement =  categoriesJson.get("categories");

                    JsonArray  arrayItems = categoriesElement.getAsJsonObject().get("items").getAsJsonArray();

                    if (categoriesMap.size() == 0) {
                        for (JsonElement j : arrayItems) {
                            categoriesMap.put(j.getAsJsonObject().get("name").getAsString(), j.getAsJsonObject().get("id").getAsString());
                        }
                    }
                    String category_id = "";
                    if (categoriesMap.containsKey(category)) {
                        category_id = categoriesMap.get(category);
                }
//                    else if (categoriesMap.containsValue(category)){
//                        category_id = category;
//                    }

                    try {
                        httpRequest = HttpRequest.newBuilder()
                                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                                .uri(URI.create(apiPath + "/v1/browse/categories/" + category + "/playlists"))
                                .GET()
                                .build();
                    } catch (Exception e) {
                        System.out.println("Test unpredictable error message");
                    }

                    if (!categoriesMap.containsKey(category) && !categoriesMap.containsValue(category)){
                        System.out.println("Unknown category name.");
                        continue;
                    }


                    httpRequest = HttpRequest.newBuilder()
                            .header("Authorization", "Bearer " + ACCESS_TOKEN)
                            .uri(URI.create(apiPath + "/v1/browse/categories/" + category_id + "/playlists"))
                            .GET()
                            .build();
                    client = HttpClient.newBuilder().build();
                    response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                    json = JsonParser.parseString(response.body()).getAsJsonObject();
                   // System.out.println(json);
                    playlists = json.get("playlists");
                    //System.out.println(playlists);
                    jsonArray = playlists.getAsJsonObject().get("items").getAsJsonArray();

                    for (JsonElement j : jsonArray) {
                        String name = j.getAsJsonObject().get("name").getAsString();
                        String url = j.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();

                        System.out.println(name);
                        System.out.println(url);
                        System.out.println();
                    }

                    break;
                case "exit": {
                    System.out.println("---GOODBYE!---");
                    server.stop(1);

                    System.exit(0);
                }
            }
        }
    }

    private static String encodeValue(String value) throws UnsupportedEncodingException {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());

    }

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
            getInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
