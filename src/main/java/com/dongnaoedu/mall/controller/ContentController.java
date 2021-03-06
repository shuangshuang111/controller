package com.dongnaoedu.mall.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.dongnaoedu.mall.pojo.TbPanelContent;
import com.dongnaoedu.mall.pojo.common.DataTablesResult;
import com.dongnaoedu.mall.pojo.common.Result;
import com.dongnaoedu.mall.service.ContentService;
import com.dongnaoedu.mall.utils.ResultUtil;

@RestController
@Api("内容列表信息")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/content/list/{panelId}",method = RequestMethod.GET)
    @ApiOperation(value = "通过panelId获得板块内容列表")
    public DataTablesResult getContentByCid(@PathVariable int panelId){

        DataTablesResult result=contentService.getPanelContentListByPanelId(panelId);
        return result;
    }

    @RequestMapping(value = "/content/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加板块内容")
    public Result<Object> addContent(@ModelAttribute TbPanelContent tbPanelContent){

        contentService.addPanelContent(tbPanelContent);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/content/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑板块内容")
    public Result<Object> updateContent(@ModelAttribute TbPanelContent tbPanelContent){

        contentService.updateContent(tbPanelContent);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/content/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除板块内容")
    public Result<Object> addContent(@PathVariable int[] ids){

        for(int id:ids){
            contentService.deletePanelContent(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
