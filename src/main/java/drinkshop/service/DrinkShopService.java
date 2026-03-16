package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.export.CsvExporter;
import drinkshop.receipt.ReceiptGenerator;
import drinkshop.reports.DailyReportService;

import java.util.List;

public class DrinkShopService {

    private final ProductService productService;
    private final OrderService orderService;
    private final RetetaService retetaService;
    private final StocService stocService;
    private final DailyReportService report;
    private final TipBauturaService tipBauturaService;
    private final CategorieService categorieService;

    public DrinkShopService(
          ProductService productService,
            OrderService orderService,
            RetetaService retetaService,
            StocService stocService,
            DailyReportService report,
            TipBauturaService tipBauturaService,
            CategorieService categorieBauturaService
    ) {
        this.productService = productService;
        this.orderService = orderService;
        this.retetaService = retetaService;
        this.stocService = stocService;
        this.report = report;
        this.tipBauturaService = tipBauturaService;
        this.categorieService = categorieBauturaService;
    }

    // ---------- PRODUCT ----------
    public void addProduct(Product p) {
        productService.addProduct(p);
    }

    public void updateProduct(Product p) {
        productService.updateProduct(p);
    }

    public void deleteProduct(int id) {
        productService.deleteProduct(id);
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public List<Product> filtreazaDupaCategorie(String categorie) {
        return productService.filterByCategorie(categorie);
    }

    public List<Product> filtreazaDupaTip(String tip) {
        return productService.filterByTip(tip);
    }

    // ---------- ORDER ----------
    public void addOrder(Order order) {

        for(OrderItem o : order.getItems()){
            for(int i = 0; i < o.getQuantity(); i++)
                comandaProdus(o.getProduct());
        }

        orderService.addOrder(order);
    }

    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    public double computeTotal(Order order) {
        return orderService.computeTotal(order);
    }

    public String generateReceipt(Order order) {
        return ReceiptGenerator.generate(order, productService.getAllProducts());
    }

    public double getDailyRevenue() {
        return report.getTotalRevenue();
    }

    public void exportCsv(String path) {
        CsvExporter.exportOrders(productService.getAllProducts(), orderService.getAllOrders(), path);
    }

    // ---------- STOCK + RECIPE ----------
    public void comandaProdus(Product produs) {
        Reteta reteta = retetaService.findById(produs.getId());

        if (!stocService.areSuficient(reteta)) {
            throw new IllegalStateException("Stoc insuficient pentru produsul: " + produs.getNume());
        }
        stocService.consuma(reteta);
    }

    public List<Reteta> getAllRetete() {
        return retetaService.getAll();
    }

    public void addReteta(Reteta reteta) {
        retetaService.addReteta(reteta);
    }

    public void updateReteta(Reteta reteta) {
        retetaService.updateReteta(reteta);
    }

    public void deleteReteta(int id) {
        retetaService.deleteReteta(id);
    }

    // ---------- TIP BAUTURA ----------
    public List<TipBautura> getAllTipuri() {
        return tipBauturaService.getAll();
    }

    public void addTip(TipBautura tipBautura) {
        tipBauturaService.add(tipBautura);
    }

    public void updateTip(TipBautura tipBautura) {
        tipBauturaService.update(tipBautura);
    }

    public void deleteTip(int id) {
        tipBauturaService.delete(id);
    }

    // ---------- CATEGORIE BAUTURA ----------
    public List<CategorieBautura> getAllCategorii() {
        return categorieService.getAll();
    }

    public void addCategorie(CategorieBautura categorieBautura) {
        categorieService.add(categorieBautura);
    }

    public void updateCategorie(CategorieBautura categorieBautura) {
        categorieService.update(categorieBautura);
    }

    public void deleteCategorie(int id) {
        categorieService.delete(id);
    }

    public List<Stoc> getAllStocks(){
        return stocService.getAll();
    }

}