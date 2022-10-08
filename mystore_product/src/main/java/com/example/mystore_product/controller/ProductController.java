package com.example.mystore_product.controller;


import com.example.mystore_product.service.IProductService;
import com.mystore.controller.BaseController;
import com.mystore.entity.Product;
import com.mystore.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("products")
public class ProductController extends BaseController {
    @Autowired
    private IProductService productService;

    @RequestMapping("hot_list")
    public JsonResult<List<Product>> getHotList() {
        List<Product> data = productService.findHotList();
        System.out.println("欢迎访问到热点商品------------------------------------------");
        return new JsonResult<List<Product>>(OK, data);
    }

}