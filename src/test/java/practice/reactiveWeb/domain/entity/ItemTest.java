package practice.reactiveWeb.domain.entity;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    @Test
    void itemBasicsShouldWork() {
        Item sampleItem = new Item("tv tray", "Alf tv tray", 19.99);
        
        assertThat(sampleItem.getName()).isEqualTo("tv tray");
        assertThat(sampleItem.getDescription()).isEqualTo("Alf tv tray");
        assertThat(sampleItem.getPrice()).isEqualTo(19.99);
    }

}