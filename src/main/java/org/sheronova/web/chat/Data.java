package org.sheronova.web.chat;

public class Data {

    private String sender;
    private String addr;
    private String content;
    private Integer usersCount;
    private String users;

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
