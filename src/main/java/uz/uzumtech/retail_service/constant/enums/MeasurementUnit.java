package uz.uzumtech.retail_service.constant.enums;

public enum MeasurementUnit {
    PIECE("piece"),
    GRAM("gram"),
    LITRE("litre");

    final String name;

    MeasurementUnit(String name) {
        this.name = name;
    }
}
