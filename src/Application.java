import Entities.Article;
import Entities.Delivery;
import Entities.StorageUnit;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;

import java.math.BigDecimal;
import java.util.*;

public class Application {
    /* First part of getting data. */
    private TreeMap<String, Article> articleHashMap;

    private TreeMap<String, StorageUnit> storageAreaA;
    private TreeMap<String, StorageUnit> storageAreaB;
    private TreeMap<String, StorageUnit> storageAreaC;

    private HashMap<AbstractMap.SimpleEntry<String, String>, Integer> distances;

    public Application() {
        articleHashMap = new TreeMap<>();
        distances = new HashMap<>();

        storageAreaA = new TreeMap<>();
        storageAreaB = new TreeMap<>();
        storageAreaC = new TreeMap<>();
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

    void getStorageUnits() throws Exception {
        ODataFeed dataFeed = DataGetter.getEntriesFeed("StorageArea");

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            StorageUnit unit = new StorageUnit((String) map.get("Code"),
                                               (String) map.get("Area"),
                                               (Integer) map.get("Capacity"),
                                               (Integer) map.get("MaxWeight"),
                                               (BigDecimal) map.get(
                                                       "MaxVolume"));

            if (unit.area.equals("A")) {
                storageAreaA.put(unit.code, unit);
            }

            if (unit.area.equals("B")) {
                storageAreaB.put(unit.code, unit);
            }

            if (unit.area.equals("C")) {
                storageAreaC.put(unit.code, unit);
            }
        }
    }

    void getStocks() throws Exception {
        ODataFeed dataFeed = DataGetter.getEntriesFeed("Stock");

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            for (String storageUnit : storageAreaA.keySet()) {
                String articleCode = (String) map.get("ArticleCode");
                Article article = new Article();

                article.code = articleCode;
                article.palletQuantity = articleHashMap.get(
                        articleCode).palletQuantity;
                article.singleUnitWeight = articleHashMap.get(
                        articleCode).singleUnitWeight;
                article.palletWeight = articleHashMap.get(
                        articleCode).palletWeight;
                article.singleUnitVolume = articleHashMap.get(
                        articleCode).singleUnitVolume;
                article.palleteVolume = articleHashMap.get(
                        articleCode).palleteVolume;

                article.currentQuantity = (Integer) map.get("StockAreaA");
                storageAreaA.get(storageUnit).articles.put(articleCode,
                                                           article);
            }

            for (String storageUnit : storageAreaB.keySet()) {
                String articleCode = (String) map.get("ArticleCode");
                Article article = new Article();

                article.code = articleCode;
                article.palletQuantity = articleHashMap.get(
                        articleCode).palletQuantity;
                article.singleUnitWeight = articleHashMap.get(
                        articleCode).singleUnitWeight;
                article.palletWeight = articleHashMap.get(
                        articleCode).palletWeight;
                article.singleUnitVolume = articleHashMap.get(
                        articleCode).singleUnitVolume;
                article.palleteVolume = articleHashMap.get(
                        articleCode).palleteVolume;

                article.currentQuantity = (Integer) map.get("StockAreaB");
                storageAreaB.get(storageUnit).articles.put(articleCode,
                                                           article);
            }

            for (String storageUnit : storageAreaC.keySet()) {
                String articleCode = (String) map.get("ArticleCode");
                Article article = new Article();

                article.code = articleCode;
                article.palletQuantity = articleHashMap.get(
                        articleCode).palletQuantity;
                article.singleUnitWeight = articleHashMap.get(
                        articleCode).singleUnitWeight;
                article.palletWeight = articleHashMap.get(
                        articleCode).palletWeight;
                article.singleUnitVolume = articleHashMap.get(
                        articleCode).singleUnitVolume;
                article.palleteVolume = articleHashMap.get(
                        articleCode).palleteVolume;

                article.currentQuantity = (Integer) map.get("StockAreaC");
                storageAreaC.get(storageUnit).articles.put(articleCode,
                                                           article);
            }
        }
    }

    void getTransportationNetworks() throws Exception {
        String entityType = "TransportationNetwork";
        ODataFeed dataFeed = DataGetter.getEntriesFeed(entityType);

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            distances.put(new AbstractMap.SimpleEntry<>(
                                  (String) map.get("SourceAreaCode"),
                                  (String) map.get("DestinationAreaCode")),
                          (Integer) map.get("MoveTime"));
        }
    }

    void getStockRules() throws Exception {
        ODataFeed dataFeed = DataGetter.getEntriesFeed("StockRule");

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            String articleCode = (String) map.get("ArticleCode");

            for (String storageUnit : storageAreaA.keySet()) {
                storageAreaA.get(storageUnit).articles.get(
                        articleCode).minQuantity = (Integer) map.get(
                        "MinQuantity");
                storageAreaA.get(storageUnit).articles.get(
                        articleCode).maxQuantity = (Integer) map.get(
                        "MaxQuantity");
                storageAreaA.get(storageUnit).articles.get(
                        articleCode).maxCapacity = (Integer) map.get(
                        "MaxCapacity");
            }

            for (String storageUnit : storageAreaB.keySet()) {
                storageAreaB.get(storageUnit).articles.get(
                        articleCode).minQuantity = (Integer) map.get(
                        "MinQuantity");
                storageAreaB.get(storageUnit).articles.get(
                        articleCode).maxQuantity = (Integer) map.get(
                        "MaxQuantity");
                storageAreaB.get(storageUnit).articles.get(
                        articleCode).maxCapacity = (Integer) map.get(
                        "MaxCapacity");
            }

            for (String storageUnit : storageAreaC.keySet()) {
                storageAreaC.get(storageUnit).articles.get(
                        articleCode).minQuantity = (Integer) map.get(
                        "MinQuantity");
                storageAreaC.get(storageUnit).articles.get(
                        articleCode).maxQuantity = (Integer) map.get(
                        "MaxQuantity");
                storageAreaC.get(storageUnit).articles.get(
                        articleCode).maxCapacity = (Integer) map.get(
                        "MaxCapacity");
            }
        }
    }

    public void visualizeStorageUnits() {
        System.out.println("Storage area A");
        for (String storageUnit : storageAreaA.keySet()) {
            System.out.println("\t" + storageUnit);

            for (String articolCode : storageAreaA.get(storageUnit).articles
                                              .keySet()) {
                System.out.println(
                        "\t\t" + articolCode + ": " + storageAreaA.get(
                                storageUnit).articles.get(
                                articolCode).currentQuantity);
            }
        }

        System.out.println("Storage area B");
        for (String storageUnit : storageAreaB.keySet()) {
            System.out.println("\t" + storageUnit);

            for (String articolCode : storageAreaB.get(storageUnit).articles
                                              .keySet()) {
                System.out.println(
                        "\t\t" + articolCode + ": " + storageAreaB.get(
                                storageUnit).articles.get(
                                articolCode).currentQuantity);
            }
        }

        System.out.println("Storage area C");
        for (String storageUnit : storageAreaC.keySet()) {
            System.out.println("\t" + storageUnit);

            for (String articolCode : storageAreaC.get(storageUnit).articles
                                              .keySet()) {
                System.out.println(
                        "\t\t" + articolCode + ": " + storageAreaC.get(
                                storageUnit).articles.get(
                                articolCode).currentQuantity);
            }
        }
    }

    public void simulate() throws Exception {
        ODataFeed dataFeed = DataGetter.getEntriesFeed("Delivery");

        for (ODataEntry entry : dataFeed.getEntries()) {
            Map<String, Object> map = entry.getProperties();

            Delivery delivery = new Delivery((Date) map.get("Time"),
                                             (String) map.get("ArticleCode"),
                                             (String) map.get("Type"),
                                             (Integer) map.get(
                                                     "QuantitySingleUnits"));
            if (delivery.type.equals("IN")) {
                boolean solved = false;

                for (String storageUnit : storageAreaA.keySet()) {
                    Article article = storageAreaA.get(storageUnit).articles
                                              .get(delivery.articleCode);

                    if (article.maxCapacity - article.currentQuantity >=
                        delivery.quantity) {
                        article.currentQuantity += delivery.quantity;
                        solved = true;
                        break;
                    }
                }
                if (solved) { continue; }

                for (String storageUnit : storageAreaB.keySet()) {
                    Article article = storageAreaB.get(storageUnit).articles
                                              .get(delivery.articleCode);

                    if (article.maxCapacity - article.currentQuantity >=
                        delivery.quantity) {
                        article.currentQuantity += delivery.quantity;
                        solved = true;
                        break;
                    }
                }
                if (solved) { continue; }

                for (String storageUnit : storageAreaC.keySet()) {
                    Article article = storageAreaC.get(storageUnit).articles
                                              .get(delivery.articleCode);

                    if (article.maxCapacity - article.currentQuantity >=
                        delivery.quantity) {
                        article.currentQuantity += delivery.quantity;
                        break;
                    }
                }
            } else {
                /* Delivery type is OUT. */

            }
        }
    }
}
