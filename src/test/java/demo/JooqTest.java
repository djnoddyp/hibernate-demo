package demo;

import demo.generated.enums.Style;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static demo.generated.Tables.BIKE;
import static demo.generated.Tables.BIKESHOP;
import static demo.generated.Tables.EMPLOYEE;
import static org.junit.Assert.*;


public class JooqTest {

    final String user = "postgres";
    final String password = "postgres";
    final String url = "jdbc:postgresql://localhost:5434/jooq_demo";
    DSLContext sql;
    Connection conn;

    @Before
    public void setup() {
        try {
            conn = DriverManager.getConnection(url, user, password);
            sql = DSL.using(conn, SQLDialect.POSTGRES);
            sql.deleteFrom(EMPLOYEE).execute();
            sql.deleteFrom(BIKE).execute();
            sql.deleteFrom(BIKESHOP).execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSimplePersist() {
        Integer bikeshopId = insertBikeshopReturnGeneratedId();
        sql.insertInto(BIKE)
                .columns(BIKE.BIKESHOP_ID, BIKE.MAKE, BIKE.MODEL, BIKE.STYLE, BIKE.GEARS, BIKE.PRICE)
                .values(bikeshopId, "Giant", "Trance", Style.MOUNTAIN, 21, new BigDecimal("799.99"))
                .values(bikeshopId, "Specialized", "Comp", Style.HYBRID, 18, new BigDecimal("549.59"))
                .values(bikeshopId, "Cannondale", "X", Style.ROAD, 10, new BigDecimal("1250.99"))
                .execute();

        assertEquals(3, sql
                .select()
                .from(BIKE)
                .where(BIKE.BIKESHOP_ID.eq(bikeshopId))
                .fetch().size());
    }

    @Test
    public void testUpdate() {
        Integer bikeshopId = insertBikeshopReturnGeneratedId();
        sql.insertInto(BIKE)
                .columns(BIKE.BIKESHOP_ID, BIKE.MAKE, BIKE.MODEL, BIKE.STYLE, BIKE.GEARS, BIKE.PRICE)
                .values(bikeshopId, "Giant", "Trance", Style.MOUNTAIN, 21, new BigDecimal("799.99"))
                .execute();

        // Update data
        sql.update(BIKE)
                .set(BIKE.STYLE, Style.ROAD)
                .where(BIKE.MODEL.eq("Trance"))
                .execute();

        assertEquals(Style.ROAD, sql
                .select(BIKE.STYLE)
                .from(BIKE)
                .where(BIKE.MODEL.eq("Trance"))
                .fetchOne()
                .getValue(BIKE.STYLE));
    }
    
    @Test(expected = DataAccessException.class)
    public void testNotNullConstraint() {
        Integer id = insertBikeshopReturnGeneratedId();
        sql.insertInto(BIKE)
                .columns(BIKE.BIKESHOP_ID, BIKE.MAKE, BIKE.MODEL, BIKE.STYLE, BIKE.GEARS)
                .values(id, "Giant", "Trance", Style.MOUNTAIN, 21)
                .execute();
    }


    Integer insertBikeshopReturnGeneratedId() {
        return sql
                .insertInto(BIKESHOP)
                .columns(BIKESHOP.ADDRESS)
                .values("10 Rosebery Road")
                .returning(BIKESHOP.ID)
                .fetchOne().getId();
    }

}
