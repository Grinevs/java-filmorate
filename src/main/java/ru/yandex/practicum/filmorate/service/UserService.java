package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(User user, Integer friendId) throws NotFoundException {
        user.getFriendList().add(friendId);
        User friend = userStorage.getUserById(friendId);
        friend.getFriendList().add(user.getId());
        userStorage.patchUser(user);
        userStorage.patchUser(friend);
        return user;
    }

    public User removeFriend(User user, Integer friendId) throws NotFoundException {
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

    public Set<Integer> commonFriends(User user, User friend) {
        Set<Integer> listCommonFriends = new HashSet<>();
        listCommonFriends = user.getFriendList().stream().filter(f->
                friend.getFriendList().contains(f)).collect(Collectors.toSet());
        return listCommonFriends;
    }
}