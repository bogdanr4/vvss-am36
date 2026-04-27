module drinkshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;

    exports drinkshop.repository;
    exports drinkshop.service;
    exports drinkshop.service.validator;

    opens drinkshop.repository;
    opens drinkshop.service;
    opens drinkshop.service.validator;
    opens drinkshop.ui to javafx.fxml;
    exports drinkshop.ui;

    opens drinkshop.domain to javafx.base;
    exports drinkshop.domain;
}