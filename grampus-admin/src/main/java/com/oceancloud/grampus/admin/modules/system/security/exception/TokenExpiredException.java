package com.oceancloud.grampus.admin.modules.system.security.exception;

import com.oceancloud.grampus.admin.modules.system.code.ErrorCode;
import com.oceancloud.grampus.framework.core.exception.ApiException;

/**
 * Token过期异常
 *
 * @author Beck
 * @since 2021-05-07
 */
public class TokenExpiredException extends ApiException {
	private static final long serialVersionUID = -7284332526504876630L;

	public TokenExpiredException() {
		super(ErrorCode.Auth.TOKEN_EXPIRED_ERROR.getCode());
	}

	public TokenExpiredException(String msg) {
		super(ErrorCode.Auth.TOKEN_EXPIRED_ERROR.getCode(), msg);
	}
}
