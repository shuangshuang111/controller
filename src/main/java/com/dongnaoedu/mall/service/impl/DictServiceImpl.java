package com.dongnaoedu.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dongnaoedu.mall.common.constant.DictConstant;
import com.dongnaoedu.mall.mapper.TbDictMapper;
import com.dongnaoedu.mall.pojo.TbDict;
import com.dongnaoedu.mall.pojo.TbDictExample;
import com.dongnaoedu.mall.service.DictService;

import java.util.List;

/**
 * 
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private TbDictMapper tbDictMapper;

    @Override
    public List<TbDict> getDictList() {

        TbDictExample example=new TbDictExample();
        TbDictExample.Criteria criteria=example.createCriteria();
        //条件查询
        criteria.andTypeEqualTo(DictConstant.DICT_EXT);
        List<TbDict> list = tbDictMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<TbDict> getStopList() {

        TbDictExample example=new TbDictExample();
        TbDictExample.Criteria criteria=example.createCriteria();
        //条件查询
        criteria.andTypeEqualTo(DictConstant.DICT_STOP);
        List<TbDict> list = tbDictMapper.selectByExample(example);
        return list;
    }

    @Override
    public int addDict(TbDict tbDict) {

        tbDictMapper.insert(tbDict);
        return 1;
    }

    @Override
    public int updateDict(TbDict tbDict) {

        tbDictMapper.updateByPrimaryKey(tbDict);
        return 1;
    }

    @Override
    public int delDict(int id) {

        tbDictMapper.deleteByPrimaryKey(id);
        return 1;
    }
}
