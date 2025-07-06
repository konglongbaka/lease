package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.*;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.atguigu.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.atguigu.lease.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    private ApartmentFacilityService apartmentFacilityService;
    @Autowired
    private ApartmentFacilityMapper apartmentFacilityMapper;
    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;
    @Autowired
    private ApartmentFeeValueMapper apartmentFeeValueMapper;
    @Autowired
    private ApartmentLabelService apartmentLabelService;
    @Autowired
    private ApartmentLabelMapper apartmentLabelMapper;
    @Autowired
    private GraphInfoService graphInfoService;
    @Autowired
    private GraphInfoMapper graphInfoMapper;
    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;
    @Autowired
    private RoomInfoMapper roomInfoMapper;
    @Autowired
    private LeaseAgreementMapper leaseAgreementMapper;
    @Override
    public void saveOrUpdateInfo(ApartmentSubmitVo apartmentSubmitVo) {
        boolean update = apartmentSubmitVo.getId()!=null;
        super.saveOrUpdate(apartmentSubmitVo);
        //判断是否为update
        if (update){
            //批量
            //删除配套
            LambdaQueryWrapper<ApartmentFacility> wrapper1 =new LambdaQueryWrapper<>();
            wrapper1.eq(ApartmentFacility::getApartmentId,apartmentSubmitVo.getId());
            apartmentFacilityService.remove(wrapper1);
            //删除lable
            LambdaQueryWrapper<ApartmentLabel> wrapper2 =new LambdaQueryWrapper<>();
            wrapper2.eq(ApartmentLabel::getApartmentId,apartmentSubmitVo.getId());
            apartmentLabelService.remove(wrapper2);
            //删除fee
            LambdaQueryWrapper<ApartmentFeeValue> wrapper3 =new LambdaQueryWrapper<>();
            wrapper3.eq(ApartmentFeeValue::getApartmentId,apartmentSubmitVo.getId());
            apartmentFeeValueService.remove(wrapper3);
            //删除img
            LambdaQueryWrapper<GraphInfo> wrapper4 = new LambdaQueryWrapper<>();
            wrapper4.eq(GraphInfo::getItemId,apartmentSubmitVo.getId());
            wrapper4.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        }

        //更新
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        if (!(graphVoList).isEmpty()){
            ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setItemId(apartmentSubmitVo.getId());
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfoList.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfoList);
        }
        //更新配套
        List<Long> facilityInfoIds = apartmentSubmitVo.getFacilityInfoIds();
        if(!facilityInfoIds.isEmpty()){
            ArrayList<ApartmentFacility> facilityList = new ArrayList<>();
            for (Long id:facilityInfoIds){
                ApartmentFacility apartmentFacility = new ApartmentFacility();
                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
                apartmentFacility.setFacilityId(id);
                facilityList.add(apartmentFacility);
            }
            apartmentFacilityService.saveBatch(facilityList);
        }
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!(labelIds).isEmpty()) {
            List<ApartmentLabel> apartmentLabelList = new ArrayList<>();
            for (Long labelId : labelIds) {
                ApartmentLabel apartmentLabel = new ApartmentLabel();
                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
                apartmentLabel.setLabelId(labelId);
                apartmentLabelList.add(apartmentLabel);
            }
            apartmentLabelService.saveBatch(apartmentLabelList);
        }
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!(feeValueIds).isEmpty()) {
            ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = new ApartmentFeeValue();
                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
                apartmentFeeValue.setFeeValueId(feeValueId);
                apartmentFeeValueList.add(apartmentFeeValue);
            }
            apartmentFeeValueService.saveBatch(apartmentFeeValueList);
        }

    }

    @Override
    public IPage<ApartmentItemVo> pageSelect(IPage<ApartmentInfo> page, ApartmentQueryVo queryVo) {
        LambdaQueryWrapper<ApartmentInfo> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(queryVo.getCityId()!=null,ApartmentInfo::getCityId,queryVo.getCityId());
        queryWrapper.eq(queryVo.getDistrictId()!=null,ApartmentInfo::getDistrictId,queryVo.getDistrictId());
        queryWrapper.eq(queryVo.getProvinceId()!=null,ApartmentInfo::getProvinceId,queryVo.getProvinceId());
        IPage<ApartmentInfo> resultPage = apartmentInfoMapper.selectPage(page,queryWrapper);
        List<ApartmentInfo> records = resultPage.getRecords();
        List<ApartmentItemVo> result = new ArrayList<>();
        for (ApartmentInfo record :records){
            ApartmentItemVo apartmentItemVo = new ApartmentItemVo();
            BeanUtils.copyProperties(record, apartmentItemVo);
            Long id = record.getId();
            LambdaQueryWrapper<RoomInfo> wrapper =new LambdaQueryWrapper<>();
            wrapper.eq(RoomInfo::getApartmentId,id);
            wrapper.eq(RoomInfo::getIsRelease,1);
            wrapper.eq(RoomInfo::getIsDeleted,0);
            LambdaQueryWrapper<LeaseAgreement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(LeaseAgreement::getIsDeleted, 0)
                    .in(LeaseAgreement::getStatus, 2, 5);
            apartmentItemVo.setTotalRoomCount(0L);
            apartmentItemVo.setFreeRoomCount(0L);
            Long totalToomCount = roomInfoMapper.selectCount(wrapper);
            Long roomCount = leaseAgreementMapper.selectCount(lambdaQueryWrapper);
            if (totalToomCount!=0){
                apartmentItemVo.setTotalRoomCount(totalToomCount);
                apartmentItemVo.setFreeRoomCount(totalToomCount-roomCount);
            }
            result.add(apartmentItemVo);
        }

        return new Page<ApartmentItemVo>()
                .setRecords(result)
                .setTotal(resultPage.getTotal())
                .setSize(resultPage.getSize())
                .setCurrent(resultPage.getCurrent());
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
        BeanUtils.copyProperties(apartmentInfo,apartmentDetailVo);
        apartmentDetailVo.setFacilityInfoList(apartmentFacilityMapper.selectByApartmentId(id));
        apartmentDetailVo.setGraphVoList(graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT,id));
        apartmentDetailVo.setLabelInfoList(apartmentLabelMapper.selectByApartmentId(id));
        apartmentDetailVo.setFeeValueVoList(apartmentFeeValueMapper.selectByApartmentId(id));
        return apartmentDetailVo;
    }

    @Override
    public void removeApartmentById(Long id) {
        LambdaQueryWrapper<RoomInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoomInfo::getApartmentId,id);
        Long count = roomInfoMapper.selectCount(lambdaQueryWrapper);
        if (count>0){
            throw new LeaseException(ResultCodeEnum.ADMIN_DELETE_ERROR);
        }
        LambdaQueryWrapper<ApartmentFacility> wrapper1 =new LambdaQueryWrapper<>();
        wrapper1.eq(ApartmentFacility::getApartmentId,id);
        apartmentFacilityService.remove(wrapper1);
        //删除lable
        LambdaQueryWrapper<ApartmentLabel> wrapper2 =new LambdaQueryWrapper<>();
        wrapper2.eq(ApartmentLabel::getApartmentId,id);
        apartmentLabelService.remove(wrapper2);
        //删除fee
        LambdaQueryWrapper<ApartmentFeeValue> wrapper3 =new LambdaQueryWrapper<>();
        wrapper3.eq(ApartmentFeeValue::getApartmentId,id);
        apartmentFeeValueService.remove(wrapper3);
        //删除img
        LambdaQueryWrapper<GraphInfo> wrapper4 = new LambdaQueryWrapper<>();
        wrapper4.eq(GraphInfo::getItemId,id);
        wrapper4.eq(GraphInfo::getItemType, ItemType.APARTMENT);
    }


}




