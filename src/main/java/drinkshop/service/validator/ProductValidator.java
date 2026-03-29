package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    @Override
    public void validate(Product product) {

        String errors = "";

        if (product.getId() <= 0)
            errors += "ID invalid!";

        if (product.getNume() == null || product.getNume().isBlank()) {
            errors += "Invalid Name!";
        } else if (product.getNume().length() > 255) {
            errors += "Name too long!";
        }

        if (product.getPret() < 0) {
            errors += "Negative price!";
        } else if (product.getPret() == 0) {
            errors += "Null price!";
        }


        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
