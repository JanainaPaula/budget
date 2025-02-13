package br.com.janadev.budget.domain.expense;

import java.util.List;
import java.util.Objects;

public enum Category {
    FOOD("Food"),
    HEALTH("Health"),
    HOUSE("House"),
    TRANSPORT("Transport"),
    EDUCATION("Education"),
    LEISURE("Leisure"),
    UNFORESEEN("Unforeseen"),
    OTHERS("Others");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public Category getCategoryByName(String name){
        List<Category> categories = List.of(Category.values());
        return categories.stream()
                .filter(category -> Objects.equals(category.getName(), name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(""));
    }
}

