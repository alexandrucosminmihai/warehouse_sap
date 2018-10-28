import Entities.*;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;

import java.math.BigDecimal;
import java.util.*;

public class Application {
    private ArrayList<StorageUnit> storageAreaA;
    private ArrayList<StorageUnit> storageAreaB;
    private ArrayList<StorageUnit> storageAreaC;

    private HashMap<String, Article> articleHashMap;
    private ArrayList<Delivery>      deliveries;
    private HashSet<Stock>           stockHashSet;

    private HashMap<AbstractMap.SimpleEntry<String, String>, Integer>
            transportationNetworksHashMap;
    private HashMap<AbstractMap.SimpleEntry<String, String>, StockRules>
            stockRulesHashMap;

    public Application() {
        stockHashSet = new HashSet<>();
        articleHashMap = new HashMap<>();

        stockRulesHashMap = new HashMap<>();
        transportationNetworksHashMap = new HashMap<>();

        deliveries = new ArrayList<>();

        storageAreaA = new ArrayList<>();
        storageAreaB = new ArrayList<>();
        storageAreaC = new ArrayList<>();
    }

    void getArticles() throws Exception {
        ODataFeed dataFeed = DataGetter.getEntriesFeed("Article");

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            Article article = new Article((String) map.get("Code"),
                                          ((Integer) map.get("PalletQuantity")),
                                          (BigDecimal) map.get(
                                                  "SingleUnitWeight"),
                                          (BigDecimal) map.get("PalletWeight"),
                                          (BigDecimal) map.get(
                                                  "SingleUnitVolume"),
                                          (BigDecimal) map.get("PalletVolume"));
            articleHashMap.put(article.code, article);
        }
    }

    void getTransportationNetworks() throws Exception {
        String entityType = "TransportationNetwork";
        ODataFeed dataFeed = DataGetter.getEntriesFeed(entityType);

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            TransportationNetwork route = new TransportationNetwork(
                    (String) map.get("SourceAreaCode"),
                    (String) map.get("DestinationAreaCode"),
                    (Integer) map.get("MoveTime"));

            transportationNetworksHashMap.put(
                    new AbstractMap.SimpleEntry<>(route.sourceAreaCode,
                                                  route.destinationAreaCode),
                    route.moveTime);
        }
    }

    void getStocks() throws Exception {
        ODataFeed dataFeed = DataGetter.getEntriesFeed("Stock");

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            Stock stock = new Stock((String) map.get("ArticleCode"),
                                    (Integer) map.get("StockAreaA"),
                                    (Integer) map.get("StockAreaB"),
                                    (Integer) map.get("StockAreaC"));
            stockHashSet.add(stock);
        }
    }

    void getStockRules() throws Exception {
        ODataFeed dataFeed = DataGetter.getEntriesFeed("StockRule");

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            StockRules stockRules = new StockRules(
                    (String) map.get("ArticleCode"),
                    (String) map.get("StorageAreaCode"),
                    (Integer) map.get("MinQuantity"),
                    (Integer) map.get("MaxQuantity"),
                    (Integer) map.get("MaxCapacity"));

            stockRulesHashMap.put(
                    new AbstractMap.SimpleEntry<>(stockRules.articleCode,
                                                  stockRules.storageAreaCode),
                    stockRules);
        }
    }

    void getStorageAreas() throws Exception {
        ODataFeed dataFeed = DataGetter.getEntriesFeed("StockRule");

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            StorageUnit area = new StorageUnit((String) map.get("Code"),
                                               (String) map.get("Area"),
                                               (Integer) map.get("Capacity"),
                                               (Integer) map.get("MaxWeight"),
                                               (BigDecimal) map.get(
                                                       "MaxVolume"));

            if (area.area.equals("A")) storageAreaA.add(area);
            if (area.area.equals("B")) storageAreaB.add(area);
            if (area.area.equals("C")) storageAreaC.add(area);
        }
    }

    public void getDeliveries() throws Exception {
        ODataFeed dataFeed = DataGetter.getEntriesFeed("Delivery");

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            Delivery delivery = new Delivery((Date) map.get("Time"),
                                             (String) map.get("ArticleCode"),
                                             (String) map.get("OUT"),
                                             (Integer) map.get(
                                                     "QuantitySingleUnits"));
            deliveries.add(delivery);
        }
    }

    public void visualizeStorageUnits() {
        System.out.println("Storage area A");
        for (StorageUnit storageUnit : storageAreaA) {
            System.out.println("\t" + storageUnit);
        }
    }
}
