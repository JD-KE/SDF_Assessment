package assessment.CSV;

public class App {
    private String name;
    private String category;
    private Double rating;
    
    public App(String name, String category, double rating) {
        this.name = name;
        this.category = category;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getRating() {
        return rating;
    }

    
    
}
