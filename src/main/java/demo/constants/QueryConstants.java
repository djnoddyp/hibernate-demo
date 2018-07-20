package demo.constants;

public class QueryConstants {

    private QueryConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BIKE_BIKESHOP = "SELECT b FROM Bike b WHERE b.bikeShop = :bikeShop";
    public static final String BIKE_STYLE_MODEL = "SELECT b.style FROM Bike b WHERE b.bikeName.model = :model";
    public static final String BIKE_PRICEINDOLLARS_MODEL = "SELECT b.priceInDollars FROM Bike b WHERE b.bikeName.model = :model";
    public static final String BIKESHOP = "SELECT bs from BikeShop bs WHERE bs.id = :id";
    public static final String BIKESHOP_JOIN_FETCH_EMPLOYEES = "SELECT bs from BikeShop bs JOIN FETCH bs.employees WHERE bs.id = :id";
    
}
