import org.junit.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class BrandTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Brand.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Brand firstBrand = new Brand("Bonita");
    Brand secondBrand = new Brand("Bonita");
    assertTrue(firstBrand.equals(secondBrand));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Brand myBrand = new Brand("Bonita");
    myBrand.save();
    assertTrue(Brand.all().get(0).equals(myBrand));
  }

  @Test
  public void find_findBrandInDatabase_true() {
    Brand myBrand = new Brand("Bonita");
    myBrand.save();
    Brand savedBrand = Brand.find(myBrand.getId());
    assertTrue(myBrand.equals(savedBrand));
  }
}
