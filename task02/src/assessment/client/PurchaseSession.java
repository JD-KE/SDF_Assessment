package assessment.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PurchaseSession {
    
    private final Socket socket;

    public PurchaseSession(Socket socket) {
        this.socket = socket;
    }

    public void start() throws Exception {
        try (InputStream is = socket.getInputStream(); OutputStream os = socket.getOutputStream();) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);

            String requestId = "";
            int itemCount = 0;
            int count = 0;
            double budget = 0;
            List<Product> products = new ArrayList<>();
            
            // reading from server
            Product product = null;
            boolean stopRead = false;
            boolean readProduct = false;
            boolean readingProducts = false;

            while (!stopRead) {
                String line  = br.readLine().trim();
                System.out.println(line);
                if (line.startsWith("request_id: ")) {
                    requestId = line.substring(12).trim();

                } else if (line.startsWith("item_count: ")) {
                    itemCount = Integer.parseInt(line.substring(12).trim());

                } else if (line.startsWith("budget: ")) {
                    budget = Double.parseDouble(line.substring(8).trim());

                } else if (line.startsWith("prod_list")) {
                    readingProducts = true;

                } else if (line.startsWith("prod_start") && readingProducts) {
                    product = new Product();
                    readProduct = true;

                } else if (line.startsWith("prod_id: ") && readProduct) {
                    product.setProdId(line.substring(9).trim());

                } else if (line.startsWith("title: ") && readProduct) {
                    product.setTitle(line.substring(7).trim());

                } else if (line.startsWith("price: ") && readProduct) {
                    product.setPrice(Double.parseDouble(line.substring(7).trim()));

                } else if (line.startsWith("rating: ") && readProduct) {
                    product.setRating(Double.parseDouble(line.substring(7).trim()));

                } else if (line.startsWith("prod_end") && readProduct) {
                    products.add(product);
                    count++;
                    readProduct = false;
                    if (count >= itemCount) {
                        readingProducts = false;
                        stopRead = true;
                    }
                        
                } else {
                    continue;
                }
                
            }

            System.out.printf("%s, %d, %f%n", requestId, itemCount, budget);

            //sorting products by highest rating then highest price
            products.sort(Comparator.comparing(Product::getRating).thenComparing(Product::getPrice).reversed());

            for (Product listProduct: products) {
                System.out.printf("%s, %s, %5.2f, %3.2f%n",
                    listProduct.getProdId(),listProduct.getTitle(),listProduct.getPrice(), listProduct.getRating());
            }

            // figuring out what to purchase, get spent amount and remaining budget
            double spent = 0;
            // List<Product> toPurchase = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            for (Product listProduct: products) {
                if (listProduct.getPrice() > budget) {
                    continue;
                } else {
                    sb.append(listProduct.getProdId());
                    sb.append(" ");
                    budget -= listProduct.getPrice();
                    spent += listProduct.getPrice();
                }
            }

            // make items comma separated list
            String items = sb.toString().trim().replaceAll(" ", ",");

            System.out.println(items);
            System.out.printf("budget: %f, spent: %f%n", budget, spent);

            // write to server required fields

            bw.write(String.format("request_id: %s\n", requestId));
            bw.flush();
            bw.write("name: Tan Jin De\n");
            bw.flush();
            bw.write("email: tanjinde2000@yahoo.com.sg\n");
            bw.flush();
            bw.write(String.format("items: %s\n", items));
            bw.flush();
            bw.write(String.format("spent: %f\n", spent));
            bw.flush();
            bw.write(String.format("remaining: %f\n", budget));
            bw.flush();
            bw.write("client_end\n");
            bw.flush();

            // get success or failed status
            String status = br.readLine();
            System.out.println(status);

            // close
            is.close();
            os.close();
            socket.close();

        }
        System.out.println(socket.isClosed());
    }

}
