package org.wso2.carbon.identity.extension.authz.rest.otac.dao;

/**
 * SQL Constants
 */
public class SQLConstants {

    public static final String OTAC_ID_COLUMN = "ID";
    public static final String OTAC_SECRET_COLUMN = "SECRET";
    public static final String OTAC_EXPIRY_COLUMN = "EXPIRY";
    public static final String OTAC_STATUS_COLUMN = "STATUS";

    public static final String REQUEST_SECRET_TABLE = "IDN_ONE_TIME_AUTHORIZATION_CODES";

    public static final String INSERT_TO_REQUEST_SECRET_TABLE =
            "INSERT INTO \n" +
                REQUEST_SECRET_TABLE + "\n" +
            "(" +
                    OTAC_ID_COLUMN + ",\n" +
                    OTAC_SECRET_COLUMN + ",\n" +
                    OTAC_EXPIRY_COLUMN + ",\n" +
                    OTAC_STATUS_COLUMN + ",\n" +
            ")\n" +
            "VALUES\n" +
            "   (?, ?, ?, ?)";

    public static final String GET_REQUEST_SECRET_BY_SECRET =
            "SELECT\n" +
                    OTAC_ID_COLUMN + ",\n" +
                    OTAC_SECRET_COLUMN + ",\n" +
                    OTAC_EXPIRY_COLUMN + ",\n" +
                    OTAC_STATUS_COLUMN + ",\n" +
            "FROM\n" +
                REQUEST_SECRET_TABLE + "\n" +
            "WHERE\n" +
                    OTAC_SECRET_COLUMN + " = ?";
}
