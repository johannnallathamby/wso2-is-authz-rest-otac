package org.wso2.carbon.identity.extension.authz.rest.otac;

/**
 * OTAC Rest Authentication constants
 */
public class Constants {

    public static final String PROTECTED_REQUEST_URI = "/scim2/Users";
    public static final String OTAC_HEADER_NAME = "One-Time-Authorization-Code";

    public static final String OTAC_REGISTRY_RESOURCE = "/identity/otac";
    public static final String OTAC_VALIDITY_PROPERTY = "otacValidity";

    public static final String CORRELATION_ID_MDC = "Correlation-ID";
    public static final String AUTH_CONTEXT = "auth-context";
    public static final String OTAC_RESPONSE_FACADE = "response-wrapper";

    public enum OTAC_STATE {
        ACTIVE, INACTIVE, EXPIRED
    }

    public enum OTAC_FAILURE {
        INVALID("OTAC-10010", "Invalid one-time-authorization-code", "Invalid one-time-authorization-code"),
        EXPIRED("OTAC-10011", "Expired one-time-authorization-code", "Expired one-time-authorization-code"),
        INACTIVE("OTAC-10012", "Inactive one-time-authorization-code", "Inactive one-time-authorization-code"),
        UNKNOWN("OTAC-10013", "Unknown", "Unknown");

        private final String code;
        private final String message;
        private final String description;

        OTAC_FAILURE(String code, String message, String description) {
            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return code + " - " + message;
        }
    }

    public enum SERVER_ERROR {

        OTAC_ADD_ERROR("OTAC-15002", "Error while storing the OTAC"),
        OTAC_RETRIEVE_ERROR("OTAC-15003", "Error while retrieving the OTAC"),
        OTAC_VALIDITY_RETRIEVE_ERROR("OTAC-15019","Error retrieving OTAC validity");

        private final String code;
        private final String message;

        SERVER_ERROR(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
