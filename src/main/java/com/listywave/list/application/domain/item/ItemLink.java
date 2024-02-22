package com.listywave.list.application.domain.item;

import com.listywave.common.exception.CustomException;
import com.listywave.common.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ItemLink {

    private static final int LENGTH_LIMIT = 2048;

    @Column(name = "link", length = LENGTH_LIMIT, nullable = false)
    private final String value;

    public ItemLink(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value != null && value.length() > LENGTH_LIMIT) {
            throw new CustomException(ErrorCode.LENGTH_EXCEEDED, "첨부 link 길이는 " + LENGTH_LIMIT + "자를 넘을 수 없습니다.");
        }
    }
}
