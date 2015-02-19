package ru.mera.service.gogo.impl;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.service.command.Descriptor;
import ru.mera.bt.model.Task;
import ru.mera.bt.model.User;
import ru.mera.bt.service.UserService;
import ru.mera.service.gogo.BTServiceProxy;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by antfom on 10.02.2015.
 */
@Component(immediate = true)
@Service (value = ru.mera.service.gogo.BTServiceProxy.class)
@Properties({
        @Property(name = "osgi.command.scope", value = "bt"),
        @Property(name = "osgi.command.function", value = { "adduser" , "usertasks", "finduser" })
})
public class BTServiceProxyImpl implements BTServiceProxy {

    @Reference(cardinality = ReferenceCardinality.OPTIONAL_UNARY, policy = ReferencePolicy.DYNAMIC)
    private volatile UserService usersService;

    @Activate
    public void start(){
        System.err.println("service gogo sarted");
    }

    @Deactivate
    public void stop(){
        System.err.println("service gogo stopped");
    }

    @Override
    @Descriptor("Add a new user")
    public final String adduser(@Descriptor("user name") String name,@Descriptor("user email") String email) {
        if(usersService!=null) {
            User user = usersService.createUser(name, email);
            try {
                user.storeToDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "User " + name + " created with id " + user.getId();
        }
        return "user service not found";
    }

    @Override
    @Descriptor("Find tasks by user name or id")
    public Set<Task> usertasks(@Descriptor("find by: name, id")String findBy, @Descriptor("name or id") String attr) {
        if(usersService!=null) {
            User user = null;
            try {
                if(findBy.equals("name")) {
                    Set<User> userSet = usersService.findUser(attr);
                    Iterator<User> it = userSet.iterator();
                    if(it.hasNext())
                        user = it.next();
                }
                else if(findBy.equals("id")){
                    Set<User> userSet = usersService.findUser(Integer.parseInt(attr));
                    Iterator<User> it = userSet.iterator();
                    if(it.hasNext())
                        user = it.next();
                }

                return user.getTasksFromDB();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            } catch (NumberFormatException e){
                System.err.println(e.getMessage());
            }

        }
        throw new IllegalStateException();
    }

    @Override
    @Descriptor("Find tasks by user name and email")
    public Set<Task> usertasks(@Descriptor("find by: email")String findBy,@Descriptor("name") String attr1, @Descriptor("email") String attr2) {
        if(usersService!=null) {
            User user = null;
            try {
                if(findBy.equals("email")) {
                    Set<User> userSet = usersService.findUser(attr1, attr2);
                    Iterator<User> it = userSet.iterator();
                    if (it.hasNext())
                        user = it.next();
                }
                return user.getTasksFromDB();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        throw new IllegalStateException();
    }

    @Override
    @Descriptor("Find user by  name")
    public Set<User> finduser(@Descriptor("name")String name) {
        if(usersService!=null) {
            try {
                return usersService.findUser(name);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            throw new IllegalStateException("user not found");
        }
        throw new IllegalStateException("user service not found");
    }

    @Override
    @Descriptor("Find user by email")
    public Set<User> finduser(@Descriptor("name")String name, @Descriptor("email")String email) {
        if(usersService!=null) {
            try {
                return usersService.findUser(name, email);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            throw new IllegalStateException("user not found");
        }
        throw new IllegalStateException("user service not found");
    }

    @Override
    @Descriptor("Find user by email")
    public Set<User> finduser(@Descriptor("id")int id) {
        if(usersService!=null) {
            try {
                return usersService.findUser(id);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            throw new IllegalStateException("user not found");
        }
        throw new IllegalStateException("user service not found");
    }
}
