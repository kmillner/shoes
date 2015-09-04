import org.sql2o.*;
import java.util.List;

public class Store {

  private int id;
  private String name;
  private int price;

  public Store (String name, int price) {
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public int getPrice() {
    return price;
  }

  @Override
  public boolean equals(Object otherStoreInstance) {
    if (!(otherStoreInstance instanceof Store)) {
      return false;
    } else {
      Store newStoreInstance = (Store) otherStoreInstance;
      return this.getName().equals(newStoreInstance.getName()) &&
             this.getPrice() == (newStoreInstance.getPrice()) &&
             this.getId() == newStoreInstance.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stores (name, price) VALUES (:name, :price);";
      this.id = (int) con.createQuery(sql, true)
          .addParameter("name", name)
          .addParameter("price", price)
          .executeUpdate()
          .getKey();
    }
  }

  public void updateStore(String name, int price) {
    this.name = name;
    this.price = price;
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stores SET (name, price) = (:name, :price) WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("price", price)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM stores WHERE id = :id";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();
      String joinDeleteQuery = "DELETE FROM stores_brands WHERE stores_id = :id";
        con.createQuery(joinDeleteQuery)
          .addParameter("id", id)
          .executeUpdate();
    }
  }

  public static List<Store> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stores";
      return con.createQuery(sql).executeAndFetch(Store.class);
    }
  }

  public static Store find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stores WHERE id=:id";
      Store student = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchFirst(Store.class);
      return student;
    }
  }

  public void addBrand(Brand brand) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stores_brands (stores_id, brands_id) VALUES (:stores_id, :brands_id)";
      con.createQuery(sql)
        .addParameter("stores_id", id)
        .addParameter("brands_id", brand.getId())
        .executeUpdate();
    }
  }
//this works in sql!!!!
  public List<Brand> getBrands() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT brands.* FROM stores JOIN stores_brands ON (stores.id = stores_brands.stores_id) JOIN brands ON (stores_brands.brands_id = brands.id) WHERE stores.id = :id";
      List<Brand> brands = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetch(Brand.class);
      return brands;
    }
  }

}
