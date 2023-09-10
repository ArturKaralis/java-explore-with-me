package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.dao.UserRepository;
import ru.practicum.ewm.user.dto.UserDtoRequest;
import ru.practicum.ewm.user.dto.UserDtoResponse;
import ru.practicum.ewm.user.mapper.UserMapper;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    @Transactional
    @Override
    public UserDtoResponse save(UserDtoRequest userDto) {
        return UserMapper.mapToUserDto(repo.saveAndFlush(UserMapper.mapToUser(userDto)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDtoResponse> findAllByIds(List<Long> ids, int from, int size) {
        int pageNum = from / size;
        final Pageable pageable = PageRequest.of(pageNum, size);
        if (ids == null || ids.isEmpty()) {
            return UserMapper.mapToUserDto(repo.findAll(pageable).getContent());
        } else {
            return UserMapper.mapToUserDto(repo.findUsersByIdIn(ids, pageable));
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        repo.deleteById(id);
    }
}
