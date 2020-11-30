/*
 * Scary disclaimer notice - If this software causes significant harm to
 * you pc, mac, laptop, smart phone, dumb phone, tablet, tv, washing machine
 * or dishwasher - which it will - do not blame the author of this software.
 * He spent minutes researching best practices (read poor practices) and copying
 * and pasting lots of dangerous code from the online internet. Hence, this
 * application should not be compiled, executed, distributed, downloaded,
 * sold, purchased, or otherwise used without expressed written consent from
 * the National Football League.
 *
 * Under no circumstances should this program be run as-is, or even as-it-was
 * or will-be. There is no hope for it. You have been warned. Twice now really.
 * I can name one hundred other ways you can spend your time other than running
 * this application. Adopt a dog, walk the dog, feed the dog, pet the dog. I
 * guess what I'm trying to say is that the ITunes EULA actually forbids users
 * from building nuclear weapons if you can believe it:
 *
 * "You also agree that you will not use these products for any purposes
 * prohibited by United States law, including, without limitation, the development,
 * design, manufacture or production of nuclear, missiles, or chemical or
 * biological weapons.”
 */
package com.arturospizzeria.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Our bread and butter. Represents both simple and
 * complex types on our menu. A simple type is a stand alone
 * item like a beverage or side order, whereas a complex type
 * might be an ingredient for a pizza like a topping or type
 * of crust.
 */
@NoArgsConstructor
@Entity
@Table(name = "menu")
public class MenuItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double price;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private Boolean available;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    @NotBlank(message = "Description is mandatory")
    private String description;

    public MenuItem(Double price, String name, Boolean isAvailable, Category category, String description) {
        this.price = price;
        this.name = name;
        this.available = isAvailable;
        this.category = category;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public enum Category {
        PIZZA_CHEESE, PIZZA_SAUCE, PIZZA_CRUST, PIZZA_SIZE, PIZZA_TOPPING, SIDE, BEVERAGE
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", available=" + available +
                ", category=" + category +
                ", description='" + description + '\'' +
                '}';
    }
}
