package com.dongnaoedu.mall.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dongnaoedu.mall.pojo.TbThanks;
import com.dongnaoedu.mall.pojo.common.DataTablesResult;
import com.dongnaoedu.mall.pojo.common.Result;
import com.dongnaoedu.mall.service.ThanksService;
import com.dongnaoedu.mall.utils.ResultUtil;

/**
 * 
 */
@RestController
public class ThanksController {

    @Autowired
    private ThanksService thanksService;

    @RequestMapping(value = "/thanks/list",method = RequestMethod.GET)
    public DataTablesResult getThanksList(){

        DataTablesResult result=thanksService.getThanksList();
        return result;
    }

    @RequestMapping(value = "/thanks/count",method = RequestMethod.GET)
    public Result<Object> getThanksCount(){

        Long result=thanksService.countThanks();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/thanks/add",method = RequestMethod.POST)
    public Result<Object> addThanks(@ModelAttribute TbThanks tbThanks){

        thanksService.addThanks(tbThanks);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/thanks/update",method = RequestMethod.POST)
    public Result<Object> updateThanks(@ModelAttribute TbThanks tbThanks){

        thanksService.updateThanks(tbThanks);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/thanks/del/{ids}",method = RequestMethod.DELETE)
    public Result<Object> delThanks(@PathVariable int[] ids){

        for(int id:ids){
            thanksService.deleteThanks(id);
        }
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/thanks/{id}",method = RequestMethod.GET)
    public Result<TbThanks> getThanks(@PathVariable int id){

        TbThanks tbThanks=thanksService.getThankById(id);
        return new ResultUtil<TbThanks>().setData(tbThanks);
    }
}
