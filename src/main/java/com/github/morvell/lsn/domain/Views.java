package com.github.morvell.lsn.domain;

/**
 * @author Андрей Захаров
 * @created 05/04/2020
 */
public final class Views {

    public interface Id {
    }

    public interface IdName extends Id {
    }

    public interface FullComment extends IdName {
    }

    public interface FullMessage extends IdName {
    }

}
