package com.gws.common.constants.backstage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ylx
 */
public class Roles {
	private Roles() {
		throw new AssertionError("instantiation is not permitted");
	}
	public static final String SUPER_ADMIN="超级管理员";
	public static final String ADMIN="管理员";
	
	public static final List<String> ADMIN_ROLES = new ArrayList<>();
	static{
		ADMIN_ROLES.add(SUPER_ADMIN);
		ADMIN_ROLES.add(ADMIN);
	}
	
}
