package org.wso2.carbon.identity.extension.authz.rest.otac.dao;

import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.identity.extension.authz.rest.otac.Constants;
import org.wso2.carbon.identity.extension.authz.rest.otac.Utils;
import org.wso2.carbon.identity.extension.authz.rest.otac.exception.OneTimeAuthzCodeRestAuthServerException;
import org.wso2.carbon.identity.extension.authz.rest.otac.model.OneTimeAuthzCode;

import java.util.List;

/**
 * DAO implementation for One-Time-Authorization-Code Rest Authentication bundle
 */
public class OneTimeAuthzCodeDAOImpl implements OneTimeAuthzCodeDAO {

    private static final OneTimeAuthzCodeDAO daoImpl = new OneTimeAuthzCodeDAOImpl();

    public static OneTimeAuthzCodeDAO getInstance() {
        return daoImpl;
    }

    private OneTimeAuthzCodeDAOImpl() {

    }

    @Override
    public void addOTAC(OneTimeAuthzCode oneTimeAuthzCode) throws OneTimeAuthzCodeRestAuthServerException {

        JdbcTemplate jdbcTemplate = Utils.getNewIdentityTemplate();

        try {
            jdbcTemplate.executeInsert(SQLConstants.INSERT_TO_REQUEST_SECRET_TABLE, preparedStatement -> {
                int parameterIndex = 1;
                preparedStatement.setString(parameterIndex, oneTimeAuthzCode.getId());
                preparedStatement.setString(++parameterIndex, oneTimeAuthzCode.getSecret());
                preparedStatement.setTimestamp(++parameterIndex, oneTimeAuthzCode.getExpiry());
                preparedStatement.setString(++parameterIndex, oneTimeAuthzCode.getSecretStatus());
            }, oneTimeAuthzCode, false);
        } catch (DataAccessException e) {
            throw Utils.handleServerException(Constants.SERVER_ERROR.OTAC_ADD_ERROR, null, e);
        }
    }

    @Override
    public OneTimeAuthzCode getOTAC(String oneTimeAuthzCode) throws OneTimeAuthzCodeRestAuthServerException {

        JdbcTemplate jdbcTemplate = Utils.getNewIdentityTemplate();

        List<OneTimeAuthzCode> oneTimeAuthzCodes;
        try {
            oneTimeAuthzCodes = jdbcTemplate.executeQuery(SQLConstants.GET_REQUEST_SECRET_BY_SECRET, (resultSet, i) -> {
                OneTimeAuthzCode oneTimeAuthzCodeReturn = new OneTimeAuthzCode();
                oneTimeAuthzCodeReturn.setId(resultSet.getString(SQLConstants.OTAC_ID_COLUMN));
                oneTimeAuthzCodeReturn.setExpiry(resultSet.getTimestamp(SQLConstants.OTAC_EXPIRY_COLUMN));
                oneTimeAuthzCodeReturn.setSecretStatus(resultSet.getString(SQLConstants.OTAC_STATUS_COLUMN));
                oneTimeAuthzCodeReturn.setSecret(resultSet.getString(SQLConstants.OTAC_SECRET_COLUMN));
                return oneTimeAuthzCodeReturn;
            }, preparedStatement -> {
                preparedStatement.setString(1, oneTimeAuthzCode);
            });
            return oneTimeAuthzCodes == null ? null : buildOneTimeAuthzCode(oneTimeAuthzCodes);
        } catch (DataAccessException e) {
            throw Utils.handleServerException(Constants.SERVER_ERROR.OTAC_RETRIEVE_ERROR, null, e);
        }
    }

    private OneTimeAuthzCode buildOneTimeAuthzCode(List<OneTimeAuthzCode> oneTimeAuthzCodes) {

        OneTimeAuthzCode oneTimeAuthzCode = new OneTimeAuthzCode();
        oneTimeAuthzCodes.forEach(secret -> {
            oneTimeAuthzCode.setId(secret.getId());
            oneTimeAuthzCode.setExpiry(secret.getExpiry());
            oneTimeAuthzCode.setSecret(secret.getSecret());
            oneTimeAuthzCode.setSecretStatus(secret.getSecretStatus());
        });
        return oneTimeAuthzCode;
    }

}
