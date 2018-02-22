package model;

import java.io.Serializable;

/**
 * Message Class
 *
 * @author Carlos Santos
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickname;
    private String msg;

    public Message() {

    }

    public Message(String nickname, String msg) {
        this.nickname = nickname;
        this.msg = msg;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return getNickname() + ": " + getMsg() + "\n";
    }
}
