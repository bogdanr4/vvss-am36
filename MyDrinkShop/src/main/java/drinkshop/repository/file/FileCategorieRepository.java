package drinkshop.repository.file;

import drinkshop.domain.CategorieBautura;
import drinkshop.service.validator.ValidationException;

public class FileCategorieRepository
        extends FileAbstractRepository<Integer, CategorieBautura> {

    public FileCategorieRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(CategorieBautura entity) {
        return entity.getId();
    }

    @Override
    protected CategorieBautura extractEntity(String line) {
        String[] elems = line.split(",");
        if (elems.length != 2) {
            throw new ValidationException("Invalid line format: " + line);
        }
        int id = Integer.parseInt(elems[0]);
        String name = elems[1];
        return new CategorieBautura(id, name);
    }

    @Override
    protected String createEntityAsString(CategorieBautura entity) {
        return entity.getId() + "," + entity.getNume();
    }
}
