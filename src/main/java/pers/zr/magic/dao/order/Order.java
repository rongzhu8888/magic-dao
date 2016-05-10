package pers.zr.magic.dao.order;

import pers.zr.magic.dao.constants.OrderType;

/**
 * Created by zhurong on 2016-5-10.
 */
public class Order {

    private String column;

    private OrderType type;

    public Order(String column, OrderType type) {
        this.column = column;
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public OrderType getType() {
        return type;
    }
}
