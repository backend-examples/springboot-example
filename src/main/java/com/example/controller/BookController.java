package com.example.controller;

import com.example.enums.ResultCodeEnum;
import com.example.util.EpubUtil;
import com.example.util.ResponseResult;
import nl.siegmann.epublib.domain.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/epub")
public class BookController {

    private String epubFile = "http://101.35.44.70:9000/file/%E4%B8%89%E5%9B%BD%E6%BC%94%E4%B9%89.epub";

    @GetMapping("/getEpubBook")
    public ResponseResult getEpubBook(@RequestParam String filename) {
        Book book = EpubUtil.initEpub(filename);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("book", book);

        return ResponseResult.setResult(ResultCodeEnum.SUCCESS).data(resultMap);
    }

    @GetMapping("/getEpubMetaData")
    public ResponseResult getEpubMetaData (@RequestParam String filename) {
        Book book = EpubUtil.initEpub(filename);
        Metadata metaData = EpubUtil.getMetaData(book);

        return ResponseResult.ok().data(metaData);
    }

    @GetMapping("/getEpubResources")
    public ResponseResult getEpubResources (@RequestParam String filename) {
        Book book = EpubUtil.initEpub(filename);
        Resources resources = EpubUtil.getResources(book);

        return ResponseResult.ok().data(resources);
    }

    @GetMapping("/getEpubResourcesData")
    public ResponseResult getEpubResourcesData (@RequestParam String filename) {
        Book book = EpubUtil.initEpub(filename);
        Map<String, Object> resourcesData = EpubUtil.getResourcesData(book);

        return ResponseResult.ok().data(resourcesData);
    }

    @GetMapping("/getEpubContents")
    public ResponseResult getEpubContents (@RequestParam String filename) {
        Book book = EpubUtil.initEpub(filename);
        List<Resource> contents = EpubUtil.getContents(book);

        return ResponseResult.ok().data(contents);
    }

    @GetMapping("/getEpubSpine")
    public ResponseResult getEpubSpine (@RequestParam String filename) {
        Book book = EpubUtil.initEpub(filename);
        Spine spine = EpubUtil.getSpine(book);

        return ResponseResult.ok().data(spine);
    }

    @GetMapping("/getEpubMenusContent")
    public ResponseResult getEpubMenusContent (@RequestParam String filename) {
        Book book = EpubUtil.initEpub(filename);
        TableOfContents menusContent = EpubUtil.getMenusContent(book);
        return ResponseResult.ok().data(menusContent);
    }

    @GetMapping("/getEpubResourceById")
    public ResponseResult getEpubResourceById (@RequestParam String filename, @RequestParam String id) {
        Book book = EpubUtil.initEpub(filename);

        Resource resource = EpubUtil.getResourceById(book, id);
        return ResponseResult.ok().data(resource);
    }

    @GetMapping("/getEpubResourceByHref")
    public ResponseResult getEpubResourceByHref (@RequestParam String filename, @RequestParam String href) {
        Book book = EpubUtil.initEpub(filename);

        Resource resource = EpubUtil.getResourceByHref(book, href);

        return ResponseResult.ok().data(resource);
    }
}
