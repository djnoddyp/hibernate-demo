package demo.constants;

public class QueryConstants {

    private QueryConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BIKE_BIKESHOP = "SELECT b FROM Bike b WHERE b.bikeShop = :bikeShop";

    public static final String BIKE_STYLE_MODEL = "SELECT b.style FROM Bike b WHERE b.bikeName.model = :model";

    public static final String BIKE_PRICEINDOLLARS_MODEL = "SELECT b.priceInDollars FROM Bike b WHERE b.bikeName.model = :model";

    public static final String BIKESHOP = "SELECT bs FROM BikeShop bs WHERE bs.id = :id";

    public static final String BIKESHOP_JOIN_FETCH_EMPLOYEES = "SELECT bs FROM BikeShop bs JOIN FETCH bs.employees WHERE bs.id = :id";

    public static final String BIKESHOP_DTO = "SELECT NEW demo.dtos.BikeShopDTO(bs.id, bs.address) FROM BikeShop bs WHERE bs.id = :id";

    public static final String BIKESHOP_NATIVE = "SELECT bikeshop_id, make, model, style, price, sum(price) " +
            "OVER (PARTITION BY bikeshop_id ORDER BY price) FROM bike";

}
