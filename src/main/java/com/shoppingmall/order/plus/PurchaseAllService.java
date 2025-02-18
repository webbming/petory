package com.shoppingmall.order.plus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shoppingmall.order.repository.PurchaseAllRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;


@Service
public class PurchaseAllService {
	
	@Autowired
	PurchaseAllRepository repository;


}