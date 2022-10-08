package com.example.mystore_detailed.service;


import com.mystore.entity.Product;

import java.util.List;

/** 处理商品数据的业务层接口 */
public interface IProductService {
    /**
     * 根据商品id查询商品详情
     * @param id 商品id
     * @return 匹配的商品详情，如果没有匹配的数据则返回null
     */
    Product findById(Integer id);
}
