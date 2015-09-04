import org.sql2o.*;
import java.util.List;

public class Brand {

  private int id;
  private String name;

  public Brand (String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherBrandInstance) {
    if (!(otherBrandInstance instanceof Brand)) {
      return false;
    } else {
      Brand newBrandInstance = (Brand) otherBrandInstance;
      return this.getName().equals(newBrandInstance.getName()) &&
             this.getId() == newBrandInstance.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO brands (name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
          .addParameter("name", name)
          .executeUpdate()
          .getKey();
    }
  }

  public static List<Brand> all() {
    String sql = "SELECT * FROM brands";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Brand.class);
    }
  }

  public static Brand find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM brands WHERE id=:id";
      Brand course = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchFirst(Brand.class);
      return course;
    }
  }

  public void addStore(Store store) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stores_brands (stores_id, brands_id) VALUES (:stores_id, :brands_id)";
      con.createQuery(sql)
        .addParameter("id", store.getId())
        .addParameter("brands_id", id)
        .executeUpdate();
    }
  }
  //this works in sql!

  public List<Store> getStores() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT stores.* FROM brands JOIN stores_brands ON (brands.id = stores_brands.brands_id) JOIN stores ON (stores_brands.id = stores.id) WHERE brands.id = :id";
      List<Store> stores = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetch(Store.class);
      return stores;
    }
  }
}
