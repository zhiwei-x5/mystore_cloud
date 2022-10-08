package com.example.mystore_detailed.service.impl;


import com.example.mystore_detailed.mapper.ProductMapper;
import com.example.mystore_detailed.service.IProductService;
import com.mystore.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** 处理商品数据的业务层实现类 */
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product findById(Integer id) {
        // 根据参数id调用私有方法执行查询，获取商品数据
        Product product = productMapper.findById(id);
        // 判断查询结果是否为null
        // 将查询结果中的部分属性设置为null
        // 返回查询结果
        return product;
    }

}
