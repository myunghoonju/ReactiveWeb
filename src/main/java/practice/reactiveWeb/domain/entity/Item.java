package practice.reactiveWeb.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Item {

    @Id
    private String id;

    private String name;

    private double price;

    private String description;

    private String distributorRegion;

    private LocalDateTime releaseDate;

    private int availableUnits;

    private Point point;

    private boolean active;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Item(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
