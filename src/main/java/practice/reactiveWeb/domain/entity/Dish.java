package practice.reactiveWeb.domain.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dish {

    private String desc;
    private boolean delivered = false;

    public Dish(String desc) {
        this.desc = desc;
    }

    public static Dish deliver(Dish dish) {
        Dish deliveredDish = new Dish(dish.desc);
        deliveredDish.setDelivered(true);

        return deliveredDish;
    }

    public boolean idDelivered() {
        return delivered;
    }

    @Override
    public String toString() {
        return "Dish {" +
                "desc = " + desc + " '\'" +
                ", delivered = " + delivered + " }";
    }
}
