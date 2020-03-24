package com.shaolonger.monitorplatform.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service基类
 */
public class ServiceBase {

    @PersistenceContext
    protected EntityManager entityManager;

    public <E> Page<E> findPageBySqlAndParam(Class<E> eClass, String dataSql, String countSql, Pageable pageable, Map<String, Object> paramMap) {
        Query countQuery = entityManager.createNativeQuery(countSql);
        Query dataQuery = entityManager.createNativeQuery(dataSql, eClass);

        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
            dataQuery.setParameter(entry.getKey(), entry.getValue());
        }

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        // 设置返回的第一条数据
        dataQuery.setFirstResult(pageNumber * pageSize);
        // 设置返回最大条数，也就是限定一页返回的数量
        dataQuery.setMaxResults(pageSize);
        long total = ((BigInteger)(countQuery.getSingleResult())).longValue();
        List<E> content = total > pageable.getOffset() ? dataQuery.getResultList() : Collections.emptyList();

        return new PageImpl<E>(content, pageable, total);
    }
}
