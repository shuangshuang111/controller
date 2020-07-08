package com.dongnaoedu.mall.service;

import java.util.List;

import com.dongnaoedu.mall.pojo.TbItemCat;
import com.dongnaoedu.mall.pojo.common.ZTreeNode;

public interface ItemCatService {

    TbItemCat getItemCatById(Long id);

    List<ZTreeNode> getItemCatList(int parentId);

    int addItemCat(TbItemCat tbItemCat);

    int updateItemCat(TbItemCat tbItemCat);

    void deleteItemCat(Long id);

    void deleteZTree(Long id);
}
