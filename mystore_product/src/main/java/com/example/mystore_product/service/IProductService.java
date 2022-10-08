package com.example.mystore_product.service;


import com.mystore.entity.Product;

import java.util.List;

/** 处理商品数据的业务层接口 */
public interface IProductService {
    /**
     * 查询热销商品的前四名
     * @return 热销商品前四名的集合
     */
    List<Product> findHotList();
}
