package com.imooc.service.impl;

import com.imooc.mapper.CarouselMapper;
import com.imooc.pojo.Carousel;
import com.imooc.service.ICarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarouselServiceImpl implements ICarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Carousel> queryAll(Integer isShow) {

        Example carouseExample = new Example(Carousel.class);
        carouseExample.orderBy("sort").desc();

        Example.Criteria criteria = carouseExample.createCriteria();
        criteria.andEqualTo("isShow",isShow);

        List<Carousel> carousels = carouselMapper.selectByExample(carouseExample);

        return carousels;
    }
}
