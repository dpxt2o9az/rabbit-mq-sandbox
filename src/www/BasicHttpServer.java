package www;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

public class BasicHttpServer {

    public static void main(String[] args) throws Exception {
        RPCClient rpcClient = new RPCClient();
        RPCClientHttpHandler handler = new RPCClientHttpHandler(rpcClient);

        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        HttpContext context = server.createContext("/example");

        context.setHandler(handler);
        server.start();
    }

    static class RPCClientHttpHandler implements HttpHandler {

        private final RPCClient client;

        public RPCClientHttpHandler(RPCClient client) {
            this.client = client;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI requestURI = exchange.getRequestURI();
            printRequestInfo(exchange);
            String response = "This is the response at " + requestURI;
            String message = callRPC(exchange);
            response = response + " fib=" + message;
            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String callRPC(HttpExchange x) throws IOException {
            URI uri = x.getRequestURI();
            String query = uri.getQuery();
            String[] parms = query.split("\\&");
            String value = null;
            for (String p : parms) {
                String[] kvp = p.split("\\=");
                if (kvp[0].equals("v")) {
                    value = kvp[1];
                }
            }
            try {
                String message = client.call(value);
                return message;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return null;
        }

        private static void printRequestInfo(HttpExchange exchange) {
            System.out.println("-- headers --");
            Headers requestHeaders = exchange.getRequestHeaders();
            requestHeaders.entrySet().forEach(System.out::println);

            System.out.println("-- principle --");
            HttpPrincipal principal = exchange.getPrincipal();
            System.out.println(principal);

            System.out.println("-- HTTP Method --");
            String requestMethod = exchange.getRequestMethod();
            System.out.println(requestMethod);

            System.out.println("-- query --");
            URI requestURI = exchange.getRequestURI();
            String query = requestURI.getQuery();
            System.out.println(query);
        }

    }

}
