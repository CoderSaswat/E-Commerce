package com.ecommerce.order.controller;

import com.ecommerce.order.dto.ItemsPriceDto;
import com.ecommerce.order.dto.OrdersDto;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;

    @Value("${billingServiceBaseUrl}")
    private String billingServiceBaseUrl;

    @GetMapping("/orders-without-price")
    public List<OrdersDto> getOrders(){
        log.info("getting orders without prices");
        return getAllOrders();
    }

    @PostMapping("/orders")
    public void placeOrder(@RequestBody OrdersDto ordersDto){
        Order order = new Order();
        order.setOrderPlacedAt(LocalDateTime.now());
        order.setName(ordersDto.getName());
        order.setIsPaid(Boolean.TRUE);
        order.setNoOfItems(ordersDto.getNoOfItems());
        order.setId(UUID.randomUUID());
        orderRepository.save(order);
    }

    @GetMapping ("/orders")
    public List<OrdersDto> getOrderFromDB(){

        Map<String, ItemsPriceDto> map = getOrderPriceFromBillingMicroserviceFromDB();
        List<Order> orderList = orderRepository.findAll();
        List<OrdersDto> ordersDtoList = new ArrayList<>();
        for (Order order : orderList) {
            OrdersDto ordersDto = new OrdersDto();
            ItemsPriceDto itemsPriceDto = map.get(order.getName());
            ordersDto.setNoOfItems(order.getNoOfItems());
            ordersDto.setOrderPlacedAt(order.getOrderPlacedAt());
            ordersDto.setIsPaid(order.getIsPaid());
            ordersDto.setId(order.getId());
            ordersDto.setName(order.getName());
            ordersDto.setPrice(itemsPriceDto.getPrice()*ordersDto.getNoOfItems());
            ordersDtoList.add(ordersDto);
        }
        return ordersDtoList;
    }


    private List<OrdersDto> getAllOrders() {
        List<OrdersDto> ordersDtoList = new ArrayList<>();
        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setName("hp laptop");
        ordersDto.setNoOfItems(2);
        ordersDto.setOrderPlacedAt(LocalDateTime.now());
        ordersDto.setIsPaid(Boolean.TRUE);
        ordersDtoList.add(ordersDto);
        return ordersDtoList;
    }

    @GetMapping("/orders-with-price")
    public List<OrdersDto> getOrdersWithPrices(){
        log.info("getting orders with price");
        return getAllOrdersWithPrices();
    }

    private List<OrdersDto> getAllOrdersWithPrices() {
        Map<String, ItemsPriceDto> map = getOrderPriceFromBillingMicroservice();
        List<OrdersDto> allOrders = getAllOrders();
        for (OrdersDto ordersDto : allOrders) {
            ItemsPriceDto itemsPriceDto = map.get(ordersDto.getName());
            ordersDto.setPrice(itemsPriceDto.getPrice()*ordersDto.getNoOfItems());
        }
        return allOrders;
    }

    private Map<String, ItemsPriceDto> getOrderPriceFromBillingMicroservice() {
        log.info("billing service base url from properties file {}",billingServiceBaseUrl);
//        String url = "http://billing-service:8080/item-prices";
        String url = billingServiceBaseUrl+"/item-prices";

        log.info("url : {}",url);
        ParameterizedTypeReference<List<ItemsPriceDto>> responseType =
                new ParameterizedTypeReference<>() {
                };
        ResponseEntity<List<ItemsPriceDto>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                responseType
        );
        List<ItemsPriceDto> itemsPriceDtoList = responseEntity.getBody();


        // Convert the list to a map
        return itemsPriceDtoList.stream()
                .collect(Collectors.toMap(ItemsPriceDto::getName, item -> item));
    }

    private Map<String, ItemsPriceDto> getOrderPriceFromBillingMicroserviceFromDB() {
        log.info("billing service base url from properties file {}",billingServiceBaseUrl);
//        String url = "http://billing-service:8080/item-prices";
        String url = billingServiceBaseUrl+"/items";

        log.info("url : {}",url);
        ParameterizedTypeReference<List<ItemsPriceDto>> responseType =
                new ParameterizedTypeReference<>() {
                };
        ResponseEntity<List<ItemsPriceDto>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                responseType
        );
        List<ItemsPriceDto> itemsPriceDtoList = responseEntity.getBody();


        // Convert the list to a map
        return itemsPriceDtoList.stream()
                .collect(Collectors.toMap(ItemsPriceDto::getName, item -> item));
    }
}
