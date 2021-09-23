package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    void getInput() throws IOException, InterruptedException {
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
}
