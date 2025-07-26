package com.test.user.domain.dto.response;

import com.test.user.repository.entity.Phone;

public record PhoneResponseDTO (
        String number,
        String cityCode,
        String contryCode
)
{
    public PhoneResponseDTO(Phone phone) {
        this(phone.getNumber(), phone.getCityCode(), phone.getContryCode());
    }
}
