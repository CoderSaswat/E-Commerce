package com.ecommerce.billing.controller;

import com.ecommerce.billing.dto.ItemsPriceDto;
import com.ecommerce.billing.entity.Item;
import com.ecommerce.billing.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PriceController {

    private final RestTemplate restTemplate;
    private final ItemRepository itemRepository;

    @GetMapping("/item-prices")
    public List<ItemsPriceDto> getOrders(){
        log.info("getting all items with prices");
        return getAllItemsWithPrices();
    }

    @PostMapping("/items")
    public void addItems(@RequestBody ItemsPriceDto itemsPriceDto){
        log.info("adding item with price");
        Item item = new Item();
        item.setPrice(itemsPriceDto.getPrice());
        item.setName(itemsPriceDto.getName());
        item.setId(UUID.randomUUID());
        itemRepository.save(item);
    }

    @GetMapping("/items")
    public List<ItemsPriceDto> getItemsFromDb(){
        log.info("getting item with price from db");
        List<Item> itemList = itemRepository.findAll();
        List<ItemsPriceDto> itemsPriceDtoList = new ArrayList<>();
        for (Item item : itemList) {
            ItemsPriceDto itemsPriceDto = new ItemsPriceDto();
            itemsPriceDto.setName(item.getName());
            itemsPriceDto.setPrice(item.getPrice());
            itemsPriceDtoList.add(itemsPriceDto);
        }
        return itemsPriceDtoList;
    }


    private List<ItemsPriceDto> getAllItemsWithPrices() {
        List<ItemsPriceDto> priceDtoList = new ArrayList<>();
        ItemsPriceDto itemsPriceDto = new ItemsPriceDto();
        itemsPriceDto.setName("hp laptop");
        itemsPriceDto.setPrice(100);
        priceDtoList.add(itemsPriceDto);
        return priceDtoList;
    }


}
