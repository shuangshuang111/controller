package com.dongnaoedu.mall.service.impl;

import com.dongnaoedu.mall.common.exception.MallException;
import com.dongnaoedu.mall.common.jedis.JedisClient;
import com.dongnaoedu.mall.mapper.TbItemCatMapper;
import com.dongnaoedu.mall.mapper.TbItemDescMapper;
import com.dongnaoedu.mall.mapper.TbItemMapper;
import com.dongnaoedu.mall.pojo.TbItem;
import com.dongnaoedu.mall.pojo.TbItemCat;
import com.dongnaoedu.mall.pojo.TbItemDesc;
import com.dongnaoedu.mall.pojo.TbItemExample;
import com.dongnaoedu.mall.pojo.common.DataTablesResult;
import com.dongnaoedu.mall.pojo.dto.ItemDto;
import com.dongnaoedu.mall.service.ItemService;
import com.dongnaoedu.mall.service.SearchItemService;
import com.dongnaoedu.mall.utils.DtoUtil;
import com.dongnaoedu.mall.utils.IDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final static Logger log= LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private SearchItemService searchItemService;

    @Autowired
    private JedisClient jedisClient;

    @Value("${PRODUCT_ITEM}")
    private String PRODUCT_ITEM;

    @Override
    public ItemDto getItemById(Long id) {
        ItemDto itemDto=new ItemDto();

        TbItem tbItem=tbItemMapper.selectByPrimaryKey(id);
        itemDto= DtoUtil.TbItem2ItemDto(tbItem);

        TbItemCat tbItemCat=tbItemCatMapper.selectByPrimaryKey(itemDto.getCid());
        itemDto.setCname(tbItemCat.getName());

        TbItemDesc tbItemDesc=tbItemDescMapper.selectByPrimaryKey(id);
        itemDto.setDetail(tbItemDesc == null ? "" : tbItemDesc.getItemDesc());

        return itemDto;
    }

    @Override
    public TbItem getNormalItemById(Long id) {

        return tbItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTablesResult getItemList(int draw, int start, int length, int cid, String search,
                                        String orderCol, String orderDir) {

        DataTablesResult result=new DataTablesResult();

        //分页执行查询返回结果
        PageHelper.startPage(start/length+1,length);
        List<TbItem> list = tbItemMapper.selectItemByCondition(cid,"%"+search+"%",orderCol,orderDir);
        PageInfo<TbItem> pageInfo=new PageInfo<>(list);
        result.setRecordsFiltered((int)pageInfo.getTotal());
        result.setRecordsTotal(getAllItemCount().getRecordsTotal());

        result.setDraw(draw);
        result.setData(list);

        return result;
    }

    @Override
    public DataTablesResult getItemSearchList(int draw, int start, int length,int cid, String search,
                                              String minDate, String maxDate, String orderCol, String orderDir) {

        DataTablesResult result=new DataTablesResult();

        //分页执行查询返回结果
        PageHelper.startPage(start/length+1,length);
        List<TbItem> list = tbItemMapper.selectItemByMultiCondition(cid,"%"+search+"%",minDate,maxDate,orderCol,orderDir);
        PageInfo<TbItem> pageInfo=new PageInfo<>(list);
        result.setRecordsFiltered((int)pageInfo.getTotal());
        result.setRecordsTotal(getAllItemCount().getRecordsTotal());

        result.setDraw(draw);
        result.setData(list);

        return result;
    }

    @Override
    public DataTablesResult getAllItemCount() {
        TbItemExample example=new TbItemExample();
        Long count=tbItemMapper.countByExample(example);
        DataTablesResult result=new DataTablesResult();
        result.setRecordsTotal(Math.toIntExact(count));
        return result;
    }

    @Override
    public TbItem alertItemState(Long id, Integer state) {

        TbItem tbMember = getNormalItemById(id);
        tbMember.setStatus(state.byteValue());
        tbMember.setUpdated(new Date());

        if (tbItemMapper.updateByPrimaryKey(tbMember) != 1){
            throw new MallException("修改商品状态失败");
        }
        return getNormalItemById(id);
    }

    @Override
    public int deleteItem(Long id) {

        if(tbItemMapper.deleteByPrimaryKey(id)!=1){
            throw new MallException("删除商品失败");
        }
        if(tbItemDescMapper.deleteByPrimaryKey(id)!=1){
            throw new MallException("删除商品详情失败");
        }
        //发送消息同步索引库
        try {
            searchItemService.refreshItem(1,id);
        }catch (Exception e){
            log.error(e.toString());
        }
        return 0;
    }

    @Override
    public TbItem addItem(ItemDto itemDto) {
        long id= IDUtil.getRandomId();
        TbItem tbItem= DtoUtil.ItemDto2TbItem(itemDto);
        tbItem.setId(id);
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        if(tbItem.getImage().isEmpty()){
            tbItem.setImage("http://ow2h3ee9w.bkt.clouddn.com/nopic.jpg");
        }
        if(tbItemMapper.insert(tbItem)!=1){
            throw new MallException("添加商品失败");
        }

        TbItemDesc tbItemDesc=new TbItemDesc();
        tbItemDesc.setItemId(id);
        tbItemDesc.setItemDesc(itemDto.getDetail());
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());

        if(tbItemDescMapper.insert(tbItemDesc)!=1){
            throw new MallException("添加商品详情失败");
        }
        //发送消息同步索引库
        try {
            searchItemService.refreshItem(0,id);
        }catch (Exception e){
            log.error(e.toString());
        }
        return getNormalItemById(id);
    }

    @Override
    public TbItem updateItem(Long id,ItemDto itemDto) {

        TbItem oldTbItem=getNormalItemById(id);

        TbItem tbItem= DtoUtil.ItemDto2TbItem(itemDto);

        if(tbItem.getImage().isEmpty()){
            tbItem.setImage(oldTbItem.getImage());
        }
        tbItem.setId(id);
        tbItem.setStatus(oldTbItem.getStatus());
        tbItem.setCreated(oldTbItem.getCreated());
        tbItem.setUpdated(new Date());
        if(tbItemMapper.updateByPrimaryKey(tbItem)!=1){
            throw new MallException("更新商品失败");
        }

        TbItemDesc tbItemDesc=new TbItemDesc();

        tbItemDesc.setItemId(id);
        tbItemDesc.setItemDesc(itemDto.getDetail());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setCreated(oldTbItem.getCreated());

        if(tbItemDescMapper.updateByPrimaryKey(tbItemDesc)!=1){
            throw new MallException("更新商品详情失败");
        }
        //同步缓存
        deleteProductDetRedis(id);
        //发送消息同步索引库
        try {
            searchItemService.refreshItem(0,id);
        }catch (Exception e){
            log.error(e.toString());
        }
        return getNormalItemById(id);
    }

    //同步商品详情缓存
    public void deleteProductDetRedis(Long id){
        try {
            jedisClient.del(PRODUCT_ITEM+":"+id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
