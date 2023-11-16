package assessment.client;

import java.net.Socket;

public class Main {
    private final static int DEFAULT_PORT = 3000;
    private final static String DEFAULT_HOST = "localhost";

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_PORT;
        String host = DEFAULT_HOST;

        if (args.length == 1) {
            port = Integer.parseInt(args[0].trim());
        } else if (args.length == 2) {
            host = args[0].trim();
            port = Integer.parseInt(args[1].trim());
        } else if (args.length <= 0) {
            
        }

        // System.out.printf("%s, %d%n", host, port);

        try (Socket socket =  new Socket(host, port)) {
            PurchaseSession session = new PurchaseSession(socket);
            session.start();
        }
        
    }
    
}
