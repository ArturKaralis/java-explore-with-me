package ru.practicum.ewm.users.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewm.events.enums.RateSort;
import ru.practicum.ewm.users.dto.UserDto;
import ru.practicum.ewm.users.dto.UserRateDto;
import ru.practicum.ewm.users.mapper.UserMapper;
import ru.practicum.ewm.users.model.User;
import ru.practicum.ewm.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size) {
        int pageNumber = (int) Math.ceil((double) from / size);
        Pageable pageable = PageRequest.of(pageNumber, size);

        if (userIds != null) {
            return userRepository.findAllByIdIn(userIds).stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageable).map(UserMapper::toUserDto).getContent();
        }
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        if (userRepository.existsByName(userDto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User name is already used.");
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        userRepository.deleteById(user.getId());
    }

    @Override
    public List<UserRateDto> getRatedUsers(Long userId, String rateSort, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "You are not registered."));

        int pageNumber = (int) Math.ceil((double) from / size);
        Pageable pageable;

        if (RateSort.valueOf(rateSort).equals(RateSort.HIGH)) {
            pageable = PageRequest.of(pageNumber, size, Sort.by("rate").descending());
        } else if (RateSort.valueOf(rateSort).equals(RateSort.LOW)) {
            pageable = PageRequest.of(pageNumber, size, Sort.by("rate").ascending());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sorting parameter.");
        }

        return userRepository.findAllByRateIsNotNull(pageable).map(UserMapper::toUserRateDto).getContent();
    }
}