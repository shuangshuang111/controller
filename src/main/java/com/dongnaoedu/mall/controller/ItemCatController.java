package com.dongnaoedu.mall.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dongnaoedu.mall.pojo.TbItemCat;
import com.dongnaoedu.mall.pojo.common.Result;
import com.dongnaoedu.mall.pojo.common.ZTreeNode;
import com.dongnaoedu.mall.service.ItemCatService;
import com.dongnaoedu.mall.utils.ResultUtil;

import java.util.List;

@RestController
@Api("商品购物车")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping(value = "/item/cat/list",method = RequestMethod.GET)
    @ApiOperation(value = "通过父ID获取商品分类列表")
    public List<ZTreeNode> getItemCatList(@RequestParam(name="id",defaultValue = "0") int parentId){

        List<ZTreeNode> list=itemCatService.getItemCatList(parentId);
        return list;
    }

    @RequestMapping(value = "/item/cat/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加商品分类")
    public Result<Object> addItemCategory(@ModelAttribute TbItemCat tbItemCat){

        itemCatService.addItemCat(tbItemCat);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/item/cat/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑商品分类")
    public Result<Object> updateItemCategory(@ModelAttribute TbItemCat tbItemCat){

        itemCatService.updateItemCat(tbItemCat);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/item/cat/del/{id}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除商品分类")
    public Result<Object> deleteItemCategory(@PathVariable Long id){

        itemCatService.deleteItemCat(id);
        return new ResultUtil<Object>().setData(null);
    }
}
