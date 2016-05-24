package pers.zr.opensource.magic.dao.order;

import pers.zr.opensource.magic.dao.constants.OrderType;

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
