package com.example.util;

import lombok.SneakyThrows;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class EpubUtil {

    /**
     * 根据file初始化Epub
     * @param file
     * @return Book
     */
    @SneakyThrows
    public static Book initEpub(File file) {
        InputStream input = null;

        //从输入流当中读取epub格式文件
        EpubReader reader = new EpubReader();
        input = new FileInputStream(file);

        Book book = reader.readEpub(input);

        if (input != null) {
            input.close();
        }

        return book;
    }

    /**
     * 根据filename初始化Epub
     * @param filename
     * @return Book
     */
    @SneakyThrows
    public static Book initEpub(String filename) {
        InputStream input = null;
        File file = FileUtils.getFile(filename);

        //从输入流当中读取epub格式文件
        EpubReader reader = new EpubReader();
        input = new FileInputStream(file);

        Book book = reader.readEpub(input);

        if (input != null) {
            input.close();
        }

        return book;
    }

    /**
     * 获取书本的头部信息
     * @param book
     * @return Metadata
     */
    public static Metadata getMetaData (Book book) {
        return book.getMetadata();
    }

    /**
     * 获取封面图片
     * @param book
     * @return Resource
     */
    public static Resource getCoverImage (Book book) {
        return book.getCoverImage();
    }

    /**
     * 获取封面页
     * @param book
     * @return Resource
     */
    public static Resource getCoverPage (Book book) {
        return book.getCoverPage();
    }

    /**
     * 获取目录
     * @param book
     * @return Resource
     */
    public static Resource getNcxResource (Book book) {
        return book.getNcxResource();
    }

    /**
     *
     * @param book
     * @return Resource
     */
    public static Resource getOpfResource (Book book) {
        return book.getOpfResource();
    }

    /**
     * 获取书本的全部资源
     * @param book
     * @return Resources
     */
    public static Resources getResources (Book book) {
        return book.getResources();
    }

    /**
     * 获取所有的资源数据
     * @param book
     * @return Map<String, Object>
     */
    @SneakyThrows
    public static Map<String, Object> getResourcesData (Book book) {
        List<Resource> resourceList = new ArrayList<>();
        List<byte[]> dataList = new ArrayList<>();
        List<MediaType> mediaTypeList = new ArrayList<>();

        Collection<String> allHrefs = getResources(book).getAllHrefs();
        for (String href : allHrefs) {
            Resource resource = getResources(book).getByHref(href);
            //data就是资源的内容数据，可能是css,html,图片等等
            byte[] data = resource.getData();
            // 获取到内容的类型  css,html,还是图片
            MediaType mediaType = resource.getMediaType();

            resourceList.add(resource);
            dataList.add(data);
            mediaTypeList.add(mediaType);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("resourceList", resourceList);
        resultMap.put("dataList", dataList);
        resultMap.put("mediaTypeList", mediaTypeList);

        return resultMap;
    }

    /**
     * 获取所有章节内容
     * @param book
     * @return List<Resource>
     */
    public static List<Resource> getContents (Book book) {
        return book.getContents();
    }

    /**
     * 通过spine获取线性的阅读菜单，此菜单依照阅读顺序排序
     * @param book
     * @return Spine
     */
    public static Spine getSpine (Book book) {
        return book.getSpine();
    }

    /**
     * 通过spine获取所有的数据
     * @param book
     * @return Map<String, Object>
     */
    @SneakyThrows
    public static Map<String, Object> getBookData (Book book) {
        List<Resource> resourceList = new ArrayList<>();
        List<byte[]> dataList = new ArrayList<>();
        List<MediaType> mediaTypeList = new ArrayList<>();

        List<SpineReference> spineReferences = getSpine(book).getSpineReferences();
        for (SpineReference spineReference : spineReferences) {
            Resource resource = spineReference.getResource();
            //data就是资源的内容数据，可能是css,html,图片等等
            byte[] data = resource.getData();
            // 获取到内容的类型  css,html,还是图片
            MediaType mediaType = resource.getMediaType();

            resourceList.add(resource);
            dataList.add(data);
            mediaTypeList.add(mediaType);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("resourceList", resourceList);
        resultMap.put("dataList", dataList);
        resultMap.put("mediaTypeList", mediaTypeList);

        return resultMap;
    }

    /**
     * 通过TableOfContents获取树形菜单。此菜单按照章节之间的关系（树形）排序
     * @param book
     * @return TableOfContents
     */
    public static TableOfContents getMenusContent (Book book) {
        return book.getTableOfContents();
    }

    /**
     * 获取到目录对应的资源数据
     * @param book
     * @return Map<String, Object>
     */
    @SneakyThrows
    public static Map<String, Object> getTargetDataByMenu (Book book) {
        List<Resource> resourceList = new ArrayList<>();
        List<byte[]> dataList = new ArrayList<>();
        List<MediaType> mediaTypeList = new ArrayList<>();

        List<TOCReference> tocReferences = getMenusContent(book).getTocReferences();
        for (TOCReference tocReference : tocReferences) {
            Resource resource = tocReference.getResource();

            // data就是资源的内容数据，可能是css,html,图片等等
            byte[] data = resource.getData();
            // 获取到内容的类型  css,html,还是图片
            MediaType mediaType = resource.getMediaType();
            if(tocReference.getChildren().size() > 0){
                //获取子目录的内容
            }

            resourceList.add(resource);
            dataList.add(data);
            mediaTypeList.add(mediaType);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("resourceList", resourceList);
        resultMap.put("dataList", dataList);
        resultMap.put("mediaTypeList", mediaTypeList);

        return resultMap;
    }

    /**
     * 根据href获取到对应的资源
     * @param book
     * @param href
     * @return Resource
     */
    public static Resource getResourceByHref(Book book, String href) {
       return book.getResources().getByHref(href);
    }

    /**
     * 根据id获取对应的资源
     * @param book
     * @param id
     * @return Resource
     */
    public static Resource getResourceById (Book book, String id) {
        return book.getResources().getById(id);
    }
}
