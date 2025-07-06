package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.AttrKey;
import com.atguigu.lease.model.entity.AttrValue;
import com.atguigu.lease.web.admin.mapper.AttrKeyMapper;
import com.atguigu.lease.web.admin.mapper.AttrValueMapper;
import com.atguigu.lease.web.admin.service.AttrKeyService;
import com.atguigu.lease.web.admin.vo.attr.AttrKeyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author liubo
* @description 针对表【attr_key(房间基本属性表)】的数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class AttrKeyServiceImpl extends ServiceImpl<AttrKeyMapper, AttrKey>
    implements AttrKeyService{
    @Autowired
    private AttrKeyMapper attrKeyMapper;
    @Autowired
    private AttrValueMapper attrValueMapper;

    @Override
    public List<AttrKeyVo> labellist() {
        List<AttrKeyVo> list = new ArrayList<>();
        List<AttrKey> attrKeys = attrKeyMapper.selectList(null);
//        attrKeys.forEach(
//                AttrKeyVo vo = new AttrKeyVo();
//        BeanUtils.copyProperties(attrKey, vo);
//        LambdaQueryWrapper<AttrValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(AttrValue::getAttrKeyId,attrKey.getId());
//        vo.setAttrValueList(attrValueMapper.selectList(lambdaQueryWrapper));
//        list.add(vo);
//        );
        for (AttrKey attrKey : attrKeys) {
            AttrKeyVo vo = new AttrKeyVo();
            BeanUtils.copyProperties(attrKey, vo);
            LambdaQueryWrapper<AttrValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AttrValue::getAttrKeyId,attrKey.getId());
            vo.setAttrValueList(attrValueMapper.selectList(lambdaQueryWrapper));
            list.add(vo);
        }
        return list;
    }
}




