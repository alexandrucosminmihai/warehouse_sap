public class Main {

    /* Here starts the processing. */
    public static void main(String[] args) throws Exception {
        Application application = new Application();

        /* Get data. */
        application.getArticles();

        application.getTransportationNetworks();

        application.getStocks();

        application.getStockRules();

        application.getStorageAreas();

//        application.getDeliveries();

        application.visualizeStorageUnits();
    }
}
