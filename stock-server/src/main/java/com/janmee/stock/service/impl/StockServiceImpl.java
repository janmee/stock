package com.janmee.stock.service.impl;

import com.seewo.core.util.bean.ObjectUtils;
import com.janmee.stock.dao.StockDao;
import com.janmee.stock.entity.Stock;
import com.janmee.stock.service.StockService;
import com.janmee.stock.vo.query.StockQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import javax.transaction.Transactional;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StockServiceImpl implements StockService {

	private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

	@Autowired
	private StockDao stockDao;

	@Override
	public Page<Stock> findAll(Pageable pageable) {
		return stockDao.findAll(pageable);
	}

	@Override
	public Page<Stock> findAll(StockQuery query, Pageable pageable) {
		Specification<Stock> spec = this.buildSpecification(query);
		return stockDao.findAll(spec, pageable);
	}

	@Override
    public Stock findOne(String id) {
        return stockDao.findOne(id);
    }

	@Override
	public Stock create(Stock stock) {
		return stockDao.save(stock);
	}

	@Override
	public Stock update(Stock stock){
		return stockDao.save(stock);
	}

	@Override
	public void delete(String id){
		stockDao.delete(id);
	}

	/**
     * 创建动态查询条件组合.
     */
    private Specification<Stock> buildSpecification(final StockQuery query) {
        return new Specification<Stock>() {
            @Override
            public Predicate toPredicate(Root<Stock> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (ObjectUtils.isNotBlank(query.getName())) {
                    Path<String> name = root.get("name");
                    predicates.add(criteriaBuilder.equal(name, query.getName()));
                }
			    if (ObjectUtils.isNotBlank(query.getCname())) {
                    Path<String> cname = root.get("cname");
                    predicates.add(criteriaBuilder.equal(cname, query.getCname()));
                }
			    if (ObjectUtils.isNotBlank(query.getCategory())) {
                    Path<String> category = root.get("category");
                    predicates.add(criteriaBuilder.equal(category, query.getCategory()));
                }
			    if (ObjectUtils.isNotBlank(query.getCategoryId())) {
                    Path<String> categoryId = root.get("categoryId");
                    predicates.add(criteriaBuilder.equal(categoryId, query.getCategoryId()));
                }
			    if (ObjectUtils.isNotBlank(query.getSymbol())) {
                    Path<String> symbol = root.get("symbol");
                    predicates.add(criteriaBuilder.equal(symbol, query.getSymbol()));
                }
			    if (ObjectUtils.isNotBlank(query.getMktcap())) {
                    Path<String> mktcap = root.get("mktcap");
                    predicates.add(criteriaBuilder.equal(mktcap, query.getMktcap()));
                }
			    if (ObjectUtils.isNotBlank(query.getPe())) {
                    Path<String> pe = root.get("pe");
                    predicates.add(criteriaBuilder.equal(pe, query.getPe()));
                }
			    if (ObjectUtils.isNotBlank(query.getMarket())) {
                    Path<String> market = root.get("market");
                    predicates.add(criteriaBuilder.equal(market, query.getMarket()));
                }
			    if (!predicates.isEmpty()) {
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
                return criteriaBuilder.conjunction();
            }
        };
    }
}
