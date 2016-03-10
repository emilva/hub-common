package com.blackducksoftware.integration.hub.policy.api;

public enum PolicyStatusEnum {
    IN_VIOLATION,
    IN_VIOLATION_OVERRIDDEN,
    NOT_IN_VIOLATION,
    UNKNOWN;

    public static PolicyStatusEnum getPolicyStatusEnum(String policyStatus) {
        if (policyStatus == null) {
            return PolicyStatusEnum.UNKNOWN;
        }
        PolicyStatusEnum policyStatusEnum;
        try {
            policyStatusEnum = PolicyStatusEnum.valueOf(policyStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            // ignore expection
            policyStatusEnum = UNKNOWN;
        }
        return policyStatusEnum;
    }
}