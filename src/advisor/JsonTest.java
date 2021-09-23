//package advisor;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class JsonTest {
//
//    public static void main(String[] args) {
//        String json = """
//                            {
//                              "albums" : {
//                                "href" : "https://api.spotify.com/v1/browse/new-releases?country=SE&offset=0&limit=20",
//                                "items" : [ {
//                                  "album_type" : "single",
//                                  "artists" : [ {
//                                    "external_urls" : {
//                                      "spotify" : "https://open.spotify.com/artist/2RdwBSPQiwcmiDo9kixcl8"
//                                    },
//                                    "href" : "https://api.spotify.com/v1/artists/2RdwBSPQiwcmiDo9kixcl8",
//                                    "id" : "2RdwBSPQiwcmiDo9kixcl8",
//                                    "name" : "Pharrell Williams",
//                                    "type" : "artist",
//                                    "uri" : "spotify:artist:2RdwBSPQiwcmiDo9kixcl8"
//                                  } ],
//                                  "available_markets" : [ "AD", "AR", "AT", "AU", "BE", "BG", "BO", "BR", "CA", "CH", "CL", "CO", "CR", "CY", "CZ", "DE", "DK", "DO", "EC", "EE", "ES", "FI", "FR", "GB", "GR", "GT", "HK", "HN", "HU", "ID", "IE", "IS", "IT", "JP", "LI", "LT", "LU", "LV", "MC", "MT", "MX", "MY", "NI", "NL", "NO", "NZ", "PA", "PE", "PH", "PL", "PT", "PY", "SE", "SG", "SK", "SV", "TR", "TW", "US", "UY" ],
//                                  "external_urls" : {
//                                    "spotify" : "https://open.spotify.com/album/5ZX4m5aVSmWQ5iHAPQpT71"
//                                  },
//                                  "href" : "https://api.spotify.com/v1/albums/5ZX4m5aVSmWQ5iHAPQpT71",
//                                  "id" : "5ZX4m5aVSmWQ5iHAPQpT71",
//                                  "images" : [ {
//                                    "height" : 640,
//                                    "url" : "https://i.scdn.co/image/e6b635ebe3ef4ba22492f5698a7b5d417f78b88a",
//                                    "width" : 640
//                                  }, {
//                                    "height" : 300,
//                                    "url" : "https://i.scdn.co/image/92ae5b0fe64870c09004dd2e745a4fb1bf7de39d",
//                                    "width" : 300
//                                  }, {
//                                    "height" : 64,
//                                    "url" : "https://i.scdn.co/image/8a7ab6fc2c9f678308ba0f694ecd5718dc6bc930",
//                                    "width" : 64
//                                  } ],
//                                  "name" : "Runnin'",
//                                  "type" : "album",
//                                  "uri" : "spotify:album:5ZX4m5aVSmWQ5iHAPQpT71"
//                                }, {
//                                  "album_type" : "single",
//                                  "artists" : [ {
//                                    "external_urls" : {
//                                      "spotify" : "https://open.spotify.com/artist/3TVXtAsR1Inumwj472S9r4"
//                                    },
//                                    "href" : "https://api.spotify.com/v1/artists/3TVXtAsR1Inumwj472S9r4",
//                                    "id" : "3TVXtAsR1Inumwj472S9r4",
//                                    "name" : "Drake",
//                                    "type" : "artist",
//                                    "uri" : "spotify:artist:3TVXtAsR1Inumwj472S9r4"
//                                  } ],
//                                  "available_markets" : [ "AD", "AR", "AT", "AU", "BE", "BG", "BO", "BR", "CH", "CL", "CO", "CR", "CY", "CZ", "DE", "DK", "DO", "EC", "EE", "ES", "FI", "FR", "GB", "GR", "GT", "HK", "HN", "HU", "ID", "IE", "IS", "IT", "JP", "LI", "LT", "LU", "LV", "MC", "MT", "MY", "NI", "NL", "NO", "NZ", "PA", "PE", "PH", "PL", "PT", "PY", "SE", "SG", "SK", "SV", "TR", "TW", "UY" ],
//                                  "external_urls" : {
//                                    "spotify" : "https://open.spotify.com/album/0geTzdk2InlqIoB16fW9Nd"
//                                  },
//                                  "href" : "https://api.spotify.com/v1/albums/0geTzdk2InlqIoB16fW9Nd",
//                                  "id" : "0geTzdk2InlqIoB16fW9Nd",
//                                  "images" : [ {
//                                    "height" : 640,
//                                    "url" : "https://i.scdn.co/image/d40e9c3d22bde2fbdb2ecc03cccd7a0e77f42e4c",
//                                    "width" : 640
//                                  }, {
//                                    "height" : 300,
//                                    "url" : "https://i.scdn.co/image/dff06a3375f6d9b32ecb081eb9a60bbafecb5731",
//                                    "width" : 300
//                                  }, {
//                                    "height" : 64,
//                                    "url" : "https://i.scdn.co/image/808a02bd7fc59b0652c9df9f68675edbffe07a79",
//                                    "width" : 64
//                                  } ],
//                                  "name" : "Sneakin’",
//                                  "type" : "album",
//                                  "uri" : "spotify:album:0geTzdk2InlqIoB16fW9Nd"
//                                }],
//                                "limit" : 20,
//                                "next" : "https://api.spotify.com/v1/browse/new-releases?country=SE&offset=20&limit=20",
//                                "offset" : 0,
//                                "previous" : null,
//                                "total" : 500
//                              }
//                            }
//                """;
//
//        JsonObject jsonObj = JsonParser.parseString(json).getAsJsonObject();
//        JsonElement albums =  jsonObj.get("albums");
//
//        JsonArray items = albums.getAsJsonObject().get("items").getAsJsonArray();
//
//        for (JsonElement j : items) {
//            JsonArray artistsArr = j.getAsJsonObject().get("artists").getAsJsonArray();
//
//            List<String> artists = new ArrayList<>();
//            List<String> links = new ArrayList<>();
//
//            for (JsonElement k : artistsArr) {
//                String name = k.getAsJsonObject().get("name").getAsString();
//                artists.add(name);
//
//                String url = k.getAsJsonObject().get("external_urls").getAsJsonObject().get("spotify").getAsString();
//                links.add(url);
//            }
//            String title = j.getAsJsonObject().get("name").getAsString();
//
//
//            System.out.println(title);
//            String artistList = artists.stream().reduce("", (text, name) -> text + ", " + name);
//            artistList = artistList.substring(2);
//            System.out.println("[" + artistList + "]");
//            System.out.println(links.get(0));
//            System.out.println();
//        }
//
//    }
//
//}
