package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(User user, Integer friendId) throws NotFoundException, ValidationException {
        log.debug("Запрос на добавление в друзья id={} и  friendid={}", user.getId(), friendId);
        userStorage.checkIdUser(user.getId());
        userStorage.checkIdUser(friendId);
        user.getFriendList().add(friendId);
        User friend = userStorage.getUserById(friendId);
        friend.getFriendList().add(user.getId());
        userStorage.patchUser(user);
        userStorage.patchUser(friend);
        return user;
    }

    public User removeFriend(User user, Integer friendId) throws NotFoundException, ValidationException {
        log.debug("Запрос на удаление из друзей id={} и  friendid={}", user.getId(), friendId);
        if (!user.getFriendList().contains(friendId)) {
            throw new NotFoundException("друг с таким Id не найден");
        }
        user.getFriendList().remove(friendId);
        User friend = userStorage.getUserById(friendId);
        friend.getFriendList().remove(user.getId());
        userStorage.patchUser(user);
        userStorage.patchUser(friend);
        return user;
    }

    public List<User> getCommonFriends(User user, User friend) {
        log.info("Запрос общих друзей между id={} и friendId={}", user.getId(), friend.getId());
        Set<Integer> listCommonFriends = user.getFriendList().stream().filter(f ->
                friend.getFriendList().contains(f)).collect(Collectors.toSet());
        return idSetToUserList(listCommonFriends);
    }

    public User addUser(User user) throws ValidationException, NotFoundException {
        userStorage.addUser(user);
        return user;
    }

    public User patchUser(User user) throws ValidationException, NotFoundException {
        userStorage.patchUser(user);
        return user;
    }

    public List<User> getUserList() {
        return userStorage.getUserList();
    }

    public User getUserById(Integer id) throws NotFoundException {
        return userStorage.getUserById(id);
    }

    public List<User> getFriends(Integer id) throws NotFoundException {
        return idSetToUserList(userStorage.getUserById(id).getFriendList());
    }

    private List<User> idSetToUserList(Set<Integer> friendsIdSet) {
        List<User> userList = friendsIdSet.stream()
                .map(u -> {
                    try {
                        return userStorage.getUserById(u);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
        return userList;
    }
}
