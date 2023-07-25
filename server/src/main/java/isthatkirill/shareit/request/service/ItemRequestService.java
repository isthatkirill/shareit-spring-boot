package isthatkirill.shareit.request.service;

import isthatkirill.shareit.request.dto.ItemRequestDto;
import isthatkirill.shareit.request.dto.ItemRequestDtoLong;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoLong create(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDtoLong> getAll(Integer from, Integer size, Long userId);

    List<ItemRequestDtoLong> getOwn(Long userId);

    ItemRequestDtoLong getById(Long requestId, Long userId);

}
