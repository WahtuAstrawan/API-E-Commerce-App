public class Review {
    private int id_order;
    private int star;
    private String description;

    public Review(int id_order, int star, String description) {
        this.id_order = id_order;
        this.star = star;
        this.description = description;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
