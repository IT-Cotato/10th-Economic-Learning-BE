package com.ripple.BE.post.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostType {
    ECONOMY_TALK("경제 톡톡"),
    FREE("자유"),
    QUESTION("질문"),
    INFORMATION("정보"),
    BOOK_RECOMMENDATION("도서 추천");

    private final String postType;
}
