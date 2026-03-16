package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.service.DrinkShopService;
import drinkshop.service.validator.ValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DrinkShopController {

    @FXML public TableView<Stoc> stockTable;
    @FXML public TableColumn<Product, Integer> stockId;
    @FXML public TableColumn<Product, String> stockName;
    @FXML public TableColumn<Product, Integer> stockQuantity;
    @FXML public TableColumn<Product, Integer> stockMinimum;

    private DrinkShopService service;

    // ---------- PRODUCT ----------
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProdId;
    @FXML private TableColumn<Product, String> colProdName;
    @FXML private TableColumn<Product, Double> colProdPrice;
    @FXML private TableColumn<Product, String> colProdCategorie;
    @FXML private TableColumn<Product, String> colProdTip;
    @FXML private TextField txtProdName, txtProdPrice;
    @FXML private ComboBox<String> comboProdCategorie;
    @FXML private ComboBox<String> comboProdTip;

    // ---------- RETETE ----------
    @FXML private TableView<Reteta> retetaTable;
    @FXML private TableColumn<Reteta, Integer> colRetetaId;
    @FXML private TableColumn<Reteta, String> colRetetaDesc;

    @FXML private TableView<IngredientReteta> newRetetaTable;
    @FXML private TableColumn<IngredientReteta, String> colNewIngredName;
    @FXML private TableColumn<IngredientReteta, Double> colNewIngredCant;
    @FXML private TextField txtNewIngredName, txtNewIngredCant;

    // ---------- ORDER (CURRENT) ----------
    @FXML private TableView<OrderItem> currentOrderTable;
    @FXML private TableColumn<OrderItem, String> colOrderProdName;
    @FXML private TableColumn<OrderItem, Integer> colOrderQty;

    @FXML private TableView<TipBautura> tipTable;
    @FXML private TableColumn<TipBautura, Integer> colTipId;
    @FXML private TableColumn<TipBautura, String> colTipName;
    @FXML private TextField txtTipName;

    // ---------- CATEGORII ----------
    @FXML private TableView<CategorieBautura> categorieTable;
    @FXML private TableColumn<CategorieBautura, Integer> colCategorieId;
    @FXML private TableColumn<CategorieBautura, String> colCategorieName;
    @FXML private TextField txtCategorieName;

    @FXML private ComboBox<Integer> comboQty;
    @FXML private Label lblOrderTotal;
    @FXML private TextArea txtReceipt;

    @FXML private Label lblTotalRevenue;

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<Stoc> stockList = FXCollections.observableArrayList();
    private ObservableList<Reteta> retetaList = FXCollections.observableArrayList();
    private ObservableList<IngredientReteta> newRetetaList = FXCollections.observableArrayList();
    private ObservableList<OrderItem> currentOrderItems = FXCollections.observableArrayList();
    private ObservableList<TipBautura> tipList = FXCollections.observableArrayList();
    private ObservableList<CategorieBautura> categorieList = FXCollections.observableArrayList();

    private Order currentOrder = new Order(1);

    public void setService(DrinkShopService service) {
        this.service = service;
        initData();
    }

    @FXML
    private void initialize() {

        // PRODUCTS
        colProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdName.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colProdPrice.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colProdCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colProdTip.setCellValueFactory(new PropertyValueFactory<>("tip"));
        productTable.setItems(productList);



        // RETETE
        colRetetaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRetetaDesc.setCellValueFactory(data -> {
            Reteta r = data.getValue();
            String desc = r.getIngrediente().stream()
                    .map(i -> i.getDenumire() + " (" + i.getCantitate() + ")")
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(desc);
        });
        retetaTable.setItems(retetaList);

        colNewIngredName.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        colNewIngredCant.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        newRetetaTable.setItems(newRetetaList);

        // CURRENT ORDER TABLE
        colOrderProdName.setCellValueFactory(data -> {
            int prodId = data.getValue().getProduct().getId();
            Product p = productList.stream().filter(pr -> pr.getId() == prodId).findFirst().orElse(null);
            return new SimpleStringProperty(p != null ? p.getNume() : "N/A");
        });
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currentOrderTable.setItems(currentOrderItems);

        comboQty.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10));

        // TIPURI
        colTipId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tipTable.setItems(tipList);

        // CATEGORII
        colCategorieId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategorieName.setCellValueFactory(new PropertyValueFactory<>("nume"));
        categorieTable.setItems(categorieList);

        // CATEGORII
        stockId.setCellValueFactory(new PropertyValueFactory<>("id"));
        stockName.setCellValueFactory(new PropertyValueFactory<>("ingredient"));
        stockMinimum.setCellValueFactory(new PropertyValueFactory<>("stocMinim"));
        stockQuantity.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        stockTable.setItems(stockList);
    }

    private void initData() {
        productList.setAll(service.getAllProducts());
        retetaList.setAll(service.getAllRetete());
        lblTotalRevenue.setText("Daily Revenue: " + service.getDailyRevenue());
        updateOrderTotal();
        refreshComboBoxes();
        tipList.setAll(service.getAllTipuri());
        categorieList.setAll(service.getAllCategorii());
        stockList.setAll(service.getAllStocks());
    }

    private void refreshComboBoxes() {
        List<String> tipNames = new ArrayList<>();
        tipNames.add("ALL");
        service.getAllTipuri().forEach(t -> tipNames.add(t.getName()));
        comboProdTip.setItems(FXCollections.observableArrayList(tipNames));

        List<String> categorieNames = new ArrayList<>();
        categorieNames.add("ALL");
        service.getAllCategorii().forEach(c -> categorieNames.add(c.getNume()));
        comboProdCategorie.setItems(FXCollections.observableArrayList(categorieNames));
    }

    // ---------- PRODUCT ----------
    @FXML
    private void onAddProduct() {
        Reteta r=retetaTable.getSelectionModel().getSelectedItem();

        if (r == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Selectati o reteta pentru care adugati un produs");
            alert.showAndWait();
            return;
        }else
        if (service.getAllProducts().stream().filter(p->p.getId()==r.getId()).toList().size()>0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Exista un produs cu reteta adaugata.");
            alert.showAndWait();
            return;
        }
        Product p = new Product(r.getId(),
                txtProdName.getText(),
                Double.parseDouble(txtProdPrice.getText()),
                comboProdCategorie.getValue(),
                comboProdTip.getValue());
        service.addProduct(p);
        initData();
    }

    @FXML
    private void onUpdateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        Product updated = new Product(selected.getId(), txtProdName.getText(),
                Double.parseDouble(txtProdPrice.getText()),
                comboProdCategorie.getValue(), comboProdTip.getValue());
        service.updateProduct(updated);
        initData();
    }

    @FXML
    private void onDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        service.deleteProduct(selected.getId());
        initData();
    }

    @FXML
    private void onFilterCategorie() {
        productList.setAll(service.filtreazaDupaCategorie(comboProdCategorie.getValue()));
    }

    @FXML
    private void onFilterTip() {
        productList.setAll(service.filtreazaDupaTip(comboProdTip.getValue()));
    }

    // ---------- RETETA NOUA ----------
    @FXML
    private void onAddNewIngred() {
        newRetetaList.add(new IngredientReteta(txtNewIngredName.getText(),
                Double.parseDouble(txtNewIngredCant.getText())));
    }

    @FXML
    private void onDeleteNewIngred() {
        IngredientReteta sel = newRetetaTable.getSelectionModel().getSelectedItem();
        if (sel != null) newRetetaList.remove(sel);
    }

    @FXML
    private void onAddNewReteta() {
        Reteta r = new Reteta(service.getAllRetete().size()+1, new ArrayList<>(newRetetaList));
        service.addReteta(r);
        newRetetaList.clear();
        initData();
    }

    @FXML
    private void onClearNewRetetaIngredients() {
        newRetetaTable.getItems().clear();
        txtNewIngredName.clear();
        txtNewIngredCant.clear();
    }

    // ---------- CURRENT ORDER ----------
    @FXML
    private void onAddOrderItem() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        Integer qty = comboQty.getValue();

        if (selected == null) {
            showError("Selectează un produs din listă.");
            return;
        }
        if (qty == null) {
            showError("Selectează cantitatea.");
            return;
        }

        currentOrderItems.add(new OrderItem(selected, qty));
        updateOrderTotal();
    }

    @FXML
    private void onDeleteOrderItem() {
        OrderItem sel = currentOrderTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            currentOrderItems.remove(sel);
            updateOrderTotal();
        }
    }

    @FXML
    private void onFinalizeOrder() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);
        currentOrder.computeTotalPrice();

        service.addOrder(currentOrder);

        txtReceipt.setText(service.generateReceipt(currentOrder));

        currentOrderItems.clear();
        currentOrder = new Order(currentOrder.getId() + 1);
        updateOrderTotal();
        stockList.setAll(service.getAllStocks());
    }

    private void updateOrderTotal() {
        currentOrder.getItems().clear();
        currentOrder.getItems().addAll(currentOrderItems);
        double total = service.computeTotal(currentOrder);
        lblOrderTotal.setText("Total: " + total);
    }

    @FXML
    private void onAddTip() {
        String name = txtTipName.getText().trim();
        if (name.isEmpty()) { showError("Introduceți un nume."); return; }
        int newId = service.getAllTipuri().stream().mapToInt(TipBautura::getId).max().orElse(0) + 1;
        try {
            service.addTip(new TipBautura(newId, name));
        } catch (ValidationException e) {
            showError(e.getMessage());
            return;
        }
        txtTipName.clear();
        initData();
    }

    @FXML
    private void onUpdateTip() {
        TipBautura selected = tipTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Selectați un tip."); return; }
        String name = txtTipName.getText().trim();
        if (name.isEmpty()) { showError("Introduceți un nume."); return; }
        try {
            service.updateTip(new TipBautura(selected.getId(), name));
        } catch (ValidationException e) {
            showError(e.getMessage());
            return;
        }
        txtTipName.clear();
        initData();
    }

    @FXML
    private void onDeleteTip() {
        TipBautura selected = tipTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Selectați un tip."); return; }
        try {
            service.deleteTip(selected.getId());
        } catch (ValidationException e) {
            showError(e.getMessage());
            return;
        }
        initData();
    }

    // ---------- CATEGORII ----------
    @FXML
    private void onAddCategorie() {
        String name = txtCategorieName.getText().trim();
        if (name.isEmpty()) { showError("Introduceți un nume."); return; }
        int newId = service.getAllCategorii().stream().mapToInt(CategorieBautura::getId).max().orElse(0) + 1;
        try {
            service.addCategorie(new CategorieBautura(newId, name));
        } catch (ValidationException e) {
            showError(e.getMessage());
            return;
        }
        txtCategorieName.clear();
        initData();
    }

    @FXML
    private void onUpdateCategorie() {
        CategorieBautura selected = categorieTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Selectați o categorie."); return; }
        String name = txtCategorieName.getText().trim();
        if (name.isEmpty()) { showError("Introduceți un nume."); return; }
        try {
            service.updateCategorie(new CategorieBautura(selected.getId(), name));
        } catch (ValidationException e) {
            showError(e.getMessage());
            return;
        }
        txtCategorieName.clear();
        initData();
    }

    @FXML
    private void onDeleteCategorie() {
        CategorieBautura selected = categorieTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Selectați o categorie."); return; }
        try {
            service.deleteCategorie(selected.getId());
        } catch (ValidationException e) {
            showError(e.getMessage());
            return;
        }
        initData();
    }


    // ---------- EXPORT + REVENUE ----------
    @FXML
    private void onExportOrdersCsv() {
        service.exportCsv("orders.csv");
    }

    @FXML
    private void onDailyRevenue() {
        lblTotalRevenue.setText("Daily Revenue: " + service.getDailyRevenue());
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}