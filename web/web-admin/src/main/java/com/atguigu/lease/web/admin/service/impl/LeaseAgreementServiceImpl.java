package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.LeaseAgreement;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.LeaseAgreementService;
import com.atguigu.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.atguigu.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {
    @Autowired
    private LeaseAgreementMapper leaseAgreementMapper;
    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;
    @Autowired
    private RoomInfoMapper roomInfoMapper;
    @Autowired
    private PaymentTypeMapper paymentTypeMapper;
    @Autowired
    private LeaseTermMapper leaseTermMapper;

    @Override
    public IPage<AgreementVo> pageSelect(IPage<AgreementVo> page, AgreementQueryVo queryVo) {
        return leaseAgreementMapper.selectPageAgreementVo(page, queryVo);
    }

    @Override
    public AgreementVo agreementVogetById(Long id) {
        AgreementVo agreementVo = new AgreementVo();
        LeaseAgreement leaseAgreement = leaseAgreementMapper.selectById(id);
        BeanUtils.copyProperties(leaseAgreement, agreementVo);
//        @Schema(description = "签约公寓信息")
//        private ApartmentInfo apartmentInfo;
//
//        @Schema(description = "签约房间信息")
//        private RoomInfo roomInfo;
//
//        @Schema(description = "支付方式")
//        private PaymentType paymentType;
//
//        @Schema(description = "租期")
//        private LeaseTerm leaseTerm;
        agreementVo.setApartmentInfo(apartmentInfoMapper.selectById(leaseAgreement.getApartmentId()));
        agreementVo.setRoomInfo(roomInfoMapper.selectById(leaseAgreement.getRoomId()));
        agreementVo.setPaymentType(paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId()));
        agreementVo.setLeaseTerm(leaseTermMapper.selectById(leaseAgreement.getLeaseTermId()));
        return agreementVo;
    }
}




