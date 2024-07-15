package com.example.example.mapper;

import com.example.example.entities.News;
import com.example.example.model.*;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@DecoratedWith(NewsMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper
{
    News requestToNews(UpsertNewsRequest request);

    News requestToNews(Long id, UpsertNewsRequest request);

    News requestToNewsForDeleting(Long id, DeletingNewsRequest request);

    NewsResponse newsToResponse(News news);

    List<NewsResponse> newsListToResponseList(List<News> newsList);

    NewsResponseWithCountComments newsToResponseWithCounts(News news);

    List<NewsResponseWithCountComments> newsListToResponseListWithCountComments(List<News> newsList);

    default NewsListResponseWithCountComments newsListToNewsListResponseWithCountComments(List<News> newsList) {
        NewsListResponseWithCountComments response = new NewsListResponseWithCountComments();
        response.setNewsListWithCountComments(newsListToResponseListWithCountComments(newsList));
        return response;
    }

}
