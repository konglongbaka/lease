package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.AttrValue;
import com.atguigu.lease.model.entity.FeeKey;
import com.atguigu.lease.model.entity.FeeValue;
import com.atguigu.lease.web.admin.mapper.FeeKeyMapper;
import com.atguigu.lease.web.admin.mapper.FeeValueMapper;
import com.atguigu.lease.web.admin.service.FeeKeyService;
import com.atguigu.lease.web.admin.vo.attr.AttrKeyVo;
import com.atguigu.lease.web.admin.vo.fee.FeeKeyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author liubo
* @description 针对表【fee_key(杂项费用名称表)】的数据库操作Service实现
* @createDate 2023-07-24 15:48:00
*/
@Service
public class FeeKeyServiceImpl extends ServiceImpl<FeeKeyMapper, FeeKey>
    implements FeeKeyService{
    @Autowired
    private FeeKeyMapper feeKeyMapper;
    @Autowired
    private FeeValueMapper feeValueMapper;
    @Override
    public List<FeeKeyVo> labellist() {
        List<FeeKeyVo> list = new ArrayList<>();
        List<FeeKey> feeKeys = feeKeyMapper.selectList(null);
        for (FeeKey feeKey: feeKeys) {
            FeeKeyVo vo = new FeeKeyVo();
            BeanUtils.copyProperties(feeKey, vo);
            LambdaQueryWrapper<FeeValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(FeeValue::getFeeKeyId, feeKey.getId());
            vo.setFeeValueList(feeValueMapper.selectList(lambdaQueryWrapper));
            list.add(vo);
        }
        return list;
    }
}




