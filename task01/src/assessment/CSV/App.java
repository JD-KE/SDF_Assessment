package assessment.CSV;

public class App {
    private String name;
    private String category;
    private String rating;
    
    public App(String name, String category, String rating) {
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

    public String getRating() {
        return rating;
    }

    public boolean isRatingNaN() {
        if ("NaN".equals(rating)) {
            return true;
        } else {
            return false;
        }
    }

    
    
}
