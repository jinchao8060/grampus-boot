package com.vdegree.grampus.common.lock.exception;

/**
 * 加锁异常
 * Company: v-degree
 *
 * @author Beck
 * @date 2021-01-29
 */
public class LockException extends RuntimeException {

	public LockException() {
		super();
	}

	public LockException(String message) {

		super(message);
	}

}
