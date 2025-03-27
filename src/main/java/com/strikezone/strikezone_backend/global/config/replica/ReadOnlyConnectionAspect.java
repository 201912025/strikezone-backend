package com.strikezone.strikezone_backend.global.config.replica;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(0)  // 트랜잭션 어드바이스보다 먼저 실행되도록 함
public class ReadOnlyConnectionAspect {

    @Around("@annotation(com.strikezone.strikezone_backend.global.config.replica.ReadOnlyConnection)")
    public Object setReadOnlyDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 읽기 전용 메소드이면 REPLICA를 사용
            RoutingDataSource.setDataSourceType(RoutingDataSource.DataSourceType.REPLICA);
            return joinPoint.proceed();
        } finally {
            // 메소드 실행 후 ThreadLocal 값 초기화
            RoutingDataSource.clearDataSourceType();
        }
    }
}
