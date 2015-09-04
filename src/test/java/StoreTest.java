import org.junit.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class StoreTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Store.all().size(), 0);
  }

  @Test
  public void all_returnsAnEmptyArrayWhenNothingInDB() {
    assertTrue(Store.all().size() == 0);
  }

  @Test
  public void equals_returnsTrueIfTwoObjectsAreSame() {
    Store store1 = new Store("Jammin' Kicks", 10);
    Store store2 = new Store("Jammin' Kicks", 10);
    assertTrue(store1.equals(store2));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Store newStore = new Store("Jammin' Kicks", 10);
    newStore.save();
    assertTrue(Store.all().get(0).equals(newStore));
  }

  @Test
  public void find_findStoretInDatabase_true() {
    Store myStore = new Store("Jammin' Kicks", 10);
    myStore.save();
    Store savedStore = Store.find(myStore.getId());
    assertTrue(myStore.equals(savedStore));
  }

  @Test
  public void update_updatesInformationForAnObject() {
    Store newStore = new Store("Jammin' Kicks", 10);
    newStore.save();
    newStore.updateStore("PayLess Shoes", 12);
    Store savedStore = Store.find(newStore.getId());
    assertEquals("PayLess Shoes", savedStore.getName());
  }

  @Test
  public void delete_checkThatDeletesFromDatabase_false() {
    Store newStore = new Store("Jammin' Kicks", 10);
    newStore.save();
    newStore.delete();
    Store otherStore = Store.find(newStore.getId());
    assertEquals(false, newStore.equals(otherStore));
  }
}
