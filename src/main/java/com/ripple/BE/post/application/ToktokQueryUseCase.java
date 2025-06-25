package com.ripple.BE.post.application;

import com.ripple.BE.post.domain.type.PostSort;
import com.ripple.BE.post.dto.response.ToktokPreviewListResponseDTO;
import com.ripple.BE.post.dto.response.ToktokPreviewResponseDTO;
import com.ripple.BE.post.dto.response.ToktokResponseDTO;

public interface ToktokQueryUseCase {

    ToktokPreviewResponseDTO getTodayToktok();

    ToktokResponseDTO getToktok(final long id, final long userId);

    ToktokPreviewListResponseDTO getToktoks(final int page, final PostSort sort);

    ToktokPreviewListResponseDTO searchToktoks(
            final String keyword, final int page, final long userId);
}
