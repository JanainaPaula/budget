package br.com.janadev.budget.domain.expense;

import br.com.janadev.budget.domain.expense.exception.CategoryNotFoundException;

import java.util.List;
import java.util.Objects;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_CATEGORY_NOT_FOUND;

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

    public String getName() {
        return this.name;
    }

    public static Category getCategoryByName(String name) {
        List<Category> categories = List.of(Category.values());
        return categories.stream()
                .filter(category -> Objects.equals(category.getName().toLowerCase(), name.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new CategoryNotFoundException(EXPENSE_CATEGORY_NOT_FOUND));
    }
}

