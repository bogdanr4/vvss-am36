package drinkshop.service;

import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;

import java.util.List;

public class TipBauturaService {

    private final Repository<Integer, TipBautura> repo;

    public TipBauturaService(Repository<Integer, TipBautura> repo) {
        this.repo = repo;
    }

    public void add(TipBautura tipBautura) { repo.save(tipBautura); }
    public void update(TipBautura tipBautura) { repo.update(tipBautura); }
    public void delete(int id) { repo.delete(id); }
    public List<TipBautura> getAll() { return repo.findAll(); }
}
