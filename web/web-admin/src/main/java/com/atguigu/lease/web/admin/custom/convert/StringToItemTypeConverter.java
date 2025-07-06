package com.atguigu.lease.web.admin.custom.convert;

import com.atguigu.lease.model.enums.ItemType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StringToItemTypeConverter implements Converter<String, ItemType> {

    @Override
    public ItemType convert(String code) {

        ItemType[] values = ItemType.values();
        for (ItemType value:values){
            if (Integer.valueOf(code).equals(value.getCode())){
                return value;
            }
        }
        throw new IllegalArgumentException("非法字符"+code);
    }
}