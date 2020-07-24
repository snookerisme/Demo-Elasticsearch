package com.demo.elasticsearch.controller;

import com.demo.elasticsearch.model.MyModel;
import com.demo.elasticsearch.request.DeleteById;
import com.demo.elasticsearch.request.FindAllRequest;
import com.demo.elasticsearch.request.FindByIdRequest;
import com.demo.elasticsearch.service.MyModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MyModelController {

    @Autowired
    private MyModelService myModelService;

    @GetMapping("/index")
    public String index(){
        return "Hello world!!";
    }

    @PostMapping("/save")
    public Mono<MyModel> saveModel(@RequestBody MyModel request){
        return myModelService.saveMyModel(request);
    }

    @PostMapping("/findById")
    public Mono<MyModel> findMyModelById(@RequestBody FindByIdRequest request){
       return myModelService.findMyModelById(request.getId());
    }

    @PostMapping("/findAll")
    public Flux<MyModel> findMyModelById(@RequestBody FindAllRequest request){
        return myModelService.findAllMyModels(request.getField(),request.getValue());
    }

    @PostMapping("/delete")
    public Mono<String> deleteMyModelById(@RequestBody DeleteById request){
        return myModelService.deleteMyModelById(request.getId());
    }
}
