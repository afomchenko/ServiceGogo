package ru.mera.service.gogo;

import ru.mera.bt.model.Task;
import ru.mera.bt.model.User;

import java.util.Set;

/**
 * Created by antfom on 10.02.2015.
 */
public interface BTServiceProxy {
    String SCOPE = "bt";

    String adduser(String name, String email);

    Set<Task> usertasks(String findBy, String attr);

    Set<Task> usertasks(String findBy, String attr1, String attr2);

    Set<User> finduser(String name);

    User finduser(String name, String email);

    User finduser(int id);
}
