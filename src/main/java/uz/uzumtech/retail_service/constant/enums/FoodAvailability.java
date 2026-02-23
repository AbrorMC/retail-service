package uz.uzumtech.retail_service.constant.enums;

public enum FoodAvailability {
    NOT_AVAILABLE("Недоступно"),
    ONE("1 порция"),
    TWO("2 порции"),
    ENDING("Заканчивается"),
    AVAILABLE("Доступно");

    final String description;

    FoodAvailability(String description) {this.description = description;}

    public String getDescription() {
        return description;
    }
}
