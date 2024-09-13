package com.spring_cloud.eureka.client.auth.domain;

public enum UserRoleEnum {
    COMPANY(Authority.COMPANY),  // 사용자 권한
    DELIVERY_MANAGER(Authority.DELIVERY_MANAGER),  // 사업주 권한
    HUB_MANAGER(Authority.HUB_MANAGER),  // 관리자 권한
    MASTER(Authority.MASTER); // 마스터 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String COMPANY = "ROLE_COMPNAY";
        public static final String DELIVERY_MANAGER = "ROLE_DELIVERY_MANAGER";
        public static final String HUB_MANAGER = "ROLE_HUB_MANAGER";
        public static final String MASTER = "ROLE_MASTER";
    }
}