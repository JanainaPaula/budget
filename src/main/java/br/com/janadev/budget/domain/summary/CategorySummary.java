package br.com.janadev.budget.domain.summary;

public class CategorySummary{
    private final String category;
    private final double total;

    private CategorySummary(String category, double total) {
        this.category = category;
        this.total = total;
    }

    public static CategorySummary of(String category, double total){
        return new CategorySummary(category, total);
    }

    public String getCategory() {
        return category;
    }

    public double getTotal() {
        return total;
    }
}
