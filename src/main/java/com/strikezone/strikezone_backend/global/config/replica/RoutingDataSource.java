package com.strikezone.strikezone_backend.global.config.replica;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<>();

    public enum DataSourceType {
        MASTER, REPLICA
    }

    public static void setDataSourceType(DataSourceType dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceType dataSourceType = contextHolder.get();
        // 기본값은 MASTER
        return (dataSourceType == null) ? DataSourceType.MASTER : dataSourceType;
    }
}
