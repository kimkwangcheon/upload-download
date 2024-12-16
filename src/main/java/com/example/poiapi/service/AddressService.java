package com.example.poiapi.service;

import com.example.poiapi.repository.AddressMapper;
import com.example.poiapi.repository.TestMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressService {

    private final AddressMapper addressMapper;

    public AddressService(AddressMapper addressMapper){
        this.addressMapper = addressMapper;
    }

    public void insertAddress(List<Map<String, Object>> data) {
        addressMapper.insertAddress(data);
    }
}
