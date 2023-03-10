package com.example.demo.repository;

import com.example.demo.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    private String memeberName; // 회원 이름
    private OrderStatus orderStatus; // 주문 상태(ex. ORDER, CANCEL)


}
