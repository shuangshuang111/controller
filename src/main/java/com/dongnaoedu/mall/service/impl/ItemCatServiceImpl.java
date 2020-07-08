package com.dongnaoedu.mall.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongnaoedu.mall.common.exception.MallException;
import com.dongnaoedu.mall.mapper.TbItemCatMapper;
import com.dongnaoedu.mall.pojo.TbItemCat;
import com.dongnaoedu.mall.pojo.TbItemCatExample;
import com.dongnaoedu.mall.pojo.common.ZTreeNode;
import com.dongnaoedu.mall.service.ItemCatService;
import com.dongnaoedu.mall.utils.DtoUtil;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public TbItemCat getItemCatById(Long id) {

        TbItemCat tbItemCat=tbItemCatMapper.selectByPrimaryKey(id);
        if(tbItemCat==null){
            throw new MallException("通过id获取商品分类失败");
        }
        return tbItemCat;
    }

    @Override
    public List<ZTreeNode> getItemCatList(int parentId) {
        TbItemCatExample example=new TbItemCatExample();
        TbItemCatExample.Criteria criteria=example.createCriteria();
        //排序
        example.setOrderByClause("sort_order");
        //条件查询
        criteria.andParentIdEqualTo(new Long(parentId));
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);

        //转换成ZtreeNode
        List<ZTreeNode> resultList=new ArrayList<>();

        for(TbItemCat tbItemCat:list){

            ZTreeNode node= DtoUtil.TbItemCat2ZTreeNode(tbItemCat);

            resultList.add(node);
        }

        return resultList;
    }

    @Override
    public int addItemCat(TbItemCat tbItemCat) {

        tbItemCat.setCreated(new Date());
        tbItemCat.setUpdated(new Date());

        if(tbItemCatMapper.insert(tbItemCat)!=1){
            throw new MallException("添加商品分类失败");
        }
        return 1;
    }

    @Override
    public int updateItemCat(TbItemCat tbItemCat) {

        TbItemCat old=getItemCatById(tbItemCat.getId());
        tbItemCat.setCreated(old.getCreated());
        tbItemCat.setUpdated(new Date());

        if(tbItemCatMapper.updateByPrimaryKey(tbItemCat)!=1){
            throw new MallException("添加商品分类失败");
        }
        return 1;
    }

    @Override
    public void deleteItemCat(Long id) {

        deleteZTree(id);
    }

    @Override
    public void deleteZTree(Long id) {

        //查询该节点所有子节点
        List<ZTreeNode> node=getItemCatList(Math.toIntExact(id));
        if(node.size()>0){
            //如果有子节点，遍历子节点
            for(int i=0;i<node.size();i++){
                deleteItemCat((long) node.get(i).getId());
            }
        }
        //没有子节点直接删除
        if(tbItemCatMapper.deleteByPrimaryKey(id)!=1){
            throw new MallException("删除商品分类失败");
        }
    }

}
