package com.imooc.service;

import com.imooc.pojo.Carousel;

import java.util.List;

public interface ICarouselService {

    List<Carousel> queryAll(Integer isShow);
}
