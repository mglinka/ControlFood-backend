package com.project.utils;

import org.slf4j.MDC;

public class TransactionContext {

    public static void setTransactionId(String id) {
        MDC.put("transactionId", id);
    }

    public static void clear() {
        MDC.remove("transactionId");
    }

}
