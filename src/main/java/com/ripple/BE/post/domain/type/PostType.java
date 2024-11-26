package com.ripple.BE.post.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostType {
    ECONOMY_TALK("경제 톡톡"),
    FREE("자유"),
    QUESTION("질문"),
    ECONOMY_STUDY_TIP("경제 공부 tip"),
    EVENT_NEWS("이벤트/소식"),
    BOOK_RECOMMENDATION("도서 추천");

    private final String postType;
}
