package com.example.mystore_detailed.controller;

import com.example.mystore_detailed.service.IProductService;
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


    @GetMapping("{id}/details")
    public JsonResult<Product> getById(@PathVariable("id") Integer id) {
        // 调用业务对象执行获取数据
        Product data = productService.findById(id);
        // 返回成功和数据
        return new JsonResult<Product>(OK, data);
    }
}