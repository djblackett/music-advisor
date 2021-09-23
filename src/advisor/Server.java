package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    HttpServer server;
    String ACCESS_CODE;


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


}



