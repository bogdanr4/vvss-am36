package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.repository.Repository;

import java.util.List;

public class CategorieService {

    private final Repository<Integer, CategorieBautura> repo;

    public CategorieService(Repository<Integer, CategorieBautura> repo) {
        this.repo = repo;
    }

    public void add(CategorieBautura categorieBautura) { repo.save(categorieBautura); }
    public void update(CategorieBautura categorieBautura) { repo.update(categorieBautura); }
    public void delete(int id) { repo.delete(id); }
    public List<CategorieBautura> getAll() { return repo.findAll(); }
}
