import java.util.Random;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import java.util.Map;

public class App {

  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
    //
    get("/viewstores", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      List<Store> stores = Store.all();
      model.put("stores", stores);
      model.put("template", "templates/viewstores.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/viewbrands", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      List<Brand> brands = Brand.all();
      model.put("brands", brands);
      model.put("template", "templates/viewbrands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stores/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      model.put("template", "templates/viewstores.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stores", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      String name = request.queryParams("name");
      int price = Integer.parseInt(request.queryParams("price"));

      Store newStore = new Store(name, price);
      newStore.save();
      model.put("store", newStore);
      model.put("template", "templates/viewstores.vtl");
      response.redirect("/");
      return null;
    });
 //this works!!!!
    post("/store/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      String name = request.queryParams("name");
      int price = Integer.parseInt(request.queryParams("price"));

      Store newStore = new Store(name, price);
      newStore.save();

      response.redirect("/viewstores");
      return null;
    });

    post("/brands", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      int stores_id = Integer.parseInt(request.queryParams("stores_id"));

      Brand newBrand = new Brand(name);
      newBrand.save();

      List<Brand> brands = Brand.all();
      model.put("brand", newBrand);
      model.put("template", "templates/viewbrands.vtl");
      response.redirect("/");
      return null;
    });
 //this works!!!
    post("/brand/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      String name = request.queryParams("name");

      Brand newBrand = new Brand(name);
      newBrand.save();

      response.redirect("/viewbrands");
      return null;
    });

    get("/stores/:id/brands", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Store store = Store.find(Integer.parseInt(request.params(":id")));
      List<Brand> brands = store.getBrands();

      model.put("store", store);
      model.put("brands", brands);
      model.put("template", "templates/brand.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stores/:id/brands/store-form", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Store store = Store.find(Integer.parseInt(request.params(":id")));

      List<Brand> brands = store.getBrands();

      String name = request.queryParams("name");
      // int price = Integer.parseInt(request.queryParams("price"));
      model.put("store", store);
      model.put("brands", brands);
      model.put("template", "templates/store-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stores/:id/brands/store-form", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Store store = Store.find(Integer.parseInt(request.params(":id")));

      String name = request.queryParams("name");
      int price = Integer.parseInt(request.queryParams("price"));
      // store.update(name, price);

    response.redirect("/viewstores");
    return null;
  });

 //this works!!!!
    post("/stores/:id/brands/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Store toBeDeleted = Store.find(Integer.parseInt(request.params(":id")));
      toBeDeleted.delete();

      response.redirect("/viewstores");
      return null;
    });

    post("/stores/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Store toBeDeleted = Store.find(Integer.parseInt(request.params(":id")));
      toBeDeleted.delete();

      response.redirect("/viewstores");
      return null;
    });

    get("/brands/:id/stores", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Brand brand = Brand.find(Integer.parseInt(request.params(":id")));
      List<Store> stores = brand.getStores();

      model.put("brand", brand);
      model.put("stores", stores);
      model.put("template", "templates/store.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stores/:id/brands", (request, response) -> {
    HashMap<String, Object> model = new HashMap<String, Object>();

    Store store = Store.find(Integer.parseInt(request.params(":id")));
    List<Brand> brands = store.getBrands();

    model.put("store", store);
    model.put("brands", brands);
    model.put("template", "templates/brand-stores.vtl");
    return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  //stores/$brand.getId()/brands/update
    get("/stores/:id/brands/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Brand brand = Brand.find(Integer.parseInt(request.params(":id")));      // int brands_id = Integer.parseInt(request.queryParams("brands_id"));
      List<Store> stores = Store.all();
      model.put("stores", stores);
      model.put("brand", brand);
      model.put("template", "templates/brand-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/stores/:id/brands/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Brand brand = Brand.find(Integer.parseInt(request.params(":id")));
      //int brandId = Integer.parseInt(request.params("brand_id"));
      //Brand brand = Brand.find(id);
      Store store = Store.find(Integer.parseInt(request.params(":id")));
      //Store store = Store.find(id);
      brand.addStore(store);
      response.redirect("/store");
      return null;
      });

    // post("/stores/:id/brands/update", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   Brand brand = Brand.find(Integer.parseInt(request.queryParams(":id")));
    //   Brand brand = Brand.find(id);
    //   Store store = Store.find(Integer.parseInt(request.queryParams(":id")));
    //   Store store = Store.find(id);
    //   store.addBrand(brand);
    //   response.redirect("/store");
    //   return null;
    //   });

    // post("/stores/:id/brands/update", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   int taskId = Integer.parseInt(request.queryParams("task_id"));
    //   int categoryId = Integer.parseInt(request.queryParams("category_id"));
    //   Category category = Category.find(categoryId);
    //   Task task = Task.find(taskId);
    //   task.addCategory(category);
    //   response.redirect("/tasks/" + taskId);
    //   return null;

  // post("/stores/:id/brands/edit", (request, response) -> {
  //   HashMap<String, Object> model = new HashMap<String, Object>();
  //   Brand brand = Brand.find(Integer.parseInt(request.queryParams(":id")));
  //   int id = Integer.parseInt(request.params("id"));
  //   Brand brand = Brand.find(id);
  //   model.put("brand", brand);
  //   model.put("stores", Store.all());
  //   model.put("template", "templates/index.vtl");
  //   return new ModelAndView(model, layout);
  //   response.redirect("/viewbrands");
  //   return null;
  // });

    // post("/stores/:id/brands/edit", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //
    //   Store newStore = Store.find(Integer.parseInt(request.params(":id")));
    //   int brands_id = Integer.parseInt(request.queryParams("brands_id"));
    //   Brand newBrand = Brand.find(brands_id);
    //   newStore.addBrand(newBrand);
    //   List<Store> stores = Store.all();
    //   // Object[] params = request.queryParams().toArray();
    //   // for (Object param : params) {
    //     // String brandId = (String) request.queryParams((String) param);
    //     newStore.addBrand(newBrand);
    //   // }
    //   response.redirect("/brand");
    //   return null;
    // });

  }
}
