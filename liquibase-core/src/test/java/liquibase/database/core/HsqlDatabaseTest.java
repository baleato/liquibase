package liquibase.database.core;

import junit.framework.TestCase;
import liquibase.database.Database;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.structure.core.Table;

public class HsqlDatabaseTest extends TestCase {
    public void testGetDefaultDriver() {
        Database database = new HsqlDatabase();

        assertEquals("org.hsqldb.jdbcDriver", database.getDefaultDriver("jdbc:hsqldb:mem:liquibase"));

        assertNull(database.getDefaultDriver("jdbc:oracle://localhost;databaseName=liquibase"));
    }

    public void testGetConcatSql() {
        Database database = new HsqlDatabase();
        String expectedResult = "CONCAT(str1, CONCAT(str2, CONCAT(str3, str4)))";
        String value = "v";
        String[] values = new String[]{"str1", "str2", "str3", "str4"};

        assertEquals(database.getConcatSql(value), value);
        assertEquals(database.getConcatSql(values), expectedResult);
        assertNull(database.getConcatSql((String[]) null));
    }

    /**
     * Verifies that {@link HsqlDatabase#escapeObjectName(String, Class)}
     * respects the value of {@link HsqlDatabase#getObjectQuotingStrategy()}.
     */
    public void testEscapeObjectName() {
        Database databaseWithDefaultQuoting = new HsqlDatabase();
        databaseWithDefaultQuoting.setObjectQuotingStrategy(ObjectQuotingStrategy.LEGACY);
        assertEquals("Test", databaseWithDefaultQuoting.escapeObjectName("Test", Table.class));
        
        Database databaseWithAllQuoting = new HsqlDatabase();
        databaseWithAllQuoting.setObjectQuotingStrategy(ObjectQuotingStrategy.QUOTE_ALL_OBJECTS);
        assertEquals("\"Test\"", databaseWithAllQuoting.escapeObjectName("Test", Table.class));
    }
}
