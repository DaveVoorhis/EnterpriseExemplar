package org.reldb.exemplars.java.backend.exception;

/**
 * These should be unique to each exception, and are returned in error responses as the 'code'
 * attribute, which is a key the frontend can use to look up localisations (human language
 * translations, etc.) of the relevant message. The backend response 'message' attribute -- which
 * contains the backend-generated error message -- is intended to be helpful to developers, but
 * user-facing error messages should be constructed using the 'code' attribute and the 'details'
 * attribute.
 */
public enum ErrorCodes {
    ERR_BadRequest,
    ERR_Unauthorized,
    ERR_NotFound,
    ERR_UnexpectedServerError,
    ERR_NotImplemented,
    ERR_DemoNotFound,
    ERR_InvalidRequestId,
    ERR_PermissionNotFound,
    ERR_RoleDeletionDisallowed,
    ERR_RoleInUseNotDeleted,
    ERR_UserLacksPermission,
    ERR_RoleImmutable,
    ERR_RoleNotFound,
    ERR_SomethingUnusualHappened,
    ERR_UserCredentialsInvalid,
    ERR_UserNotEnabled,
    ERR_UserNotFound
}
