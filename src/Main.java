public class Main {

    /* Here starts the processing. */
    public static void main(String[] args) throws Exception {
        Application application = new Application();

        /* Get data. */
        application.getArticles();

        application.getStorageUnits();

        application.getStocks();

        application.getTransportationNetworks();

        application.getStockRules();

        application.visualizeStorageUnits();

        application.simulate();
    }
}
